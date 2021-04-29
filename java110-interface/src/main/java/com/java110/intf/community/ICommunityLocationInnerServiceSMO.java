package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.community.CommunityLocationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICommunityLocationInnerServiceSMO
 * @Description 小区位置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/communityLocationApi")
public interface ICommunityLocationInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param communityLocationDto 数据对象分享
     * @return CommunityLocationDto 对象数据
     */
    @RequestMapping(value = "/queryCommunityLocations", method = RequestMethod.POST)
    List<CommunityLocationDto> queryCommunityLocations(@RequestBody CommunityLocationDto communityLocationDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityLocationDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommunityLocationsCount", method = RequestMethod.POST)
    int queryCommunityLocationsCount(@RequestBody CommunityLocationDto communityLocationDto);
}
