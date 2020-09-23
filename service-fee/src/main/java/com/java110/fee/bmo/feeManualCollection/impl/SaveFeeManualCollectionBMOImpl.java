package com.java110.fee.bmo.feeManualCollection.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.feeManualCollection.ISaveFeeManualCollectionBMO;
import com.java110.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.java110.po.feeManualCollection.FeeManualCollectionPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeManualCollectionBMOImpl")
public class SaveFeeManualCollectionBMOImpl implements ISaveFeeManualCollectionBMO {

    @Autowired
    private IFeeManualCollectionInnerServiceSMO feeManualCollectionInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeManualCollectionPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeManualCollectionPo feeManualCollectionPo) {

        feeManualCollectionPo.setCollectionId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
        int flag = feeManualCollectionInnerServiceSMOImpl.saveFeeManualCollection(feeManualCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
