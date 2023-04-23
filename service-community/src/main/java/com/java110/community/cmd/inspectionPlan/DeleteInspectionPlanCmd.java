package com.java110.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.po.inspection.InspectionPlanPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "inspectionPlan.deleteInspectionPlan")
public class DeleteInspectionPlanCmd extends Cmd {

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "巡检计划名称不能为空");
        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setInspectionPlanId(reqJson.getString("inspectionPlanId"));
        //根据巡检计划id查询巡检任务
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);
        Assert.listIsNull(inspectionTaskDtos, "该巡检计划正在使用，不能删除！");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(reqJson, InspectionPlanPo.class);
        int flag = inspectionPlanV1InnerServiceSMOImpl.deleteInspectionPlan(inspectionPlanPo);

        if (flag < 1) {
            throw new CmdException("删除巡检计划失败");
        }
    }
}
