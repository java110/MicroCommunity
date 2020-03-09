package com.java110.api.bmo.activities.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.activities.IActivitiesBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("activitiesBMOImpl")
public class ActivitiesBMOImpl extends ApiBaseBMO implements IActivitiesBMO {

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addHeaderImg(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "70000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("activitiesId"));
        businessUnit.put("fileRealName", paramInJson.getString("headerImg"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);

        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addActivities(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACTIVITIES);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessActivities = new JSONObject();
        businessActivities.putAll(paramInJson);
        businessActivities.put("readCount","0");
        businessActivities.put("likeCount","0");
        businessActivities.put("collectCount","0");
        businessActivities.put("state","11000"); // 先设置为不审核
        //businessActivities.put("activitiesId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessActivities", businessActivities);
        return business;
    }

}
