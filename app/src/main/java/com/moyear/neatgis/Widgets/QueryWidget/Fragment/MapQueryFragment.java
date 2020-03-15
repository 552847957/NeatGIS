package com.moyear.neatgis.Widgets.QueryWidget.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Widgets.QueryWidget.Adapter.AttributeAdapter;
import com.moyear.neatgis.Widgets.QueryWidget.Adapter.LayerSpinnerAdapter;
import com.moyear.neatgis.Widgets.QueryWidget.Bean.KeyAndValueBean;
import com.moyear.neatgis.Widgets.QueryWidget.Listener.MapQueryOnTouchListener;

import java.util.ArrayList;
import java.util.List;


public class MapQueryFragment extends Fragment {

    private MapView mMapView;

    private MapQueryOnTouchListener mapQueryOnTouchListener;//要素选择事件

    private View.OnTouchListener defauleOnTouchListener;//默认点击事件

    public MapQueryFragment(MapView mapView) {
       this.mMapView = mapView;
       defauleOnTouchListener = mapView.getOnTouchListener();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.widget_view_query_mapquery, container, false);

        bindView(rootView);
        initView(rootView);
        return rootView;
    }

    private void bindView(View rootView) {


    }

    private void initView(View rootView) {

        LinearLayout llQuerySelection = rootView.findViewById(R.id.ll_widget_view_query_selection);//内容区域

        Spinner spinnerTargetLayerList = rootView.findViewById(R.id.widget_view_query_mapquery_spinnerLayer);//目标图层选择栏
        TextView txtLayerName = rootView.findViewById(R.id.widget_view_query_mapquery_txtLayerName);
        Switch swQueryAllLayer = rootView.findViewById(R.id.sw_widget_view_query_map_all_layer);
        RecyclerView rlFieldList = rootView.findViewById(R.id.rl_widget_view_query_mapquery_field);


        List<KeyAndValueBean> keyAndValueBeans = new ArrayList<>();

        AttributeAdapter attributeAdapter = new AttributeAdapter(getContext(), keyAndValueBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlFieldList.setLayoutManager(linearLayoutManager);
        rlFieldList.setAdapter(attributeAdapter);

        mapQueryOnTouchListener = new MapQueryOnTouchListener(getContext(), mMapView, txtLayerName, attributeAdapter);
        LayerSpinnerAdapter layerSelectedSpinnerAdapter = new LayerSpinnerAdapter(getContext(), mMapView.getMap().getOperationalLayers());

        spinnerTargetLayerList.setAdapter(layerSelectedSpinnerAdapter);
        mapQueryOnTouchListener.setSpinnerLayerList(spinnerTargetLayerList);

        swQueryAllLayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mapQueryOnTouchListener.setSelectAllLayers(true);
                    llQuerySelection.setVisibility(View.GONE);
                } else {
                    mapQueryOnTouchListener.setSelectAllLayers(false);
                    llQuerySelection.setVisibility(View.VISIBLE);
                }

            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();

        initMapQuery();
    }

    /**
     * 初始化图查属性
     */
    private void initMapQuery() {
        mMapView.setMagnifierEnabled(true);//放大镜
        if (mapQueryOnTouchListener != null) {
            mMapView.setOnTouchListener(mapQueryOnTouchListener);

            mapQueryOnTouchListener.clear();//清空当前选择
        }
    }


}
