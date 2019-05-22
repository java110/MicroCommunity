package com.java110.core.smo.room;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.RoomDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRoomInnerServiceSMO
 * @Description 小区房屋接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/roomApi")
public interface IRoomInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param roomDto 数据对象分享
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/queryRooms", method = RequestMethod.POST)
    List<RoomDto> queryRooms(@RequestBody RoomDto roomDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRoomsCount", method = RequestMethod.POST)
    int queryRoomsCount(@RequestBody RoomDto roomDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRoomsWithOutSellCount", method = RequestMethod.POST)
    int queryRoomsWithOutSellCount(@RequestBody RoomDto roomDto);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param roomDto 数据对象分享
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/queryRoomsWithOutSell", method = RequestMethod.POST)
    List<RoomDto> queryRoomsWithOutSell(@RequestBody RoomDto roomDto);


    /**
     * <p>根据业主查询房屋信息</p>
     *
     *
     * @param roomDto 数据对象分享
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/queryRoomsByOwner", method = RequestMethod.POST)
    List<RoomDto> queryRoomsByOwner(@RequestBody RoomDto roomDto);
}
