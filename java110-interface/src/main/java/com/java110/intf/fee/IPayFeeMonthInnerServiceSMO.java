package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailRefreshFeeMonthDto;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPayFeeDetailMonthInnerServiceSMO
 * @Description 月缴费表接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/payFeeMonthApi")
public interface IPayFeeMonthInnerServiceSMO {


    /**
     * 缴费处理离散表
     * @param payFeeDetailRefreshFeeMonthDto
     * @return
     */
    @RequestMapping(value = "/payFeeDetailRefreshFeeMonth", method = RequestMethod.POST)
    int payFeeDetailRefreshFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto);


    @RequestMapping(value = "/doGeneratorOrRefreshAllFeeMonth", method = RequestMethod.POST)
    int doGeneratorOrRefreshAllFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto);

}
