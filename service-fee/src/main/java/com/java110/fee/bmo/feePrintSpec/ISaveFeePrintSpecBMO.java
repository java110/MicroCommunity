package com.java110.fee.bmo.feePrintSpec;

import com.java110.po.feePrintSpec.FeePrintSpecPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeePrintSpecBMO {


    /**
     * 添加打印说明
     * add by wuxw
     *
     * @param feePrintSpecPo
     * @return
     */
    ResponseEntity<String> save(FeePrintSpecPo feePrintSpecPo);


}
