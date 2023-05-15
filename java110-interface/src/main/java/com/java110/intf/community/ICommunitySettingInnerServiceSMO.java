package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.community.CommunitySettingDto;
import com.java110.po.communitySetting.CommunitySettingPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICommunitySettingInnerServiceSMO
 * @Description 小区相关设置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/communitySettingApi")
public interface ICommunitySettingInnerServiceSMO {


    @RequestMapping(value = "/saveCommunitySetting", method = RequestMethod.POST)
    public int saveCommunitySetting(@RequestBody CommunitySettingPo communitySettingPo);

    @RequestMapping(value = "/updateCommunitySetting", method = RequestMethod.POST)
    public int updateCommunitySetting(@RequestBody CommunitySettingPo communitySettingPo);

    @RequestMapping(value = "/deleteCommunitySetting", method = RequestMethod.POST)
    public int deleteCommunitySetting(@RequestBody CommunitySettingPo communitySettingPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param communitySettingDto 数据对象分享
     * @return CommunitySettingDto 对象数据
     */
    @RequestMapping(value = "/queryCommunitySettings", method = RequestMethod.POST)
    List<CommunitySettingDto> queryCommunitySettings(@RequestBody CommunitySettingDto communitySettingDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communitySettingDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommunitySettingsCount", method = RequestMethod.POST)
    int queryCommunitySettingsCount(@RequestBody CommunitySettingDto communitySettingDto);
}
