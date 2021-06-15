package com.java110.entity.user;

import com.java110.entity.DefaultEntity;

import java.util.Set;

/**
 * 客户信息实体
 * Created by wuxw on 2016/12/27.
 */
public class Cust extends DefaultEntity {

    private String custId;

    private String name;

    private String email;

    private String cellphone;

    private String realName;

    private String sex;

    private String password;

    private String lanId;

    private String custAdress;

    private String custType;

    private String openId;



    private Set<CustAttr> custAttrs;


    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanId() {
        return lanId;
    }

    public void setLanId(String lanId) {
        this.lanId = lanId;
    }

    public String getCustAdress() {
        return custAdress;
    }

    public void setCustAdress(String custAdress) {
        this.custAdress = custAdress;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }


    public Set<CustAttr> getCustAttrs() {
        return custAttrs;
    }

    public void setCustAttrs(Set<CustAttr> custAttrs) {
        this.custAttrs = custAttrs;
    }
}
