package com.java110.fee.bmo.feeFormula;

import com.java110.dto.fee.FeeFormulaDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeFormulaBMO {


    /**
     * 查询费用公式
     * add by wuxw
     *
     * @param feeFormulaDto
     * @return
     */
    ResponseEntity<String> get(FeeFormulaDto feeFormulaDto);


}
