package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.po.community.CommunityAttrPo;
import com.java110.po.community.CommunityPo;
import org.springframework.cloud.openfeign.FeignClient;
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
     * <p>查询小区楼信息</p>
     *
     * @param communityDto 数据对象分享
     * @return CommunityDto 对象数据
     */
    @RequestMapping(value = "/queryCommunitys", method = RequestMethod.POST)
    List<CommunityDto> queryCommunitys(@RequestBody CommunityDto communityDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommunitysCount", method = RequestMethod.POST)
    int queryCommunitysCount(@RequestBody CommunityDto communityDto);


    /**
     * 查询小区成员表
     *
     * @param communityMemberDto 小区成员数据封装
     * @return CommunityMemberDto 数据
     */
    @RequestMapping(value = "/getCommunityMembers", method = RequestMethod.POST)
    List<CommunityMemberDto> getCommunityMembers(@RequestBody CommunityMemberDto communityMemberDto);


    /**
     * 查询商户入驻小区
     *
     * @param communityMemberDto 小区成员数据封装
     * @return CommunityMemberDto 数据
     */
    @RequestMapping(value = "/getStoreCommunitys", method = RequestMethod.POST)
    List<CommunityDto> getStoreCommunitys(@RequestBody CommunityMemberDto communityMemberDto);

    /**
     * 查询小区成员数量
     *
     * @param communityMemberDto 小区成员数据封装
     * @return CommunityMemberDto 数据
     */
    @RequestMapping(value = "/getCommunityMemberCount", method = RequestMethod.POST)
    int getCommunityMemberCount(@RequestBody CommunityMemberDto communityMemberDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param communityAttrDto 数据对象分享
     * @return CommunityDto 对象数据
     */
    @RequestMapping(value = "/getCommunityAttrs", method = RequestMethod.POST)
    List<CommunityAttrDto> getCommunityAttrs(@RequestBody CommunityAttrDto communityAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/getCommunityAttrsCount", method = RequestMethod.POST)
    int getCommunityAttrsCount(@RequestBody CommunityAttrDto communityAttrDto);


    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveCommunityAttr", method = RequestMethod.POST)
    int saveCommunityAttr(@RequestBody CommunityAttrPo communityAttrPo);
}
