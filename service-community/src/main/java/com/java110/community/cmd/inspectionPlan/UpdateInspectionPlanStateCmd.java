package com.java110.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.intf.community.IInspectionPlanInnerServiceSMO;
import com.java110.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.java110.po.inspection.InspectionPlanPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "inspectionPlan.updateInspectionPlanState")
public class UpdateInspectionPlanStateCmd extends Cmd {

    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "inspectionPlanId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写状态");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        InspectionPlanDto inspectionPlanDto = new InspectionPlanDto();
        inspectionPlanDto.setCommunityId(reqJson.getString("communityId"));
        inspectionPlanDto.setInspectionPlanId(reqJson.getString("inspectionPlanId"));
        List<InspectionPlanDto> inspectionPlanDtos = inspectionPlanInnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto);

        Assert.listOnlyOne(inspectionPlanDtos, "根据计划ID查询到多条记录，请检查数据");
        JSONObject businessInspectionPlan = new JSONObject();
        businessInspectionPlan.putAll(BeanConvertUtil.beanCovertMap(inspectionPlanDtos.get(0)));
        businessInspectionPlan.put("state", reqJson.getString("state"));
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(businessInspectionPlan, InspectionPlanPo.class);
        int flag = inspectionPlanV1InnerServiceSMOImpl.updateInspectionPlan(inspectionPlanPo);
        if (flag < 1) {
            throw new CmdException("修改巡检计划失败");
        }
    }
}
