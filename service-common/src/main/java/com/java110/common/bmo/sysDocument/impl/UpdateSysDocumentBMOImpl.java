package com.java110.common.bmo.sysDocument.impl;

import com.java110.common.bmo.sysDocument.IUpdateSysDocumentBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.ISysDocumentInnerServiceSMO;
import com.java110.po.sysDocument.SysDocumentPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateSysDocumentBMOImpl")
public class UpdateSysDocumentBMOImpl implements IUpdateSysDocumentBMO {

    @Autowired
    private ISysDocumentInnerServiceSMO sysDocumentInnerServiceSMOImpl;

    /**
     * @param sysDocumentPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(SysDocumentPo sysDocumentPo) {

        int flag = sysDocumentInnerServiceSMOImpl.updateSysDocument(sysDocumentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
