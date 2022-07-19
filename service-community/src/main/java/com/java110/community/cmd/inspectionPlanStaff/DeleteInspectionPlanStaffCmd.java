package com.java110.community.cmd.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.java110.po.inspection.InspectionPlanStaffPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "inspectionPlanStaff.deleteInspectionPlanStaff")
public class DeleteInspectionPlanStaffCmd extends Cmd {

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "ipStaffId", "ipStaffId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区信息不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanStaffPo inspectionPlanStaffPo = BeanConvertUtil.covertBean(reqJson, InspectionPlanStaffPo.class);
        int flag = inspectionPlanStaffV1InnerServiceSMOImpl.deleteInspectionPlanStaff(inspectionPlanStaffPo);
        if (flag < 1) {
            throw new CmdException("删除巡检师傅失败");
        }
    }
}
