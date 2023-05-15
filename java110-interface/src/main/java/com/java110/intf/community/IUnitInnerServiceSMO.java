package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.UnitDto;
import com.java110.dto.unit.FloorAndUnitDto;
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
@RequestMapping("/unitApi")
public interface IUnitInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param unitDto 数据对象分享
     * @return UnitDto 对象数据
     */
    @RequestMapping(value = "/queryUnits", method = RequestMethod.POST)
    List<UnitDto> queryUnits(@RequestBody UnitDto unitDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param unitDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUnitsCount", method = RequestMethod.POST)
    int queryUnitsCount(@RequestBody UnitDto unitDto);

    /**
     * 根据小区ID查询单元信息
     *
     * @param unitDto 单元数据封装信息
     * @return 单元信息
     */
    @RequestMapping(value = "/queryUnitsByCommunityId", method = RequestMethod.POST)
    List<UnitDto> queryUnitsByCommunityId(@RequestBody UnitDto unitDto);

    /**
     * 查询小区单元信息（instance过程）
     * 根据bId 查询小区单元信息
     *
     * @param floorAndUnitDto bId 信息
     * @return 小区单元信息
     * @throws
     */
    @RequestMapping(value = "/getFloorAndUnitInfo", method = RequestMethod.POST)
    public List<FloorAndUnitDto> getFloorAndUnitInfo(@RequestBody FloorAndUnitDto floorAndUnitDto);
}
