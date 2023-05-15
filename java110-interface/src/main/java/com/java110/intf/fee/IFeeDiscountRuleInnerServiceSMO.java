package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeDiscountRuleDto;
import com.java110.po.feeDiscountRule.FeeDiscountRulePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeDiscountRuleInnerServiceSMO
 * @Description 费用折扣规则接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeDiscountRuleApi")
public interface IFeeDiscountRuleInnerServiceSMO {


    @RequestMapping(value = "/saveFeeDiscountRule", method = RequestMethod.POST)
    public int saveFeeDiscountRule(@RequestBody FeeDiscountRulePo feeDiscountRulePo);

    @RequestMapping(value = "/updateFeeDiscountRule", method = RequestMethod.POST)
    public int updateFeeDiscountRule(@RequestBody FeeDiscountRulePo feeDiscountRulePo);

    @RequestMapping(value = "/deleteFeeDiscountRule", method = RequestMethod.POST)
    public int deleteFeeDiscountRule(@RequestBody FeeDiscountRulePo feeDiscountRulePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeDiscountRuleDto 数据对象分享
     * @return FeeDiscountRuleDto 对象数据
     */
    @RequestMapping(value = "/queryFeeDiscountRules", method = RequestMethod.POST)
    List<FeeDiscountRuleDto> queryFeeDiscountRules(@RequestBody FeeDiscountRuleDto feeDiscountRuleDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeDiscountRuleDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeDiscountRulesCount", method = RequestMethod.POST)
    int queryFeeDiscountRulesCount(@RequestBody FeeDiscountRuleDto feeDiscountRuleDto);
}
