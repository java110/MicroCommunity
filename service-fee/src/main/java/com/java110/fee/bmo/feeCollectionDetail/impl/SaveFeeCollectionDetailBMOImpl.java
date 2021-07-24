package com.java110.fee.bmo.feeCollectionDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.feeCollectionDetail.ISaveFeeCollectionDetailBMO;
import com.java110.intf.fee.IFeeCollectionDetailInnerServiceSMO;
import com.java110.po.feeCollectionDetail.FeeCollectionDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeCollectionDetailBMOImpl")
public class SaveFeeCollectionDetailBMOImpl implements ISaveFeeCollectionDetailBMO {

    @Autowired
    private IFeeCollectionDetailInnerServiceSMO feeCollectionDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeCollectionDetailPo feeCollectionDetailPo) {

        feeCollectionDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = feeCollectionDetailInnerServiceSMOImpl.saveFeeCollectionDetail(feeCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
