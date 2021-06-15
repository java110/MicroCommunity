package com.java110.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * @ClassName ResultVo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 18:41
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class FeeDetailResultVo extends ResultVo implements Serializable {

    private double totalReceivedAmount;
    private double totalReceivableAmount;

    public FeeDetailResultVo() {

    }

    public FeeDetailResultVo(double totalReceivableAmount, double totalReceivedAmount, int records, int total, Object data) {
        this.totalReceivableAmount = totalReceivableAmount;
        this.totalReceivedAmount = totalReceivedAmount;
        this.setCode(CODE_OK);
        this.setMsg(MSG_OK);
        this.setRecords(records);
        this.setTotal(total);
        this.setData(data);

    }

    public double getTotalReceivedAmount() {
        return totalReceivedAmount;
    }

    public void setTotalReceivedAmount(double totalReceivedAmount) {
        this.totalReceivedAmount = totalReceivedAmount;
    }

    public double getTotalReceivableAmount() {
        return totalReceivableAmount;
    }

    public void setTotalReceivableAmount(double totalReceivableAmount) {
        this.totalReceivableAmount = totalReceivableAmount;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
    }

}
