package com.java110.common.bmo.hcGovTranslate;
import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateHcGovTranslateBMO {


    /**
     * 修改社区政务同步
     * add by wuxw
     * @param hcGovTranslatePo
     * @return
     */
    ResponseEntity<String> update(HcGovTranslatePo hcGovTranslatePo);


}
