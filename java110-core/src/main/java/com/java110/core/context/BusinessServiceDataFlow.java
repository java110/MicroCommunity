package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.InitDataFlowContextException;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务系统数据流
 * Created by wuxw on 2018/5/18.
 */
public class BusinessServiceDataFlow extends AbstractDataFlowContext {
    private final static Logger logger = LoggerFactory.getLogger(BusinessServiceDataFlow.class);


    private String bId;

    private Map<String,Object> paramOut;

    @Override
    public IOrders getOrder() {
        return this;
    }


    public BusinessServiceDataFlow(Date startDate, String code) {
        super(startDate, code);
    }

    public BusinessServiceDataFlow doBuilder(String reqInfo, Map<String, String> headerAll) throws InitDataFlowContextException {
        try{
            Business business = null;
            JSONObject reqInfoObj = JSONObject.parseObject(reqInfo);
            JSONObject orderObj = reqInfoObj.getJSONObject("orders");
            JSONObject businessObject = reqInfoObj.getJSONObject("business");
            this.setReqJson(reqInfoObj);
            this.setDataFlowId(orderObj.containsKey("dataFlowId")?orderObj.getString("dataFlowId"):"-1");
            this.setTransactionId(orderObj.getString("transactionId"));
            this.setOrderTypeCd(orderObj.getString("orderTypeCd"));
            this.setRequestTime(orderObj.getString("requestTime"));
            this.setBusinessType(orderObj.getString("businessType"));
            this.setbId(businessObject.getString("bId"));
            paramOut = new HashMap<String, Object>();
            this.businesses = new ArrayList<Business>();
            business = new Business().builder(businessObject);
            businesses.add(business);
            this.setCurrentBusiness(business);
            if (headerAll != null){
                this.requestCurrentHeaders.putAll(headerAll);
                this.requestHeaders.putAll(headerAll);
            }
        }catch (Exception e){
            logger.error("初始化对象 BusinessServiceDataFlow 失败",e);
            throw new InitDataFlowContextException(ResponseConstant.RESULT_PARAM_ERROR,"初始化对象 BusinessServiceDataFlow 失败 "+reqInfo);
        }
        return this;
    }

    public Map<String, Object> getParamOut() {
        return paramOut;
    }

    public void addParamOut(String key,Object value) {
        this.paramOut.put(key,value);
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }
}
