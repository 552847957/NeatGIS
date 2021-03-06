package com.moyear.neatgis.Widgets.QueryWidget.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.moyear.neatgis.R;

import java.util.List;

/**
 * 图例
 * Created by gis-luq on 2017/5/5.
 */

public class LayerSpinnerAdapter extends BaseAdapter {

    public static final int CONTAIN_ALL_LAYERS = 0000;

    private Context context;
    private List<Layer> layerList = null;

    public class AdapterHolder {//列表绑定项
        public ImageView imageView;
        public TextView textView;//图层
    }

    /**
     *
     *
     * @param c
     * @param list 图层列表
     */

    public LayerSpinnerAdapter(Context c, List<Layer> list) {
        this.layerList = list;
        this.context = c;
    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        notifyDataSetChanged();//刷新数据
    }

    @Override
    public int getCount() {
        int num = 0;
        for (int i = 0;i < layerList.size(); i++) {
            Layer layer = layerList.get(i);
            if (layer.isVisible()) {
                num++;
            }
        }
        return num;
    }

    @Override
    public Object getItem(int position) {

        return layerList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdapterHolder holder = new AdapterHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.widget_view_query_attributequery_spinner_item, null);
        holder.textView = convertView.findViewById(R.id.widget_view_query_attributequery_spinner_item_txtName);

        final int index = layerList.size() - position - 1;
        if (index < 0)
            return convertView;//为空

        //仅获取当前显示的layer
        FeatureLayer layer = null;
        int indexPositon = 0;//计数
        for (int i = 0;i < layerList.size(); i++) {
            Layer layerTpl = layerList.get(i);
            if (layerTpl.isVisible()) {
                if (indexPositon == position) {
                    layer = (FeatureLayer) layerTpl;
                }
                indexPositon++;
            }
        }
        holder.textView.setText(layer.getName());

        return convertView;
    }

}
