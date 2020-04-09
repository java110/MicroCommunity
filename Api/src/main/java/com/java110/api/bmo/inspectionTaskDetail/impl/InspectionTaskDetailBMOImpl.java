package com.java110.api.bmo.inspectionTaskDetail.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.inspectionTaskDetail.IInspectionTaskDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionTaskDetail.IInspectionTaskDetailInnerServiceSMO;
import com.java110.dto.inspectionTaskDetail.InspectionTaskDetailDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("inspectionTaskDetailBMOImpl")
public class InspectionTaskDetailBMOImpl extends ApiBaseBMO implements IInspectionTaskDetailBMO {

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionTaskDetail = new JSONObject();
        businessInspectionTaskDetail.putAll(paramInJson);
        businessInspectionTaskDetail.put("taskDetailId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionTaskDetail", businessInspectionTaskDetail);
        return business;
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setTaskDetailId(paramInJson.getString("taskDetailId"));
        inspectionTaskDetailDto.setCommunityId(paramInJson.getString("communityId"));
        List<InspectionTaskDetailDto> inspectionTaskDetailDtos = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);

        Assert.listOnlyOne(inspectionTaskDetailDtos, "未找到需要修改的活动 或多条数据");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_TASK_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionTaskDetail = new JSONObject();
        businessInspectionTaskDetail.putAll(BeanConvertUtil.beanCovertMap(inspectionTaskDetailDtos.get(0)));
        businessInspectionTaskDetail.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionTaskDetail", businessInspectionTaskDetail);
        return business;
    }
}
