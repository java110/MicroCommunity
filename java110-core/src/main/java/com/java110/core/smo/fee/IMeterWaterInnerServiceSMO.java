package com.java110.core.smo.fee;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.meterWater.MeterWaterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMeterWaterInnerServiceSMO
 * @Description 水电费接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/meterWaterApi")
public interface IMeterWaterInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param meterWaterDto 数据对象分享
     * @return MeterWaterDto 对象数据
     */
    @RequestMapping(value = "/queryMeterWaters", method = RequestMethod.POST)
    List<MeterWaterDto> queryMeterWaters(@RequestBody MeterWaterDto meterWaterDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param meterWaterDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMeterWatersCount", method = RequestMethod.POST)
    int queryMeterWatersCount(@RequestBody MeterWaterDto meterWaterDto);
}
