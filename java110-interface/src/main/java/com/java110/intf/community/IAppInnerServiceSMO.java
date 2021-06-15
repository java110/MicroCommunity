package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.app.AppDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAppInnerServiceSMO
 * @Description 应用接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/appApi")
public interface IAppInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param appDto 数据对象分享
     * @return AppDto 对象数据
     */
    @RequestMapping(value = "/queryApps", method = RequestMethod.POST)
    List<AppDto> queryApps(@RequestBody AppDto appDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param appDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAppsCount", method = RequestMethod.POST)
    int queryAppsCount(@RequestBody AppDto appDto);


    /**
     * <p>修改APP信息</p>
     *
     *
     * @param appDto 数据对象分享
     * @return AppDto 对象数据
     */
    @RequestMapping(value = "/updateApp", method = RequestMethod.POST)
    int updateApp(@RequestBody AppDto appDto);


    /**
     * <p>添加APP信息</p>
     *
     *
     * @param appDto 数据对象分享
     * @return AppDto 对象数据
     */
    @RequestMapping(value = "/saveApp", method = RequestMethod.POST)
    int saveApp(@RequestBody AppDto appDto);

    /**
     * <p>删除APP信息</p>
     *
     *
     * @param appDto 数据对象分享
     * @return AppDto 对象数据
     */
    @RequestMapping(value = "/deleteApp", method = RequestMethod.POST)
    int deleteApp(@RequestBody AppDto appDto);
}
