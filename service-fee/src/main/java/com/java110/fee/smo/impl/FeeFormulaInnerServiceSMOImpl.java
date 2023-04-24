package com.java110.fee.smo.impl;


import com.java110.fee.dao.IFeeFormulaServiceDao;
import com.java110.intf.fee.IFeeFormulaInnerServiceSMO;
import com.java110.dto.fee.FeeFormulaDto;
import com.java110.po.feeFormula.FeeFormulaPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用公式内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeFormulaInnerServiceSMOImpl extends BaseServiceSMO implements IFeeFormulaInnerServiceSMO {

    @Autowired
    private IFeeFormulaServiceDao feeFormulaServiceDaoImpl;


    @Override
    public int saveFeeFormula(@RequestBody FeeFormulaPo feeFormulaPo) {
        int saveFlag = 1;
        feeFormulaServiceDaoImpl.saveFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaPo));
        return saveFlag;
    }

     @Override
    public int updateFeeFormula(@RequestBody  FeeFormulaPo feeFormulaPo) {
        int saveFlag = 1;
         feeFormulaServiceDaoImpl.updateFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaPo));
        return saveFlag;
    }

     @Override
    public int deleteFeeFormula(@RequestBody  FeeFormulaPo feeFormulaPo) {
        int saveFlag = 1;
        feeFormulaPo.setStatusCd("1");
        feeFormulaServiceDaoImpl.updateFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaPo));
        return saveFlag;
    }

    @Override
    public List<FeeFormulaDto> queryFeeFormulas(@RequestBody  FeeFormulaDto feeFormulaDto) {

        //校验是否传了 分页信息

        int page = feeFormulaDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeFormulaDto.setPage((page - 1) * feeFormulaDto.getRow());
        }

        List<FeeFormulaDto> feeFormulas = BeanConvertUtil.covertBeanList(feeFormulaServiceDaoImpl.getFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaDto)), FeeFormulaDto.class);

        return feeFormulas;
    }


    @Override
    public int queryFeeFormulasCount(@RequestBody FeeFormulaDto feeFormulaDto) {
        return feeFormulaServiceDaoImpl.queryFeeFormulasCount(BeanConvertUtil.beanCovertMap(feeFormulaDto));    }

    public IFeeFormulaServiceDao getFeeFormulaServiceDaoImpl() {
        return feeFormulaServiceDaoImpl;
    }

    public void setFeeFormulaServiceDaoImpl(IFeeFormulaServiceDao feeFormulaServiceDaoImpl) {
        this.feeFormulaServiceDaoImpl = feeFormulaServiceDaoImpl;
    }
}
