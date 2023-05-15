package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.ApplicationKeyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IApplicationKeyInnerServiceSMO
 * @Description 钥匙申请接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/applicationKeyApi")
public interface IApplicationKeyInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param applicationKeyDto 数据对象分享
     * @return ApplicationKeyDto 对象数据
     */
    @RequestMapping(value = "/queryApplicationKeys", method = RequestMethod.POST)
    List<ApplicationKeyDto> queryApplicationKeys(@RequestBody ApplicationKeyDto applicationKeyDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param applicationKeyDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryApplicationKeysCount", method = RequestMethod.POST)
    int queryApplicationKeysCount(@RequestBody ApplicationKeyDto applicationKeyDto);
}
