package com.java110.dto.attendanceClasses;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 考勤班组属性数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AttendanceClassesAttrDto extends PageDto implements Serializable {

    private String classesId;
    private String[] classesIds;
    private String attrId;
    private String specCd;
    private String name;
    private String storeId;
    private String value;


    private Date createTime;

    private String statusCd = "0";


    public String getClassesId() {
        return classesId;
    }

    public void setClassesId(String classesId) {
        this.classesId = classesId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String[] getClassesIds() {
        return classesIds;
    }

    public void setClassesIds(String[] classesIds) {
        this.classesIds = classesIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
