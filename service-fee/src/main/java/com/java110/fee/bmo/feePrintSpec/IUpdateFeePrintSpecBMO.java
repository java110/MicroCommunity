package com.java110.fee.bmo.feePrintSpec;

import com.java110.po.feePrintSpec.FeePrintSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeePrintSpecBMO {


    /**
     * 修改打印说明
     * add by wuxw
     *
     * @param feePrintSpecPo
     * @return
     */
    ResponseEntity<String> update(FeePrintSpecPo feePrintSpecPo);


}
