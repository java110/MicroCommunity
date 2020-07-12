package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.advert.AdvertItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAdvertItemInnerServiceSMO
 * @Description 广告项信息接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/advertItemApi")
public interface IAdvertItemInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param advertItemDto 数据对象分享
     * @return AdvertItemDto 对象数据
     */
    @RequestMapping(value = "/queryAdvertItems", method = RequestMethod.POST)
    List<AdvertItemDto> queryAdvertItems(@RequestBody AdvertItemDto advertItemDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param advertItemDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAdvertItemsCount", method = RequestMethod.POST)
    int queryAdvertItemsCount(@RequestBody AdvertItemDto advertItemDto);
}
