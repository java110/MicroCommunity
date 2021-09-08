package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPayFeeDetailDiscountInnerServiceSMO
 * @Description 缴费优惠接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/payFeeDetailDiscountApi")
public interface IPayFeeDetailDiscountInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param payFeeDetailDiscountDto 数据对象分享
     * @return PayFeeDetailDiscountDto 对象数据
     */
    @RequestMapping(value = "/queryPayFeeDetailDiscounts", method = RequestMethod.POST)
    List<PayFeeDetailDiscountDto> queryPayFeeDetailDiscounts(@RequestBody PayFeeDetailDiscountDto payFeeDetailDiscountDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param payFeeDetailDiscountDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPayFeeDetailDiscountsCount", method = RequestMethod.POST)
    int queryPayFeeDetailDiscountsCount(@RequestBody PayFeeDetailDiscountDto payFeeDetailDiscountDto);
}
