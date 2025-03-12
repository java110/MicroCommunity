package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "user.queryAdminStaffAppAuth")
public class QueryAdminStaffAppAuthCmd extends Cmd {

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        StaffAppAuthDto staffAppAuthDto = BeanConvertUtil.covertBean(reqJson, StaffAppAuthDto.class);
        staffAppAuthDto.setStoreId(reqJson.getString("propertyStoreId"));

        UserDto userDto = new UserDto();
        userDto.setUserId(staffAppAuthDto.getStaffId());
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        staffAppAuthDto.setStaffName(userDtos.get(0).getName());

        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);

        if (ListUtil.isNull(staffAppAuthDtos)) {
            staffAppAuthDto.setStateName("未认证");
            staffAppAuthDto.setState("1001");
            staffAppAuthDto.setOpenId("-");
            staffAppAuthDto.setAppType("-");
            staffAppAuthDto.setOpenName("-");
        } else {
            staffAppAuthDto.setStateName("已认证");
            staffAppAuthDto.setState("2002");
            staffAppAuthDto.setOpenId(staffAppAuthDtos.get(0).getOpenId());
            staffAppAuthDto.setAppType(staffAppAuthDtos.get(0).getAppType());
            staffAppAuthDto.setOpenName(staffAppAuthDtos.get(0).getOpenName());
            staffAppAuthDto.setCreateTime(staffAppAuthDtos.get(0).getCreateTime());
        }
        ResponseEntity responseEntity = ResultVo.createResponseEntity(staffAppAuthDto);
        context.setResponseEntity(responseEntity);
    }
}
