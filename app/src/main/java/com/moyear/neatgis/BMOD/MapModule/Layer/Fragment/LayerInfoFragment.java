package com.moyear.neatgis.BMOD.MapModule.Layer.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.moyear.neatgis.R;

import gisluq.lib.Util.ToastUtils;

public class LayerInfoFragment extends Fragment {

    private Layer mTargetLayer;

    private FeatureLayer mFeatureLayer;

    EditText edtLayerDesc;
    EditText edtLayerCredit;

    TextView txtLayerInfoDetail;

    public LayerInfoFragment(Layer targetLayer) {
        this.mTargetLayer = targetLayer;
        if (mFeatureLayer instanceof FeatureLayer) {
            mFeatureLayer = (FeatureLayer) mTargetLayer;
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layer_prop_info, container, false);

        if (mTargetLayer == null) {
            ToastUtils.showShort(getContext(),"TargetLayer is null");
            return null;
        }

        bindView(rootView);
        initView();

        return rootView;
    }

    private void bindView(View rootView) {

        edtLayerDesc = rootView.findViewById(R.id.edt_layer_attr_info_layer_desc);
        edtLayerCredit = rootView.findViewById(R.id.edt_layer_attr_info_layer_credit);

        txtLayerInfoDetail = rootView.findViewById(R.id.txt_layer_prop_info_layer_info_detail);

    }

    private void initView() {

        edtLayerDesc.setText(mTargetLayer.getDescription());
        edtLayerCredit.setText(mTargetLayer.getAttribution());


        txtLayerInfoDetail.setText(getLayerInfoDetail());
    }

    /**
     * 获取图层的详细信息
     *
     * @return
     */
    private String getLayerInfoDetail() {
        String layerInfoDetail = "";
        String layerType = "图层类型：" + mTargetLayer.getClass().getSimpleName();
        String layerName = "图层名：" + mTargetLayer.getName();
        String layerId = "图层ID：" + mTargetLayer.getId();

        String layerItemName = "是否可识别要素：" + mTargetLayer.isIdentifyEnabled();

        layerInfoDetail = layerType + "\n" + layerName + "\n" + layerId + "\n" + layerItemName;
        return layerInfoDetail;
    }

}
