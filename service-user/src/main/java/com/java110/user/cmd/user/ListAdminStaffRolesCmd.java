package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.privilege.PrivilegeUserDto;
import com.java110.dto.privilege.RoleCommunityDto;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.intf.user.IRoleCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "user.listAdminStaffRoles")
public class ListAdminStaffRolesCmd extends Cmd {

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含 员工信息");
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        PrivilegeUserDto privilegeUserDto = BeanConvertUtil.covertBean(reqJson, PrivilegeUserDto.class);
        privilegeUserDto.setStoreId("");
        privilegeUserDto.setUserId(reqJson.getString("staffId"));
        privilegeUserDto.setPrivilegeFlag(PrivilegeUserDto.PRIVILEGE_FLAG_GROUP);
        int count = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUsersCount(privilegeUserDto);
        List<PrivilegeUserDto> roles = null;
        if (count > 0) {
            roles = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUsers(privilegeUserDto);
            for (PrivilegeUserDto privilegeUserDto1 : roles) {
                RoleCommunityDto roleCommunityDto = new RoleCommunityDto();
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
}
