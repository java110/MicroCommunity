package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeDiscount.ComputeDiscountDto;
import com.java110.dto.feeDiscount.FeeDiscountDto;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.fee.dao.IFeeDiscountServiceDao;
import com.java110.fee.discount.IComputeDiscount;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
public class FeeDiscountInnerServiceSMOImpl extends BaseServiceSMO implements IFeeDiscountInnerServiceSMO {

    @Autowired
    private IFeeDiscountServiceDao feeDiscountServiceDaoImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;


    @Override
    public int saveFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo) {
        int saveFlag = 1;
        feeDiscountServiceDaoImpl.saveFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountPo));
        return saveFlag;
    }

    @Override
    public int updateFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo) {
        int saveFlag = 1;
        feeDiscountServiceDaoImpl.updateFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo) {
        int saveFlag = 1;
        feeDiscountPo.setStatusCd("1");
        feeDiscountServiceDaoImpl.updateFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountPo));
        return saveFlag;
    }

    @Override
    public List<FeeDiscountDto> queryFeeDiscounts(@RequestBody FeeDiscountDto feeDiscountDto) {

        //校验是否传了 分页信息

        int page = feeDiscountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDiscountDto.setPage((page - 1) * feeDiscountDto.getRow());
        }

        List<FeeDiscountDto> feeDiscounts = BeanConvertUtil.covertBeanList(feeDiscountServiceDaoImpl.getFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountDto)), FeeDiscountDto.class);

        return feeDiscounts;
    }


    /**
     * 计算折扣
     *
     * @param feeDetailDto
     * @return
     */

    public List<ComputeDiscountDto> computeDiscount(@RequestBody FeeDetailDto feeDetailDto) {
        List<ComputeDiscountDto> computeDiscountDtos = new ArrayList<>();
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(feeDetailDto.getFeeId());
        feeDto.setCommunityId(feeDetailDto.getCommunityId());
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        Assert.listOnlyOne(feeDtos, "费用不存在");

        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setConfigId(feeDtos.get(0).getConfigId());
        payFeeConfigDiscountDto.setRow(feeDetailDto.getRow());
        payFeeConfigDiscountDto.setPage(feeDetailDto.getRow());
        payFeeConfigDiscountDto.setCommunityId(feeDetailDto.getCommunityId());
        List<PayFeeConfigDiscountDto> payFeeConfigDiscountDtos =
                payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscounts(payFeeConfigDiscountDto);

        if (payFeeConfigDiscountDtos == null || payFeeConfigDiscountDtos.size() < 1) {
            return computeDiscountDtos;
        }

        for (PayFeeConfigDiscountDto tmpPayFeeConfigDiscountDto : payFeeConfigDiscountDtos) {
            doCompute(tmpPayFeeConfigDiscountDto, Double.parseDouble(feeDetailDto.getCycles()), computeDiscountDtos);
        }
        return computeDiscountDtos;

    }

    private void doCompute(PayFeeConfigDiscountDto tmpPayFeeConfigDiscountDto, double cycles, List<ComputeDiscountDto> computeDiscountDtos) {

        FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
        feeDiscountDto.setCommunityId(tmpPayFeeConfigDiscountDto.getCommunityId());
        feeDiscountDto.setDiscountId(tmpPayFeeConfigDiscountDto.getDiscountId());
        List<FeeDiscountDto> feeDiscountDtos = queryFeeDiscounts(feeDiscountDto);
        if (feeDiscountDtos == null || feeDiscountDtos.size() < 1) {
            return;
        }
        IComputeDiscount computeDiscount = (IComputeDiscount) ApplicationContextFactory.getBean(feeDiscountDtos.get(0).getBeanImpl());
        ComputeDiscountDto computeDiscountDto = computeDiscount.compute(feeDiscountDtos.get(0));
        computeDiscountDtos.add(computeDiscountDto);
    }


    @Override
    public int queryFeeDiscountsCount(@RequestBody FeeDiscountDto feeDiscountDto) {
        return feeDiscountServiceDaoImpl.queryFeeDiscountsCount(BeanConvertUtil.beanCovertMap(feeDiscountDto));
    }

    public IFeeDiscountServiceDao getFeeDiscountServiceDaoImpl() {
        return feeDiscountServiceDaoImpl;
    }

    public void setFeeDiscountServiceDaoImpl(IFeeDiscountServiceDao feeDiscountServiceDaoImpl) {
        this.feeDiscountServiceDaoImpl = feeDiscountServiceDaoImpl;
    }
}
