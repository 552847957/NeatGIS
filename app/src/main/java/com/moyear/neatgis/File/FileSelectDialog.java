package com.moyear.neatgis.File;

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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.material.tabs.TabLayout;
import com.moyear.neatgis.File.Adapter.FileChooserFragmentPagerAdapter;
import com.moyear.neatgis.File.Fragment.LibraryFileChooseFragment;
import com.moyear.neatgis.File.Fragment.SDFileSelectFragment;
import com.moyear.neatgis.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gisluq.lib.Util.ScreenUtils;

public class FileSelectDialog extends DialogFragment {

    public static final int SELECTED_MODE_SINGLE_FILE = 0x001;//文件单选模式
    public static final int SELECTED_MODE_SINGLE_FOLDER = 0x002;//文件夹单选模式
    public static final int SELECTED_MODE_MULTI_FILE = 0x003;//文件多选模式
    public static final int SELECTED_MODE_MULTI_FOLDER = 0x004;//文件夹多选模式

    private Context mContext;

    private boolean isFullScreen = false;

    private int selectedMode = -1;

    private OnSingleFileSelectListener onSingleFileSelectListener;

    private OnMultiFileSelectListener onMultiFileSelectListener;

    private List<String> tabTtileList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    /**
     * 文件多选监听器
     * 将所选的文件目录传递给activity
     *
     */
    public interface OnMultiFileSelectListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void onSelectedFiles(String[] selectedFilesPath);
    }

    /**
     * 文件单选监听器
     * 将所选的文件目录传递给activity
     *
     */
    public interface OnSingleFileSelectListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void onSelectedFile(String selectedFilePath);
    }

    //单选构造器
    public FileSelectDialog(Context context, int selectedMode, OnSingleFileSelectListener onSingleFileSelectListener) {
        this.mContext = context;
        this.selectedMode = selectedMode;
        this.onSingleFileSelectListener = onSingleFileSelectListener;
    }

    //文件单选构造器
    public FileSelectDialog(Context context, OnSingleFileSelectListener onSingleFileSelectListener) {

//        FileSelectDialog(context, SELECTED_MODE_SINGLE_FILE, onSingleFileSelectListener);
        this.mContext = context;
        this.selectedMode = SELECTED_MODE_SINGLE_FILE;
        this.onSingleFileSelectListener = onSingleFileSelectListener;
    }

    //文件多选构造器
    public FileSelectDialog(Context context, OnMultiFileSelectListener onMultiFileSelectListener) {
        this.mContext = context;
        this.selectedMode = SELECTED_MODE_MULTI_FILE;
        this.onMultiFileSelectListener = onMultiFileSelectListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_file_chooser, container, false);

        initToolBar(rootView);
        initFragment(rootView);

        return rootView;

    }

    private void initToolBar(View rootView) {
        RelativeLayout rlToolbar = rootView.findViewById(R.id.toolbar);
        ImageButton btnClose = rootView.findViewById(R.id.btn_dialog_file_chooser_close);
        ImageButton btnMenu = rootView.findViewById(R.id.btn_dialog_file_chooser_more);
        ImageButton btnFullScreen = rootView.findViewById(R.id.btn_dialog_file_chooser_fullscreen);
        //TextView txtLayerAttr = rootView.findViewById(R.id.txt_dialog_layer_attr);

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

    private void initFragment(View rootView) {

        tabTtileList.add("库");
        LibraryFileChooseFragment libraryFileChooseFragment = new LibraryFileChooseFragment(this);
        fragmentList.add(libraryFileChooseFragment);

        tabTtileList.add("本地");
        SDFileSelectFragment sdFileChooseFragment = new SDFileSelectFragment(this);
        fragmentList.add(sdFileChooseFragment);

        FileChooserFragmentPagerAdapter sectionsPagerAdapter =
                new FileChooserFragmentPagerAdapter(mContext, getChildFragmentManager(), tabTtileList, fragmentList);

        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = rootView.findViewById(R.id.tab_file_chooser);
        tabs.setupWithViewPager(viewPager);
    }

    /**
     * 获取文件选择模式
     *
     * @return
     */
    public int getSelectedMode() {
        return selectedMode;
    }


    /**
     * 获取文件单选监听器
     *
     * @return
     */
    public OnSingleFileSelectListener getOnSingleFileSelectListener() {
        return onSingleFileSelectListener;
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
