package com.java110.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.intf.community.IInspectionPlanInnerServiceSMO;
import com.java110.intf.community.IInspectionRouteV1InnerServiceSMO;
import com.java110.po.inspection.InspectionRoutePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "inspectionRoute.deleteInspectionRoute")
public class DeleteInspectionRouteCmd extends Cmd {

    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionRouteV1InnerServiceSMO inspectionRouteV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "路线ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        InspectionPlanDto inspectionPlanDto = new InspectionPlanDto();
        inspectionPlanDto.setInspectionRouteId(reqJson.getString("inspectionRouteId"));
        //根据巡检路线id查询巡检计划
        List<InspectionPlanDto> inspectionPlanDtos = inspectionPlanInnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto);
        Assert.listIsNull(inspectionPlanDtos, "该巡检路线正在使用，不能删除！");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRoutePo inspectionRoutePo = BeanConvertUtil.covertBean(reqJson, InspectionRoutePo.class);
        int flag = inspectionRouteV1InnerServiceSMOImpl.deleteInspectionRoute(inspectionRoutePo);

        if (flag < 1) {
            throw new CmdException("删除巡检路线失败");
        }
    }
}
