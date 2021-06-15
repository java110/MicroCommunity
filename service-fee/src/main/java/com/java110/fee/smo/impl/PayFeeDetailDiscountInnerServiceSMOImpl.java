package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.dao.IPayFeeDetailDiscountServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 缴费优惠内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PayFeeDetailDiscountInnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeDetailDiscountInnerServiceSMO {

    @Autowired
    private IPayFeeDetailDiscountServiceDao payFeeDetailDiscountServiceDaoImpl;


    @Override
    public List<PayFeeDetailDiscountDto> queryPayFeeDetailDiscounts(@RequestBody PayFeeDetailDiscountDto payFeeDetailDiscountDto) {

        //校验是否传了 分页信息

        int page = payFeeDetailDiscountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            payFeeDetailDiscountDto.setPage((page - 1) * payFeeDetailDiscountDto.getRow());
        }

        List<PayFeeDetailDiscountDto> payFeeDetailDiscounts = BeanConvertUtil.covertBeanList(payFeeDetailDiscountServiceDaoImpl.getPayFeeDetailDiscountInfo(BeanConvertUtil.beanCovertMap(payFeeDetailDiscountDto)), PayFeeDetailDiscountDto.class);


        return payFeeDetailDiscounts;
    }



    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param payFeeDetailDiscount 小区缴费优惠信息
     * @param users                用户列表
     */
    private void refreshPayFeeDetailDiscount(PayFeeDetailDiscountDto payFeeDetailDiscount, List<UserDto> users) {
        for (UserDto user : users) {
            if (payFeeDetailDiscount.getDetailDiscountId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, payFeeDetailDiscount);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param payFeeDetailDiscounts 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<PayFeeDetailDiscountDto> payFeeDetailDiscounts) {
        List<String> userIds = new ArrayList<String>();
        for (PayFeeDetailDiscountDto payFeeDetailDiscount : payFeeDetailDiscounts) {
            userIds.add(payFeeDetailDiscount.getDetailDiscountId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryPayFeeDetailDiscountsCount(@RequestBody PayFeeDetailDiscountDto payFeeDetailDiscountDto) {
        return payFeeDetailDiscountServiceDaoImpl.queryPayFeeDetailDiscountsCount(BeanConvertUtil.beanCovertMap(payFeeDetailDiscountDto));
    }

    public IPayFeeDetailDiscountServiceDao getPayFeeDetailDiscountServiceDaoImpl() {
        return payFeeDetailDiscountServiceDaoImpl;
    }

    public void setPayFeeDetailDiscountServiceDaoImpl(IPayFeeDetailDiscountServiceDao payFeeDetailDiscountServiceDaoImpl) {
        this.payFeeDetailDiscountServiceDaoImpl = payFeeDetailDiscountServiceDaoImpl;
    }


}
