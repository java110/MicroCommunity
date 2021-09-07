package com.java110.common.bmo.hcGovTranslate;

import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import org.springframework.http.ResponseEntity;
public interface ISaveHcGovTranslateBMO {


    /**
     * 添加社区政务同步
     * add by wuxw
     * @param hcGovTranslatePo
     * @return
     */
    ResponseEntity<String> save(HcGovTranslatePo hcGovTranslatePo);


}
