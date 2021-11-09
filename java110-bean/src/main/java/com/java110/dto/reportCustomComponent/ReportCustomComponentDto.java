package com.java110.dto.reportCustomComponent;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 报表组件数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportCustomComponentDto extends PageDto implements Serializable {

    private String componentType;
private String componentId;
private String javaScript;
private String name;
private String componentGroup;
private String remark;
private String queryModel;
private String sql;


    private Date createTime;

    private String statusCd = "0";


    public String getComponentType() {
        return componentType;
    }
public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
public String getComponentId() {
        return componentId;
    }
public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
public String getJavaScript() {
        return javaScript;
    }
public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getComponentGroup() {
        return componentGroup;
    }
public void setComponentGroup(String componentGroup) {
        this.componentGroup = componentGroup;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getQueryModel() {
        return queryModel;
    }
public void setQueryModel(String queryModel) {
        this.queryModel = queryModel;
    }
public String getSql() {
        return sql;
    }
public void setSql(String sql) {
        this.sql = sql;
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
