package com.java110.fee.bmo.feeCollectionDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeCollectionDetail.IUpdateFeeCollectionDetailBMO;
import com.java110.intf.fee.IFeeCollectionDetailInnerServiceSMO;
import com.java110.po.feeCollectionDetail.FeeCollectionDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeCollectionDetailBMOImpl")
public class UpdateFeeCollectionDetailBMOImpl implements IUpdateFeeCollectionDetailBMO {

    @Autowired
    private IFeeCollectionDetailInnerServiceSMO feeCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeeCollectionDetailPo feeCollectionDetailPo) {

        int flag = feeCollectionDetailInnerServiceSMOImpl.updateFeeCollectionDetail(feeCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
