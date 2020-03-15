package com.moyear.neatgis.Permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.core.app.ActivityCompat;

/**
 * 用户权限管理类
 */
public class PermissionsUtils {

    public static final int PERMISSION_REQUEST_CODE = 147; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static PermissionsChecker mPermissionsChecker; // 权限检测器

    /**
     * 权限检查
     * @param context
     */
    public static void PermissionsChecker(Context context){
        mPermissionsChecker = new PermissionsChecker(context);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult((Activity)context, PERMISSION_REQUEST_CODE, PERMISSIONS);
        }
    }

    /**
     *获取权限申请
     * created by moyear on 2020.03.12
     *
     * @param activity
     * @param permissions 安卓权限的数组
     */
    public static void requirePermissions(Activity activity, String [] permissions) {

        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);

    }







}
