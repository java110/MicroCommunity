package com.java110.api.bmo.@@templateCode@@.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.@@templateCode@@.I@@TemplateCode@@BMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.@@templateCode@@.I@@TemplateCode@@InnerServiceSMO;
import com.java110.dto.@@templateCode@@.@@TemplateCode@@Dto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("@@templateCode@@BMOImpl")
public class @@TemplateCode@@BMOImpl extends ApiBaseBMO implements I@@TemplateCode@@BMO {

    @Autowired
    private I@@TemplateCode@@InnerServiceSMO @@templateCode@@InnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject add@@TemplateCode@@(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_@@TEMPLATECODE@@);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject business@@TemplateCode@@ = new JSONObject();
        business@@TemplateCode@@.putAll(paramInJson);
        business@@TemplateCode@@.put("@@templateKey@@", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("business@@TemplateCode@@", business@@TemplateCode@@);
        return business;
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject update@@TemplateCode@@(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        @@TemplateCode@@Dto @@templateCode@@Dto = new @@TemplateCode@@Dto();
        @@templateCode@@Dto.set@@TemplateKey@@(paramInJson.getString("@@templateKey@@"));
        @@templateCode@@Dto.set@@ShareName@@Id(paramInJson.getString("@@shareName@@Id"));
        List<@@TemplateCode@@Dto> @@templateCode@@Dtos = @@templateCode@@InnerServiceSMOImpl.query@@TemplateCode@@s(@@templateCode@@Dto);

        Assert.listOnlyOne(@@templateCode@@Dtos, "未找到需要修改的活动 或多条数据");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_@@TEMPLATECODE@@);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject business@@TemplateCode@@ = new JSONObject();
        business@@TemplateCode@@.putAll(BeanConvertUtil.beanCovertMap(@@templateCode@@Dtos.get(0)));
        business@@TemplateCode@@.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("business@@TemplateCode@@", business@@TemplateCode@@);
        return business;
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject delete@@TemplateCode@@(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_@@TEMPLATECODE@@);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject business@@TemplateCode@@ = new JSONObject();
        business@@TemplateCode@@.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("business@@TemplateCode@@", business@@TemplateCode@@);
        return business;
    }

}
