package com.java110.user.bmo.rentingConfig.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IRentingConfigInnerServiceSMO;
import com.java110.po.rentingConfig.RentingConfigPo;
import com.java110.user.bmo.rentingConfig.IUpdateRentingConfigBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRentingConfigBMOImpl")
public class UpdateRentingConfigBMOImpl implements IUpdateRentingConfigBMO {

    @Autowired
    private IRentingConfigInnerServiceSMO rentingConfigInnerServiceSMOImpl;

    /**
     * @param rentingConfigPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(RentingConfigPo rentingConfigPo) {

        int flag = rentingConfigInnerServiceSMOImpl.updateRentingConfig(rentingConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
