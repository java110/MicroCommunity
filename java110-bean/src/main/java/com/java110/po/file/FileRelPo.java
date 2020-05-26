package com.java110.po.file;

import java.io.Serializable;

/**
 * @ClassName FileRelPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/26 12:38
 * @Version 1.0
 * add by wuxw 2020/5/26
 **/
public class FileRelPo implements Serializable {

    private String fileRelId;
    private String relTypeCd;
    private String saveWay;
    private String objId;
    private String fileRealName;
    private String fileSaveName;

    public String getFileRelId() {
        return fileRelId;
    }

    public void setFileRelId(String fileRelId) {
        this.fileRelId = fileRelId;
    }

    public String getRelTypeCd() {
        return relTypeCd;
    }

    public void setRelTypeCd(String relTypeCd) {
        this.relTypeCd = relTypeCd;
    }

    public String getSaveWay() {
        return saveWay;
    }

    public void setSaveWay(String saveWay) {
        this.saveWay = saveWay;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }
}
