package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.service.ServiceDto;
import com.java110.dto.service.ServiceProvideDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IServiceInnerServiceSMO
 * @Description 服务接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/serviceApi")
public interface IServiceInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/queryServices", method = RequestMethod.POST)
    List<ServiceDto> queryServices(@RequestBody ServiceDto serviceDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param serviceDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryServicesCount", method = RequestMethod.POST)
    int queryServicesCount(@RequestBody ServiceDto serviceDto);


    /**
     * <p>修改APP信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/updateService", method = RequestMethod.POST)
    int updateService(@RequestBody ServiceDto serviceDto);


    /**
     * <p>添加APP信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/saveService", method = RequestMethod.POST)
    int saveService(@RequestBody ServiceDto serviceDto);

    /**
     * <p>删除APP信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/deleteService", method = RequestMethod.POST)
    int deleteService(@RequestBody ServiceDto serviceDto);


    /**
     * <p>查询小区楼信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/queryServiceProvides", method = RequestMethod.POST)
    List<ServiceProvideDto> queryServiceProvides(@RequestBody ServiceProvideDto serviceDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param serviceDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryServiceProvidesCount", method = RequestMethod.POST)
    int queryServiceProvidesCount(@RequestBody ServiceProvideDto serviceDto);


    /**
     * <p>修改APP信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceProvideDto 对象数据
     */
    @RequestMapping(value = "/updateServiceProvide", method = RequestMethod.POST)
    int updateServiceProvide(@RequestBody ServiceProvideDto serviceDto);


    /**
     * <p>添加APP信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceProvideDto 对象数据
     */
    @RequestMapping(value = "/saveServiceProvide", method = RequestMethod.POST)
    int saveServiceProvide(@RequestBody ServiceProvideDto serviceDto);

    /**
     * <p>删除APP信息</p>
     *
     * @param serviceDto 数据对象分享
     * @return ServiceProvideDto 对象数据
     */
    @RequestMapping(value = "/deleteServiceProvide", method = RequestMethod.POST)
    int deleteServiceProvide(@RequestBody ServiceProvideDto serviceDto);


}
