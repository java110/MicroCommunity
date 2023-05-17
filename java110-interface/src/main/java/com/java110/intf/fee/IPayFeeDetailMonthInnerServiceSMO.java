package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
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
@RequestMapping("/payFeeDetailMonthApi")
public interface IPayFeeDetailMonthInnerServiceSMO {


    @RequestMapping(value = "/savePayFeeDetailMonth", method = RequestMethod.POST)
    public int savePayFeeDetailMonth(@RequestBody PayFeeDetailMonthPo payFeeDetailMonthPo);

    @RequestMapping(value = "/savePayFeeDetailMonths", method = RequestMethod.POST)
    public int savePayFeeDetailMonths(@RequestBody List<PayFeeDetailMonthPo> payFeeDetailMonthPos);


    @RequestMapping(value = "/updatePayFeeDetailMonth", method = RequestMethod.POST)
    public int updatePayFeeDetailMonth(@RequestBody  PayFeeDetailMonthPo payFeeDetailMonthPo);

    @RequestMapping(value = "/deletePayFeeDetailMonth", method = RequestMethod.POST)
    public int deletePayFeeDetailMonth(@RequestBody  PayFeeDetailMonthPo payFeeDetailMonthPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param payFeeDetailMonthDto 数据对象分享
     * @return PayFeeDetailMonthDto 对象数据
     */
    @RequestMapping(value = "/queryPayFeeDetailMonths", method = RequestMethod.POST)
    List<PayFeeDetailMonthDto> queryPayFeeDetailMonths(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param payFeeDetailMonthDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPayFeeDetailMonthsCount", method = RequestMethod.POST)
    int queryPayFeeDetailMonthsCount(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto);

    @RequestMapping(value = "/queryPayFeeDetailMaxMonths", method = RequestMethod.POST)
    List<PayFeeDetailMonthDto> queryPayFeeDetailMaxMonths(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto);

    /**
     * 处理需要离散的缴费记录
     * @param payFeeDetailMonthDto
     * @return
     */
    @RequestMapping(value = "/getWaitDispersedFeeDetail", method = RequestMethod.POST)
    List<FeeDetailDto> getWaitDispersedFeeDetail(@RequestBody PayFeeDetailMonthDto payFeeDetailMonthDto);
}
