package com.java110.fee.bmo.importFeeDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.importFeeDetail.IUpdateImportFeeDetailBMO;
import com.java110.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateImportFeeDetailBMOImpl")
public class UpdateImportFeeDetailBMOImpl implements IUpdateImportFeeDetailBMO {

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    /**
     * @param importFeeDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ImportFeeDetailPo importFeeDetailPo) {

        int flag = importFeeDetailInnerServiceSMOImpl.updateImportFeeDetail(importFeeDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
