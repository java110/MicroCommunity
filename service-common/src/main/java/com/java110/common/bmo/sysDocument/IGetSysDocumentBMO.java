package com.java110.common.bmo.sysDocument;
import com.java110.dto.sysDocument.SysDocumentDto;
import org.springframework.http.ResponseEntity;
public interface IGetSysDocumentBMO {


    /**
     * 查询系统文档
     * add by wuxw
     * @param  sysDocumentDto
     * @return
     */
    ResponseEntity<String> get(SysDocumentDto sysDocumentDto);


}
