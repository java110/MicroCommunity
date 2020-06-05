package com.java110.api.bmo.inspectionTaskDetail.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.inspectionTaskDetail.IInspectionTaskDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.dto.inspectionTaskDetail.InspectionTaskDetailDto;
import com.java110.po.inspection.InspectionTaskDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
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
    public void addInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("taskDetailId", "-1");
        InspectionTaskDetailPo inspectionTaskPo = BeanConvertUtil.covertBean(paramInJson, InspectionTaskDetailPo.class);

        super.insert(dataFlowContext, inspectionTaskPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK_DETAIL);

    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setTaskDetailId(paramInJson.getString("taskDetailId"));
        inspectionTaskDetailDto.setCommunityId(paramInJson.getString("communityId"));
        List<InspectionTaskDetailDto> inspectionTaskDetailDtos = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);

        Assert.listOnlyOne(inspectionTaskDetailDtos, "未找到需要修改的活动 或多条数据");

        JSONObject businessInspectionTaskDetail = new JSONObject();
        businessInspectionTaskDetail.putAll(BeanConvertUtil.beanCovertMap(inspectionTaskDetailDtos.get(0)));
        businessInspectionTaskDetail.putAll(paramInJson);
        InspectionTaskDetailPo inspectionTaskPo = BeanConvertUtil.covertBean(businessInspectionTaskDetail, InspectionTaskDetailPo.class);

        super.insert(dataFlowContext, inspectionTaskPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_TASK_DETAIL);
    }
}
