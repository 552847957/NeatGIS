package com.moyear.neatgis.BMOD.MapModule.Layer.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.layers.Layer;
import com.moyear.neatgis.R;

public class LayerTagFragment extends Fragment {


    private Layer mTargetLayer;


    public LayerTagFragment(Layer targetLayer) {
        this.mTargetLayer = targetLayer;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layer_prop_tag, container, false);

        bindView(rootView);
        initView();

        return rootView;
    }


    private void bindView(View rootView) {

    }

    private void initView() {


    }

}
