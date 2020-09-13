package com.java110.fee.bmo.prestoreFee;

import com.java110.po.prestoreFee.PrestoreFeePo;
import org.springframework.http.ResponseEntity;
public interface ISavePrestoreFeeBMO {


    /**
     * 添加预存费用
     * add by wuxw
     * @param prestoreFeePo
     * @return
     */
    ResponseEntity<String> save(PrestoreFeePo prestoreFeePo);


}
