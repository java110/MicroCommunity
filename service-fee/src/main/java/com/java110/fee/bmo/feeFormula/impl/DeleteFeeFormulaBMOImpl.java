package com.java110.fee.bmo.feeFormula.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.feeFormula.IDeleteFeeFormulaBMO;
import com.java110.intf.fee.IFeeFormulaInnerServiceSMO;
import com.java110.po.feeFormula.FeeFormulaPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeFormulaBMOImpl")
public class DeleteFeeFormulaBMOImpl implements IDeleteFeeFormulaBMO {

    @Autowired
    private IFeeFormulaInnerServiceSMO feeFormulaInnerServiceSMOImpl;

    /**
     * @param feeFormulaPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(FeeFormulaPo feeFormulaPo) {

        int flag = feeFormulaInnerServiceSMOImpl.deleteFeeFormula(feeFormulaPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
