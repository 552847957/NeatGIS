package com.moyear.neatgis.BMOD.MapModule.Resource;

import android.app.Activity;
import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.R;

import gisluq.lib.Util.SysUtils;

/**
 * 资源绑定注册类
 */
public class ResourceConfig {

    public Context context;
    private Activity activiy;

    public ResourceConfig(Context context) {
        this.context = context;
        this.activiy = (Activity)context;

        initConfig();
    }

    /**资源列表**/
    public MapView mapView = null;//地图控件
    public RelativeLayout compassView = null;//指北针控件
    public TextView txtMapScale = null;//比例尺
    public View baseWidgetView = null;//widget组件
    public ImageView imgCenterView = null;//中心十字叉
    public ToggleButton togbtnLocation = null;//定位按钮
    public TextView txtLocation = null;
    public FloatingActionButton btnPointCollect = null;//采集点按钮

    public LinearLayout baseWidgetToolsView;//widget组件工具列表

    public BottomSheetBehavior bottomSheetWidgetView;//底部组件bottomSheetView

    /**
     * 初始化资源列表
     */
    private void initConfig() {
        this.mapView = (MapView)activiy.findViewById(R.id.activity_map_mapview);
        this.compassView = (RelativeLayout)activiy.findViewById(R.id.activity_map_compass);
        this.txtMapScale =  (TextView)activiy.findViewById(R.id.activity_map_mapview_scale);
        this.baseWidgetView = activiy.findViewById(R.id.base_widget_view_baseview);
        this.imgCenterView = (ImageView)activiy.findViewById(R.id.activity_map_imgCenterView);
        this.togbtnLocation = (ToggleButton) activiy.findViewById(R.id.activity_map_togbtnLocation);

        this.txtLocation = (TextView)activiy.findViewById(R.id.activity_map_mapview_locationInfo);

        this.baseWidgetToolsView = (LinearLayout)activiy.findViewById(R.id.base_widget_view_tools_linerview);

        this.btnPointCollect = activiy.findViewById(R.id.activity_map_faBtnpointCollect);


        /****
         * added by moyear
         * on 2020.02.11
         */
        //手机模式则加载bottomSheet组件布局
        boolean ispad = SysUtils.isPad(context);
        if (!ispad) {
            bottomSheetWidgetView = BottomSheetBehavior.from(((Activity)context).findViewById(R.id.base_widget_view_baseview));
        }

    }


}
