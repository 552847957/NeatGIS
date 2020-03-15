package com.moyear.neatgis.BMOD.MapModule.Layer.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Activity.ui.main.PageViewModel;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Adapter.LayerFieldsListAdapter;

import java.util.List;

import gisluq.lib.Util.ToastUtils;

public class LayerFieldFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private List<Field> listFileds;

    private Layer mTargetLayer;

    private FeatureLayer featureLayer;

    private RecyclerView rvFieldList;


    public LayerFieldFragment(Layer targetLayer) {
        this.mTargetLayer = targetLayer;

        if (mTargetLayer instanceof FeatureLayer) {
            featureLayer = (FeatureLayer) mTargetLayer;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        if (featureLayer == null) {
            ToastUtils.showShort(getContext(),"TargetLayer is not a FeatureLayer,but a" + mTargetLayer.getClass().getName());
            return null;
        }

        View rootView = inflater.inflate(R.layout.fragment_layer_prop_field, container, false);

        bindView(rootView);
        initView();

        return rootView;
    }

    private void bindView(View rootView) {
        rvFieldList = rootView.findViewById(R.id.rv_layer_attr_field_layer_fields_list);
        
    }

    private void initView() {

        listFileds = featureLayer.getFeatureTable().getFields();

        LayerFieldsListAdapter layerFieldsListAdapter = new LayerFieldsListAdapter(getContext(),listFileds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFieldList.setLayoutManager(linearLayoutManager);
        rvFieldList.setAdapter(layerFieldsListAdapter);

    }



}