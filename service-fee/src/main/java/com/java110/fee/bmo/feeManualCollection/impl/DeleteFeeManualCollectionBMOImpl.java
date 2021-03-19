package com.java110.fee.bmo.feeManualCollection.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeManualCollection.IDeleteFeeManualCollectionBMO;
import com.java110.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.java110.po.feeManualCollection.FeeManualCollectionPo;
import com.java110.po.feeManualCollectionDetail.FeeManualCollectionDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeManualCollectionBMOImpl")
public class DeleteFeeManualCollectionBMOImpl implements IDeleteFeeManualCollectionBMO {

    @Autowired
    private IFeeManualCollectionInnerServiceSMO feeManualCollectionInnerServiceSMOImpl;
    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(FeeManualCollectionPo feeManualCollectionPo) {

        FeeManualCollectionDetailPo feeManualCollectionDetailPo = new FeeManualCollectionDetailPo();
        feeManualCollectionDetailPo.setCollectionId(feeManualCollectionPo.getCollectionId());

        feeManualCollectionDetailInnerServiceSMOImpl.deleteFeeManualCollectionDetail(feeManualCollectionDetailPo);

        int flag = feeManualCollectionInnerServiceSMOImpl.deleteFeeManualCollection(feeManualCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
