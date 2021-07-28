package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.smsConfig.SmsConfigDto;
import com.java110.po.smsConfig.SmsConfigPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ISmsConfigInnerServiceSMO
 * @Description 短信配置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/smsConfigApi")
public interface ISmsConfigInnerServiceSMO {


    @RequestMapping(value = "/saveSmsConfig", method = RequestMethod.POST)
    public int saveSmsConfig(@RequestBody SmsConfigPo smsConfigPo);

    @RequestMapping(value = "/updateSmsConfig", method = RequestMethod.POST)
    public int updateSmsConfig(@RequestBody  SmsConfigPo smsConfigPo);

    @RequestMapping(value = "/deleteSmsConfig", method = RequestMethod.POST)
    public int deleteSmsConfig(@RequestBody  SmsConfigPo smsConfigPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param smsConfigDto 数据对象分享
     * @return SmsConfigDto 对象数据
     */
    @RequestMapping(value = "/querySmsConfigs", method = RequestMethod.POST)
    List<SmsConfigDto> querySmsConfigs(@RequestBody SmsConfigDto smsConfigDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param smsConfigDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/querySmsConfigsCount", method = RequestMethod.POST)
    int querySmsConfigsCount(@RequestBody SmsConfigDto smsConfigDto);
}
