package com.moyear.neatgis.BMOD.MapModule.Layer.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.moyear.neatgis.R;

import gisluq.lib.Util.ToastUtils;

public class LayerGeneralFragment extends Fragment {

    private Layer mTargetLayer;

    private FeatureLayer featureLayer;

    EditText edtLayerName;
    EditText edtLayerSpatial;

    TextView txtExtentYMax;
    TextView txtExtentYMin;
    TextView txtExtentXMin;
    TextView txtExtentXMax;


    RadioGroup rgFeatureLayerType;
    RadioButton rbPoint;
    RadioButton rbPolyline;
    RadioButton rbPolygon;

    public LayerGeneralFragment(Layer targetLayer) {
        this.mTargetLayer = targetLayer;

        if (mTargetLayer instanceof FeatureLayer) {
            featureLayer = (FeatureLayer) mTargetLayer;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layer_prop_general, container, false);

        bindView(rootView);
        initView(rootView);

        return rootView;
    }

    //绑定组件
    private void bindView(View rootView) {
        edtLayerName = rootView.findViewById(R.id.edt_layer_attr_general_layer_name);
        edtLayerSpatial = rootView.findViewById(R.id.edt_layer_attr_general_spatial);


        rgFeatureLayerType = rootView.findViewById(R.id.rg_dialog_layer_attr_general_layer_type);

        txtExtentYMax = rootView.findViewById(R.id.txt_layer_prop_general_extent_y_max);
        txtExtentYMin = rootView.findViewById(R.id.txt_layer_prop_general_extent_y_min);
        txtExtentXMin = rootView.findViewById(R.id.txt_layer_prop_general_extent_x_min);
        txtExtentXMax = rootView.findViewById(R.id.txt_layer_prop_general_extent_x_max);

    }

    //初始化布局
    private void initView(View rootView) {
        if (mTargetLayer == null) {
            ToastUtils.showShort(getContext(),"TargetLayer is null");
            return;
        }

        edtLayerName.setText(mTargetLayer.getName());
        edtLayerSpatial.setText(mTargetLayer.getSpatialReference().getWkid() + "");
        edtLayerSpatial.setEnabled(false);

        Envelope envelope = mTargetLayer.getFullExtent();

        txtExtentYMax.setText(envelope.getYMax() + "");
        txtExtentYMin.setText(envelope.getYMin() + "");
        txtExtentXMax.setText(envelope.getXMax() + "");
        txtExtentXMin.setText(envelope.getXMin() + "");

        if (featureLayer != null) {

            rbPoint = rootView.findViewById(R.id.rb_dialog_layer_attr_general_type_point);
            rbPolyline = rootView.findViewById(R.id.rb_dialog_layer_attr_general_type_polyline);
            rbPolygon = rootView.findViewById(R.id.rb_dialog_layer_attr_general_type_polygon);

            switch (featureLayer.getFeatureTable().getGeometryType()) {
                case POINT:
                    rbPoint.setChecked(true);
                    break;
                case POLYLINE:
                    rbPolyline.setChecked(true);
                    break;
                case POLYGON:
                    rbPolygon.setChecked(true);
                    break;
                case MULTIPOINT:
                    break;
                case UNKNOWN:
                    break;
            }

            //不可编辑
            rbPoint.setEnabled(false);
            rbPolygon.setEnabled(false);
            rbPolyline.setEnabled(false);

        } else {
            TextView txtLayerType = rootView.findViewById(R.id.txt_dialog_layer_attr_general_layer_type);
            txtLayerType.setVisibility(View.VISIBLE);
            txtLayerType.setText(mTargetLayer.getClass().getSimpleName());

            rgFeatureLayerType.setVisibility(View.GONE);

        }


    }

}
