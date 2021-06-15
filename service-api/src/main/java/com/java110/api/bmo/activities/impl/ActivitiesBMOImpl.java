/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.api.bmo.activities.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.activities.IActivitiesBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IActivitiesInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.dto.activities.ActivitiesDto;
import com.java110.dto.file.FileRelDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("activitiesBMOImpl")
public class ActivitiesBMOImpl extends ApiBaseBMO implements IActivitiesBMO {

    @Autowired
    private IActivitiesInnerServiceSMO activitiesInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;
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

    /**
     * 修改头部照片
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     public JSONObject editHeaderImg(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("70000");
        fileRelDto.setObjId(paramInJson.getString("activitiesId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() == 0) {
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
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FILE_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(fileRelDtos.get(0)));
        businessUnit.put("fileRealName", paramInJson.getString("headerImg"));
        businessUnit.put("fileSaveName", paramInJson.getString("headerImg"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFileRel", businessUnit);
        return business;
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateActivities(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ActivitiesDto activitiesDto = new ActivitiesDto();
        activitiesDto.setActivitiesId(paramInJson.getString("activitiesId"));
        activitiesDto.setCommunityId(paramInJson.getString("communityId"));
        List<ActivitiesDto> activitiesDtos = activitiesInnerServiceSMOImpl.queryActivitiess(activitiesDto);

        Assert.listOnlyOne(activitiesDtos, "未找到需要修改的活动 或多条数据");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ACTIVITIES);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessActivities = new JSONObject();
        businessActivities.putAll(paramInJson);
        businessActivities.put("userId",activitiesDtos.get(0).getUserId());
        businessActivities.put("userName",activitiesDtos.get(0).getUserName());
        businessActivities.put("readCount",activitiesDtos.get(0).getReadCount());
        businessActivities.put("likeCount",activitiesDtos.get(0).getLikeCount());
        businessActivities.put("collectCount",activitiesDtos.get(0).getCollectCount());
        businessActivities.put("state",activitiesDtos.get(0).getState()); // 先设置为不审核
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessActivities", businessActivities);
        return business;
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteActivities(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ACTIVITIES);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessActivities = new JSONObject();
        businessActivities.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessActivities", businessActivities);
        return business;
    }

}
