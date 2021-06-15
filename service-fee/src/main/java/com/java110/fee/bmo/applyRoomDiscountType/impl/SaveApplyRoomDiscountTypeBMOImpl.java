package com.java110.fee.bmo.applyRoomDiscountType.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.applyRoomDiscountType.ISaveApplyRoomDiscountTypeBMO;
import com.java110.intf.fee.IApplyRoomDiscountTypeInnerServiceSMO;
import com.java110.po.applyRoomDiscountType.ApplyRoomDiscountTypePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveApplyRoomDiscountTypeBMOImpl")
public class SaveApplyRoomDiscountTypeBMOImpl implements ISaveApplyRoomDiscountTypeBMO {

    @Autowired
    private IApplyRoomDiscountTypeInnerServiceSMO applyRoomDiscountTypeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param applyRoomDiscountTypePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ApplyRoomDiscountTypePo applyRoomDiscountTypePo) {

        applyRoomDiscountTypePo.setApplyType(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyType));
        int flag = applyRoomDiscountTypeInnerServiceSMOImpl.saveApplyRoomDiscountType(applyRoomDiscountTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
