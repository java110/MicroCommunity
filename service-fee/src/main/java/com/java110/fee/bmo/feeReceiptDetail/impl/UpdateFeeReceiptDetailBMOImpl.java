package com.java110.fee.bmo.feeReceiptDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeReceiptDetail.IUpdateFeeReceiptDetailBMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeReceiptDetailBMOImpl")
public class UpdateFeeReceiptDetailBMOImpl implements IUpdateFeeReceiptDetailBMO {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    /**
     * @param feeReceiptDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeeReceiptDetailPo feeReceiptDetailPo) {

        int flag = feeReceiptDetailInnerServiceSMOImpl.updateFeeReceiptDetail(feeReceiptDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
