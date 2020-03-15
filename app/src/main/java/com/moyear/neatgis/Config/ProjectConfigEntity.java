package com.moyear.neatgis.Config;


/**
 * 项目配置文件实体类
 *
 */
public class ProjectConfigEntity {

    private String projectName = "";
    private String projectOwner = "";

    private String projectPath = "";

    private String previewImagePath = "";

    private String description = "";

    /**
     *项目详细描述文件的相对路径
     */
    private String advanceDescPath = "";

    private String versionName = "";
    private int versionID = 0;

    /**
     * 是否允许项目自动备份，以防止意外
     */
    private int allowAutoBackup;

    /**
     *项目是否加密
     *
     */
    private boolean isEncrypt;

    /**
     * 项目空间坐标系名称
     */
    private String spatialName;


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setPreviewImagePath(String previewImagePath) {
        this.previewImagePath = previewImagePath;
    }

    public String getPreviewImagePath() {
        return previewImagePath;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAdvanceDescPath(String advanceDescPath) {
        this.advanceDescPath = advanceDescPath;
    }

    public String getAdvanceDescPath() {
        return advanceDescPath;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionID(int versionID) {
        this.versionID = versionID;
    }

    public int getVersionID() {
        return versionID;
    }

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }


    public void setSpatialName(String spatialName) {
        this.spatialName = spatialName;
    }

    public String getSpatialName() {
        return spatialName;
    }
}
