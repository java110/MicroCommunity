package com.java110.dto.appraise;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 评价数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AppraiseDto extends PageDto implements Serializable {

    //评价单
    public static final String OBJ_TYPE_REPAIR = "10001";

    public static final String APPRAISE_TYPE_PUBLIC = "10000"; // 评价类型 普通类型

    private String parentAppraiseId;
    private String appraiseUserName;
    private String appraiseId;
    private String appraiseScore;
    private String appraiseType;
    private String context;
    private String objId;
    private String objType;
    private String appraiseUserId;
    private String doorSpeedScore;
    private String repairmanServiceScore;


    private Date createTime;

    private String statusCd = "0";


    public String getParentAppraiseId() {
        return parentAppraiseId;
    }

    public void setParentAppraiseId(String parentAppraiseId) {
        this.parentAppraiseId = parentAppraiseId;
    }

    public String getAppraiseUserName() {
        return appraiseUserName;
    }

    public void setAppraiseUserName(String appraiseUserName) {
        this.appraiseUserName = appraiseUserName;
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public String getAppraiseScore() {
        return appraiseScore;
    }

    public void setAppraiseScore(String appraiseScore) {
        this.appraiseScore = appraiseScore;
    }

    public String getAppraiseType() {
        return appraiseType;
    }

    public void setAppraiseType(String appraiseType) {
        this.appraiseType = appraiseType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getAppraiseUserId() {
        return appraiseUserId;
    }

    public void setAppraiseUserId(String appraiseUserId) {
        this.appraiseUserId = appraiseUserId;
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

    public String getDoorSpeedScore() {
        return doorSpeedScore;
    }

    public void setDoorSpeedScore(String doorSpeedScore) {
        this.doorSpeedScore = doorSpeedScore;
    }

    public String getRepairmanServiceScore() {
        return repairmanServiceScore;
    }

    public void setRepairmanServiceScore(String repairmanServiceScore) {
        this.repairmanServiceScore = repairmanServiceScore;
    }
}
