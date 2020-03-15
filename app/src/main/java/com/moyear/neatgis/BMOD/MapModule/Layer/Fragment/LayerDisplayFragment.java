package com.moyear.neatgis.BMOD.MapModule.Layer.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.Renderer;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.UniqueValueRenderer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moyear.neatgis.Common.Adapter.CommonSpinnerAdapter;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.LayerRenderUtils;
import com.moyear.neatgis.Views.Widget.SimpleColorPicker;

import java.util.ArrayList;
import java.util.List;

import gisluq.lib.Util.ToastUtils;
import moyear.lib.graphics.AlphaPatternDrawable;


public class LayerDisplayFragment extends Fragment implements View.OnClickListener{

    private boolean hasChanged = false;

    private int RENDER_MODE_SIMPLE = 0;

    private int RENDER_MODE_UNIQUEVALUE = 1;

    public int currentRenderMode = 0;

    private int symbolFillColor = -1;//符号填充颜色

    private int symbolStrokeColor = -1;//符号描边颜色

    private float symbolSize;//符号大小

    private String selectedUniqueValueField = "";//所选中的特征值字段

    private List<String> valueFieldList;//字段列表

    private AlphaPatternDrawable simplePatternDrawable;//透明图案

    private Layer mTargetLayer;

    private FeatureLayer featureLayer;

    private AppCompatSpinner spinnerSymbolCategory;

    private AppCompatSpinner spinnerSymbolUniqueValue;

    private RelativeLayout rlColorFill;

    private RelativeLayout rlColorStroke;

    private RelativeLayout rlSymbolSize;

    private ImageView imgFillColor;

    private ImageView imgStrokeColor;

    private ImageView imgLayerFeatureShape;

    private TextView txtSymbolSize;

    private LinearLayout containerSingleSymbol, containerCategory;

    private FloatingActionButton btnSubmit;

    public LayerDisplayFragment(Layer targetLayer) {
        this.mTargetLayer = targetLayer;
        if (mTargetLayer instanceof FeatureLayer)
            featureLayer = (FeatureLayer) mTargetLayer;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layer_prop_display, container, false);

        if (featureLayer == null) {
            ToastUtils.showShort(getContext(),"TargetLayer is not a FeatureLayer,but a" + mTargetLayer.getClass().getName());
            return null;
        }

        initData();
        bindView(rootView);
        initView();

        return rootView;
    }

    private void initData() {
        simplePatternDrawable = new AlphaPatternDrawable(10);
    }


    private void bindView(View rootView) {
        containerSingleSymbol = rootView.findViewById(R.id.container_layer_prop_display_single_symbol);
        containerCategory = rootView.findViewById(R.id.container_layer_prop_display_category);

        spinnerSymbolCategory = rootView.findViewById(R.id.sp_layer_prop_symbol_category);

        rlColorFill = rootView.findViewById(R.id.rl_layer_prop_display_symbol_single_color_fill);
        rlColorStroke = rootView.findViewById(R.id.rl_layer_prop_display_symbol_single_color_stroke);
        rlSymbolSize = rootView.findViewById(R.id.rl_layer_prop_display_symbol_size);

        imgFillColor = rootView.findViewById(R.id.img_indicate_shape_color_fill);
        imgStrokeColor = rootView.findViewById(R.id.img_indicate_shape_color_stroke);
        imgLayerFeatureShape = rootView.findViewById(R.id.img_indicate_shape);

        txtSymbolSize = rootView.findViewById(R.id.txt_indicate_symbol_size);

        spinnerSymbolUniqueValue = rootView.findViewById(R.id.sp_layer_prop_symbol_field_unique_value);



        btnSubmit = rootView.findViewById(R.id.btn_changes_submit);

    }

    private void initView() {

        initCurrentSymbol();//根据当前图层的颜色来初始化布局

        //图层颜色模式切换下拉栏
        spinnerSymbolCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        containerSingleSymbol.setVisibility(View.VISIBLE);
                        containerCategory.setVisibility(View.GONE);

                        currentRenderMode = RENDER_MODE_SIMPLE;
                        break;
                    case 1:
                        containerCategory.setVisibility(View.VISIBLE);
                        containerSingleSymbol.setVisibility(View.GONE);

                        showUniqueValueRender();

                        currentRenderMode = RENDER_MODE_UNIQUEVALUE;
                        break;
                    default:
                        ToastUtils.showShort(getContext(),"选择不合理");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ToastUtils.showShort(getContext(),"尚未选中任何布局");
            }
        });

        //填充颜色
        rlColorFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initColor = symbolFillColor;//颜色选择对话框的初始颜色

                if (symbolFillColor == -1)
                    initColor = Color.rgb(255,255,255);

                SimpleColorPicker simpleColorPicker = new SimpleColorPicker(getContext(), initColor, new SimpleColorPicker.onColorSelectListener() {
                    @Override
                    public void setSelectedColor(int selectedColor) {
                        imgFillColor.setBackgroundColor(selectedColor);
                        symbolFillColor = selectedColor;

                        hasChanged = true;
                        btnSubmit.show();

                    }
                });

                simpleColorPicker.show(getFragmentManager(),"SimpleColorPicker");
            }
        });

        //描边颜色
        rlColorStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  initColor = symbolStrokeColor;//颜色选择对话框的初始颜色

                if (symbolStrokeColor == -1)
                    initColor = Color.rgb(255,255,255);

                SimpleColorPicker simpleColorPicker = new SimpleColorPicker(getContext(), initColor, new SimpleColorPicker.onColorSelectListener() {
                    @Override
                    public void setSelectedColor(int selectedColor) {
                        imgStrokeColor.setBackgroundColor(selectedColor);
                        symbolStrokeColor = selectedColor;

                        hasChanged = true;
                        btnSubmit.show();

                    }
                });

                simpleColorPicker.show(getFragmentManager(),"SimpleColorPicker");

            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasChanged) {
                    ToastUtils.showShort(getContext(), "尚未做出任何更改");
                    return;
                }

                if (currentRenderMode == RENDER_MODE_SIMPLE) {
                    setSimpleRender();
                } else if (currentRenderMode == RENDER_MODE_UNIQUEVALUE) {
                    setUniqueValueRender();
                } else {

                }

                ToastUtils.showShort(getContext(), "应用改变成功");

                btnSubmit.setRotation(0);
                ViewCompat.animate(btnSubmit)
                        .rotation(360)
                        .withLayer()
                        .setDuration(1000)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();

                btnSubmit.hide();
                hasChanged = false;

            }
        });

    }



    //首次加载shp文件使用随机颜色渲染,根据当前图层的颜色来初始化布局
    private void initCurrentSymbol() {
        //设置形状
        imgLayerFeatureShape.setImageBitmap(LayerRenderUtils.getLayerFeatureBitmap(getContext(), featureLayer));

        Renderer currentLayerRender = featureLayer.getRenderer();

        if (currentLayerRender instanceof SimpleRenderer) {
            SimpleRenderer simpleRenderer = (SimpleRenderer) currentLayerRender;
            Symbol currenSymbol = simpleRenderer.getSymbol();

            //标记（点）符号
            if (currenSymbol instanceof SimpleMarkerSymbol) {
                SimpleMarkerSymbol simpleMarkerSymbol = (SimpleMarkerSymbol) currenSymbol;

                SimpleLineSymbol simpleLineSymbol = simpleMarkerSymbol.getOutline();//点要素的轮廓


                int interiorColor = simpleMarkerSymbol.getColor();//内部颜色

                symbolFillColor = interiorColor;

                imgFillColor.setBackgroundColor(interiorColor);

                if (simpleLineSymbol == null) {
                    imgStrokeColor.setBackground(simplePatternDrawable);

                } else {
                    int outlineColor = simpleLineSymbol.getColor();//轮廓颜色
                    imgStrokeColor.setBackgroundColor(outlineColor);

                    symbolStrokeColor = outlineColor;
                }

                //设置符号大小
                symbolSize = simpleMarkerSymbol.getSize();
                txtSymbolSize.setText(symbolSize + "");

            } else if (currenSymbol instanceof SimpleLineSymbol) {//线要素
                SimpleLineSymbol simpleLineSymbol = (SimpleLineSymbol) currenSymbol;

                int lineSymbolColor = simpleLineSymbol.getColor();//线要素的颜色
                symbolFillColor = lineSymbolColor;

                imgFillColor.setBackgroundColor(lineSymbolColor);

                imgStrokeColor.setImageDrawable(simplePatternDrawable);

                //设置符号大小
                symbolSize = simpleLineSymbol.getWidth();
                txtSymbolSize.setText(symbolSize + "");

            } else if (currenSymbol instanceof SimpleFillSymbol) {//面要素
                SimpleFillSymbol simpleFillSymbol = (SimpleFillSymbol) currenSymbol;
                int fillColor = simpleFillSymbol.getColor();
                symbolFillColor = fillColor;

                LineSymbol outLineSymbol = simpleFillSymbol.getOutline();

                if (outLineSymbol != null) {
                    int outlineColor = outLineSymbol.getColor();
                    symbolStrokeColor = outlineColor;
                    imgStrokeColor.setBackgroundColor(outlineColor);
                } else {
                    imgStrokeColor.setImageDrawable(simplePatternDrawable);
                }

                imgFillColor.setBackgroundColor(fillColor);

            } else {
                imgFillColor.setBackground(simplePatternDrawable);

                ToastUtils.showShort(getContext(), "Current layer render symbol is "
                        + currenSymbol.getClass().getSimpleName());
            }

        } else {
            ToastUtils.showShort(getContext(), "Current layer render is not SimpleRenderer,but a "
                    + currentLayerRender.getClass().getSimpleName());


            imgFillColor.setBackground(simplePatternDrawable);
        }

    }


    private void setSimpleRender() {

        if (!hasChanged)
            return;

        SimpleRenderer renderer = null;

        // 根据要素类型，设置Shapefile文件的渲染方式
        switch (featureLayer.getFeatureTable().getGeometryType()) {
            case POINT://点要素图层
                SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, symbolFillColor, 8);
                if (symbolStrokeColor != -1) {
                    SimpleLineSymbol markerLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, symbolStrokeColor, 1.0f);
                    simpleMarkerSymbol.setOutline(markerLineSymbol);
                }
                renderer = new SimpleRenderer(simpleMarkerSymbol);

                break;
            case POLYLINE://线要素图层
                //TODO:解决线要素描边颜色,大小问题
                if (symbolFillColor == -1) {
                    ToastUtils.showShort(getContext(), "线要素图层颜色，请设置填充而不是描边");
                    return;
                } else {
                    SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, symbolFillColor, 1.0f);
                    renderer = new SimpleRenderer(simpleLineSymbol);
                }
                break;
            case POLYGON://面要素图层
                SimpleLineSymbol outlineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, symbolStrokeColor, 1.0f);
                SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, symbolFillColor, outlineSymbol);
                renderer = new SimpleRenderer(simpleFillSymbol);

                break;
            default:
                SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, symbolStrokeColor, 1.0f);
                SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, symbolFillColor, lineSymbol);
                renderer = new SimpleRenderer(fillSymbol);

                ToastUtils.showShort(getContext() ,"非点线面要素暂不支持处理");

        }

        featureLayer.setRenderer(renderer);

    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rl_layer_prop_display_symbol_single_color_stroke:
//                break;
//        }

    }

    private void showUniqueValueRender() {
        valueFieldList = new ArrayList<>();

        List<Field> fields = featureLayer.getFeatureTable().getFields();
        for (Field field : fields) {
            valueFieldList.add(field.getName());

        }

        CommonSpinnerAdapter commonSpinnerAdapter = new CommonSpinnerAdapter(getContext(), valueFieldList);
        spinnerSymbolUniqueValue.setAdapter(commonSpinnerAdapter);

        spinnerSymbolUniqueValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUniqueValueField = valueFieldList.get(position);

                hasChanged = true;
                btnSubmit.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setUniqueValueRender() {

        if (selectedUniqueValueField.equals(""))
            return;

        int BLACK = 0x00000000;
        int RED = 0xFFFF0000;
        int BLUE = 0xFF0000FF;

        SimpleMarkerSymbol redMarker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, RED, 10);
        SimpleMarkerSymbol blueMarker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.TRIANGLE, BLUE, 10);
        SimpleMarkerSymbol defaultMarker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, BLACK, 10);

        UniqueValueRenderer uniqueValRenderer = new UniqueValueRenderer(null, null, null, defaultMarker);
        uniqueValRenderer.getFieldNames().add(selectedUniqueValueField);

        FeatureTable featureTable = featureLayer.getFeatureTable();

        List<Field> fields = featureTable.getFields();

        uniqueValRenderer.setDefaultLabel("DefaultLabel");
        uniqueValRenderer.setDefaultSymbol(defaultMarker);

//        QueryParameters queryParameters = new QueryParameters();
//        queryParameters.setWhereClause();
//
//        featureLayer.getFeatureTable().queryFeaturesAsync();


        for (Field field : fields ) {


        }

        List<Object> value = new ArrayList<>();

        value.add("长沙");
        UniqueValueRenderer.UniqueValue redUV = new UniqueValueRenderer.UniqueValue("Color: Red", "Red", redMarker, value);
        uniqueValRenderer.getUniqueValues().add(redUV);

        List<Object> blueValue = new ArrayList<>();
        blueValue.add("北京");
        UniqueValueRenderer.UniqueValue blueUV = new UniqueValueRenderer.UniqueValue("Color: Blue", "Blue", blueMarker, blueValue);
        uniqueValRenderer.getUniqueValues().add(blueUV);

        featureLayer.setRenderer(uniqueValRenderer);
    }




}
