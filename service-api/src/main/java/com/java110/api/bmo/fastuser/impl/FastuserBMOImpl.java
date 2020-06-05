package com.java110.api.bmo.fastuser.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.fastuser.IFastuserBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

@Service("fastuserBMOImpl")
public class FastuserBMOImpl extends ApiBaseBMO implements IFastuserBMO {


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFastuser(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FASTUSER);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFastuer = new JSONObject();
        businessFastuer.putAll(paramInJson);
        businessFastuer.put("state","11000"); // 先设置为不审核
        //businessActivities.put("activitiesId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFastuser", businessFastuer);
        return business;
    }

}
