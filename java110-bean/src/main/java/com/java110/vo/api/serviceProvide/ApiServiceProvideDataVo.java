package com.java110.vo.api.serviceProvide;

import java.io.Serializable;
import java.util.Date;

public class ApiServiceProvideDataVo implements Serializable {

    private String id;
private String name;
private String serviceCode;
private String params;
private String queryModel;
private String sql;
private String template;
private String proc;
private String javaScript;
private String remark;
public String getId() {
        return id;
    }
public void setId(String id) {
        this.id = id;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getServiceCode() {
        return serviceCode;
    }
public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
public String getParams() {
        return params;
    }
public void setParams(String params) {
        this.params = params;
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
public String getTemplate() {
        return template;
    }
public void setTemplate(String template) {
        this.template = template;
    }
public String getProc() {
        return proc;
    }
public void setProc(String proc) {
        this.proc = proc;
    }
public String getJavaScript() {
        return javaScript;
    }
public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }



}
