package com.java110.user.bmo.rentingPool.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.rentingPool.RentingPoolFlowDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.po.rentingPoolFlow.RentingPoolFlowPo;
import com.java110.user.bmo.rentingPool.IAuditRentingBMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审核房屋出租
 */
@Service("auditRentingBMOImpl")
public class AuditRentingBMOImpl implements IAuditRentingBMO {
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRentingPoolFlowInnerServiceSMO rentingPoolFlowInnerServiceSMOImpl;

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public ResponseEntity<String> audit(RentingPoolFlowPo rentingPoolFlowPo, String userId) {
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户信息不存在");

        UserDto tmpUserDto = userDtos.get(0);

        rentingPoolFlowPo.setUseName(tmpUserDto.getName());
        rentingPoolFlowPo.setUserTel(tmpUserDto.getTel());

        RentingPoolDto rentingPoolDto = new RentingPoolDto();
        rentingPoolDto.setRentingId(rentingPoolFlowPo.getRentingId());

        List<RentingPoolDto> rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

        Assert.listOnlyOne(rentingPoolDtos, "未包含房屋出租信息");

        rentingPoolFlowPo.setCommunityId(rentingPoolDtos.get(0).getCommunityId());
        int saveFlag = rentingPoolFlowInnerServiceSMOImpl.saveRentingPoolFlow(rentingPoolFlowPo);

        if (saveFlag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "审核失败");
        }
        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setRentingId(rentingPoolFlowPo.getRentingId());
        rentingPoolPo.setCommunityId(rentingPoolDtos.get(0).getCommunityId());
        if (RentingPoolFlowDto.STATE_PROXY_TRUE.equals(rentingPoolFlowPo.getState())) {
            rentingPoolPo.setState(RentingPoolDto.STATE_PROXY_AUDIT);
        } else {
            rentingPoolPo.setState(RentingPoolDto.STATE_FINISH); // 审核失败
        }
        saveFlag = rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);

        if (saveFlag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "审核修改出租状态失败");
        }

        return ResultVo.success();
    }
}
