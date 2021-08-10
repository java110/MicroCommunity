package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IFloorInnerServiceSMO
 * @Description 小区楼接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/initializeApi")
public interface IinitializeBuildingUnitSMO {

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param floorIds 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteBuildingUnit", method = RequestMethod.POST)
    int deleteBuildingUnit(@RequestBody Map floorIds);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deletefFloor", method = RequestMethod.POST)
    int deletefFloor(@RequestBody Map communityId);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteBuildingRoom", method = RequestMethod.POST)
    int deleteBuildingRoom(@RequestBody Map communityId);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteParkingArea", method = RequestMethod.POST)
    int deleteParkingArea(@RequestBody Map communityId);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteParkingSpace", method = RequestMethod.POST)
    int deleteParkingSpace(@RequestBody Map communityId);
}
