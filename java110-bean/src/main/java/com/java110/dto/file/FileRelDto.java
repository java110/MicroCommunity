package com.java110.dto.file;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 文件存放数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FileRelDto extends PageDto implements Serializable {


    public static final String REL_TYPE_CD_REPAIR = "14000";//报修图片
    public static final String REL_TYPE_CD_RENTING = "16000";//租赁图片
    public static final String REL_TYPE_CD_GOODS_COVER = "17000";//商品封面
    public static final String REL_TYPE_CD_GOODS_CAROUSEL_FIGURE = "18000";//商品轮播
    public static final String SAVE_WAY_FTP = "ftp";//租赁图片
    public static final String BEFORE_REPAIR_PHOTOS = "23000";  //维修前图片
    public static final String AFTER_REPAIR_PHOTOS = "24000";  //维修后图片

    private String relTypeCd;
    private String[] relTypeCds;
    private String saveWay;
    private String fileRelId;
    private String fileRealName;
    private String objId;
    private String[] objIds;
    private String fileSaveName;


    private Date createTime;

    private String statusCd = "0";


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

    public String getFileRelId() {
        return fileRelId;
    }

    public void setFileRelId(String fileRelId) {
        this.fileRelId = fileRelId;
    }

    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String[] getRelTypeCds() {
        return relTypeCds;
    }

    public void setRelTypeCds(String[] relTypeCds) {
        this.relTypeCds = relTypeCds;
    }

    public String[] getObjIds() {
        return objIds;
    }

    public void setObjIds(String[] objIds) {
        this.objIds = objIds;
    }
}
