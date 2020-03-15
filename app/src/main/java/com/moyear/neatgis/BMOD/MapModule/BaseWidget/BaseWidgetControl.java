package com.moyear.neatgis.BMOD.MapModule.BaseWidget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moyear.neatgis.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import gisluq.lib.Util.SysUtils;

/**
 * 应用程序组件基类
 * 负责和UI交互
 * Created by gis-luq on 2018/4/10.
 *
 * edited by moyear on 2020.02.11.
 *
 */

public class BaseWidgetControl {


    /**是否为平板**/
    boolean isPad = false;

    /**上下文，创建view的时候需要用到*/
    private Context context;

    /**视图-全部*/
    private View baseWidgetView;

    /**组件视图-包括打开按钮*/
    private View widgetView;

    /**标题*/
    private TextView txtTitleView;

    /**从内存关闭组件*/
    private ImageButton btnRemoveWidget;

    /**打开组件*/
    private ImageButton btnOpenWidget;

    /**零时关闭组件*/
    private ImageButton btnClosedWidget;

    /**widget更多按钮组件*/
    private ImageButton btnMore;

    /**内容区域*/
    public RelativeLayout baseWidgetViewContext;

    /**组件扩展区域*/
    public RelativeLayout baseWidgetExtentView;

    /**底部组件bottomSheetView**/
    BottomSheetBehavior bottomSheetWidgetView;

    private BaseWidget widget;//组件信息

    public BaseWidgetControl(Context context) {
        this.context = context;
        //检测是否为平板
        isPad = SysUtils.isPad(context);

        //初始化组件view
        initBaseWidgetView();
        //默认不显示widget组件
        baseWidgetView.setVisibility(View.GONE);//默认状态

    }

    public void setTitle( String title) {
        txtTitleView.setText(title);
    }

    /**
     * 显示组件视图
     * @param v
     */
    public void startBaseWiget(View v) {

        ViewParent viewParent = v.getParent();
        if (viewParent != null) {
            RelativeLayout vp = (RelativeLayout) viewParent;
            vp.removeAllViews();//取消v的父类view与v的绑定关系，否则v将无法添加道widget中
        }

        this.baseWidgetViewContext.removeAllViews();
        this.baseWidgetViewContext.addView(v);

        if (isPad) {
            baseWidgetView.setVisibility(View.VISIBLE);//Widget主框架-显示
            widgetView.setVisibility(View.VISIBLE);//Widget视图-显示
            btnOpenWidget.setVisibility(View.GONE);//打开按钮-默认不显示

            animOpen();//打开组件开启动画
        } else {
            baseWidgetView.setVisibility(View.VISIBLE);//Widget主框架-显示
            widgetView.setVisibility(View.VISIBLE);//Widget视图-显示
            btnOpenWidget.setVisibility(View.GONE);//打开按钮-默认不显示

            if (bottomSheetWidgetView != null) {
                bottomSheetWidgetView.setState(BottomSheetBehavior.STATE_COLLAPSED);
                btnOpenWidget.setVisibility(View.GONE);//不显示widget显示按钮
            } else {
                Log.e("TAG","bottomSheetWidgetView is null!");
            }
        }

    }


    /**
     * 初始化组件视图
     */
    private void initBaseWidgetView() {
        baseWidgetView = ((Activity)context).findViewById(R.id.base_widget_view_baseview);
        widgetView = ((Activity)context).findViewById(R.id.base_widget_view_widgetview);

        txtTitleView = (TextView) baseWidgetView.findViewById(R.id.base_widget_view_txtTitle);
        btnRemoveWidget = baseWidgetView.findViewById(R.id.base_widget_view_btnRemove);
        btnOpenWidget = ((Activity)context).findViewById(R.id.base_widget_view_btnOpen);//有修改 2020.02.07
        btnClosedWidget = baseWidgetView.findViewById(R.id.btn_base_widget_view_close);
        btnMore = baseWidgetView.findViewById(R.id.base_widget_view_btnMore);//新增 add by moyear on 2020.02.26
        baseWidgetViewContext = (RelativeLayout) baseWidgetView.findViewById(R.id.base_widget_view_ContextView);
        baseWidgetExtentView = (RelativeLayout) baseWidgetView.findViewById(R.id.base_widget_view_widgetExtendview);

        if (!isPad) {//如果为手机模式，则加载bottomSheet形式的widget
            bottomSheetWidgetView = BottomSheetBehavior.from(((Activity)context).findViewById(R.id.base_widget_view_baseview));
            //默认为隐藏状态
            bottomSheetWidgetView.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        btnOpenWidget.setVisibility(View.GONE);//默认不显示

        //关闭widget
        btnRemoveWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                widget.inactive();//widget处于关闭状态
                //bottomSheetWidgetView.setState(BottomSheetBehavior.STATE_COLLAPSED);
                baseWidgetView.setVisibility(View.GONE);

                animClosed();
            }
        });

        //打开widget：展开widget按钮的点击事件
        btnOpenWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPad) {
                    widgetView.setVisibility(View.VISIBLE);
                    btnOpenWidget.setVisibility(View.GONE);

                    animOpen();
                } else {
                    if (bottomSheetWidgetView != null) {
                        bottomSheetWidgetView.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        btnOpenWidget.setVisibility(View.GONE);//不显示widget显示按钮
                    }
                }

            }
        });

        //不显示widget
        btnClosedWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //仅仅是不显示，但是出于活动状态

                if (isPad) {
                    widgetView.setVisibility(View.GONE);
                    btnOpenWidget.setVisibility(View.VISIBLE);//显示widget展开按钮

                    animClosed();
                } else {
                    if (bottomSheetWidgetView != null) {
                        bottomSheetWidgetView.setState(BottomSheetBehavior.STATE_HIDDEN);
                        btnOpenWidget.setVisibility(View.VISIBLE);//显示widget展开按钮
                    }
                }

            }
        });

        //如果为手机
        if (!isPad) {
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu pm = new PopupMenu(context, v);
                    pm.getMenuInflater().inflate(R.menu.menu_widget_btn_more, pm.getMenu());

                    String isDraggable = bottomSheetWidgetView.isDraggable() ? "固定" : "不固定";
                    pm.getMenu().findItem(R.id.menu_widget_btn_more_fix).setTitle(isDraggable);

                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.menu_widget_btn_more_fix:
                                    bottomSheetWidgetView.setDraggable(!bottomSheetWidgetView.isDraggable());
                                    break;
                                case R.id.menu_widget_btn_more_setting:
                                    Toast.makeText(context, "设置，代码待写!!!", Toast.LENGTH_SHORT).show();
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


    }

    /**
     * 设置组件信息
     * @param widget
     */
    public void setWidget(BaseWidget widget) {
        this.widget = widget;
        this.widget.setWidgetExtentView(baseWidgetExtentView);
        this.widget.active();//打开组件时执行active方法用于装载UI信息
    }

    /**
     * 关闭组件
     */
    public void hideWidget(){
        widget.inactive();//widget处于关闭状态
        baseWidgetView.setVisibility(View.GONE);

        animClosed();
    }

    /**
     * 打开动画
     */
    private void animOpen() {
        boolean isPad = SysUtils.isPad(context);
        Animation animationOpen;

        if (isPad) {
            animationOpen = AnimationUtils.loadAnimation(context,R.anim.widget_enter_left);
        } else {
            animationOpen = AnimationUtils.loadAnimation(context,R.anim.widget_enter_slide_in);
        }
//        baseWidgetView.startAnimation(animationOpen);

    }

    /**
     * 关闭动画
     */
    private void animClosed() {
        boolean isPad = SysUtils.isPad(context);
        Animation animationExit;

        if (isPad) {
            animationExit = AnimationUtils.loadAnimation(context, R.anim.widget_exit_left);
        } else {
            animationExit = AnimationUtils.loadAnimation(context, R.anim.widget_exit_slide_out);
        }
//        baseWidgetView.startAnimation(animationExit);
    }



}
