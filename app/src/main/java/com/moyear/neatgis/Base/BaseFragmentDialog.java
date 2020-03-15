package com.moyear.neatgis.Base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moyear.neatgis.R;

import java.lang.reflect.Field;

import gisluq.lib.Util.ScreenUtils;


/**
 *拓展对话框
 * 实现可以添加tablayout、可以选择是否全屏，以及是否固定等功能
 * created by moyear on 2020.02.29
 *
 */
public class BaseFragmentDialog extends DialogFragment {

    Context mContext;

    private String title;

    private boolean isFullScreen = false;

    private View contentView;

    private TextView txtDialogTitle;

    private ImageButton btnMenu;

    private LinearLayout lvContenView;

    public BaseFragmentDialog() {

    }

    public BaseFragmentDialog(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_frament_dialog_base, container, false);
        lvContenView = rootView.findViewById(R.id.lv_content);

        initToolBar(rootView);

        initView();

        return rootView;
    }

    private void initView() {
        if (contentView != null)
            lvContenView.addView(contentView);
    }

    public void setView(View view) {
        if (view == null)
            return;

        contentView = view;
    }


    private void initToolBar(View rootView) {
        RelativeLayout rlToolbar = rootView.findViewById(R.id.toolbar);
        ImageButton btnClose = rootView.findViewById(R.id.btn_dialog_fragment_base_close);
        btnMenu = rootView.findViewById(R.id.btn_dialog_fragment_base_more);
        ImageButton btnFullScreen = rootView.findViewById(R.id.btn_dialog_fragment_base_fullscreen);
        txtDialogTitle = rootView.findViewById(R.id.txt_dialog_fragment_base_title);

        if (txtDialogTitle != null)
            txtDialogTitle.setText(title);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                            if (isFullScreen) {
                                int height = (int) (ScreenUtils.getScreenHeight(getContext()) * 0.8);
                                int width = (int) (ScreenUtils.getScreenWidth(getContext()) * 0.9);

                                params.width = width;
                                params.height = height;

                                isFullScreen = false;
                            } else {
                                // 动态获取屏幕高度（不含状态栏），以避免状态栏变黑
                                params.height = ScreenUtils.getScreenHeight(getContext()) - ScreenUtils.getStatusHeight(getContext());
                                params.width = ViewGroup.LayoutParams.MATCH_PARENT;

                                isFullScreen = true;
                            }
                            window.setAttributes(params);
                        }
                    }
                }

            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu pm = new PopupMenu(getContext(), v);
                pm.getMenuInflater().inflate(R.menu.menu_dialog_layer_attr, pm.getMenu());

                //根据对话框是否可点击背景取消切换menu的文本
                String strFixable = isCancelable() ? "固定":"不固定";
                pm.getMenu().findItem(R.id.menu_dialog_layer_attr_item_fix).setTitle(strFixable);

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        switch (item.getItemId()) {
                            case R.id.menu_dialog_layer_attr_item_fix://切换对话框是否固
                                setCancelable(!isCancelable());
                                break;
                            case R.id.menu_dialog_layer_attr_item_setting:
                                Toast.makeText(getContext(), "设置，代码待写!!!", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                pm.show();

            }
        });

    }

    private void setDialogTitle(String title) {
        txtDialogTitle.setText(title);
    }

    /**
     *设置标题
     *
     * @param title
     */
    public static void setTitle(String title) {
        setTitle(title);
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



}
