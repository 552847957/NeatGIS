package com.moyear.neatgis.Utils;

import android.content.Context;
import android.util.Log;

import com.moyear.neatgis.Config.SystemDirPath;

import java.io.File;

public class MyDataBaseUtils {

    private static String TAG = "MyDataBaseUtils";


    /**
     *创建一个数据库文件夹
     *
     * @param context
     * @param databaseName
     * @return
     */
    public static boolean createDataBase(Context context, String databaseName) {
        boolean result = false;

        String dataPath = SystemDirPath.getDataPaths(context);
        String newDataBasePath = dataPath + File.separator +databaseName;

        File newDataBase = new File(newDataBasePath);

        if (newDataBase.exists()) {
            Log.w(TAG,newDataBasePath + "已经存在，无法重新创建");

        } else {
            result = newDataBase.mkdirs();
            if (result)
                Log.d(TAG,newDataBasePath + "创建成功");
            else
                Log.d(TAG,newDataBasePath + "创建失败");
        }

        return result;
    }



}
