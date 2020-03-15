package com.moyear.neatgis.Widgets.QueryWidget.Fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.Common.Adapter.CommonSpinnerAdapter;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Widgets.QueryWidget.Adapter.LayerSpinnerAdapter;
import com.moyear.neatgis.Widgets.QueryWidget.Adapter.QueryResultAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import gisluq.lib.Util.ToastUtils;


public class AttributeQueryFragment extends Fragment {

    private List<Feature> featuresResult;

    private MapView mMapView;

    private RecyclerView rlResultList;

    private QueryResultAdapter queryResultAdapter;

    public AttributeQueryFragment(MapView mapView) {
        this.mMapView = mapView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.widget_view_query_attributequery, container, false);

        initData();

        bindView(rootView);
        initView(rootView);
        return rootView;
    }

    private void initData() {
        featuresResult = new ArrayList<>();//查询统计结果

    }

    private void initView(View attributeQueryView) {
        final Spinner spinnerLayerList = attributeQueryView.findViewById(R.id.widget_view_query_attributequery_spinnerLayer);
        final TextView txtQueryInfo = attributeQueryView.findViewById(R.id.widget_view_query_attributequery_txtQueryInfo);
        Button btnQuery = attributeQueryView.findViewById(R.id.widget_view_query_attributequery_btnQuery);

        //初始化查询到的要素列表
        rlResultList = attributeQueryView.findViewById(R.id.rl_widget_view_query_attributequery_results);

        queryResultAdapter = new QueryResultAdapter(getContext(), featuresResult, mMapView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlResultList.setLayoutManager(linearLayoutManager);
        rlResultList.setAdapter(queryResultAdapter);


        //初始化图层下拉栏中的图层列表
        List<String> layerNameList = new ArrayList<>();

        for(Layer layer : mMapView.getMap().getOperationalLayers()) {
            layerNameList.add(layer.getName());
        }

        CommonSpinnerAdapter commonSpinnerAdapter = new CommonSpinnerAdapter(getContext(), layerNameList);
        spinnerLayerList.setAdapter(commonSpinnerAdapter);


        //查询按钮
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取查询图层
                final FeatureLayer featureLayer = (FeatureLayer) mMapView.getMap().getOperationalLayers()
                        .get(spinnerLayerList.getSelectedItemPosition());

                //获取模糊查询关键字
                String search = txtQueryInfo.getText().toString();

                queryAttribute(featureLayer, search);


            }
        });
    }

    private void bindView(View rootView) {

    }


    /**
     *  属性查询
     * @param featureLayer
     * @param search
     */
    private void queryAttribute(FeatureLayer featureLayer, String search) {

        List<Feature> results = new ArrayList<>();//查询统计结果

        featureLayer.setSelectionWidth(15);
        featureLayer.setSelectionColor(Color.YELLOW);

        QueryParameters query = new QueryParameters();
        String whereStr = getWhereStrFunction(featureLayer, search);
        query.setWhereClause(whereStr);

        final ListenableFuture<FeatureQueryResult> featureQueryResult
                = featureLayer.getFeatureTable().queryFeaturesAsync(query);
        featureQueryResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {

                    FeatureQueryResult result = featureQueryResult.get();
                    Iterator<Feature> iterator = result.iterator();
                    Feature feature;

                    while (iterator.hasNext()) {
                        feature = iterator.next();
                        results.add(feature);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ToastUtils.showShort(getContext(),"查询出" + results.size() + "个符合要求的结果");
                queryResultAdapter.refreshData(results);
            }
        });

    }



    /**
     * 获取模糊查询字符串
     * @param featureLayer
     * @param search
     * @return
     */
    private String getWhereStrFunction(FeatureLayer featureLayer, String search) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fields = featureLayer.getFeatureTable().getFields();
        boolean isNumber = isNumberFunction(search);

        for (Field field : fields) {
            switch (field.getFieldType()) {
                case TEXT:
                    stringBuilder.append(" upper(");
                    stringBuilder.append(field.getName());
                    stringBuilder.append(") LIKE '%");
                    stringBuilder.append(search.toUpperCase());
                    stringBuilder.append("%' or");
                    break;
                case SHORT:
                case INTEGER:
                case FLOAT:
                case DOUBLE:
                case OID:
                    if(isNumber == true) {
                        stringBuilder.append(" upper(");
                        stringBuilder.append(field.getName());
                        stringBuilder.append(") = ");
                        stringBuilder.append(search);
                        stringBuilder.append(" or");
                    }
                    break;
                case UNKNOWN:
                case GLOBALID:
                case BLOB:
                case GEOMETRY:
                case RASTER:
                case XML:
                case GUID:
                case DATE:
                    break;
            }
        }
        String result = stringBuilder.toString();
        return result.substring(0, result.length() - 2);
    }

    /**
     * 判断是否为数字
     * @param string
     * @return
     */
    public boolean isNumberFunction(String string) {
        boolean result = false;
        Pattern pattern = Pattern.compile("^[-+]?[0-9]");
        if (pattern.matcher(string).matches()) {
            //数字
            result = true;
        } else {
            //非数字
        }
        //带小数的
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('^');
        stringBuilder.append('[');
        stringBuilder.append("-+");
        stringBuilder.append("]?[");
        stringBuilder.append("0-9]+(");
        stringBuilder.append('\\');
        stringBuilder.append(".[0-9");
        stringBuilder.append("]+)");
        stringBuilder.append("?$");
        Pattern pattern1 = Pattern.compile(stringBuilder.toString());
        if (pattern1.matcher(string).matches()) {
            //数字
            result = true;
        } else {
            //非数字
        }
        return  result;
    }


}
