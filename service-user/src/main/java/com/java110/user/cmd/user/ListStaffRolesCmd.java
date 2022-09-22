package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.privilegeUser.PrivilegeUserDto;
import com.java110.dto.roleCommunity.RoleCommunityDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.intf.user.IRoleCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.community.ApiCommunityDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "user.listStaffRoles")
public class ListStaffRolesCmd extends Cmd {

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含 员工信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String storeId = context.getReqHeaders().get("store-id");
        Assert.hasLength(storeId, "未包含商户信息");

        PrivilegeUserDto privilegeUserDto = BeanConvertUtil.covertBean(reqJson, PrivilegeUserDto.class);
        privilegeUserDto.setUserId(reqJson.getString("staffId"));
        privilegeUserDto.setPrivilegeFlag(PrivilegeUserDto.PRIVILEGE_FLAG_GROUP);
        int count = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUsersCount(privilegeUserDto);
        List<PrivilegeUserDto> roles = null;
        if (count > 0) {
            roles = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUsers(privilegeUserDto);
            for (PrivilegeUserDto privilegeUserDto1 : roles) {
                RoleCommunityDto roleCommunityDto=new RoleCommunityDto();
                roleCommunityDto.setRoleId(privilegeUserDto1.getpId());
                roleCommunityDto.setRow(Integer.valueOf("999"));
                List<RoleCommunityDto> roleCommunityDtos = roleCommunityV1InnerServiceSMOImpl.queryRoleCommunitys(roleCommunityDto);
                privilegeUserDto1.setRoleCommunityDtoList(roleCommunityDtos);
            }


        } else {
            roles = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo(1, roles.size(), roles);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);


    }

    private void freshOrgName(List<OrgDto> orgDtos, List<OrgStaffRelDto> orgStaffRels) {

        for (OrgStaffRelDto orgStaffRelDto : orgStaffRels) {
            orgStaffRelDto.setParentOrgId(orgStaffRelDto.getOrgId());
            findParents(orgStaffRelDto, orgDtos, null);
        }
    }

    private void findParents(OrgStaffRelDto orgStaffRelDto, List<OrgDto> orgDtos, OrgDto curOrgDto) {
        for (OrgDto orgDto : orgDtos) {
            if (!orgStaffRelDto.getParentOrgId().equals(orgDto.getOrgId())) { // 他自己跳过
                continue;
            }
            orgStaffRelDto.setParentOrgId(orgDto.getParentOrgId());
            curOrgDto = orgDto;
            if (StringUtil.isEmpty(orgStaffRelDto.getOrgName())) {
                orgStaffRelDto.setOrgName(orgDto.getOrgName() );
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

        findParents(orgStaffRelDto, orgDtos, curOrgDto);
    }
}
