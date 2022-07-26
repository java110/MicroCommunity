package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "org.deleteOrgRelStaff")
public class DeleteStaffOrgRelCmd extends Cmd {
    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userId", "未包含用户信息");
        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户信息");
        Assert.hasKeyAndValue(reqJson, "orgId", "未包含组织信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setOrgId(reqJson.getString("orgId"));
        orgStaffRelDto.setStaffId(reqJson.getString("staffId"));
        List<OrgStaffRelDto> orgStaffRelDtos = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);
        Assert.listOnlyOne(orgStaffRelDtos, "关系不存在");
        OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
        orgStaffRelPo.setRelId(orgStaffRelDtos.get(0).getRelId());
        int flag =  orgStaffRelV1InnerServiceSMOImpl.deleteOrgStaffRel(orgStaffRelPo);
        if(flag < 1){
            throw new CmdException("关联员工失败");
        }
    }
}
