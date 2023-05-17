package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import com.java110.fee.dao.IPayFeeDetailMonthServiceDao;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 月缴费表内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PayFeeDetailMonthInnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeDetailMonthInnerServiceSMO {

    @Autowired
    private IPayFeeDetailMonthServiceDao payFeeDetailMonthServiceDaoImpl;


    @Override
    public int savePayFeeDetailMonth(@RequestBody PayFeeDetailMonthPo payFeeDetailMonthPo) {
        int saveFlag = 1;
        payFeeDetailMonthServiceDaoImpl.savePayFeeDetailMonthInfo(BeanConvertUtil.beanCovertMap(payFeeDetailMonthPo));
        return saveFlag;
    }

    @Override
    public int savePayFeeDetailMonths(@RequestBody List<PayFeeDetailMonthPo> payFeeDetailMonthPos) {
        int saveFlag = 1;
        if (payFeeDetailMonthPos == null || payFeeDetailMonthPos.size() < 1) {
            return saveFlag;
        }

        Map info = new HashMap();
        info.put("payFeeDetailMonthPos", payFeeDetailMonthPos);
        payFeeDetailMonthServiceDaoImpl.savePayFeeDetailMonthInfos(info);
        return saveFlag;
    }

    @Override
    public int updatePayFeeDetailMonth(@RequestBody PayFeeDetailMonthPo payFeeDetailMonthPo) {
        int saveFlag = 1;
        payFeeDetailMonthServiceDaoImpl.updatePayFeeDetailMonthInfo(BeanConvertUtil.beanCovertMap(payFeeDetailMonthPo));
        return saveFlag;
    }

    @Override
    public int deletePayFeeDetailMonth(@RequestBody PayFeeDetailMonthPo payFeeDetailMonthPo) {
        int saveFlag = 1;
        payFeeDetailMonthServiceDaoImpl.deletePayFeeDetailMonthInfo(BeanConvertUtil.beanCovertMap(payFeeDetailMonthPo));
        return saveFlag;
    }

    @Override
    public List<PayFeeDetailMonthDto> queryPayFeeDetailMonths(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto) {

        //校验是否传了 分页信息

        int page = payFeeDetailMonthDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            payFeeDetailMonthDto.setPage((page - 1) * payFeeDetailMonthDto.getRow());
        }

        List<PayFeeDetailMonthDto> payFeeDetailMonths = BeanConvertUtil.covertBeanList(payFeeDetailMonthServiceDaoImpl.getPayFeeDetailMonthInfo(BeanConvertUtil.beanCovertMap(payFeeDetailMonthDto)), PayFeeDetailMonthDto.class);

        return payFeeDetailMonths;
    }


    @Override
    public int queryPayFeeDetailMonthsCount(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto) {
        return payFeeDetailMonthServiceDaoImpl.queryPayFeeDetailMonthsCount(BeanConvertUtil.beanCovertMap(payFeeDetailMonthDto));
    }

    @Override
    public List<PayFeeDetailMonthDto> queryPayFeeDetailMaxMonths(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto) {
        int page = payFeeDetailMonthDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            payFeeDetailMonthDto.setPage((page - 1) * payFeeDetailMonthDto.getRow());
        }

        List<PayFeeDetailMonthDto> payFeeDetailMonths = BeanConvertUtil.covertBeanList(payFeeDetailMonthServiceDaoImpl.queryPayFeeDetailMaxMonths(BeanConvertUtil.beanCovertMap(payFeeDetailMonthDto)), PayFeeDetailMonthDto.class);

        return payFeeDetailMonths;
    }

    /**
     * 查询需要离散的 缴费记录
     *
     * @param payFeeDetailMonthDto
     * @return
     */
    @Override
    public List<FeeDetailDto> getWaitDispersedFeeDetail(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto) {
        List<FeeDetailDto> payFeeDetailMonths = BeanConvertUtil.covertBeanList(payFeeDetailMonthServiceDaoImpl.getWaitDispersedFeeDetail(BeanConvertUtil.beanCovertMap(payFeeDetailMonthDto)), FeeDetailDto.class);

        return payFeeDetailMonths;
    }

    public IPayFeeDetailMonthServiceDao getPayFeeDetailMonthServiceDaoImpl() {
        return payFeeDetailMonthServiceDaoImpl;
    }

    public void setPayFeeDetailMonthServiceDaoImpl(IPayFeeDetailMonthServiceDao payFeeDetailMonthServiceDaoImpl) {
        this.payFeeDetailMonthServiceDaoImpl = payFeeDetailMonthServiceDaoImpl;
    }
}
