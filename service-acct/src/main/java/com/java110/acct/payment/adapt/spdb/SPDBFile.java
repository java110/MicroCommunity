package com.java110.acct.payment.adapt.spdb;

import cn.hutool.crypto.SecureUtil;

import java.io.File;

/**
 * 【公共文件上传】接口专用
 *
 * @author z
 */
public class SPDBFile {
    /**
     * 文件对象
     */
    private File file;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小，单位字节
     */
    private String fileSize;
    /**
     * 文件sha1后的值
     */
    private String fileSha1;
    /**
     * 文件路径
     */
    private String path;


    /**
     * 构建文件参数
     */
    public void build() {
        this.fileName = file.getName();
        this.fileSize = file.length() + "B";
        this.fileSha1 = SecureUtil.sha1(file);
    }

    public SPDBFile(File file) {
        this.file = file;
    }

    public SPDBFile(File file, String fileName, String fileSize, String fileSha1, String path) {
        this.file = file;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileSha1 = fileSha1;
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSha1() {
        return fileSha1;
    }

    public void setFileSha1(String fileSha1) {
        this.fileSha1 = fileSha1;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
