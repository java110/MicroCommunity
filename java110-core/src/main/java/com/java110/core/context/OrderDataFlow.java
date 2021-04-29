package com.java110.core.context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.entity.order.Business;
import com.java110.entity.order.BusinessAttrs;
import com.java110.entity.order.Orders;
import com.java110.entity.order.OrdersAttrs;
import com.java110.utils.util.StringUtil;

import java.util.*;

/**
 * 主要用于离散成对象 httpApi service 请求过程消息记录和返回记录
 * Created by wuxw on 2018/4/13.
 */
public class OrderDataFlow extends AbstractOrderDataFlowContext {

    public OrderDataFlow(){}

    public OrderDataFlow(Date startDate, String code) {
        super(startDate, code);
    }

    /**
            * 订单
     */
    private Orders orders;

    /**
            * 订单项信息
     */
    private List<Business> businessList;


    /*private String dataFlowId;



    *//**
     * 请求头信息
     *//*
    private Map<String,String> reqHeaders;

    *//**
     * 请求体信息（只支持json）
     *//*
    private JSONObject reqJson;

    *//**
     * 返回头信息
     *//*
    private Map<String,String> resHeaders;

    *//**
     * 返回体信息 （只支持json）
     *//*
    private JSONObject resJson;*/



    /**
     * 构建 OrderDataFlow 对象
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public OrderDataFlow doBuilder(String reqInfo, Map<String,String> headerAll) throws Exception{

        //校验请求报文格式
        Assert.isJsonObject(reqInfo,"当前报文不是有效json,请检查"+reqInfo);
         this.setDataFlowId(UUID.randomUUID().toString().replace("-","").toLowerCase());
        //赋值请求报文
        this.setReqJson(JSONObject.parseObject(reqInfo));
        //赋值 请求头信息
        this.setReqHeaders(headerAll);

        //构建返回头
        builderResHeaders();

        //构建返回json 为{"msg":[]}
        builderResJson();

        //构建 订单信息
        builderOrders();

        //构建订单项信息
        builderBusiness();

        return this;
    }

    /**
     * 构建返回头信息
     */
    private void builderResHeaders() {
        Map<String,String> tmpResHeaders = new HashMap<String,String>();
        tmpResHeaders.put(CommonConstant.HTTP_TRANSACTION_ID,this.getReqHeaders().get(CommonConstant.HTTP_TRANSACTION_ID));
        tmpResHeaders.put(CommonConstant.HTTP_RES_TIME,DateUtil.getyyyyMMddhhmmssDateString());
        this.setResHeaders(tmpResHeaders);
    }


    /**
     * 构建订单
     *
     */
    private void builderOrders() throws Exception{
        this.orders = new Orders();

        Assert.hasKey(this.getReqHeaders(),CommonConstant.HTTP_APP_ID,"构建OrderDataFlow对象失败，请求头中未包含"+CommonConstant.HTTP_APP_ID);

        this.orders.setAppId(this.getReqHeaders().get(CommonConstant.HTTP_APP_ID));

        Assert.hasKey(this.getReqHeaders(),CommonConstant.HTTP_USER_ID,"构建OrderDataFlow对象失败，请求头中未包含"+CommonConstant.HTTP_USER_ID);

        this.orders.setUserId(this.getReqHeaders().get(CommonConstant.HTTP_USER_ID));

        Assert.hasKey(this.getReqHeaders(),CommonConstant.HTTP_TRANSACTION_ID,"构建OrderDataFlow对象失败，请求头中未包含"+CommonConstant.HTTP_TRANSACTION_ID);

        this.orders.setExtTransactionId(this.getReqHeaders().get(CommonConstant.HTTP_TRANSACTION_ID));

        Assert.hasKey(this.getReqHeaders(),CommonConstant.HTTP_REQ_TIME,"构建OrderDataFlow对象失败，请求头中未包含"+CommonConstant.HTTP_REQ_TIME);

        Assert.isDate(this.getReqHeaders().get(CommonConstant.HTTP_REQ_TIME),"构建OrderDataFlow对象失败，请求头中"+CommonConstant.HTTP_REQ_TIME+"格式错误");

        this.orders.setRequestTime(this.getReqHeaders().get(CommonConstant.HTTP_REQ_TIME));

        String oId = this.getReqHeaders().get(CommonConstant.O_ID);

        if(StringUtil.isEmpty(oId)){
            oId = "-1";
        }

        this.orders.setoId(oId);

        JSONObject tmpOrderJson = this.getReqJson().getJSONObject("orders");

        Assert.jsonObjectHaveKey(tmpOrderJson,"orderTypeCd","请求报文错误，未找到orderTypeCd节点");
        this.orders.setOrderTypeCd(tmpOrderJson.getString("orderTypeCd"));

        if(tmpOrderJson.containsKey("orderProcess") && !StringUtil.isEmpty(tmpOrderJson.getString("orderProcess"))){
            this.orders.setOrderProcess(tmpOrderJson.getString("orderProcess"));
        }

        if(tmpOrderJson.containsKey("oId") && !StringUtil.isEmpty(tmpOrderJson.getString("oId"))){
            this.orders.setoId(tmpOrderJson.getString("oId"));
        }

        if(!tmpOrderJson.containsKey("attrs")){
            return ;
        }

        JSONArray tmpOrderAttrs = tmpOrderJson.getJSONArray("attrs");


        List<OrdersAttrs> ordersAttrsList = new ArrayList<OrdersAttrs>();
        OrdersAttrs ordersAttrs = null;
        for(int tmpOrderAttrsIndex = 0;tmpOrderAttrsIndex < tmpOrderAttrs.size();tmpOrderAttrsIndex ++){
            ordersAttrs= new OrdersAttrs();
            ordersAttrs.setAttrId("-"+(tmpOrderAttrsIndex+1));
            ordersAttrs.setSpecCd(tmpOrderAttrs.getJSONObject(tmpOrderAttrsIndex).getString("specId"));
            ordersAttrs.setValue(tmpOrderAttrs.getJSONObject(tmpOrderAttrsIndex).getString("value"));
            ordersAttrsList.add(ordersAttrs);
        }

        this.orders.setOrdersAttrs(ordersAttrsList);

    }

    /**
     * 构建  订单项
     */
    private void builderBusiness() {

        this.businessList = new ArrayList<Business>();
        if(!this.getReqJson().containsKey("business")){
            return;
        }

        JSONArray tmpBusiness = this.getReqJson().getJSONArray("business");

        Business business = null;
        for (int tmpBusinessIndex = 0;tmpBusinessIndex < tmpBusiness.size();tmpBusinessIndex++){
            business = new Business();
            business.setbId("-"+(tmpBusinessIndex+1));
            Assert.jsonObjectHaveKey(tmpBusiness.getJSONObject(tmpBusinessIndex),"businessTypeCd","构建OrderDataFlow对象失败，business中未包含businessTypeCd节点");

            business.setBusinessTypeCd(tmpBusiness.getJSONObject(tmpBusinessIndex).getString("businessTypeCd"));

            business.setoId("-1");

            Assert.jsonObjectHaveKey(tmpBusiness.getJSONObject(tmpBusinessIndex),"datas","构建OrderDataFlow对象失败，business中未包含datas节点");

            business.setData(tmpBusiness.getJSONObject(tmpBusinessIndex).getJSONObject("datas"));

            if(tmpBusiness.getJSONObject(tmpBusinessIndex).containsKey("invokeModel")){
                business.setInvokeModel(tmpBusiness.getJSONObject(tmpBusinessIndex).getString("invokeModel"));
            }

            Assert.jsonObjectHaveKey(tmpBusiness.getJSONObject(tmpBusinessIndex),"seq","构建OrderDataFlow对象失败，business中未包含seq节点");

            business.setSeq(tmpBusiness.getJSONObject(tmpBusinessIndex).getLongValue("seq"));

            if(tmpBusiness.getJSONObject(tmpBusinessIndex).containsKey("attrs")){
                builderBusinessAttrs(tmpBusiness.getJSONObject(tmpBusinessIndex),business);
            }

            businessList.add(business);
        }
    }

    /**
     * 构建订单项属性
     * @param tmpBusiness 每个business
     * @param business
     */
    private void builderBusinessAttrs(JSONObject tmpBusiness, Business business) {

        JSONArray tmpBusinessAttrs = tmpBusiness.getJSONArray("attrs");


        List<BusinessAttrs> businessAttrsList = new ArrayList<BusinessAttrs>();
        BusinessAttrs businessAttrs = null;
        for(int tmpBusinessAttrsIndex = 0;tmpBusinessAttrsIndex < tmpBusinessAttrs.size();tmpBusinessAttrsIndex ++){
            businessAttrs= new BusinessAttrs();
            businessAttrs.setAttrId("-"+(tmpBusinessAttrsIndex+1));
            businessAttrs.setSpecCd(tmpBusinessAttrs.getJSONObject(tmpBusinessAttrsIndex).getString("specId"));
            businessAttrs.setValue(tmpBusinessAttrs.getJSONObject(tmpBusinessAttrsIndex).getString("value"));
            businessAttrsList.add(businessAttrs);
        }
    }

    /**
     * 初始化构建返回报文
     */
    private void builderResJson(){
        JSONObject resJson = new JSONObject();
        resJson.put("msg",new JSONArray());
        this.setResJson(resJson);
    }


    public Orders getOrders() {
        return orders;
    }




    public List<Business> getBusinessList() {
        return businessList;
    }


}
