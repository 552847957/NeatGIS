package com.moyear.neatgis.Permission;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

/**
 * 检查权限的工具类
 * <p/>
 * Created by wangchenlong on 16/1/26.
 *
 * Edited by moyear on 2020.03.07
 *
 */
public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    // 判断权限集合
    public boolean hasAllPermissions(String... permissions) {

        return !lacksPermissions(permissions);
    }

    /**
     * 判断是否具有某个权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {

        return lacksPermission(permission);

    }

}
