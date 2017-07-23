package com.java110.entity.rule;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应表rule_entrance
 * Created by wuxw on 2017/7/23.
 */
public class RuleEntrance implements Serializable {



    private String rule_id;

    private String rule_condition;

    private String status_cd;
    private Date create_dt;

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getRule_condition() {
        return rule_condition;
    }

    public void setRule_condition(String rule_condition) {
        this.rule_condition = rule_condition;
    }

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }
}
