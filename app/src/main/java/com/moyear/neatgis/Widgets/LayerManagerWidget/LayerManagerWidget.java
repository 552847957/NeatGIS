package com.moyear.neatgis.Widgets.LayerManagerWidget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.data.FeatureCollection;
import com.esri.arcgisruntime.data.FeatureCollectionTable;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.data.VectorTileCache;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.raster.Raster;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.data.GeoPackageFeatureTable;


import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.google.android.material.tabs.TabLayout;
import com.moyear.neatgis.BMOD.MapModule.BaseWidget.BaseWidget;
import com.moyear.neatgis.Common.Adapter.CommonFragmentPagerAdapter;
import com.moyear.neatgis.File.FileInfo;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.FileUtils;
import com.moyear.neatgis.Utils.LayerOperateUtils;
import com.moyear.neatgis.Utils.LayerRenderUtils;
import com.moyear.neatgis.Widgets.LayerManagerWidget.BaseMap.BaseMapManager;
import com.moyear.neatgis.Widgets.LayerManagerWidget.BaseMap.BasemapLayerInfo;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment.BaseMapListFragment;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment.LegendListFragment;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment.OperationLayerListFragment;
import com.moyear.neatgis.Widgets.LayerManagerWidget.UserLayers.GoogleLayer.GoogleWebTiledLayer;
import com.moyear.neatgis.Widgets.LayerManagerWidget.UserLayers.TianDiTuLayer.TianDiTuLayer;
import com.moyear.neatgis.Widgets.LayerManagerWidget.UserLayers.TianDiTuLayer.TianDiTuLayerInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图层控制
 * Created by gis-luq on 2017/5/5.
 */
public class LayerManagerWidget extends BaseWidget {

    private static String TAG = "LayerManagerWidget";

    public View mWidgetView = null;//

    private Context context;

    @Override
    public void active() {
        super.active();
        super.showWidget(mWidgetView);
//        super.showMessageBox(super.name);
    }

    @Override
    public void create() {

        context = super.context;

        initBaseMapResource();//初始化底图

        initOperationalLayers();//初始化业务图层

        initGeoPackageLayers();//初始化业务图层 gpkg

        initJsonLayers();//初始化json图层

        initWidgetView();//初始化UI

    }

    //初始化UI
    private void initWidgetView() {
        List<String> tabTitleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        tabTitleList.add("图层列表");
        fragmentList.add(new OperationLayerListFragment(super.mapView));
        tabTitleList.add("图例");
        fragmentList.add(new LegendListFragment(super.mapView));
        tabTitleList.add("底图");
        fragmentList.add(new BaseMapListFragment(super.mapView));

        mWidgetView = LayoutInflater.from(super.context).inflate(R.layout.widget_view_layer_manager,null);
        TabLayout tabLayout = mWidgetView.findViewById(R.id.tabs_layer_manager);
        ViewPager viewPager = mWidgetView.findViewById(R.id.viewpager_layer_manager);

        AppCompatActivity activity = (AppCompatActivity) context;
        CommonFragmentPagerAdapter sectionsPagerAdapter =
                new CommonFragmentPagerAdapter(context, activity.getSupportFragmentManager(), tabTitleList, fragmentList);

        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void inactive() {
        super.inactive();
    }

    /**
     * 初始化业务图层-shapefile
     */
    private void initOperationalLayers() {
        String path = getOperationalLayersPath();
        List<FileInfo> fileInfos = FileUtils.getFileListInfo(path,"shp");

        if (fileInfos == null)
            return;

        for (int i = 0;i < fileInfos.size(); i++) {
            FileInfo fileInfo = fileInfos.get(i);

            final ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(fileInfo.getPath());
            shapefileFeatureTable.loadAsync();//异步方式读取文件
            shapefileFeatureTable.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //数据加载完毕后，添加到地图
                    FeatureLayer newShapefileLayer = new FeatureLayer(shapefileFeatureTable);

                    SimpleRenderer renderer = LayerRenderUtils.getSimpleRenderByLayerType(newShapefileLayer);//为新添加的featurelayer设置简单的颜色渲染
                    newShapefileLayer.setRenderer((renderer));

                    mapView.getMap().getOperationalLayers().add(newShapefileLayer);
                }
            });
        }
    }

    /**
     * 初始化GeoPackage图层
     */
    private void initGeoPackageLayers() {
        String path = getGeoPackagePath();
        List<FileInfo> fileInfos = FileUtils.getFileListInfo(path,"gpkg");

        if (fileInfos == null)
            return;

        for (int i = 0; i < fileInfos.size(); i++) {
            FileInfo fileInfo = fileInfos.get(i);
            final GeoPackage geoPackage = new GeoPackage(fileInfo.getPath());
            geoPackage.loadAsync();

            geoPackage.addDoneLoadingListener(new Runnable() {
                 @Override
                 public void run() {
                     List<GeoPackageFeatureTable> packageFeatureTables = geoPackage.getGeoPackageFeatureTables();
                     for (int j = 0; j < packageFeatureTables.size(); j++) {
                         GeoPackageFeatureTable table = packageFeatureTables.get(j);
                         FeatureLayer layer = new FeatureLayer(table);

//                         ////标注测试
//                        StringBuilder labelDefinitionString = new StringBuilder();
//                        labelDefinitionString.append("{");
//                        labelDefinitionString.append("\"labelExpressionInfo\": {");
//                        labelDefinitionString.append("\"expression\": \"return $feature.fid;\"},");
//                        labelDefinitionString.append("\"labelPlacement\": \"esriServerPolygonPlacementAlwaysHorizontal\",");
//                        labelDefinitionString.append("\"minScale\":500000,");
//                        labelDefinitionString.append("\"symbol\": {");
//                        labelDefinitionString.append("\"color\": [0,255,50,255],");
//                        labelDefinitionString.append("\"font\": {\"size\": 14, \"weight\": \"bold\"},");
//                        labelDefinitionString.append("\"type\": \"esriTS\"}");
//                        labelDefinitionString.append("}");
//                         LabelDefinition labelDefinition = LabelDefinition.fromJson(String.valueOf(labelDefinitionString));
//                         layer.getLabelDefinitions().add(labelDefinition);
//                         layer.setLabelsEnabled(true);

//                layer.setLayerName(table.getLayerName()+"-gpkg");
                         mapView.getMap().getOperationalLayers().add(layer);
                     }
                 }
            });

        }
    }

    /**
     * 初始化
     */
    private void initJsonLayers() {
        String path = getJSONPath();
        List<FileInfo> fileInfos = FileUtils.getFileListInfo(path,".json");

        if (fileInfos == null)
            return;

        for (int i = 0; i < fileInfos.size(); i++) {
            FileInfo fileInfo = fileInfos.get(i);
            String json = FileUtils.openTxt(fileInfo.getPath(),"UTF-8");
            final FeatureCollection featureCollection = FeatureCollection.fromJson(json);
            featureCollection.loadAsync();
            featureCollection.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                   List<FeatureCollectionTable> featureCollectionTable = featureCollection.getTables();
                    for (int j = 0; j < featureCollectionTable.size(); j++) {
                        FeatureCollectionTable features = featureCollectionTable.get(j);
                        FeatureLayer featureLayer = new FeatureLayer(features);
                        featureLayer.setName(features.getTableName()+"-json");
                        mapView.getMap().getOperationalLayers().add(featureLayer);
                    }
                }
            });

        }

    }

    /**
     * 初始化基础底图资源
     */
    private void initBaseMapResource() {

        String configPath = getBasemapPath("basemap.json");
        BaseMapManager basemapManager = new BaseMapManager(context, super.mapView, configPath);
        List<BasemapLayerInfo> basemapLayerInfoList = basemapManager.getBaseMapLayerInfos();

        if (basemapLayerInfoList == null)
            return;

        for (int i = 0; i < basemapLayerInfoList.size(); i++) {
            BasemapLayerInfo layerInfo = basemapLayerInfoList.get(i);
            String type = layerInfo.getLayerType();

            super.mapView.getMap().getBasemap().getBaseLayers().add(
                    LayerOperateUtils.getBaseMapLayer(context, projectPath, layerInfo));

        }
    }

    /**
     * 获取基础底图路径
     * @param path
     * @return
     */
    private String getBasemapPath(String path) {
        return projectPath + File.separator + "BaseMap" + File.separator + path;
    }

    /**
     * 获取业务图层路径
     * @return
     */
    private String getOperationalLayersPath() {
        return projectPath + File.separator + "OperationalLayers" + File.separator;
    }

    /**
     * 获取Geopackage路径
     * @return
     */
    private String getGeoPackagePath() {
        return projectPath + File.separator+"GeoPackage" + File.separator;
    }

    /**
     * 获取json路径信息
     * @return
     */
    private String getJSONPath() {
        return projectPath + File.separator + "JSON" + File.separator;
    }




}
