package com.java110.fee.bmo.importFeeDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.importFeeDetail.ISaveImportFeeDetailBMO;
import com.java110.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveImportFeeDetailBMOImpl")
public class SaveImportFeeDetailBMOImpl implements ISaveImportFeeDetailBMO {

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param importFeeDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ImportFeeDetailPo importFeeDetailPo) {

        importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        int flag = importFeeDetailInnerServiceSMOImpl.saveImportFeeDetail(importFeeDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
