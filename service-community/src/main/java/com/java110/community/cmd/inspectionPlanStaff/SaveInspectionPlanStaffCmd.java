package com.java110.community.cmd.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.java110.po.inspection.InspectionPlanStaffPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "inspectionPlanStaff.saveInspectionPlanStaff")
public class SaveInspectionPlanStaffCmd extends Cmd {

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "staffName", "请求报文中未包含staffName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "请求报文中未包含inspectionPlanId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        reqJson.put("ipStaffId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ipStaffId));

        InspectionPlanStaffPo inspectionPlanStaffPo = BeanConvertUtil.covertBean(reqJson, InspectionPlanStaffPo.class);

        int flag = inspectionPlanStaffV1InnerServiceSMOImpl.saveInspectionPlanStaff(inspectionPlanStaffPo);
        if (flag < 1) {
            throw new CmdException("保存巡检师傅失败");
        }
    }
}
