package com.moyear.neatgis.Utils;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.moyear.neatgis.BMOD.ProjectsModule.Model.ProjectInfo;
import com.moyear.neatgis.Config.ProjectConfigEntity;
import com.moyear.neatgis.Config.SystemDirPath;
import com.moyear.neatgis.File.FileInfo;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProjectUtils {

    /**
     *判断path是不是一个项目地址
     *
     * @param path
     * @return
     */
    public static boolean isProject(String path) {
        // TODO:万岁判断path是不是一个项目地址的代码
        boolean isProject = false;

        return isProject;
    }

    public static boolean createProject(ProjectConfigEntity projectConfigEntity, String projectPath) {
        boolean result = false;

        boolean resultCreateProjectPath = false;

        File projectFile = new File(projectPath);

        if (projectFile.exists()) {
            Log.e("CreateProject","Project path is already exist.");
            return result;
        } else {
            resultCreateProjectPath = projectFile.mkdirs();
        }

        boolean resultCreateConfigFile = createProjectConfigFile(projectConfigEntity, projectPath);

        result = resultCreateProjectPath && resultCreateConfigFile;

        return result;
    }

    /**
     * 创建项目配置文件
     *
     * @param projectConfigEntity
     * @return
     */
    private static boolean createProjectConfigFile(ProjectConfigEntity projectConfigEntity, String projectPath) {
        boolean result = false;

        try {
            File file = new File(projectPath,
                    "proindex.xml");
            FileOutputStream fos = new FileOutputStream(file);

            //String enter = System.getProperty("line.separator");//换行符号

            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            //TODO:增加字符编码选项
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);

            serializer.startTag(null, "project");

            //info标签
            serializer.startTag(null, "info");
            serializer.attribute(null, "name",projectConfigEntity.getProjectName());//项目名称
            serializer.attribute(null, "owner",projectConfigEntity.getProjectOwner());//项目所有者
            serializer.attribute(null, "path",projectConfigEntity.getProjectPath());//项目路径（相对路径）
            serializer.attribute(null, "previewImagePath",projectConfigEntity.getPreviewImagePath());//项目预览图路径（相对路径）
            serializer.attribute(null, "description",projectConfigEntity.getDescription());//项目文字描述
            serializer.attribute(null, "advanceDescriptionPath",projectConfigEntity.getAdvanceDescPath());//项目高级描述文件地址
            serializer.attribute(null, "versionName",projectConfigEntity.getVersionName());//项目版本名称
            serializer.attribute(null, "versionId",projectConfigEntity.getVersionID()+"");//项目版本ID
            serializer.attribute(null, "isEncrypt",projectConfigEntity.isEncrypt()+"");//项目是否加密
            //serializer.attribute(null, "allowAutoBackup",projectConfigEntity.()+"");//项目是否加密
            serializer.endTag(null, "info");

            //type标签
//            serializer.startTag(null, "type");
//            serializer.attribute(null, "typeID",projectConfigEntity.get());//项目名称
//            serializer.endTag(null, "type");

            //spatial标签
            serializer.startTag(null, "spatial");
            serializer.attribute(null, "spatialReference",projectConfigEntity.getSpatialName());//项目名称
//            serializer.attribute(null, "allowDiffer",projectConfigEntity.get());//项目名称
            serializer.endTag(null, "spatial");

            serializer.endTag(null, "project");
            serializer.endDocument();

            fos.close();

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ConfigFailure",e.toString());
        }


        return result;
    }


    /**
     * 获取工程信息列表
     * @return
     */
    public static List<ProjectInfo> getProjectInfos(Context context) {
        List<FileInfo> fileInfos = FileUtils.getFileListInfo(SystemDirPath.getProjectPath(context),"folder");
        // 获取文件名列表
        List<String> fileNames = new ArrayList<>();
        if (fileInfos != null) {
            for (int i = 0;i < fileInfos.size(); i++) {
                fileNames.add(fileInfos.get(i).getFileName());
            }
        }
        Collections.sort(fileNames);//排序

        List<ProjectInfo> infos = new ArrayList<>();
        if (fileInfos != null) {
            for (int i = 0;i < fileNames.size(); i++) {
                String name = fileNames.get(i);
                for (int j = 0;j < fileInfos.size(); j++) {
                    FileInfo fileInfo = fileInfos.get(j);
                    if (fileInfo.getFileName().equals(name)) {
                        ProjectInfo projectInfo = new ProjectInfo();
                        projectInfo.setDirName(fileInfo.getFileName());
                        projectInfo.setDirPath(fileInfo.getPath());
                        infos.add(projectInfo);
                    }
                }
            }
        }
        return infos;
    }



    /**
     * 获取基础底图路径
     * @param projectPath
     * @param path
     * @return
     */
    public static String getBasemapPath(String projectPath, String path) {
        return projectPath + File.separator + "BaseMap" + File.separator + path;
    }

    /**
     * 获取业务图层路径
     * @return
     */
    public static String getOperationalLayersPath(String projectPath) {
        return projectPath + File.separator + "OperationalLayers" + File.separator;
    }

    /**
     * 获取Geopackage路径
     * @return
     */
    public static String getGeoPackagePath(String projectPath) {
        return projectPath + File.separator+"GeoPackage" + File.separator;
    }

    /**
     * 获取json路径信息
     * @return
     */
    public static String getJSONPath(String projectPath) {
        return projectPath + File.separator + "JSON" + File.separator;
    }

}
