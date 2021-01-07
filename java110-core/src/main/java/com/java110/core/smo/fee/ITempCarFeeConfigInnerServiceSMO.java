package com.java110.core.smo.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
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
}
