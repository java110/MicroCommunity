package com.java110.entity.rule;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 业务规则主体
 * Created by wuxw on 2017/7/23.
 */
public class Rule implements Serializable{

    private String rule_id;

    private String rule_code;

    private String rule_level;

    private String rule_name;

    private String rule_desc;

    private String rule_type;

    private String rule_url;

    private Date create_dt;

    private List<RuleCondCfg> ruleCondCfgs;

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getRule_code() {
        return rule_code;
    }

    public void setRule_code(String rule_code) {
        this.rule_code = rule_code;
    }

    public String getRule_level() {
        return rule_level;
    }

    public void setRule_level(String rule_level) {
        this.rule_level = rule_level;
    }

    public String getRule_name() {
        return rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public String getRule_desc() {
        return rule_desc;
    }

    public void setRule_desc(String rule_desc) {
        this.rule_desc = rule_desc;
    }

    public String getRule_type() {
        return rule_type;
    }

    public void setRule_type(String rule_type) {
        this.rule_type = rule_type;
    }

    public String getRule_url() {
        return rule_url;
    }

    public void setRule_url(String rule_url) {
        this.rule_url = rule_url;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    public List<RuleCondCfg> getRuleCondCfgs() {
        return ruleCondCfgs;
    }

    public void setRuleCondCfgs(List<RuleCondCfg> ruleCondCfgs) {
        this.ruleCondCfgs = ruleCondCfgs;
    }
}
