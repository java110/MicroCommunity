package com.java110.vo.api;

import com.java110.vo.Vo;

/**
 * @ClassName ApiArrearsFeeVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/6/18 19:36
 * @Version 1.0
 * add by wuxw 2019/6/18
 **/
public class ApiArrearsFeeDataVo extends Vo {

    private String feeId;
    private String payerObjId;
    private String ownerName;
    private String tel;
    private String num;
    private String startTime;
    private String endTime;


    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }
}
