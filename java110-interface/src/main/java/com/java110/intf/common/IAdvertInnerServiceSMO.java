package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.advert.AdvertDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAdvertInnerServiceSMO
 * @Description 广告信息接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/advertApi")
public interface IAdvertInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param advertDto 数据对象分享
     * @return AdvertDto 对象数据
     */
    @RequestMapping(value = "/queryAdverts", method = RequestMethod.POST)
    List<AdvertDto> queryAdverts(@RequestBody AdvertDto advertDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param advertDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAdvertsCount", method = RequestMethod.POST)
    int queryAdvertsCount(@RequestBody AdvertDto advertDto);

    /**
     * 修改广告信息
     *
     * @param advertDto
     * @return
     */
    @RequestMapping(value = "/updateAdverts", method = RequestMethod.POST)
    int updateAdverts(@RequestBody AdvertDto advertDto);
}
