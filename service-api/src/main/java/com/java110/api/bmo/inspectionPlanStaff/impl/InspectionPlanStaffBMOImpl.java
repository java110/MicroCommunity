package com.java110.api.bmo.inspectionPlanStaff.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.inspectionPlanStaff.IInspectionPlanStaffBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IInspectionPlanStaffInnerServiceSMO;
import com.java110.dto.inspectionPlanStaff.InspectionPlanStaffDto;
import com.java110.po.inspection.InspectionPlanStaffPo;
import com.java110.utils.constant.BusinessTypeConstant;
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
    public void addInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("ipStaffId", "-1");

        InspectionPlanStaffPo inspectionPlanStaffPo = BeanConvertUtil.covertBean(paramInJson, InspectionPlanStaffPo.class);

        super.insert(dataFlowContext, inspectionPlanStaffPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PLAN_STAFF);

    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionPlanStaffDto inspectionPlanStaffDto = new InspectionPlanStaffDto();
        inspectionPlanStaffDto.setIpStaffId(paramInJson.getString("ipStaffId"));
        inspectionPlanStaffDto.setCommunityId(paramInJson.getString("communityId"));
        List<InspectionPlanStaffDto> inspectionPlanStaffDtos = inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto);

        Assert.listOnlyOne(inspectionPlanStaffDtos, "未找到需要修改的活动 或多条数据");

        JSONObject businessInspectionPlanStaff = new JSONObject();
        businessInspectionPlanStaff.putAll(BeanConvertUtil.beanCovertMap(inspectionPlanStaffDtos.get(0)));
        businessInspectionPlanStaff.putAll(paramInJson);
        InspectionPlanStaffPo inspectionPlanStaffPo = BeanConvertUtil.covertBean(businessInspectionPlanStaff, InspectionPlanStaffPo.class);

        super.update(dataFlowContext, inspectionPlanStaffPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PLAN_STAFF);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionPlanStaffPo inspectionPlanStaffPo = BeanConvertUtil.covertBean(paramInJson, InspectionPlanStaffPo.class);

        super.delete(dataFlowContext, inspectionPlanStaffPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PLAN_STAFF);
    }

}
