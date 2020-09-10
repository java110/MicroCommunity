package com.java110.fee.bmo.feeFormula;
import com.java110.po.feeFormula.FeeFormulaPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeFormulaBMO {


    /**
     * 修改费用公式
     * add by wuxw
     * @param feeFormulaPo
     * @return
     */
    ResponseEntity<String> delete(FeeFormulaPo feeFormulaPo);


}
