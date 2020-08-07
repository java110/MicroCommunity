package com.java110.fee.bmo.importFee.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.importFee.IUpdateImportFeeBMO;
import com.java110.intf.fee.IImportFeeInnerServiceSMO;
import com.java110.po.importFee.ImportFeePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateImportFeeBMOImpl")
public class UpdateImportFeeBMOImpl implements IUpdateImportFeeBMO {

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    /**
     * @param importFeePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ImportFeePo importFeePo) {

        int flag = importFeeInnerServiceSMOImpl.updateImportFee(importFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
