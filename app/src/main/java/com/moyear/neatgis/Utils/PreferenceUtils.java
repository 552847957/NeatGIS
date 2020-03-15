package com.moyear.neatgis.Utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 *应用界面，基本配置等偏好设置操作类
 * Created by moyear on 2020.03.12
 *
 */
public class PreferenceUtils {

    public static final String PREFERENCE_APP_SETTING = "app_settings";

    public static final String PREFERENCE_APP_SETTING_FIST_RUN = "app_preference_first_run";

    public static final String PREFERENCE_APP_SETTING_USE_PRIVATE_PATH = "use_app_private_path";


    /**
     *判断应用是不是首次运行
     *
     * @param context
     * @return
     */
    public static boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_APP_SETTING, Context.MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean(PREFERENCE_APP_SETTING_FIST_RUN,true);

        return isFirstRun;
    }

    /**
     * 设置成为非首次运行，仅第一次调用有效
     *
     * @param context
     */
    public static void setNotFirstRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_APP_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCE_APP_SETTING_FIST_RUN, false);//设置成为非首次运行
        editor.apply();

    }

    /**
     *判断项目是否是采用app私有储存路径
     *
     * @param context
     * @return
     */
    public static boolean isUsingAppPrivatePath(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_APP_SETTING, Context.MODE_PRIVATE);
        boolean isUsingAppPrivatePath = sharedPreferences.getBoolean(PREFERENCE_APP_SETTING_USE_PRIVATE_PATH,false);

        return isUsingAppPrivatePath;
    }

    /**
     *设置项目是否是采用app私有储存路径
     *
     * @param context
     * @param isUsing
     */
    public static void setUsingAppPrivatePath(Context context, boolean isUsing) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_APP_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCE_APP_SETTING_USE_PRIVATE_PATH, isUsing);
        editor.apply();
    }


}
