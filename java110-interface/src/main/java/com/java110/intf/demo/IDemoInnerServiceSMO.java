package com.java110.intf.demo;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.demo.DemoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IDemoInnerServiceSMO
 * @Description demo接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/demoApi")
public interface IDemoInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param demoDto 数据对象分享
     * @return DemoDto 对象数据
     */
    @RequestMapping(value = "/queryDemos", method = RequestMethod.POST)
    List<DemoDto> queryDemos(@RequestBody DemoDto demoDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param demoDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryDemosCount", method = RequestMethod.POST)
    int queryDemosCount(@RequestBody DemoDto demoDto);
}
