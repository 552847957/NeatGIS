package com.moyear.neatgis.Config.Xml;

import android.content.Context;
import android.util.Log;

import com.moyear.neatgis.Config.ProjectConfigEntity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ProjectConfigXmlParser {

    private final static String XML_NODE_INFO = "info";//xml info节点

    private final static String XML_NODE_INFO_NAME = "name";//项目名
    private final static String XML_NODE_INFO_OWNER = "owner";//项目所有者
    private final static String XML_NODE_INFO_PATH = "path";//项目路径
    private final static String XML_NODE_INFO_PREVIEW_PATH = "previewImagePath";//项目预览图路径
    private final static String XML_NODE_INFO_DESCRIPTION = "description";//项目简单描述
    private final static String XML_NODE_INFO_DESCRIPTION_ADVANCE_PATH = "advanceDescriptionPath";//项目高级描述
    private final static String XML_NODE_INFO_VERSION_VERSION_NAME = "versionName";//项目版本名
    private final static String XML_NODE_INFO_VERSION_VERSION_ID = "versionId";//项目版本id
    private final static String XML_NODE_INFO_IS_ENCRYPT = "isEncrypt";//项目是否加密
    private final static String XML_NODE_INFO_ALLOW_AUTOBACKUP = "allowAutoBackup";//项目是否允许自动备份


    private final static String XML_NODE_TYPE = "type";//xml type节点
    private final static String XML_NODE_SPATIAL = "spatial";//xml spatial节点
    private final static String XML_NODE_RESOURCE = "resource";//xml resource节点

    public static ProjectConfigEntity getConfig(Context c, String projectConfigPath) throws XmlPullParserException {
        ProjectConfigEntity projectConfigEntity = new ProjectConfigEntity();

        XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser pullParser = pullParserFactory.newPullParser();
        InputStream input;

        try {
            File projectConfigFile = new File(projectConfigPath);
            if (projectConfigFile.exists()) {
                input = new FileInputStream(projectConfigFile);
            } else {
                Log.e("ProjectConfigXmlParser","ProjectConfig Path do not exist");
                return projectConfigEntity;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        pullParser.setInput(input, "UTF-8");

        String nodeName = "", temp = "";

        try {
            int eventType = pullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                nodeName = pullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (XML_NODE_INFO.equals(nodeName)) {//info节点
                            String projectName = pullParser.getAttributeValue(null,XML_NODE_INFO_NAME);
                            projectConfigEntity.setProjectName(projectName);

                            String projectOwner = pullParser.getAttributeValue(null,XML_NODE_INFO_OWNER);
                            projectConfigEntity.setProjectOwner(projectOwner);

                            String projectPath = pullParser.getAttributeValue(null,XML_NODE_INFO_PATH);
                            projectConfigEntity.setProjectPath(projectPath);

                            String projectPreviewPath = pullParser.getAttributeValue(null,XML_NODE_INFO_PREVIEW_PATH);
                            projectConfigEntity.setPreviewImagePath(projectPreviewPath);

                            String projectDescription = pullParser.getAttributeValue(null,XML_NODE_INFO_DESCRIPTION);
                            projectConfigEntity.setDescription(projectDescription);

                            String projectAdvandceDescPath = pullParser.getAttributeValue(null,XML_NODE_INFO_DESCRIPTION_ADVANCE_PATH);
                            projectConfigEntity.setAdvanceDescPath(projectAdvandceDescPath);

                            String projectVersionName = pullParser.getAttributeValue(null,XML_NODE_INFO_VERSION_VERSION_NAME);
                            projectConfigEntity.setVersionName(projectVersionName);

                            int projectVersionId = Integer.parseInt(pullParser.getAttributeValue(null,XML_NODE_INFO_VERSION_VERSION_ID));
                            projectConfigEntity.setVersionID(projectVersionId);

                            boolean isEncrypt = Boolean.parseBoolean(pullParser.getAttributeValue(null,XML_NODE_INFO_IS_ENCRYPT));
                            projectConfigEntity.setEncrypt(isEncrypt);
//
//                            String allowAutoBackup = pullParser.getAttributeValue(null,XML_NODE_INFO_ALLOW_AUTOBACKUP);
//                            projectConfigEntity.set(projectOwner);

                        } else if (XML_NODE_TYPE.equals(nodeName)) {//type节点

                        } else if (XML_NODE_SPATIAL.equals(nodeName)) {//spatial节点

                        } else if (XML_NODE_RESOURCE.equals(nodeName)) {//resource节点

                        }

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = pullParser.next();
            }
            input.close();
        } catch(Exception e) {
            e.printStackTrace();
        }



        return projectConfigEntity;

    }

}
