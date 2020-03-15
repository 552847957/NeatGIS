package com.moyear.neatgis.Widgets.LayerManagerWidget.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.symbology.Symbol;
import com.moyear.neatgis.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 图例
 * Created by gis-luq on 2018/4/25.
 * Edit by moyear on 2020.03.02
 */
public class LegendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Layer> layerList;
    private Context context;

    public LegendListAdapter(Context c, List<Layer> list) {
        this.layerList = list;
        this.context = c;
    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        notifyDataSetChanged();//刷新数据
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_view_layer_managet_legent_item, parent, false);

        return new LayerLegendHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        LayerLegendHolder holder = (LayerLegendHolder) viewHolder;

        final int index = layerList.size() - position - 1;

        //仅获取当前显示的layer
        FeatureLayer layer = null;
        int indexPositon = 0;//计数
        for (int i = 0; i < layerList.size(); i++) {
            Layer layerTpl = layerList.get(i);
            if (layerTpl.isVisible()){
                if (indexPositon == position) {
                    layer = (FeatureLayer) layerTpl;
                }
                indexPositon ++;
            }
        }
        holder.textView.setText(layer.getName());

        //要素模板
        Feature feature = layer.getFeatureTable().createFeature();
        Symbol symbol = layer.getRenderer().getSymbol(feature);
        ListenableFuture<Bitmap> bitmap = symbol.createSwatchAsync(context, Color.WHITE);

        try {
            holder.imageView.setImageBitmap(bitmap.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        holder.imageView.setBackground(null);//layout中要素模板背景置为空
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int num = 0;
        for (int i = 0; i < layerList.size(); i++) {
            Layer layer = layerList.get(i);
            if (layer.isVisible()) {
                num ++;
            }
        }
        return num;
    }

    class LayerLegendHolder extends RecyclerView.ViewHolder {

        View itemView;

        public ImageView imageView;
        public TextView textView;//图层

        LayerLegendHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.widget_view_layer_managet_legent_item_img);
            textView = itemView.findViewById(R.id.widget_view_layer_managet_legent_item_txtName);
        }

    }

}
