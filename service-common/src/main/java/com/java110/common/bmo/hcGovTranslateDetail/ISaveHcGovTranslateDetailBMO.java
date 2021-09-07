package com.java110.common.bmo.hcGovTranslateDetail;

import com.java110.po.hcGovTranslateDetail.HcGovTranslateDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveHcGovTranslateDetailBMO {


    /**
     * 添加信息分类
     * add by wuxw
     * @param hcGovTranslateDetailPo
     * @return
     */
    ResponseEntity<String> save(HcGovTranslateDetailPo hcGovTranslateDetailPo);


}
