package com.java110.api.bmo.inspectionPlanStaff.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.inspectionPlanStaff.IInspectionPlanStaffBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionPlanStaff.IInspectionPlanStaffInnerServiceSMO;
import com.java110.dto.inspectionPlanStaff.InspectionPlanStaffDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("inspectionPlanStaffBMOImpl")
public class InspectionPlanStaffBMOImpl extends ApiBaseBMO implements IInspectionPlanStaffBMO {

    @Autowired
    private IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PLAN_STAFF);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionPlanStaff = new JSONObject();
        businessInspectionPlanStaff.putAll(paramInJson);
        businessInspectionPlanStaff.put("ipStaffId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionPlanStaff", businessInspectionPlanStaff);
        return business;
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionPlanStaffDto inspectionPlanStaffDto = new InspectionPlanStaffDto();
        inspectionPlanStaffDto.setIpStaffId(paramInJson.getString("ipStaffId"));
        inspectionPlanStaffDto.setCommunityId(paramInJson.getString("communityId"));
        List<InspectionPlanStaffDto> inspectionPlanStaffDtos = inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto);

        Assert.listOnlyOne(inspectionPlanStaffDtos, "未找到需要修改的活动 或多条数据");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PLAN_STAFF);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionPlanStaff = new JSONObject();
        businessInspectionPlanStaff.putAll(BeanConvertUtil.beanCovertMap(inspectionPlanStaffDtos.get(0)));
        businessInspectionPlanStaff.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionPlanStaff", businessInspectionPlanStaff);
        return business;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PLAN_STAFF);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionPlanStaff = new JSONObject();
        businessInspectionPlanStaff.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionPlanStaff", businessInspectionPlanStaff);
        return business;
    }

}
