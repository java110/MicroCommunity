package com.java110.dto.reportCustom;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 底部统计数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportCustomComponentFooterDto extends PageDto implements Serializable {

    private String footerId;
private String componentId;
private String javaScript;
private String name;
private String remark;
private String queryModel;
private String componentSql;


    private Date createTime;

    private String statusCd = "0";


    public String getFooterId() {
        return footerId;
    }
public void setFooterId(String footerId) {
        this.footerId = footerId;
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
public String getComponentSql() {
        return componentSql;
    }
public void setComponentSql(String componentSql) {
        this.componentSql = componentSql;
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
