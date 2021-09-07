package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.floorAttr.FloorAttrDto;
import com.java110.po.floorAttr.FloorAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFloorAttrInnerServiceSMO
 * @Description 考勤班组属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/floorAttrApi")
public interface IFloorAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param floorAttrDto 数据对象分享
     * @return FloorAttrDto 对象数据
     */
    @RequestMapping(value = "/queryFloorAttrs", method = RequestMethod.POST)
    List<FloorAttrDto> queryFloorAttrs(@RequestBody FloorAttrDto floorAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param floorAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFloorAttrsCount", method = RequestMethod.POST)
    int queryFloorAttrsCount(@RequestBody FloorAttrDto floorAttrDto);


    /**
     * 保存 楼栋属性
     *
     * @param floorAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveFloorAttr", method = RequestMethod.POST)
    int saveFloorAttr(@RequestBody FloorAttrPo floorAttrPo);
    /**
     * 保存 楼栋属性
     *
     * @param floorAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/updateFloorAttrInfoInstance", method = RequestMethod.POST)
    int updateFloorAttrInfoInstance(@RequestBody FloorAttrPo floorAttrPo);
}
