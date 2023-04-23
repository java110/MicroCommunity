package com.java110.community.cmd.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanStaffDto;
import com.java110.intf.community.IInspectionPlanStaffInnerServiceSMO;
import com.java110.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.java110.po.inspection.InspectionPlanStaffPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "inspectionPlanStaff.updateInspectionPlanStaff")
public class UpdateInspectionPlanStaffCmd extends Cmd {

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "ipStaffId", "ipStaffId不能为空");
        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "staffName", "请求报文中未包含staffName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "请求报文中未包含inspectionPlanId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanStaffDto inspectionPlanStaffDto = new InspectionPlanStaffDto();
        inspectionPlanStaffDto.setIpStaffId(reqJson.getString("ipStaffId"));
        inspectionPlanStaffDto.setCommunityId(reqJson.getString("communityId"));
        List<InspectionPlanStaffDto> inspectionPlanStaffDtos = inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto);

        Assert.listOnlyOne(inspectionPlanStaffDtos, "未找到需要修改的活动 或多条数据");

        JSONObject businessInspectionPlanStaff = new JSONObject();
        businessInspectionPlanStaff.putAll(BeanConvertUtil.beanCovertMap(inspectionPlanStaffDtos.get(0)));
        businessInspectionPlanStaff.putAll(reqJson);
        InspectionPlanStaffPo inspectionPlanStaffPo = BeanConvertUtil.covertBean(businessInspectionPlanStaff, InspectionPlanStaffPo.class);

        int flag = inspectionPlanStaffV1InnerServiceSMOImpl.updateInspectionPlanStaff(inspectionPlanStaffPo);
        if (flag < 1) {
            throw new CmdException("修改巡检师傅失败");
        }
    }
}
