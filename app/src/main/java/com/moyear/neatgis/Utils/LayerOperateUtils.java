package com.moyear.neatgis.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.data.VectorTileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.raster.Raster;
import com.moyear.neatgis.Widgets.LayerManagerWidget.BaseMap.BasemapLayerInfo;
import com.moyear.neatgis.Widgets.LayerManagerWidget.UserLayers.GoogleLayer.GoogleWebTiledLayer;
import com.moyear.neatgis.Widgets.LayerManagerWidget.UserLayers.TianDiTuLayer.TianDiTuLayer;
import com.moyear.neatgis.Widgets.LayerManagerWidget.UserLayers.TianDiTuLayer.TianDiTuLayerInfo;

public class LayerOperateUtils {

    private static String TAG = "LayerOperateUtils";



    /**
     *
     *从BasemapLayerInfo中获取所代表的图层
     * @param context
     * @param projectPath
     * @param layerInfo
     * @return
     */
    public static Layer getBaseMapLayer(Context context,String projectPath, BasemapLayerInfo layerInfo) {
        Layer layer = null;

        String type = layerInfo.getLayerType();

        if (type.equals(BasemapLayerInfo.LAYER_TYPE_TPK)) {//TPK
            String path = ProjectUtils.getBasemapPath(projectPath, layerInfo.getPath());

            if (FileUtils.isExist(path)) {//判断是否存在
                TileCache tileCache = new TileCache(path);
                ArcGISTiledLayer localTiledLayer = new ArcGISTiledLayer(tileCache);
                localTiledLayer.setName(layerInfo.getLayerName());
                localTiledLayer.setVisible(layerInfo.isVisible());
                localTiledLayer.setOpacity((float) layerInfo.getLayerOpacity());

                layer = localTiledLayer;

            } else {
                Log.d(TAG,"底图文件(LocalTiledPackage)不存在,"+path);
                Toast.makeText(context, "底图文件(LocalTiledPackage)不存在,"+path, Toast.LENGTH_LONG).show();
            }
        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_TIFF)) {//Tiff
            String path = ProjectUtils.getBasemapPath(projectPath, layerInfo.getPath());

            if (FileUtils.isExist(path)) {//判断是否存在
                Raster raster = new Raster(path);
                RasterLayer rasterLayer = new RasterLayer(raster);
                rasterLayer.setName(layerInfo.getLayerName());
                rasterLayer.setVisible(layerInfo.isVisible());
                rasterLayer.setOpacity((float) layerInfo.getLayerOpacity());

                layer = rasterLayer;
            } else {
                Log.d(TAG,"底图文件(LocalGeoTIFF)不存在,"+path);
                Toast.makeText(context, "底图文件(LocalGeoTIFF)不存在,"+path, Toast.LENGTH_LONG).show();
//                    continue;
            }
        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_SERVERCACHE)) {//Server缓存切片
            String path = ProjectUtils.getBasemapPath(projectPath, layerInfo.getPath());

            if (FileUtils.isExist(path)) {//判断是否存在
                TileCache tileCache = new TileCache(path);
                ArcGISTiledLayer localTiledLayer = new ArcGISTiledLayer(tileCache);
                localTiledLayer.setName(layerInfo.getLayerName());
                localTiledLayer.setVisible(layerInfo.isVisible());
                localTiledLayer.setOpacity((float) layerInfo.getLayerOpacity());

                layer = localTiledLayer;

            } else {
                Log.d(TAG,"底图文件(LocalServerCache)不存在,"+path);
                Toast.makeText(context, "底图文件(LocalServerCache)不存在,"+path, Toast.LENGTH_LONG).show();
            }
        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_ONLINE_TILEDLAYER)) {//在线瓦片
            String url = layerInfo.getPath();

            ArcGISTiledLayer tiledMapServiceLayer = new ArcGISTiledLayer(url);
            tiledMapServiceLayer.setName(layerInfo.getLayerName());
            tiledMapServiceLayer.setVisible(layerInfo.isVisible());
            tiledMapServiceLayer.setOpacity((float) layerInfo.getLayerOpacity());

            layer = tiledMapServiceLayer;

        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_ONLINE_DYNAMICLAYER)) {//在线动态图层
            String url = layerInfo.getPath();

            ArcGISMapImageLayer dynamicMapServiceLayer = new ArcGISMapImageLayer(url);
            dynamicMapServiceLayer.setName(layerInfo.getLayerName());
            dynamicMapServiceLayer.setVisible(layerInfo.isVisible());
            dynamicMapServiceLayer.setOpacity((float) layerInfo.getLayerOpacity());

            layer = dynamicMapServiceLayer;

        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_VTPK)) {//VTPK
            String path = ProjectUtils.getBasemapPath(projectPath, layerInfo.getPath());

            if (FileUtils.isExist(path)) {//判断是否存在
                VectorTileCache vectorTileCache = new VectorTileCache(path);
                ArcGISVectorTiledLayer arcGISVectorTiledLayer = new ArcGISVectorTiledLayer(vectorTileCache);
                arcGISVectorTiledLayer.setName(layerInfo.getLayerName());
                arcGISVectorTiledLayer.setVisible(layerInfo.isVisible());
                arcGISVectorTiledLayer.setOpacity((float)layerInfo.getLayerOpacity());

                layer = arcGISVectorTiledLayer;

            } else {
                Log.d(TAG,"vtpk文件不存在," + path);
                Toast.makeText(context, "vtpk文件不存在,"+path, Toast.LENGTH_LONG).show();

            }
        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_TIANDITU_MAP)) {//天地图
            TianDiTuLayerInfo tdtInfo = new TianDiTuLayerInfo();

            TianDiTuLayerInfo tdtInfo01 = tdtInfo.initwithlayerType(TianDiTuLayerInfo.TianDiTuLayerType.TDT_VECTOR,
                    TianDiTuLayerInfo.TianDiTuSpatialReferenceType.TDT_2000);
            TianDiTuLayer ltl1 = new TianDiTuLayer(tdtInfo01.getTileInfo(), tdtInfo01.getFullExtent());
            ltl1.setName(layerInfo.getLayerName());
            ltl1.setVisible(layerInfo.isVisible());
            ltl1.setOpacity((float) layerInfo.getLayerOpacity());

            ltl1.setLayerInfo(tdtInfo01);

            layer = ltl1;

        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_TIANDITU_IMAGE)) {//天地图影像图
            TianDiTuLayerInfo tdtInfo = new TianDiTuLayerInfo();
            TianDiTuLayerInfo tdtInfo01 = tdtInfo.initwithlayerType(TianDiTuLayerInfo.TianDiTuLayerType.TDT_IMAGE,
                    TianDiTuLayerInfo.TianDiTuSpatialReferenceType.TDT_2000);
            TianDiTuLayer ltl1 = new TianDiTuLayer(tdtInfo01.getTileInfo(), tdtInfo01.getFullExtent());
            ltl1.setName(layerInfo.getLayerName());
            ltl1.setVisible(layerInfo.isVisible());
            ltl1.setOpacity((float) layerInfo.getLayerOpacity());
            ltl1.setLayerInfo(tdtInfo01);

            layer = ltl1;

        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_TIANDITU_IMAGE_LABEL)) {//天地图影像标注图层
            TianDiTuLayerInfo tdtannoInfo = new TianDiTuLayerInfo();
            TianDiTuLayerInfo tdtannoInfo02 = tdtannoInfo.initwithlayerType(TianDiTuLayerInfo.TianDiTuLayerType.TDT_IMAGE,
                    TianDiTuLayerInfo.TianDiTuLanguageType.TDT_CN, TianDiTuLayerInfo.TianDiTuSpatialReferenceType.TDT_2000);
            TianDiTuLayer ltl2 = new TianDiTuLayer(tdtannoInfo02.getTileInfo(), tdtannoInfo02.getFullExtent());
            ltl2.setName(layerInfo.getLayerName());
            ltl2.setVisible(layerInfo.isVisible());
            ltl2.setOpacity((float) layerInfo.getLayerOpacity());
            ltl2.setLayerInfo(tdtannoInfo02);

            layer = ltl2;

        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_GOOGLE_VECTOR)) {//谷歌矢量
            WebTiledLayer googleWebTiledLayer = GoogleWebTiledLayer.CreateGoogleLayer(GoogleWebTiledLayer.MapType.VECTOR);
            googleWebTiledLayer.setName(layerInfo.getLayerName());
            googleWebTiledLayer.setVisible(layerInfo.isVisible());
            googleWebTiledLayer.setOpacity((float) layerInfo.getLayerOpacity());

            layer = googleWebTiledLayer;

        } else if(type.equals(BasemapLayerInfo.LAYER_TYPE_GOOGLE_IMAGE)) {//谷歌影像
            WebTiledLayer googleWebTiledLayer = GoogleWebTiledLayer.CreateGoogleLayer(GoogleWebTiledLayer.MapType.IMAGE);
            googleWebTiledLayer.setName(layerInfo.getLayerName());
            googleWebTiledLayer.setVisible(layerInfo.isVisible());
            googleWebTiledLayer.setOpacity((float) layerInfo.getLayerOpacity());

            layer = googleWebTiledLayer;

        } else if(type.equals(BasemapLayerInfo.LAYER_TYPE_GOOGLE_TERRAIN)) {//谷歌地形
            WebTiledLayer googleWebTiledLayer = GoogleWebTiledLayer.CreateGoogleLayer(GoogleWebTiledLayer.MapType.TERRAIN);
            googleWebTiledLayer.setName(layerInfo.getLayerName());
            googleWebTiledLayer.setVisible(layerInfo.isVisible());
            googleWebTiledLayer.setOpacity((float) layerInfo.getLayerOpacity());

            layer = googleWebTiledLayer;

        } else if (type.equals(BasemapLayerInfo.LAYER_TYPE_GOOGLE_ROAD)) {//谷歌道路
            WebTiledLayer googleWebTiledLayer = GoogleWebTiledLayer.CreateGoogleLayer(GoogleWebTiledLayer.MapType.ROAD);
            googleWebTiledLayer.setName(layerInfo.getLayerName());
            googleWebTiledLayer.setVisible(layerInfo.isVisible());
            googleWebTiledLayer.setOpacity((float) layerInfo.getLayerOpacity());

            layer = googleWebTiledLayer;

        }


        return layer;

    }


    /**
     * 获取Layers中的最大空间范围
     *
     * @param layers
     * @return
     */
    public static Envelope getMaxExtentFromLayers(LayerList layers) {

        if (layers == null || layers.size() < 1)
            return null;

        //以第一个图层的空间范围初始化最大的空间范围
        Layer tempLayer = layers.get(0);
        Envelope tempEnvelope = tempLayer.getFullExtent();

        double minX = tempEnvelope.getXMin();
        double maxY = tempEnvelope.getYMax();
        double maxX = tempEnvelope.getXMax();
        double minY = tempEnvelope.getYMin();

        SpatialReference spatialReference = tempLayer.getSpatialReference();

        for (Layer layer : layers) {
            if (!layer.isVisible()) continue;

            //TODO:参考坐标系逻辑待修改，效果并不理想
            if (spatialReference.getWkid() != layer.getSpatialReference().getWkid()) {
                Log.e("测试代码","The SaptialReference do not compete");
            }

            Envelope envelope = layer.getFullExtent();

            double currentMinX = envelope.getXMin();
            double currentMaxY = envelope.getYMax();
            double currentMaxX = envelope.getXMax();
            double currentMinY = envelope.getYMin();

//            Log.e("测试代码",layer.getLayerName() + "minX:" + currentMinX);
//            Log.e("测试代码",layer.getLayerName() + "maxY:" + currentMaxY);
//            Log.e("测试代码",layer.getLayerName() + "maxX:" + currentMaxX);
//            Log.e("测试代码",layer.getLayerName() + "minY:" + currentMinY);

            if (currentMinX < minX)
                minX = currentMinX;
            if (currentMaxY > maxY)
                maxY = currentMaxY;
            if (currentMaxX > maxX)
                maxX = currentMaxX;
            if (currentMinY < minY)
                minY = currentMinY;
        }

        Envelope maxEnvolpe = new Envelope(minX, maxY, maxX, minY, spatialReference);

        Log.e("zuizhong"," minX:" + minX + " maxY:" + maxY + " maxX:" + maxX + " minY:" + minY);

        return maxEnvolpe;
    }
}
