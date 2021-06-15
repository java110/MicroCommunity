package com.java110.fee.bmo.applyRoomDiscount.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.applyRoomDiscount.IUpdateApplyRoomDiscountBMO;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateApplyRoomDiscountBMOImpl")
public class UpdateApplyRoomDiscountBMOImpl implements IUpdateApplyRoomDiscountBMO {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ApplyRoomDiscountPo applyRoomDiscountPo) {

        int flag = applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
