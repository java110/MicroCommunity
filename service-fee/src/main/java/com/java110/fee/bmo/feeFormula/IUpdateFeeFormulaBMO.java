package com.java110.fee.bmo.feeFormula;

import com.java110.po.feeFormula.FeeFormulaPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeFormulaBMO {


    /**
     * 修改费用公式
     * add by wuxw
     *
     * @param feeFormulaPo
     * @return
     */
    ResponseEntity<String> update(FeeFormulaPo feeFormulaPo);


}
