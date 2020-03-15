package com.moyear.neatgis.Views.Widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.slider.Slider;
import com.moyear.neatgis.R;

import gisluq.lib.Util.ScreenUtils;

import static com.moyear.neatgis.Utils.ColorUtils.toHexEncoding;

public class SimpleColorPicker extends DialogFragment {

    private Context mContext;

    private onColorSelectListener listener;

    private int oldColor = -1;

    private int red = 255;
    private int green = 255;
    private int blue = 255;

    private View colorView;
    private Slider redSlider, greenSlider, blueSlider;
    private TextView txtRedValue, txtGreenValue, txtBlueValue;
    private TextView txtNewColor;

    private Button btnOK;
    private Button btnCancel;

    /**
     * 自定义监听器
     * 将所选的颜色传递给activity
     *
     */
    public interface onColorSelectListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void setSelectedColor(int selectedColor);
    }


    public SimpleColorPicker(Context context,onColorSelectListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    public SimpleColorPicker(Context context,int oldColor,onColorSelectListener listener) {
        this.mContext = context;
        this.oldColor = oldColor;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_layout_widget_simple_colorpicker, container, false);

        bindView(rootView);
        initView();

        return rootView;

    }

    private void bindView(View rootView) {
        colorView = rootView.findViewById(R.id.colorView);

        redSlider = rootView.findViewById(R.id.slider_dialog_colorpicker_red);
        greenSlider = rootView.findViewById(R.id.slider_dialog_colorpicker_green);
        blueSlider = rootView.findViewById(R.id.slider_dialog_colorpicker_blue);

        txtRedValue = rootView.findViewById(R.id.redToolTip);
        txtGreenValue = rootView.findViewById(R.id.greenToolTip);
        txtBlueValue = rootView.findViewById(R.id.blueToolTip);

        txtNewColor = rootView.findViewById(R.id.txt_newcolor);

        btnOK = rootView.findViewById(R.id.btn_ok);
        btnCancel = rootView.findViewById(R.id.btn_cancel);

    }

    private void initView() {

        if (oldColor != -1) {
            red = Color.red(oldColor);
            green = Color.green(oldColor);
            blue = Color.blue(oldColor);

            resetPickerByRGB(red, green, blue);
        }


        redSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                red = (int)value;
                showColor();
                txtRedValue.setText(red + "");
            }
        });

        greenSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                green = (int)value;
                showColor();
                txtGreenValue.setText(green + "");
            }
        });

        blueSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                blue = (int)value;
                showColor();
                txtBlueValue.setText(blue + "");
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setSelectedColor(getSelectedColor());
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     *
     * 根据rgb初始化选择器中所有颜色相关内容
     *
     * @param red
     * @param green
     * @param blue
     */
    private void resetPickerByRGB(int red, int green, int blue) {

        int color = Color.rgb(red, green, blue);

        colorView.setBackgroundColor(color);

        redSlider.setValue(red);
        greenSlider.setValue(green);
        blueSlider.setValue(blue);

        txtRedValue.setText(red + "");
        txtGreenValue.setText(green + "");
        txtBlueValue.setText(blue + "");

    }


    private void showColor() {
        int newColor = Color.rgb(red,green,blue);
        colorView.setBackgroundColor(newColor);

        txtNewColor.setText(toHexEncoding(newColor));

    }

    @Override
    public void onStart() {
        super.onStart();

        setCancelable(false);//设置不可单击取消

        Window window = getDialog().getWindow();
        if (window != null) {
            // 一定要设置Background，如果不设置，window属性设置无效
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            DisplayMetrics dm = new DisplayMetrics();
            if (getActivity() != null) {
                WindowManager windowManager = getActivity().getWindowManager();
                if (windowManager != null) {
                    windowManager.getDefaultDisplay().getMetrics(dm);
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.CENTER;

                    int height = (int) (ScreenUtils.getScreenHeight(getContext()) * 0.8);
                    int width = (int) (ScreenUtils.getScreenWidth(getContext()) * 0.9);

                    // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
                    //params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.width = width;
                    params.height = height;
                    window.setAttributes(params);
                }
            }
        }
    }


    public int getSelectedColor() {

        return Color.rgb(red,green,blue);

    }


}
