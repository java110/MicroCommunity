package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "user.listAdminStaffOrgs")
public class ListAdminStaffOrgsCmd extends Cmd {

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含 员工信息");
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String storeId = context.getReqHeaders().get("store-id");
        Assert.hasLength(storeId, "未包含商户信息");

        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffId(reqJson.getString("staffId"));
        List<OrgStaffRelDto> orgStaffRels = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);

        if (ListUtil.isNull(orgStaffRels)) {
            return;
        }

        OrgDto orgDto = new OrgDto();
        orgDto.setStoreId(orgStaffRels.get(0).getStoreId());
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);
        if (ListUtil.isNull(orgDtos)) {
            return;
        }


        freshOrgName(orgDtos, orgStaffRels);
        ResultVo resultVo = new ResultVo(1, orgStaffRels.size(), orgStaffRels);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void freshOrgName(List<OrgDto> orgDtos, List<OrgStaffRelDto> orgStaffRels) {

        for (OrgStaffRelDto orgStaffRelDto : orgStaffRels) {
            orgStaffRelDto.setParentOrgId(orgStaffRelDto.getOrgId());
            findParents(orgStaffRelDto, orgDtos, null, 0);
        }
    }

    private void findParents(OrgStaffRelDto orgStaffRelDto, List<OrgDto> orgDtos, OrgDto curOrgDto, int orgDeep) {
        for (OrgDto orgDto : orgDtos) {
            if (!orgStaffRelDto.getParentOrgId().equals(orgDto.getOrgId())) { // 他自己跳过
                continue;
            }
            orgStaffRelDto.setParentOrgId(orgDto.getParentOrgId());
            curOrgDto = orgDto;
            if (StringUtil.isEmpty(orgStaffRelDto.getOrgName())) {
                orgStaffRelDto.setOrgName(orgDto.getOrgName());
                continue;
            }
            orgStaffRelDto.setOrgName(orgDto.getOrgName() + " / " + orgStaffRelDto.getOrgName());
        }

        if (curOrgDto != null && OrgDto.ORG_LEVEL_STORE.equals(curOrgDto.getOrgLevel())) {
            return;
        }

        if (curOrgDto != null && curOrgDto.getParentOrgId().equals(curOrgDto.getOrgId())) {
            return;
        }

        if (curOrgDto != null && "-1".equals(curOrgDto.getParentOrgId())) {
            return;
        }
        orgDeep += 1;
        if (orgDeep > 20) {
            return;
        }

        findParents(orgStaffRelDto, orgDtos, curOrgDto, orgDeep);
    }
}
