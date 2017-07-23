package com.java110.entity.rule;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Created by wuxw on 2017/7/23.
 */
public class RuleCondCfg implements Serializable{

    private String rule_id;
    private String rule_node;
    private String data_from_flag;
    private String db_sql;
    private String default_value;
    private String node_existed;
    private String proc_param_flag;
    private String is_reverse;
    private String is_log;
    private String error_code;
    private String error_msg;
    private String remark;
    private String seq;
    private String data_stack_flag_prefix;
    private String jpath;
    private String status_cd;
    private Date create_dt;


    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getRule_node() {
        return rule_node;
    }

    public void setRule_node(String rule_node) {
        this.rule_node = rule_node;
    }

    public String getData_from_flag() {
        return data_from_flag;
    }

    public void setData_from_flag(String data_from_flag) {
        this.data_from_flag = data_from_flag;
    }

    public String getDb_sql() {
        return db_sql;
    }

    public void setDb_sql(String db_sql) {
        this.db_sql = db_sql;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    public String getNode_existed() {
        return node_existed;
    }

    public void setNode_existed(String node_existed) {
        this.node_existed = node_existed;
    }

    public String getProc_param_flag() {
        return proc_param_flag;
    }

    public void setProc_param_flag(String proc_param_flag) {
        this.proc_param_flag = proc_param_flag;
    }

    public String getIs_reverse() {
        return is_reverse;
    }

    public void setIs_reverse(String is_reverse) {
        this.is_reverse = is_reverse;
    }

    public String getIs_log() {
        return is_log;
    }

    public void setIs_log(String is_log) {
        this.is_log = is_log;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getData_stack_flag_prefix() {
        return data_stack_flag_prefix;
    }

    public void setData_stack_flag_prefix(String data_stack_flag_prefix) {
        this.data_stack_flag_prefix = data_stack_flag_prefix;
    }

    public String getJpath() {
        return jpath;
    }

    public void setJpath(String jpath) {
        this.jpath = jpath;
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
