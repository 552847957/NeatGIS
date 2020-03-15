package com.moyear.neatgis.Widgets.QueryWidget.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.R;

import java.util.List;

/**
 * 要素查询结果
 * Created by gis-luq on 2017/5/5.
 * edited by moyear on 2020.03.06
 */

public class QueryResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Feature> featureList;
    private Context context;
    private MapView mapView;

    public QueryResultAdapter(Context c, List<Feature> list, MapView mapView) {
        this.featureList = list;
        this.context = c;
        this.mapView = mapView;
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        notifyDataSetChanged();//刷新数据
    }

    public void refreshData(List<Feature> features) {
        if (features == null)
            return;

        this.featureList.clear();
        this.featureList.addAll(features);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.widget_view_query_attributequery_result_item, null);

        return new FeatureItemHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        FeatureItemHolder holder = (FeatureItemHolder) viewHolder;

        holder.textView.setText("查询结果"+ (position+1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllFeatureSelect();

                setFeatureSelect(featureList.get(position));
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    /**
     * 清空所有要素选择
     */
    public void clearAllFeatureSelect(){
        List<Layer> layers = mapView.getMap().getOperationalLayers();
        for (int i=0;i<layers.size();i++){
            FeatureLayer featureLayer = (FeatureLayer)layers.get(i);
            featureLayer.clearSelection();
        }
    }

    /**
     * 设置要素选中
     * @param feature
     */
    public void setFeatureSelect(Feature feature) {
        //设置要素选中
        FeatureLayer identifiedidLayer = feature.getFeatureTable().getFeatureLayer();
        identifiedidLayer.setSelectionColor(Color.YELLOW);
        identifiedidLayer.setSelectionWidth(20);
        identifiedidLayer.selectFeature(feature);

        Geometry buffer = GeometryEngine.buffer(feature.getGeometry(),1000);//缓冲500
        mapView.setViewpointGeometryAsync(buffer);
//        mapView.setViewpointScaleAsync(50000);
    }


    class FeatureItemHolder extends RecyclerView.ViewHolder {

        View itemView;

        public ImageView imageView;
        public TextView textView;//图层

        FeatureItemHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            //imageView = itemView.findViewById(R.id.widget_view_query_mapquery_fielditem_txtName);
            textView = itemView.findViewById(R.id.widget_view_quer_attributequery_result_item_txtName);

        }
    }

}
