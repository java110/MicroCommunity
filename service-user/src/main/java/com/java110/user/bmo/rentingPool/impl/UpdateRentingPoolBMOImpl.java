package com.java110.user.bmo.rentingPool.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.user.bmo.rentingPool.IUpdateRentingPoolBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRentingPoolBMOImpl")
public class UpdateRentingPoolBMOImpl implements IUpdateRentingPoolBMO {

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    /**
     * @param rentingPoolPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(RentingPoolPo rentingPoolPo) {

        int flag = rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
