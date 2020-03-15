package com.moyear.neatgis.Widgets.LayerManagerWidget.BaseMap;

/**
 * 图层信息列表
 * Created by gis-luq on 2015/4/2.
 *
 * Edited by moyear on 2020.03.15
 */
public class BasemapLayerInfo {

    /**
     * 支持的离线底图类型
     */
    public static String LAYER_TYPE_TPK = "LocalTiledPackage";//tpk
    public static String LAYER_TYPE_TIFF = "LocalGeoTIFF";//tiff
    public static String LAYER_TYPE_SERVERCACHE = "LocalServerCache";//server cache
    public static String LAYER_TYPE_ONLINE_TILEDLAYER = "OnlineTiledMapServiceLayer";//在线切片
    public static String LAYER_TYPE_ONLINE_DYNAMICLAYER = "OnlineDynamicMapServiceLayer";//在线动态图层
    public static String LAYER_TYPE_VTPK = "LocalVectorTilePackage";//vtpk

    public static String LAYER_TYPE_TIANDITU_MAP = "TianDiDuLayerMap";//天地图底图
    public static String LAYER_TYPE_TIANDITU_IMAGE = "TianDiDuLayerImage";//天地图影像
    public static String LAYER_TYPE_TIANDITU_IMAGE_LABEL = "TianDiDuLayerImageLabel";//天地图影像标注图层

    public static String LAYER_TYPE_GOOGLE_VECTOR = "GoogleMapLayerVector";//谷歌矢量
    public static String LAYER_TYPE_GOOGLE_IMAGE = "GoogleMapLayerImage";//谷歌影像
    public static String LAYER_TYPE_GOOGLE_TERRAIN = "GoogleMapLayerTerrain";//谷歌地形
    public static String LAYER_TYPE_GOOGLE_ROAD = "GoogleMapLayerRoad";//谷歌道路图层

    private String layerName;//名称

    private String layerType;//类型

    private String Path;//本地路径

    private int LayerIndex;//图层顺序

    private boolean Visible;//是否可以显示

    private double layerOpacity;//图层透明度

    private String layerRender;

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public void setPath(String path) {
        Path = path;
    }

    public void setLayerIndex(int layerIndex) {
        LayerIndex = layerIndex;
    }

    public void setVisible(boolean visible) {
        Visible = visible;
    }

    public void setLayerOpacity(double layerOpacity) {
        this.layerOpacity = layerOpacity;
    }

    public void setLayerRender(String layerRender) {
        this.layerRender = layerRender;
    }

    public String getLayerName() {
        return layerName;
    }

    public String getLayerType() {
        return layerType;
    }

    public String getPath() {
        return Path;
    }

    public int getLayerIndex() {
        return LayerIndex;
    }

    public boolean isVisible() {
        return Visible;
    }

    public double getLayerOpacity() {
        return layerOpacity;
    }

    public String getLayerRender() {
        return layerRender;
    }


}
