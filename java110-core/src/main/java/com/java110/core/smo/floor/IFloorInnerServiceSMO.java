package com.java110.core.smo.floor;

import com.java110.dto.FloorDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IFloorInnerServiceSMO
 * @Description 小区楼接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient("community-service")
@RequestMapping("/floorApi")
public interface IFloorInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param page        页数
     * @param row         行数
     * @param communityId 小区ID
     * @return FloorDto 对象数据
     */
    @RequestMapping(value = "/queryFloors", method = RequestMethod.GET)
    List<FloorDto> queryFloors(@RequestParam("page") int page,
                               @RequestParam("row") int row,
                               @RequestParam("communityId") String communityId);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 小区ID
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFloorsCount", method = RequestMethod.GET)
    int queryFloorsCount(@RequestParam("communityId") String communityId);
}
