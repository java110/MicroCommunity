package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.po.payFeeConfigDiscount.PayFeeConfigDiscountPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPayFeeConfigDiscountInnerServiceSMO
 * @Description 费用项折扣接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/payFeeConfigDiscountApi")
public interface IPayFeeConfigDiscountInnerServiceSMO {


    @RequestMapping(value = "/savePayFeeConfigDiscount", method = RequestMethod.POST)
    public int savePayFeeConfigDiscount(@RequestBody PayFeeConfigDiscountPo payFeeConfigDiscountPo);

    @RequestMapping(value = "/updatePayFeeConfigDiscount", method = RequestMethod.POST)
    public int updatePayFeeConfigDiscount(@RequestBody PayFeeConfigDiscountPo payFeeConfigDiscountPo);

    @RequestMapping(value = "/deletePayFeeConfigDiscount", method = RequestMethod.POST)
    public int deletePayFeeConfigDiscount(@RequestBody PayFeeConfigDiscountPo payFeeConfigDiscountPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param payFeeConfigDiscountDto 数据对象分享
     * @return PayFeeConfigDiscountDto 对象数据
     */
    @RequestMapping(value = "/queryPayFeeConfigDiscounts", method = RequestMethod.POST)
    List<PayFeeConfigDiscountDto> queryPayFeeConfigDiscounts(@RequestBody PayFeeConfigDiscountDto payFeeConfigDiscountDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param payFeeConfigDiscountDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPayFeeConfigDiscountsCount", method = RequestMethod.POST)
    int queryPayFeeConfigDiscountsCount(@RequestBody PayFeeConfigDiscountDto payFeeConfigDiscountDto);
}
