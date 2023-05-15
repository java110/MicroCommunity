package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeDiscountRuleSpecDto;
import com.java110.po.feeDiscountRuleSpec.FeeDiscountRuleSpecPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeDiscountRuleSpecInnerServiceSMO
 * @Description 折扣规则配置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeDiscountRuleSpecApi")
public interface IFeeDiscountRuleSpecInnerServiceSMO {


    @RequestMapping(value = "/saveFeeDiscountRuleSpec", method = RequestMethod.POST)
    public int saveFeeDiscountRuleSpec(@RequestBody FeeDiscountRuleSpecPo feeDiscountRuleSpecPo);

    @RequestMapping(value = "/updateFeeDiscountRuleSpec", method = RequestMethod.POST)
    public int updateFeeDiscountRuleSpec(@RequestBody  FeeDiscountRuleSpecPo feeDiscountRuleSpecPo);

    @RequestMapping(value = "/deleteFeeDiscountRuleSpec", method = RequestMethod.POST)
    public int deleteFeeDiscountRuleSpec(@RequestBody  FeeDiscountRuleSpecPo feeDiscountRuleSpecPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeDiscountRuleSpecDto 数据对象分享
     * @return FeeDiscountRuleSpecDto 对象数据
     */
    @RequestMapping(value = "/queryFeeDiscountRuleSpecs", method = RequestMethod.POST)
    List<FeeDiscountRuleSpecDto> queryFeeDiscountRuleSpecs(@RequestBody FeeDiscountRuleSpecDto feeDiscountRuleSpecDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeDiscountRuleSpecDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeDiscountRuleSpecsCount", method = RequestMethod.POST)
    int queryFeeDiscountRuleSpecsCount(@RequestBody FeeDiscountRuleSpecDto feeDiscountRuleSpecDto);
}
