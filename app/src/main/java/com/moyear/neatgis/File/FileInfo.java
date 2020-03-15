package com.moyear.neatgis.File;

import java.util.List;

public class FileInfo {

    private boolean isDirectory;

    private String fileName;

    private String path;

    private List<FileInfo> childFiles;


    public void setIsDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setChildFiles(List<FileInfo> childFiles) {
        this.childFiles = childFiles;
    }

    public List<FileInfo> getChildFiles() {
        return childFiles;
    }
}
