package com.java110.fee.bmo.feeReceiptDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeReceiptDetail.IDeleteFeeReceiptDetailBMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeReceiptDetailBMOImpl")
public class DeleteFeeReceiptDetailBMOImpl implements IDeleteFeeReceiptDetailBMO {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    /**
     * @param feeReceiptDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(FeeReceiptDetailPo feeReceiptDetailPo) {

        int flag = feeReceiptDetailInnerServiceSMOImpl.deleteFeeReceiptDetail(feeReceiptDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
