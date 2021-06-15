package com.java110.user.bmo.userAddress.impl;

import com.java110.dto.userAddress.UserAddressDto;
import com.java110.intf.user.IUserAddressInnerServiceSMO;
import com.java110.user.bmo.userAddress.IGetUserAddressBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getUserAddressBMOImpl")
public class GetUserAddressBMOImpl implements IGetUserAddressBMO {

    @Autowired
    private IUserAddressInnerServiceSMO userAddressInnerServiceSMOImpl;

    /**
     * @param userAddressDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(UserAddressDto userAddressDto) {


        int count = userAddressInnerServiceSMOImpl.queryUserAddresssCount(userAddressDto);

        List<UserAddressDto> userAddressDtos = null;
        if (count > 0) {
            userAddressDtos = userAddressInnerServiceSMOImpl.queryUserAddresss(userAddressDto);
        } else {
            userAddressDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) userAddressDto.getRow()), count, userAddressDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
