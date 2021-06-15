package com.java110.entity.center;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 业务数据
 * Created by wuxw on 2018/4/13.
 */
public class Business implements Comparable<Business>{

    private String bId;

    //业务编码
    private String serviceCode;

    private String businessTypeCd;

    private String serviceName;

    private String remark;

    private String isInstance;

    private JSONObject datas;

    //透传
    private String transferData;

    private JSONArray attrs;
    //返回 编码
    private String code;

    private String message;

    private int seq;


    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public JSONObject getDatas() {
        return datas;
    }

    public void setDatas(JSONObject datas) {
        this.datas = datas;
    }

    public JSONArray getAttrs() {
        return attrs;
    }

    public void setAttrs(JSONArray attrs) {
        this.attrs = attrs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getIsInstance() {
        return isInstance;
    }

    public void setIsInstance(String isInstance) {
        this.isInstance = isInstance;
    }

    public String getTransferData() {
        return transferData;
    }

    public void setTransferData(String transferData) {
        this.transferData = transferData;
    }

    /**
     * 构建成对象
     * @return
     * @throws Exception
     */
    public Business builder(JSONObject businessObj) throws Exception{

        try{
            this.setbId(businessObj.getString("bId"));
            this.setBusinessTypeCd(businessObj.getString("businessTypeCd"));
            if(businessObj.containsKey("serviceName")) {
                this.setServiceName(businessObj.getString("serviceName"));
            }
            if(businessObj.containsKey("remark")) {
                this.setRemark(businessObj.getString("remark"));
            }

            if(businessObj.containsKey("isInstance")){
                this.setIsInstance(businessObj.getString("isInstance"));
            }
            if(businessObj.containsKey("datas")) {
                this.setDatas(businessObj.getJSONObject("datas"));
            }

            if(businessObj.containsKey("attrs")){
                this.setAttrs(businessObj.getJSONArray("attrs"));
            }

            if(businessObj.containsKey("transferData")){
                this.setTransferData(businessObj.getString("transferData"));
            }

            if(businessObj.containsKey("response")){
                this.setCode(businessObj.getJSONObject("response").getString("code"));
                this.setMessage(businessObj.getJSONObject("response").getString("message"));
            }
        }catch (Exception e){
            throw e;
        }
        return this;
    }

    @Override
    public int compareTo(Business otherBusiness) {
        if(this.getSeq() > otherBusiness.getSeq()) {
            return -1;
        }
        return 0;
    }

    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }
}
