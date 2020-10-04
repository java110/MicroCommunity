package com.java110.common.bmo.sysDocument;
import com.java110.po.sysDocument.SysDocumentPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateSysDocumentBMO {


    /**
     * 修改系统文档
     * add by wuxw
     * @param sysDocumentPo
     * @return
     */
    ResponseEntity<String> update(SysDocumentPo sysDocumentPo);


}
