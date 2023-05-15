package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITempCarFeeConfigInnerServiceSMO
 * @Description 临时车收费标准接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/tempCarFeeConfigApi")
public interface ITempCarFeeConfigInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param tempCarFeeConfigDto 数据对象分享
     * @return TempCarFeeConfigDto 对象数据
     */
    @RequestMapping(value = "/queryTempCarFeeConfigs", method = RequestMethod.POST)
    List<TempCarFeeConfigDto> queryTempCarFeeConfigs(@RequestBody TempCarFeeConfigDto tempCarFeeConfigDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param tempCarFeeConfigDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryTempCarFeeConfigsCount", method = RequestMethod.POST)
    int queryTempCarFeeConfigsCount(@RequestBody TempCarFeeConfigDto tempCarFeeConfigDto);

    /**
     * 查询临时车收费规则
     *
     * @param tempCarFeeRuleDto
     * @return
     */
    @RequestMapping(value = "/queryTempCarFeeRules", method = RequestMethod.POST)
    List<TempCarFeeRuleDto> queryTempCarFeeRules(@RequestBody TempCarFeeRuleDto tempCarFeeRuleDto);

    /**
     * 计算临时车费用
     *
     * @param carInoutDtos
     * @return
     */
    @RequestMapping(value = "/computeTempCarFee", method = RequestMethod.POST)
    List<CarInoutDto> computeTempCarFee(@RequestBody List<CarInoutDto> carInoutDtos);

    @RequestMapping(value = "/computeTempCarInoutDetailFee", method = RequestMethod.POST)
    List<CarInoutDetailDto> computeTempCarInoutDetailFee(@RequestBody List<CarInoutDetailDto> carInoutDtos);
}
