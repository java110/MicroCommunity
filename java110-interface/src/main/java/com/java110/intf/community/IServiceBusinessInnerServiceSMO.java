package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.service.ServiceBusinessDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IServiceBusinessInnerServiceSMO
 * @Description 服务实现接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/serviceBusinessApi")
public interface IServiceBusinessInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param serviceBusinessDto 数据对象分享
     * @return ServiceBusinessDto 对象数据
     */
    @RequestMapping(value = "/queryServiceBusinesss", method = RequestMethod.POST)
    List<ServiceBusinessDto> queryServiceBusinesss(@RequestBody ServiceBusinessDto serviceBusinessDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param serviceBusinessDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryServiceBusinesssCount", method = RequestMethod.POST)
    int queryServiceBusinesssCount(@RequestBody ServiceBusinessDto serviceBusinessDto);

    /**
     * 保存服务实现
     *
     * @param serviceBusinessDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveServiceBusiness", method = RequestMethod.POST)
    int saveServiceBusiness(@RequestBody ServiceBusinessDto serviceBusinessDto);

    /**
     * 修改服务实现
     *
     * @param serviceBusinessDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/updateServiceBusiness", method = RequestMethod.POST)
    int updateServiceBusiness(@RequestBody ServiceBusinessDto serviceBusinessDto);

    /**
     * 删除服务实现
     *
     * @param serviceBusinessDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteServiceBusiness", method = RequestMethod.POST)
    int deleteServiceBusiness(@RequestBody ServiceBusinessDto serviceBusinessDto);
}
