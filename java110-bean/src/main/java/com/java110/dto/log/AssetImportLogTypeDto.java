package com.java110.dto.log;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 批量操作日志数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AssetImportLogTypeDto extends PageDto implements Serializable {


    private String typeId;
    private String logType;
    private String logTypeName;

    private String logColumn;
    private String logProperty;

    private String remark;


    private Date createTime;

    private String statusCd = "0";


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogTypeName() {
        return logTypeName;
    }

    public void setLogTypeName(String logTypeName) {
        this.logTypeName = logTypeName;
    }

    public String getLogColumn() {
        return logColumn;
    }

    public void setLogColumn(String logColumn) {
        this.logColumn = logColumn;
    }

    public String getLogProperty() {
        return logProperty;
    }

    public void setLogProperty(String logProperty) {
        this.logProperty = logProperty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
