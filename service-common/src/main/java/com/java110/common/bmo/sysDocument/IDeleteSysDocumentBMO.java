package com.java110.common.bmo.sysDocument;
import com.java110.po.sysDocument.SysDocumentPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteSysDocumentBMO {


    /**
     * 修改系统文档
     * add by wuxw
     * @param sysDocumentPo
     * @return
     */
    ResponseEntity<String> delete(SysDocumentPo sysDocumentPo);


}
