package com.moyear.neatgis.Widgets.LayerManagerWidget.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.AnimationCurve;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.R;
import com.moyear.neatgis.BMOD.MapModule.Layer.Property.LayerAttributeDialog;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Entity.LayeraAttributionEntity;
import com.google.android.material.slider.Slider;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import gisluq.lib.Util.ToastUtils;

/**
 * 图表列表
 * Created by gis-luq on 2018/4/25.
 * Edit by moyear on 2020.03.02
 */
public class LayerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Layer> layerList;
    private Context context;

    private MapView mMapView;

    public LayerListAdapter(Context c, List<Layer> list) {
        this.layerList = list;
        this.context = c;
    }

    /**
     * 临时新增 by moyear on 2020.02.20
     *
     * @param c
     * @param list
     * @param mapView
     */
    public LayerListAdapter(Context c,MapView mapView, List<Layer> list) {
        this.layerList = list;
        this.context = c;
        this.mMapView = mapView;
    }

    public void setMapView(MapView mapView) {
        this.mMapView = mapView;
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_view_layer_managet_layers_item, parent, false);

        return new LayerItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        LayerItemHolder holder = (LayerItemHolder) viewHolder;

        int index = layerList.size() - position - 1;

        holder.txtLayerName.setText(layerList.get(index).getName());

        Layer layer = layerList.get(index);//按照倒序
        holder.cbxVisibility.setChecked(layer.isVisible());//设置是否显示

        holder.cbxVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layer.setVisible(true);
                } else {
                    layer.setVisible(false);
                }
            }
        });

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayerMoreMenu(v, layer, index);
            }
        });


    }

    private void showLayerMoreMenu(View v, Layer layer, int index) {

        PopupMenu pm = new PopupMenu(context, v);
        pm.getMenuInflater().inflate(R.menu.menu_layer_tools, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_layer_tools_operate_copy://复制图层
                        //TODO：创建副本赋予随机颜色功能待写；底图创建副本待完善
                        if (layer instanceof FeatureLayer) {
                            FeatureLayer featureLayer = ((FeatureLayer) layer).copy();
                            String newName = layer.getName() + " 副本" ;
                            featureLayer.setName(newName);
                            layerList.add(featureLayer);
                            notifyDataSetChanged();

                            ToastUtils.showShort(context, "图层：" + layer.getName() + "已创建副本");
                        } else {
                            ToastUtils.showShort(context, "It is not FeatureLayer");

                        }
                        break;
                    case R.id.menu_layer_tools_operate_move://移动图层
                        ToastUtils.showShort(context, "移动图层，代码待写！！！");
                        break;
                    case R.id.menu_layer_tools_operate_delete://删除图层
                        layerList.remove(index);
                        notifyDataSetChanged();

                        ToastUtils.showShort(context, "图层：" + layer.getName() + "已移除");
                        break;
                    case R.id.menu_layer_tools_opacity://图层透明度
                        ShowOpacityUtilView(layer);
                        break;
                    case R.id.menu_layer_tools_export://图层导出
                        ToastUtils.showShort(context, "图层导出，代码代写！！！");
                        FeatureLayer featureLayer = (FeatureLayer) layer;

                        break;
                    case R.id.menu_layer_tools_scale:
                        if (mMapView != null) {
                            //TODO:添加缩放动画
                            Envelope envelope = layer.getFullExtent();

                            Viewpoint viewpoint = new Viewpoint(envelope);
                            mMapView.setViewpoint(viewpoint);

                        } else {
                            ToastUtils.showShort(context, "MapView in LayerListAdapter is null.");
                        }
                        break;
                    case R.id.menu_layer_tools_attribute:
                        showLayerAttribute(layer);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        pm.show();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return layerList.size();
    }

    /**
     *显示图层属性信息
     *
     */
    private void showLayerAttribute(Layer layer) {
        // TODO:属性，代码待写!!!
        LayeraAttributionEntity layerAttrEntity = new LayeraAttributionEntity();
        layerAttrEntity.setLayer(layer);

        AppCompatActivity activity = (AppCompatActivity) context;

        LayerAttributeDialog attributeDialog = new LayerAttributeDialog(context,layer);
        attributeDialog.show(activity.getSupportFragmentManager(),"LayerAttributeDialog");

    }


    /**
     * 显示透明度操作View
     */
    private void ShowOpacityUtilView(final Layer layer){

        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.

        AlertDialog opacityDialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate( R.layout.widget_alert_opacity, null);
        TextView txtTitle = view.findViewById(R.id.opactiy_element_layout_layerName);

        txtTitle.setText(layer.getName());
        final TextView txtOp = view.findViewById(R.id.opactiy_element_layout_layerOpacity);
        float op = layer.getOpacity();
        txtOp.setText(decimalFormat.format(op * 100) + "%");//将透明度转化为百分号形式
        Slider sliderOpacity = view.findViewById(R.id.opactiy_element_layout_layerOpactiySeekBar);
        sliderOpacity.setValue(op * 100);
        sliderOpacity.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                float op = value / 100;
                txtOp.setText(decimalFormat.format(value) + "%");
                layer.setOpacity(op);
            }
        });
        opacityDialog.setView(view);
        opacityDialog.show();
    }


    /**
     * 列表移动操作
     *
     * @param fromPosition
     * @param toPosition
     */
    public void onMove(int fromPosition, int toPosition) {

        //由于recylerview的排序与arcgis图层显示顺序相反，需对from与to的
        //位置进行修改
        int fromLayerOrder = layerList.size() - fromPosition - 1;
        int toLayerOrder = layerList.size() - toPosition - 1;

        //对原数据进行移动
//        Collections.swap(layerList, fromPosition, toPosition);

        //对原数据进行移动,必须将源数据从图层中删除，再添加到指定位置，否则将报错
        Layer layer = layerList.get(fromLayerOrder);

        layerList.remove(layerList.get(fromLayerOrder));

        layerList.add(toLayerOrder, layer);

        //通知数据移动
        notifyItemMoved(fromPosition, toPosition);
    }


    class LayerItemHolder extends RecyclerView.ViewHolder {

        View itemView;

        CheckBox cbxVisibility;
        TextView txtLayerName;

        ImageButton btnMore;

        LayerItemHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            txtLayerName = itemView.findViewById(R.id.txt_widget_view_layer_managet_legent_item_layername);
            btnMore = itemView.findViewById(R.id.widget_view_layer_managet_item_btnMore);
            cbxVisibility = itemView.findViewById(R.id.widget_view_layer_managet_item_cbxLayer);
        }

    }

}
