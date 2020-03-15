package com.moyear.neatgis.Config;

import android.content.Context;

import com.moyear.neatgis.Config.Entity.ConfigEntity;
import com.moyear.neatgis.Config.Xml.AppConfigXmlParser;

/**
 * 系统配置获取
 */
public class AppConfig {

    /**
     * 获取应用程序配置信息
     */
    public static ConfigEntity getConfig(Context context) {
        ConfigEntity config = null;
        try {
            config = AppConfigXmlParser.getConfig(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
}
