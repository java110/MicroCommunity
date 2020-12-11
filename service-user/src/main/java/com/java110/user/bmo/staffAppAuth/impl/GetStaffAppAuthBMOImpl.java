package com.java110.user.bmo.staffAppAuth.impl;

import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.user.bmo.staffAppAuth.IGetStaffAppAuthBMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getStaffAppAuthBMOImpl")
public class GetStaffAppAuthBMOImpl implements IGetStaffAppAuthBMO {

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    /**
     * @param staffAppAuthDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StaffAppAuthDto staffAppAuthDto) {
        UserDto userDto = new UserDto();
        userDto.setUserId(staffAppAuthDto.getStaffId());
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        staffAppAuthDto.setStaffName(userDtos.get(0).getName());

        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);

        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
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
        return ResultVo.createResponseEntity(staffAppAuthDto);
    }

}
