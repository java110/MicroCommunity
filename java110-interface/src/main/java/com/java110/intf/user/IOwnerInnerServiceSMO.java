package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerDto;
import com.java110.po.owner.OwnerPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerInnerServiceSMO
 * @Description 业主接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerApi")
public interface IOwnerInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param ownerDto 数据对象分享
     * @return OwnerDto 对象数据
     */
    @RequestMapping(value = "/queryOwners", method = RequestMethod.POST)
    List<OwnerDto> queryOwners(@RequestBody OwnerDto ownerDto);


    /**
     * <p>查询业主成员</p>
     *
     * @param ownerDto 数据对象分享
     * @return OwnerDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerMembers", method = RequestMethod.POST)
    List<OwnerDto> queryOwnerMembers(@RequestBody OwnerDto ownerDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnersCount", method = RequestMethod.POST)
    int queryOwnersCount(@RequestBody OwnerDto ownerDto);

    /**
     * 查询<p>小区楼</p>总记录数 根据条件查询
     *
     * @param ownerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnerCountByCondition", method = RequestMethod.POST)
    int queryOwnerCountByCondition(@RequestBody OwnerDto ownerDto);


    /**
     * <p>查询小区楼信息</p> 根据条件查询
     *
     * @param ownerDto 数据对象分享 根据条件查询
     * @return OwnerDto 对象数据
     */
    @RequestMapping(value = "/queryOwnersByCondition", method = RequestMethod.POST)
    List<OwnerDto> queryOwnersByCondition(@RequestBody OwnerDto ownerDto);


    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryNoEnterRoomOwnerCount", method = RequestMethod.POST)
    int queryNoEnterRoomOwnerCount(@RequestBody OwnerDto ownerDto);

    /**
     * 根据房屋查询业主信息
     *
     * @param ownerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnersByRoom", method = RequestMethod.POST)
    List<OwnerDto> queryOwnersByRoom(@RequestBody OwnerDto ownerDto);


    /**
     * 根据停车位查询业主信息
     *
     * @param ownerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnersByParkingSpace", method = RequestMethod.POST)
    List<OwnerDto> queryOwnersByParkingSpace(@RequestBody OwnerDto ownerDto);

    /**
     * 修改业主信息
     *
     * @param ownerPo
     * @return
     */
    @RequestMapping(value = "/updateOwnerMember", method = RequestMethod.POST)
    int updateOwnerMember(@RequestBody OwnerPo ownerPo);

    /**
     * 查询 商铺租房记录 数量
     *
     * @param ownerDto
     * @return
     */
    @RequestMapping(value = "/queryOwnerLogsCountByRoom", method = RequestMethod.POST)
    int queryOwnerLogsCountByRoom(@RequestBody OwnerDto ownerDto);

    /**
     * 查询 商铺租房记录
     *
     * @param ownerDto
     * @return
     */
    @RequestMapping(value = "/queryOwnerLogsByRoom", method = RequestMethod.POST)
    List<OwnerDto> queryOwnerLogsByRoom(@RequestBody OwnerDto ownerDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param ownerDto 数据对象分享
     * @return OwnerDto 对象数据
     */
    @RequestMapping(value = "/queryAllOwners", method = RequestMethod.POST)
    List<OwnerDto> queryAllOwners(@RequestBody OwnerDto ownerDto);
}
