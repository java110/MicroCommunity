package com.java110.dto;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @ClassName WsDataDto
 * @Description TODO
 * @Author wuxw
 * @Date 2021/10/12 0:23
 * @Version 1.0
 * add by wuxw 2021/10/12
 **/
public class WsDataDto implements Serializable {
    public static final String CMD_PING = "ping";
    private String cmd;

    private String data;

    private int code;

    private String msg;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
