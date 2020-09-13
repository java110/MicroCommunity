package com.java110.fee.bmo.feeFormula;

import com.java110.po.feeFormula.FeeFormulaPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeFormulaBMO {


    /**
     * 添加费用公式
     * add by wuxw
     *
     * @param feeFormulaPo
     * @return
     */
    ResponseEntity<String> save(FeeFormulaPo feeFormulaPo);


}
