package com.java110.dto.questionAnswer;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 答卷数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class QuestionAnswerDto extends PageDto implements Serializable {

    public static final String QA_TYPE_COMMUNITY = "3306";//小区
    public static final String QA_TYPE_STORE = "3307";//商户ID



    private String qaName;
    private String qaType;
    private String qaTypeName;
    private String startTime;
    private String endTime;
    private String storeId;
    private String objType;
    private String objId;
    private String[] objIds;
    private String qaId;


    private Date createTime;

    private String statusCd = "0";


    public String getQaName() {
        return qaName;
    }

    public void setQaName(String qaName) {
        this.qaName = qaName;
    }

    public String getQaType() {
        return qaType;
    }

    public void setQaType(String qaType) {
        this.qaType = qaType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getQaId() {
        return qaId;
    }

    public void setQaId(String qaId) {
        this.qaId = qaId;
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

    public String[] getObjIds() {
        return objIds;
    }

    public void setObjIds(String[] objIds) {
        this.objIds = objIds;
    }

    public String getQaTypeName() {
        return qaTypeName;
    }

    public void setQaTypeName(String qaTypeName) {
        this.qaTypeName = qaTypeName;
    }
}
