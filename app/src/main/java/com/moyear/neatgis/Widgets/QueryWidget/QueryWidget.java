package com.moyear.neatgis.Widgets.QueryWidget;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.google.android.material.tabs.TabLayout;
import com.moyear.neatgis.BMOD.MapModule.BaseWidget.BaseWidget;
import com.moyear.neatgis.Common.Adapter.CommonFragmentPagerAdapter;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment.BaseMapListFragment;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment.LegendListFragment;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment.OperationLayerListFragment;
import com.moyear.neatgis.Widgets.QueryWidget.Adapter.LayerSpinnerAdapter;
import com.moyear.neatgis.Widgets.QueryWidget.Adapter.QueryResultAdapter;
import com.moyear.neatgis.Widgets.QueryWidget.Fragment.AttributeQueryFragment;
import com.moyear.neatgis.Widgets.QueryWidget.Fragment.MapQueryFragment;
import com.moyear.neatgis.Widgets.QueryWidget.Listener.MapQueryOnTouchListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import gisluq.lib.Util.ToastUtils;

/**
 * 属性查询组件-图查属性，属性查图
 * Created by gis-luq on 2018/3/10.
 */
public class QueryWidget extends BaseWidget {

    private static String TAG = "QueryWidget";
    private View.OnTouchListener defauleOnTouchListener;//默认点击事件
    private MapQueryOnTouchListener mapQueryOnTouchListener;//要素选择事件

    public View mWidgetView = null;//

    /**
     * 组件面板打开时，执行的操作
     * 当点击widget按钮是, WidgetManager将会调用这个方法，面板打开后的代码逻辑.
     * 面板关闭将会调用 "inactive" 方法
     */
    @Override
    public void active() {
        super.active();//默认需要调用，以保证切换到其他widget时，本widget可以正确执行inactive()方法并关闭
        super.showWidget(mWidgetView);//加载UI并显示

        initMapQuery();
    }

    /**
     * widget组件的初始化操作，包括设置view内容，逻辑等
     * 该方法在应用程序加载完成后执行
     */
    @Override
    public void create() {
        this.context = context;
        defauleOnTouchListener = super.mapView.getOnTouchListener();

        initWidgetView();//初始化UI
    }

    /**
     * 组件面板关闭时，执行的操作
     * 面板关闭将会调用 "inactive" 方法
     */
    @Override
    public void inactive(){
        super.inactive();
        returnDefault();
    }

    /**
     * 初始化UI
     */
    private void initWidgetView() {
        /**
         * **********************************************************************************
         * 布局容器
         */
        //设置widget组件显示内容
        mWidgetView = LayoutInflater.from(super.context).inflate(R.layout.widget_view_query,null);
        TabLayout tabLayout = mWidgetView.findViewById(R.id.tabs_feature_query);
        ViewPager viewPager = mWidgetView.findViewById(R.id.viewpager_feature_query);

        List<String> tabTitleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        tabTitleList.add("图查属性");
        fragmentList.add(new MapQueryFragment(super.mapView));
        tabTitleList.add("属性查图");
        fragmentList.add(new AttributeQueryFragment(super.mapView));

        AppCompatActivity activity = (AppCompatActivity) context;
        CommonFragmentPagerAdapter sectionsPagerAdapter =
                new CommonFragmentPagerAdapter(context, activity.getSupportFragmentManager(), tabTitleList, fragmentList);

        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //图查属性tab被选择
                if (tab.getPosition() == 0) {
                    initMapQuery();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //图查属性tab没有被选择
                if (tab.getPosition() == 0) {
                    returnDefault();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * 初始化图查属性
     */
    private void initMapQuery() {
        mapView.setMagnifierEnabled(true);//放大镜
        if (mapQueryOnTouchListener != null) {
            super.mapView.setOnTouchListener(mapQueryOnTouchListener);

            mapQueryOnTouchListener.clear();//清空当前选择
        }
    }

    /**
     * 恢复默认状态
     */
    private void returnDefault() {
        if (mapQueryOnTouchListener != null) {
            super.mapView.setOnTouchListener(defauleOnTouchListener);//窗口关闭恢复默认点击状态
        }
//        mapQueryOnTouchListener.clear();//清空当前选择
        mapView.setMagnifierEnabled(false);//放大镜
    }

}

