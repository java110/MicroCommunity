package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDiscountRuleSpecDto;
import com.java110.fee.dao.IFeeDiscountRuleSpecServiceDao;
import com.java110.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.java110.po.feeDiscountRuleSpec.FeeDiscountRuleSpecPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 折扣规则配置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeDiscountRuleSpecInnerServiceSMOImpl extends BaseServiceSMO implements IFeeDiscountRuleSpecInnerServiceSMO {

    @Autowired
    private IFeeDiscountRuleSpecServiceDao feeDiscountRuleSpecServiceDaoImpl;


    @Override
    public int saveFeeDiscountRuleSpec(@RequestBody FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {
        int saveFlag = 1;
        feeDiscountRuleSpecServiceDaoImpl.saveFeeDiscountRuleSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountRuleSpecPo));
        return saveFlag;
    }

    @Override
    public int updateFeeDiscountRuleSpec(@RequestBody FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {
        int saveFlag = 1;
        feeDiscountRuleSpecServiceDaoImpl.updateFeeDiscountRuleSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountRuleSpecPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeDiscountRuleSpec(@RequestBody FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {
        int saveFlag = 1;
        feeDiscountRuleSpecPo.setStatusCd("1");
        feeDiscountRuleSpecServiceDaoImpl.updateFeeDiscountRuleSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountRuleSpecPo));
        return saveFlag;
    }

    @Override
    public List<FeeDiscountRuleSpecDto> queryFeeDiscountRuleSpecs(@RequestBody FeeDiscountRuleSpecDto feeDiscountRuleSpecDto) {

        //校验是否传了 分页信息

        int page = feeDiscountRuleSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDiscountRuleSpecDto.setPage((page - 1) * feeDiscountRuleSpecDto.getRow());
        }

        List<FeeDiscountRuleSpecDto> feeDiscountRuleSpecs = BeanConvertUtil.covertBeanList(feeDiscountRuleSpecServiceDaoImpl.getFeeDiscountRuleSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountRuleSpecDto)), FeeDiscountRuleSpecDto.class);

        return feeDiscountRuleSpecs;
    }


    @Override
    public int queryFeeDiscountRuleSpecsCount(@RequestBody FeeDiscountRuleSpecDto feeDiscountRuleSpecDto) {
        return feeDiscountRuleSpecServiceDaoImpl.queryFeeDiscountRuleSpecsCount(BeanConvertUtil.beanCovertMap(feeDiscountRuleSpecDto));
    }

    public IFeeDiscountRuleSpecServiceDao getFeeDiscountRuleSpecServiceDaoImpl() {
        return feeDiscountRuleSpecServiceDaoImpl;
    }

    public void setFeeDiscountRuleSpecServiceDaoImpl(IFeeDiscountRuleSpecServiceDao feeDiscountRuleSpecServiceDaoImpl) {
        this.feeDiscountRuleSpecServiceDaoImpl = feeDiscountRuleSpecServiceDaoImpl;
    }
}
