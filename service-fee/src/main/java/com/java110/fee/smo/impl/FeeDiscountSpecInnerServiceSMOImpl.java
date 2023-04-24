package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.fee.dao.IFeeDiscountSpecServiceDao;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用折扣内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeDiscountSpecInnerServiceSMOImpl extends BaseServiceSMO implements IFeeDiscountSpecInnerServiceSMO {

    @Autowired
    private IFeeDiscountSpecServiceDao feeDiscountSpecServiceDaoImpl;


    @Override
    public int saveFeeDiscountSpec(@RequestBody FeeDiscountSpecPo feeDiscountSpecPo) {
        int saveFlag = 1;
        feeDiscountSpecServiceDaoImpl.saveFeeDiscountSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountSpecPo));
        return saveFlag;
    }

    @Override
    public int updateFeeDiscountSpec(@RequestBody FeeDiscountSpecPo feeDiscountSpecPo) {
        int saveFlag = 1;
        feeDiscountSpecServiceDaoImpl.updateFeeDiscountSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountSpecPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeDiscountSpec(@RequestBody FeeDiscountSpecPo feeDiscountSpecPo) {
        int saveFlag = 1;
        feeDiscountSpecPo.setStatusCd("1");
        feeDiscountSpecServiceDaoImpl.updateFeeDiscountSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountSpecPo));
        return saveFlag;
    }

    @Override
    public List<FeeDiscountSpecDto> queryFeeDiscountSpecs(@RequestBody FeeDiscountSpecDto feeDiscountSpecDto) {

        //校验是否传了 分页信息

        int page = feeDiscountSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDiscountSpecDto.setPage((page - 1) * feeDiscountSpecDto.getRow());
        }

        List<FeeDiscountSpecDto> feeDiscountSpecs = BeanConvertUtil.covertBeanList(feeDiscountSpecServiceDaoImpl.getFeeDiscountSpecInfo(BeanConvertUtil.beanCovertMap(feeDiscountSpecDto)), FeeDiscountSpecDto.class);

        return feeDiscountSpecs;
    }


    @Override
    public int queryFeeDiscountSpecsCount(@RequestBody FeeDiscountSpecDto feeDiscountSpecDto) {
        return feeDiscountSpecServiceDaoImpl.queryFeeDiscountSpecsCount(BeanConvertUtil.beanCovertMap(feeDiscountSpecDto));
    }

    public IFeeDiscountSpecServiceDao getFeeDiscountSpecServiceDaoImpl() {
        return feeDiscountSpecServiceDaoImpl;
    }

    public void setFeeDiscountSpecServiceDaoImpl(IFeeDiscountSpecServiceDao feeDiscountSpecServiceDaoImpl) {
        this.feeDiscountSpecServiceDaoImpl = feeDiscountSpecServiceDaoImpl;
    }
}
