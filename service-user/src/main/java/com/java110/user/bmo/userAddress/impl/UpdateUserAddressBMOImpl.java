package com.java110.user.bmo.userAddress.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IUserAddressInnerServiceSMO;
import com.java110.po.userAddress.UserAddressPo;
import com.java110.user.bmo.userAddress.IUpdateUserAddressBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateUserAddressBMOImpl")
public class UpdateUserAddressBMOImpl implements IUpdateUserAddressBMO {

    @Autowired
    private IUserAddressInnerServiceSMO userAddressInnerServiceSMOImpl;

    /**
     * @param userAddressPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(UserAddressPo userAddressPo) {

        int flag = userAddressInnerServiceSMOImpl.updateUserAddress(userAddressPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
