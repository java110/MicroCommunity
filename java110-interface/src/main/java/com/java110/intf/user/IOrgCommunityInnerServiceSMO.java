package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.org.OrgCommunityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOrgCommunityInnerServiceSMO
 * @Description 隶属小区接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/orgCommunityApi")
public interface IOrgCommunityInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param orgCommunityDto 数据对象分享
     * @return OrgCommunityDto 对象数据
     */
    @RequestMapping(value = "/queryOrgCommunitys", method = RequestMethod.POST)
    List<OrgCommunityDto> queryOrgCommunitys(@RequestBody OrgCommunityDto orgCommunityDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param orgCommunityDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOrgCommunitysCount", method = RequestMethod.POST)
    int queryOrgCommunitysCount(@RequestBody OrgCommunityDto orgCommunityDto);
}
