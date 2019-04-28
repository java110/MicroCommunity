package com.java110.core.smo.community;


import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.CommunityMemberDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFloorInnerServiceSMO
 * @Description 小区楼接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/communityApi")
public interface ICommunityInnerServiceSMO {



    /**
     * 查询小区成员表
     *
     * @param communityMemberDto 小区成员数据封装
     * @return CommunityMemberDto 数据
     */
    @RequestMapping(value = "/getCommunityMembers", method = RequestMethod.POST)
    List<CommunityMemberDto> getCommunityMembers(@RequestBody CommunityMemberDto communityMemberDto);
}
