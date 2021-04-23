package com.java110.user.bmo.rentingConfig.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IRentingConfigInnerServiceSMO;
import com.java110.po.rentingConfig.RentingConfigPo;
import com.java110.user.bmo.rentingConfig.ISaveRentingConfigBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingConfigBMOImpl")
public class SaveRentingConfigBMOImpl implements ISaveRentingConfigBMO {

    @Autowired
    private IRentingConfigInnerServiceSMO rentingConfigInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingConfigPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RentingConfigPo rentingConfigPo) {

        rentingConfigPo.setRentingConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rentingConfigId));
        int flag = rentingConfigInnerServiceSMOImpl.saveRentingConfig(rentingConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
