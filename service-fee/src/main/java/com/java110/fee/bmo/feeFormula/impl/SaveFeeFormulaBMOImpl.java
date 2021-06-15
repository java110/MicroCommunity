package com.java110.fee.bmo.feeFormula.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.feeFormula.ISaveFeeFormulaBMO;
import com.java110.intf.fee.IFeeFormulaInnerServiceSMO;
import com.java110.po.feeFormula.FeeFormulaPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeFormulaBMOImpl")
public class SaveFeeFormulaBMOImpl implements ISaveFeeFormulaBMO {

    @Autowired
    private IFeeFormulaInnerServiceSMO feeFormulaInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeFormulaPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeFormulaPo feeFormulaPo) {

        feeFormulaPo.setFormulaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_formulaId));
        int flag = feeFormulaInnerServiceSMOImpl.saveFeeFormula(feeFormulaPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
