package com.java110.fee.bmo.feeReceipt.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeReceipt.IUpdateFeeReceiptBMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeReceiptBMOImpl")
public class UpdateFeeReceiptBMOImpl implements IUpdateFeeReceiptBMO {

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    /**
     * @param feeReceiptPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeeReceiptPo feeReceiptPo) {

        int flag = feeReceiptInnerServiceSMOImpl.updateFeeReceipt(feeReceiptPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
