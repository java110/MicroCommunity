package com.java110.fee.bmo.importFee.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.importFee.ISaveImportFeeBMO;
import com.java110.intf.fee.IImportFeeInnerServiceSMO;
import com.java110.po.importFee.ImportFeePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveImportFeeBMOImpl")
public class SaveImportFeeBMOImpl implements ISaveImportFeeBMO {

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param importFeePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ImportFeePo importFeePo) {

        importFeePo.setImportFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        int flag = importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
