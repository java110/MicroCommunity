package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author wux
 * @create 2019-02-07 上午12:38
 * @desc 订单通知数据封装对象
 **/
public class OrderNotifyDataFlow extends AbstractOrderNotifyDataFlowContext {


    protected OrderNotifyDataFlow(Date startDate, String code) {
        super(startDate, code);
    }

    private String oId;

    /**
     * 构建 对象信息
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public  OrderNotifyDataFlow builder(String reqInfo, Map<String,String> headerAll) throws Exception{

//校验请求报文格式
        Assert.isJsonObject(reqInfo,"当前报文不是有效json,请检查"+reqInfo);
        this.setDataFlowId(UUID.randomUUID().toString().replace("-","").toLowerCase());
        //赋值请求报文
        this.setResJson(JSONObject.parseObject(reqInfo));
        //赋值 请求头信息
        this.setReqHeaders(headerAll);


        //构建 订单信息
        builderOrderResponse();

        //构建订单项信息
        builderOrderNotifyInfo();

       return this;
    }

    /**
     * 构建订单通知信息
     */
    private void builderOrderNotifyInfo() throws Exception{

        JSONObject tmpReqJson = this.getReqJson();

        Assert.jsonObjectHaveKey(tmpReqJson,"business","没有包含business节点信息,"+tmpReqJson.toJSONString());

        JSONObject tmpBusiness = tmpReqJson.getJSONObject("business");

        this.setbId(tmpBusiness.getString("bId"));
        this.setBusinessTypeCd(tmpBusiness.getString("businessTypeCd"));

        Assert.jsonObjectHaveKey(tmpReqJson,"orders","没有包含orders节点信息,"+tmpReqJson.toJSONString());

        JSONObject tmpOrders = tmpReqJson.getJSONObject("orders");

        this.setTransactionId(tmpOrders.getString("transactionId"));
        this.setResponseTime(DateUtil.getDefaultDateFromString(tmpOrders.getString("responseTime")));
        this.setOrderTypeCd(tmpOrders.getString("orderTypeCd"));
        this.setDataFlowId(tmpOrders.getString("dataFlowId"));
        this.setBusinessType(tmpOrders.getString("businessType"));
    }

    /**
     * 构建返回状态结果信息
     */
    private void builderOrderResponse() {

        JSONObject tmpReqJson = this.getReqJson();

        Assert.jsonObjectHaveKey(tmpReqJson,"business","没有包含business节点信息,"+tmpReqJson.toJSONString());

        JSONObject tmpBusiness = tmpReqJson.getJSONObject("business");

        Assert.jsonObjectHaveKey(tmpBusiness,"response","没有包含response节点，"+tmpBusiness.toJSONString());

        JSONObject tmpResponse = tmpBusiness.getJSONObject("response");

        this.setCode(tmpResponse.getString("code"));

        this.setMessage(tmpResponse.getString("message"));


    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }
}
