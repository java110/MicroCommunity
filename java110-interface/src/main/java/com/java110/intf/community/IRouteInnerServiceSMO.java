package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.service.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRouteInnerServiceSMO
 * @Description 路由接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/routeApi")
public interface IRouteInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param routeDto 数据对象分享
     * @return RouteDto 对象数据
     */
    @RequestMapping(value = "/queryRoutes", method = RequestMethod.POST)
    List<RouteDto> queryRoutes(@RequestBody RouteDto routeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param routeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRoutesCount", method = RequestMethod.POST)
    int queryRoutesCount(@RequestBody RouteDto routeDto);

    /**
     * <p>修改APP信息</p>
     *
     *
     * @param routeDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/updateRoute", method = RequestMethod.POST)
    int updateRoute(@RequestBody RouteDto routeDto);


    /**
     * <p>添加APP信息</p>
     *
     *
     * @param routeDto 数据对象分享
     * @return RouteDto 对象数据
     */
    @RequestMapping(value = "/saveRoute", method = RequestMethod.POST)
    int saveRoute(@RequestBody RouteDto routeDto);

    /**
     * <p>删除APP信息</p>
     *
     *
     * @param routeDto 数据对象分享
     * @return RouteDto 对象数据
     */
    @RequestMapping(value = "/deleteRoute", method = RequestMethod.POST)
    int deleteRoute(@RequestBody RouteDto routeDto);
}
