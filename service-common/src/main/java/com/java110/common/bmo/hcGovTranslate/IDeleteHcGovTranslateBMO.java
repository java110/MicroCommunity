package com.java110.common.bmo.hcGovTranslate;
import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteHcGovTranslateBMO {


    /**
     * 修改社区政务同步
     * add by wuxw
     * @param hcGovTranslatePo
     * @return
     */
    ResponseEntity<String> delete(HcGovTranslatePo hcGovTranslatePo);


}
