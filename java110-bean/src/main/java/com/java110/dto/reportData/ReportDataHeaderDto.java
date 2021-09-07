package com.java110.dto.reportData;

import java.io.Serializable;

public class ReportDataHeaderDto implements Serializable {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = -1;

    public static final String MSG_SUCCESS= "成功";
    public static final String MSG_ERROR= "系统错误";

    public static final String RETUR_CODE= "1001"; //已发送
    public static final String RETUR_SUCCESS_CODE= "2002"; //处理成功
    public static final String RETUR_ERROR_CODE= "3003";//处理失败
    public static final String RETUR_AGAIN_CODE= "4004";//重新发送

    public static final String SYSTEM_COMMUNITY_TYPE= "777777";
    public static final String SYSTEM_GOV_TYPE= "999999";
    //暂时写死建筑物类型  一个ID 为612021083132790001	住宅
    public static final String SYSTEM_FLOOR_TYPE= "612021083132790001";

    private String serviceCode;
    private String tranId;
    private String reqTime;
    private String sign;
    private String resTime;
    private int code;
    private String msg;
    private String extCommunityId;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResTime() {
        return resTime;
    }

    public void setResTime(String resTime) {
        this.resTime = resTime;
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

    public String getExtCommunityId() {
        return extCommunityId;
    }

    public void setExtCommunityId(String extCommunityId) {
        this.extCommunityId = extCommunityId;
    }
}
