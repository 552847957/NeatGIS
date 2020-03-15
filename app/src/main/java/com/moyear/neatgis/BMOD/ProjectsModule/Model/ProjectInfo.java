package com.moyear.neatgis.BMOD.ProjectsModule.Model;

/**
 * 工程目录信息
 */
public class ProjectInfo {
    private String DirName;//工程文件夹名称
    private String DirPath;//工程路径

    private boolean isOpenning;//是否正在打开

    private String projDisc;//项目描述信息
    private String projOwner;//项目所有者

    private String createTime;//项目创建时间
    private String editTime;//项目上次修改时间

    public void setOpenning(boolean openning) {
        isOpenning = openning;
    }

    public boolean isOpenning() {
        return isOpenning;
    }

    public String getDirName() {
        return DirName;
    }

    public void setDirName(String dirName) {
        DirName = dirName;
    }

    public void setDirPath(String dirPath) {
        DirPath = dirPath;
    }

    public String getDirPath() {
        return DirPath;
    }

    public void setProjDisc(String proDisc) {
        this.projDisc = proDisc;
    }

    public String getProjDisc() {
        return projDisc;
    }

    public void setProjOwner(String projOwner) {
        this.projOwner = projOwner;
    }

    public String getProjOwner() {
        return projOwner;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditTime() {
        return editTime;
    }
}
