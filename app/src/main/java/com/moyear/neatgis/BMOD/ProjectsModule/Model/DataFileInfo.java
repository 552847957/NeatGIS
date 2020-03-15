package com.moyear.neatgis.BMOD.ProjectsModule.Model;

public class DataFileInfo {

    /**
     *
     * 本地数据库文件夹以及数据文件的信息
     *created by moyear on 2020.03.14
     *
     */


    /**
     * 是否是数据库文件夹类型
     */
    private boolean isDataBaseDir;

    /**
     * 名称
     */
    private String dbName;

    /**
     * 数据库路径
     */
    private String dbPath;

    /**
     * 数据库所有者
     */
    private String dbOwner;

    /**
     * 数据库上次修改时间
     */
    private String dbLastModifiedTime;

    public void setDataBaseDir(boolean dataBaseDir) {
        isDataBaseDir = dataBaseDir;
    }

    public boolean isDataBaseDir() {
        return isDataBaseDir;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setOwner(String libOwner) {
        this.dbOwner = libOwner;
    }

    public String getOwner() {
        return dbOwner;
    }

    public void setDbLastModifiedTime(String dbLastModifiedTime) {
        this.dbLastModifiedTime = dbLastModifiedTime;
    }

    public String getDbLastModifiedTime() {
        return dbLastModifiedTime;
    }
}
