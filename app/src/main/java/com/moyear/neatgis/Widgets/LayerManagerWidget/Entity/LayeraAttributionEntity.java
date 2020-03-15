package com.moyear.neatgis.Widgets.LayerManagerWidget.Entity;

import com.esri.arcgisruntime.layers.Layer;
;

public class LayeraAttributionEntity {

    private Layer layer;

    private int layerId;

    private String layerName;



    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    public int getLayerId() {
        return layerId;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }


}
