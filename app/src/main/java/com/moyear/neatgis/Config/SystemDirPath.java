package com.moyear.neatgis.Config;


import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.moyear.neatgis.Config.Entity.ConfigEntity;
import com.moyear.neatgis.Utils.PreferenceUtils;

import gisluq.lib.Util.SDCardUtils;

/**
 * 系统文件夹管理类
 * 记录、获取系统文件夹路径，管理存储位置信息
 */
public class SystemDirPath {

    private static String defaultMainWorkSpace = "/NeatGIS";//工作空间地址 原来：RuntimeViewer

    private static String Projects = "/projects"; //系统工程文件夹

    private static String Datas = "/data"; //数据库文件夹

    private static String SystemConf = "/system"; //系统模板

    private static String lockViewConf = "/lockscreen.conf"; //锁屏配置文件信息

    private static String SDPath = SDCardUtils.getSDCardPath();//系统SD卡路径

    private static ConfigEntity configEntity = null;

    /**
     * 获取SD卡工具路径
     * @return
     */
    public static String getSDPath() {
        //TODO:添加获取Android储存路径的代码

        return SDPath;
    }

    /**
     * 获取系统工作空间文件夹路径（主目录）
     * 主目录以系统内部存储为主
     *
     * @return
     * @param context
     */
    public static String getMainWorkSpacePath(Context context) {

        String mainWorkSpace;

        //如果使用app私有路径来储存文件
        if (PreferenceUtils.isUsingAppPrivatePath(context)) {

            mainWorkSpace = context.getExternalFilesDir("").getAbsolutePath();//Android/data/包名/files

        } else {
            if (configEntity == null) {
                configEntity = AppConfig.getConfig(context);
            }

            String path = configEntity.getWorkspacePath();

            if (path != null || (!path.equals(""))) {
                return SDPath + path;
            }

            mainWorkSpace = SDPath + defaultMainWorkSpace;//内部储存根目录/NeatGIS
        }

        FileUtils.createOrExistsFile(mainWorkSpace);//判断目录是否存在，不存在则判断是否创建成功

        return mainWorkSpace;

    }

    /**
     * 获取系统配置文件夹路径（系统配置目录仅内部存储）
     * @return
     */
    public static String getSystemConfPath(Context context) {

        String systemConfPath = getMainWorkSpacePath(context) + SystemConf;

        FileUtils.createOrExistsDir(systemConfPath);//判断目录是否存在，不存在则判断是否创建成功
        return systemConfPath;
    }


    /**
     * 获取工程文件夹路径
     * @return
     */
    public static String getProjectPath(Context context) {
        //TODO:添加获取自定义路径的代码

        String projectPath = getMainWorkSpacePath(context) + Projects;

        FileUtils.createOrExistsDir(projectPath);//判断目录是否存在，不存在则判断是否创建成功

        return projectPath;
    }

    /**
     * 获取数据储存库
     * @param context
     * @return
     */
    public static String getDataPaths(Context context) {
        String dataPath = getMainWorkSpacePath(context) + Datas;

        FileUtils.createOrExistsDir(dataPath);//判断目录是否存在，不存在则判断是否创建成功

        return dataPath;

    }

    /**
     * 获取系统锁屏配置文件路径
     * @return
     */
    public static String getLockViewConfPath(Context context) {
        return getMainWorkSpacePath(context) + SystemConf + lockViewConf;
    }


}
