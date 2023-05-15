package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDiscountDto;
import com.java110.po.feeDiscount.FeeDiscountPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeDiscountInnerServiceSMO
 * @Description 费用折扣接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeDiscountApi")
public interface IFeeDiscountInnerServiceSMO {


    @RequestMapping(value = "/saveFeeDiscount", method = RequestMethod.POST)
    public int saveFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo);

    @RequestMapping(value = "/updateFeeDiscount", method = RequestMethod.POST)
    public int updateFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo);

    @RequestMapping(value = "/deleteFeeDiscount", method = RequestMethod.POST)
    public int deleteFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeDiscountDto 数据对象分享
     * @return FeeDiscountDto 对象数据
     */
    @RequestMapping(value = "/queryFeeDiscounts", method = RequestMethod.POST)
    List<FeeDiscountDto> queryFeeDiscounts(@RequestBody FeeDiscountDto feeDiscountDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeDiscountDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeDiscountsCount", method = RequestMethod.POST)
    int queryFeeDiscountsCount(@RequestBody FeeDiscountDto feeDiscountDto);

    /**
     * 计算折扣
     *
     * @param feeDetailDto
     * @return
     */
    @RequestMapping(value = "/computeDiscount", method = RequestMethod.POST)
    public List<ComputeDiscountDto> computeDiscount(@RequestBody FeeDetailDto feeDetailDto);
}
