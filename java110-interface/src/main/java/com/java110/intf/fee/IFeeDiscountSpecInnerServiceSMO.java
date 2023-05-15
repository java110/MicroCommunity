package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeDiscountSpecInnerServiceSMO
 * @Description 费用折扣接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeDiscountSpecApi")
public interface IFeeDiscountSpecInnerServiceSMO {


    @RequestMapping(value = "/saveFeeDiscountSpec", method = RequestMethod.POST)
    public int saveFeeDiscountSpec(@RequestBody FeeDiscountSpecPo feeDiscountSpecPo);

    @RequestMapping(value = "/updateFeeDiscountSpec", method = RequestMethod.POST)
    public int updateFeeDiscountSpec(@RequestBody  FeeDiscountSpecPo feeDiscountSpecPo);

    @RequestMapping(value = "/deleteFeeDiscountSpec", method = RequestMethod.POST)
    public int deleteFeeDiscountSpec(@RequestBody  FeeDiscountSpecPo feeDiscountSpecPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeDiscountSpecDto 数据对象分享
     * @return FeeDiscountSpecDto 对象数据
     */
    @RequestMapping(value = "/queryFeeDiscountSpecs", method = RequestMethod.POST)
    List<FeeDiscountSpecDto> queryFeeDiscountSpecs(@RequestBody FeeDiscountSpecDto feeDiscountSpecDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeDiscountSpecDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeDiscountSpecsCount", method = RequestMethod.POST)
    int queryFeeDiscountSpecsCount(@RequestBody FeeDiscountSpecDto feeDiscountSpecDto);
}
