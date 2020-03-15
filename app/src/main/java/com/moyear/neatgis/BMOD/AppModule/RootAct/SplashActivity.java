package com.moyear.neatgis.BMOD.AppModule.RootAct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.moyear.neatgis.BMOD.AppModule.HomeActivity;
import com.moyear.neatgis.Permission.PermissionsActivity;
import com.moyear.neatgis.Permission.PermissionsChecker;
import com.moyear.neatgis.Permission.PermissionsUtils;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.PreferenceUtils;

import gisluq.lib.Util.AppUtils;

/**
 *  应用程序初始化页面
 */
public class SplashActivity extends AppCompatActivity {

    private static String TAG = "SplashActivity";
    private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟时间

    private Context context = null;

    private static PermissionsChecker mPermissionsChecker; // 权限检测器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_init);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {

                    if (PreferenceUtils.isFirstRun(getApplicationContext())) {
                        Intent intent = new Intent(getApplicationContext(), InitActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        showPermissonChecker();

                        Intent mainIntent = new Intent(context, HomeActivity.class);
                        context.startActivity(mainIntent);
                        ((Activity)context).finish();

                    }

                } catch (Exception e) {
                    Log.e(TAG,e.toString());
                }
            }
        }, SPLASH_DISPLAY_LENGHT);

        TextView textView = this.findViewById(R.id.activity_init_versionTxt);
        String version = AppUtils.getVersionName(this);
        textView.setText("版本号:" + version);
    }

    private void showPermissonChecker() {
        mPermissionsChecker = new PermissionsChecker(this);

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(AppUtils.REQUIRED_ALL_PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, PermissionsUtils.PERMISSION_REQUEST_CODE, AppUtils.REQUIRED_ALL_PERMISSIONS);
        }
    }


}
