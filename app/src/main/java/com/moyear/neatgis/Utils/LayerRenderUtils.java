package com.moyear.neatgis.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.symbology.Renderer;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.Symbol;

import java.util.concurrent.ExecutionException;


/**
 *
 * 图层渲染相关工具类
 *Created by moyear on 2020.03.15
 *
 */

public class LayerRenderUtils {


    /**
     *
     *根据FeatureLayer的形状（点，线，面）获取SimpleRenderer
     *
     * @param featureLayer
     * @return
     */
    public static SimpleRenderer getSimpleRenderByLayerType(FeatureLayer featureLayer) {

        // 设置Shapefile文件的渲染方式
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, ColorUtils.randomColor(), 1.0f);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, ColorUtils.randomColor(), lineSymbol);
        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, ColorUtils.randomColor(), 8);

        SimpleRenderer renderer;

        //首次加载shp文件使用随机颜色渲染
        if (featureLayer.getFeatureTable().getGeometryType() == GeometryType.POINT) {//点要素图层
            renderer = new SimpleRenderer(simpleMarkerSymbol);
        } else if (featureLayer.getFeatureTable().getGeometryType() == GeometryType.POLYLINE) {//线要素图层
            renderer = new SimpleRenderer(lineSymbol);
        } else {
            renderer = new SimpleRenderer(fillSymbol);
        }

        return renderer;
    }




    /**
     *从要素图层中获取其对应的要素形状类型的bitmap
     *
     * @param context
     * @param featureLayer
     * @return
     */
    public static Bitmap getLayerFeatureBitmap(Context context, FeatureLayer featureLayer) {
        Bitmap bitmap = null;

        if (featureLayer == null) {
            Log.e("NullFeature","FeatureLayer is null");
            return bitmap;
        }

        Feature feature = featureLayer.getFeatureTable().createFeature();
        Symbol symbol = featureLayer.getRenderer().getSymbol(feature);
        ListenableFuture<Bitmap> layerBitmap = symbol.createSwatchAsync(context, Color.WHITE);

        try {
            bitmap = layerBitmap.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return bitmap;

    }
}
