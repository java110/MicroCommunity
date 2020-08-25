package com.java110.user.bmo.rentingPool.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.user.bmo.rentingPool.ISaveRentingPoolBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingPoolBMOImpl")
public class SaveRentingPoolBMOImpl implements ISaveRentingPoolBMO {

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingPoolPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RentingPoolPo rentingPoolPo) {

        rentingPoolPo.setRentingId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rentingId));
        int flag = rentingPoolInnerServiceSMOImpl.saveRentingPool(rentingPoolPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
