package com.moyear.neatgis.BMOD.AppModule.RootAct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.moyear.neatgis.BMOD.AppModule.HomeActivity;
import com.moyear.neatgis.BMOD.AppModule.RootAct.Adapter.IntroViewPagerAdapter;
import com.moyear.neatgis.BMOD.AppModule.RootAct.Model.ScreenItem;
import com.moyear.neatgis.Base.BaseFragmentDialog;
import com.moyear.neatgis.Config.AppWorksSpaceInit;
import com.moyear.neatgis.Permission.PermissionsChecker;
import com.moyear.neatgis.Permission.PermissionsUtils;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.MyDataBaseUtils;
import com.moyear.neatgis.Utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import gisluq.lib.Util.AppUtils;
import gisluq.lib.Util.ToastUtils;

public class InitActivity extends AppCompatActivity {

    private static PermissionsChecker mPermissionsChecker; //权限检测器

    private boolean hasAllPermissionRequested = false;//应用是否具有所有的权限

    private int currentTabPosition = 0;//当前所在的tab页面位置

    private ViewPager screenPager;

    private IntroViewPagerAdapter introViewPagerAdapter;

    private TabLayout tabIndicator;

    private Button btnNext;

    private Button btnGetStarted;

    private TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_init_first_run);

        //状态栏沉浸
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mPermissionsChecker = new PermissionsChecker(this);

        hasAllPermissionRequested = mPermissionsChecker.hasAllPermissions(AppUtils.REQUIRED_ALL_PERMISSIONS);

        bindView();
        initView();

    }


    private void bindView() {

        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);

        tvSkip = findViewById(R.id.tv_skip);

    }

    private void initView() {

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("标题1","小小的体积,杜绝臃肿,保持轻巧",R.drawable.img1));
        mList.add(new ScreenItem("获取权限","为保证应用的正常运行，请授予应用相应的权限！",R.drawable.img2));
        mList.add(new ScreenItem("标题3","赠人玫瑰，手留余香",R.drawable.img3));

        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentTabPosition == 1) {

                    if (!hasAllPermissionRequested) {
                        ToastUtils.showShort(getApplicationContext(), "为保证应用正常运行，请完成授予相应的权限！");
                        return;
                    }
                }

                if (currentTabPosition < mList.size()) {
                    currentTabPosition++;
                    screenPager.setCurrentItem(currentTabPosition);
                }

                if (currentTabPosition == mList.size() - 1)
                    loadLastScreen();

            }
        });


        //tablayout页面改变的事件
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                currentTabPosition = screenPager.getCurrentItem();

                //如果到第二页，并且没有获取所有的权限的话，则显示权限获取的操作
                if (currentTabPosition == 1) {

                    if (hasAllPermissionRequested) {
                        ToastUtils.showShort(getApplicationContext(), "已完成授权");
                    } else {
                        loadRequirePermissionScreen();
                    }
                } else if (tab.getPosition() == mList.size() - 1) {
                    //如果已完成授权，并且在最后一页
                    if (hasAllPermissionRequested) {
                        loadLastScreen();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentTabPosition == 1 || !hasAllPermissionRequested) {
                    // 缺少权限时, 进入权限配置页面
                    if (mPermissionsChecker.hasAllPermissions(AppUtils.REQUIRED_ALL_PERMISSIONS)) {
                        ToastUtils.showShort(getApplicationContext(), "已完成授权");
                    } else {
                        startRequirePermissions(AppUtils.REQUIRED_ALL_PERMISSIONS);
                    }

                } else {
                    ToastUtils.showShort(getApplicationContext(), "请完成相关偏好设置,如无需要，保持默认即可");

                    showPreferenceSetting();
                }

            }
        });

    }


    private void loadRequirePermissionScreen() {
        btnGetStarted.setText("获取");
        btnGetStarted.setVisibility(View.VISIBLE);
    }

    private void showPreferenceSetting() {

        BaseFragmentDialog projectCreateDialog = new BaseFragmentDialog("初始化设置");

        View contentView = getLayoutInflater().inflate(R.layout.dialog_app_init, null, false);
        initPreferenceSettingView(projectCreateDialog, contentView);

        projectCreateDialog.setView(contentView);
        projectCreateDialog.show(getSupportFragmentManager(),"初始化设置");

    }

    //初始化配置对话框view的初始化
    private void initPreferenceSettingView(BaseFragmentDialog dialog, View contentView) {

        int[] appStoragePathSelected = new int[] {0};//所选择的应用储存目录序号，采用数组方式，已解决必须设为内部类的问题

        RelativeLayout rlAppPath = contentView.findViewById(R.id.lv_item_storage_path_app);
        RelativeLayout rlLibraryPath = contentView.findViewById(R.id.lv_item_storage_path_library);

        TextView txtAppStoragePath = contentView.findViewById(R.id.txt_storage_path_app);
        TextView txtLibraryPath = contentView.findViewById(R.id.txt_storage_path_library);

        EditText edtLibraryName = contentView.findViewById(R.id.edt_library_name);

        FloatingActionButton fabOk = contentView.findViewById(R.id.btn_ok);

        rlAppPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] appStoragePathItems = {"内部储存根目录" , "应用私有目录"};

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InitActivity.this);
                alertDialog.setTitle("文件储存目录");
                alertDialog.setSingleChoiceItems(appStoragePathItems, appStoragePathSelected[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtAppStoragePath.setText(appStoragePathItems[which]);
                        appStoragePathSelected[0] = which;

                        if (which == 1) {
                            PreferenceUtils.setUsingAppPrivatePath(getApplicationContext(), true);//设置项目采用app私有储存路径
                        } else {
                            PreferenceUtils.setUsingAppPrivatePath(getApplicationContext(), false);//设置项目采用内置储存根目录作为储存路径

                        }
                    }
                });
                alertDialog.show();

            }
        });

        rlLibraryPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtLibraryName.getText().toString().trim().equals("")) {
                    ToastUtils.showShort(getApplicationContext(), "请输入数据库名！");
                    return;
                }

                boolean resultCreateDataBase = MyDataBaseUtils.createDataBase(getApplicationContext(), edtLibraryName.getText().toString());

                if (!resultCreateDataBase)
                    ToastUtils.showShort(getApplicationContext(), edtLibraryName.getText().toString() + "创建失败");

                switch (appStoragePathSelected[0]) {
                    case 1://使用应用私有文件夹作为储存文件夹
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InitActivity.this);
                        alertDialog.setTitle("文件储存目录");
                        alertDialog.setMessage("检测已选择应用私有路径来储存文件，当应用卸载后，所有的数据将会丢失，是否继续");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent mainIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(mainIntent);

                                PreferenceUtils.setNotFirstRun(getApplicationContext());//设置成为非首次运行
                                finish();
                            }
                        });
                        alertDialog.setNegativeButton("取消", null);
                        alertDialog.show();
                        break;
                    default:
                        Intent mainIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(mainIntent);

                        PreferenceUtils.setNotFirstRun(getApplicationContext());//设置成为非首次运行
                        finish();
                }
            }
        });

    }


    private void loadLastScreen() {
        btnGetStarted.setText("开始");

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);


    }

//    /**
//     *  应用程序初始化
//     */
//    private void appInit() {
//        boolean isOk = AppWorksSpaceInit.init(getApplicationContext());//初始化系统文件夹路径
//    }


    /**
     * 弹出权限获取提示信息
     */
    private void startRequirePermissions(String [] permissions) {
        ToastUtils.showShort(getApplicationContext(), "为保证运行，请允许所申请的权限！");
        PermissionsUtils.requirePermissions(this, permissions);
    }


    /***
     *该方法的目的：为了代码的健壮性，判断是否动态获取成功
     *
     *
     * @param requestCode  请求吗
     * @param permissions   权限列表
     * @param grantResults  请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionsUtils.PERMISSION_REQUEST_CODE) {

            ArrayList deniedPermissionList = new ArrayList();

            boolean allGranted = true;

            for (int i = 0; i < grantResults.length; i++) {

                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {//没有授权

                    allGranted = false;
                    deniedPermissionList.add(permissions[i]);

                }
            }

            if (allGranted) {
                hasAllPermissionRequested = true;

            } else {
                String [] deniedPermissions = (String[]) deniedPermissionList.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(InitActivity.this);
                builder.setTitle("重新获取授权");
                builder.setMessage("检测到有权限并未授权，如果拒绝并继续使用应用，可能产生一系列未知的问题，是否继续授权");
                builder.setCancelable(false);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequirePermissions(deniedPermissions);
                    }
                });

                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadLastScreen();
                    }
                });

                builder.show();
            }


        }
    }


}
