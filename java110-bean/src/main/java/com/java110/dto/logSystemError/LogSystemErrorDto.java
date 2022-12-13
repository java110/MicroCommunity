package com.java110.dto.logSystemError;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 系统异常数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class LogSystemErrorDto extends PageDto implements Serializable {


    public static final String ERR_TYPE_JOB = "JOB";
    public static final String ERR_TYPE_OWE_FEE = "OWE_FEE";
    public static final String ERR_TYPE_NOTICE = "NOTICE";
    public static final String ERR_TYPE_COUPON = "COUPON";
    public static final String ERR_TYPE_INTEGRAL = "INTEGRAL";
    public static final String ERR_TYPE_CMD = "CMD";
    public static final String ERR_TYPE_ACCOUNT = "ACCOUNT";

    private String msg;
    private String errType;
    private String errId;


    private Date createTime;

    private String statusCd = "0";


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrType() {
        return errType;
    }

    public void setErrType(String errType) {
        this.errType = errType;
    }

    public String getErrId() {
        return errId;
    }

    public void setErrId(String errId) {
        this.errId = errId;
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
