package com.java110.fee.bmo.feeFormula.impl;

import com.java110.dto.fee.FeeFormulaDto;
import com.java110.fee.bmo.feeFormula.IGetFeeFormulaBMO;
import com.java110.intf.fee.IFeeFormulaInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeFormulaBMOImpl")
public class GetFeeFormulaBMOImpl implements IGetFeeFormulaBMO {

    @Autowired
    private IFeeFormulaInnerServiceSMO feeFormulaInnerServiceSMOImpl;

    /**
     * @param feeFormulaDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeFormulaDto feeFormulaDto) {


        int count = feeFormulaInnerServiceSMOImpl.queryFeeFormulasCount(feeFormulaDto);

        List<FeeFormulaDto> feeFormulaDtos = null;
        if (count > 0) {
            feeFormulaDtos = feeFormulaInnerServiceSMOImpl.queryFeeFormulas(feeFormulaDto);
        } else {
            feeFormulaDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeFormulaDto.getRow()), count, feeFormulaDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
