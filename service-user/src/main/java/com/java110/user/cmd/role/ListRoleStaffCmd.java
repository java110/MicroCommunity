package com.java110.user.cmd.role;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.privilegeUser.PrivilegeUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "role.listRoleStaff")
public class ListRoleStaffCmd extends Cmd {

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "roleId", "未包含角色");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String storeId = context.getReqHeaders().get("store-id");

        PrivilegeUserDto privilegeUserDto = new PrivilegeUserDto();
        privilegeUserDto.setpId(reqJson.getString("roleId"));
        privilegeUserDto.setStoreId(storeId);
        privilegeUserDto.setPrivilegeFlag(PrivilegeUserDto.PRIVILEGE_FLAG_GROUP);
        privilegeUserDto.setUserName(reqJson.getString("searchUserName"));
        privilegeUserDto.setPage(Integer.parseInt(reqJson.getString("page")));
        privilegeUserDto.setRow(Integer.parseInt(reqJson.getString("row")));

        int count = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUserInfoCount(privilegeUserDto);

        List<UserDto> staffsDtos = null;

        if (count > 0) {
            staffsDtos = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUserInfos(privilegeUserDto);
        } else {
            staffsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, staffsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
