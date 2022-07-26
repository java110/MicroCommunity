package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 保存 员工 组织关系
 */
@Java110Cmd(serviceCode = "user.saveStaffOrgRel")
public class SaveStaffOrgRelCmd extends Cmd {

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "orgId", "未包含组织");
        Assert.hasKeyAndValue(reqJson, "staffIds", "未包含员工");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String[] staffIds = reqJson.getString("staffIds").split(",");

        String storeId = context.getReqHeaders().get("store-id");

        if (staffIds == null || staffIds.length < 1) {
            throw new CmdException("未包含员工信息");
        }
        OrgStaffRelDto orgRelDto = null;
        int count = 0;
        OrgStaffRelPo orgStaffRelPo = null;
        int flag = 0;
        for(String staffId : staffIds){
            orgRelDto = new OrgStaffRelDto();
            orgRelDto.setOrgId(reqJson.getString("orgId"));
            orgRelDto.setStaffId(staffId);
            count = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRelsCount(orgRelDto);
            if(count > 0){
                continue;
            }
            orgStaffRelPo = new OrgStaffRelPo();
            orgStaffRelPo.setStoreId(storeId);
            orgStaffRelPo.setRelCd(OrgStaffRelDto.REL_CD_PUBLIC);
            orgStaffRelPo.setRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            orgStaffRelPo.setStaffId(staffId);
            orgStaffRelPo.setOrgId(reqJson.getString("orgId"));
            orgStaffRelPo.setbId("-1");
            flag =  orgStaffRelV1InnerServiceSMOImpl.saveOrgStaffRel(orgStaffRelPo);
            if(flag < 1){
                throw new CmdException("关联员工失败");
            }
        }

    }
}
