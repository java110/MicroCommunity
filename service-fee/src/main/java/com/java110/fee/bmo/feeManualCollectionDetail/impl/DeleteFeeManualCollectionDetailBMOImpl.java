package com.java110.fee.bmo.feeManualCollectionDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeManualCollectionDetail.IDeleteFeeManualCollectionDetailBMO;
import com.java110.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.java110.po.feeManualCollectionDetail.FeeManualCollectionDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeManualCollectionDetailBMOImpl")
public class DeleteFeeManualCollectionDetailBMOImpl implements IDeleteFeeManualCollectionDetailBMO {

    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(FeeManualCollectionDetailPo feeManualCollectionDetailPo) {

        int flag = feeManualCollectionDetailInnerServiceSMOImpl.deleteFeeManualCollectionDetail(feeManualCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
