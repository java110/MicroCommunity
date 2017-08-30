package com.java110.entity.mapping;



import com.java110.entity.protocol.BeanSvcCont;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射关系
 * Created by wuxw on 2017/3/1.
 */
public class CodeMapping extends BeanSvcCont implements Serializable{

    public CodeMapping() {
        this.setBeanName(this.getClass().getName());
    }

    private int id;

    //域，如常量域域，如常量域
    private String domain;

    //名称
    private String name;

    //键
    private String h_code;

    //值
    private String p_code;

    //描述
    private String description;

    //创建时间
    private Date create_dt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getH_code() {
        return h_code;
    }

    public void setH_code(String h_code) {
        this.h_code = h_code;
    }

    public String getP_code() {
        return p_code;
    }

    public void setP_code(String p_code) {
        this.p_code = p_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }
}
