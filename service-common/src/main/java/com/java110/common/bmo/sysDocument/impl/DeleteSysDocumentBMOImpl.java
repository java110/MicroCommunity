package com.java110.common.bmo.sysDocument.impl;

import com.java110.common.bmo.sysDocument.IDeleteSysDocumentBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.ISysDocumentInnerServiceSMO;
import com.java110.po.sysDocument.SysDocumentPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteSysDocumentBMOImpl")
public class DeleteSysDocumentBMOImpl implements IDeleteSysDocumentBMO {

    @Autowired
    private ISysDocumentInnerServiceSMO sysDocumentInnerServiceSMOImpl;

    /**
     * @param sysDocumentPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(SysDocumentPo sysDocumentPo) {

        int flag = sysDocumentInnerServiceSMOImpl.deleteSysDocument(sysDocumentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
