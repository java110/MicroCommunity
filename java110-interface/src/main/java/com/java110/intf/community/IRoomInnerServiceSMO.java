package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.RoomDto;
import com.java110.entity.assetImport.ImportCustomCreateFeeDto;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.po.room.RoomPo;
import org.springframework.cloud.openfeign.FeignClient;
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
     * @param roomDto 数据对象分享
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/queryRooms", method = RequestMethod.POST)
    List<RoomDto> queryRooms(@RequestBody RoomDto roomDto);

    /**
     * <p>修改房屋信息</p>
     *
     * @param roomPo 房屋信息
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/updateRooms", method = RequestMethod.POST)
    int updateRooms(@RequestBody RoomPo roomPo);

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
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRoomsWithSellCount", method = RequestMethod.POST)
    int queryRoomsWithSellCount(@RequestBody RoomDto roomDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param roomDto 数据对象分享
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/queryRoomsWithOutSell", method = RequestMethod.POST)
    List<RoomDto> queryRoomsWithOutSell(@RequestBody RoomDto roomDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param roomDto 数据对象分享
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/queryRoomsWithSell", method = RequestMethod.POST)
    List<RoomDto> queryRoomsWithSell(@RequestBody RoomDto roomDto);


    /**
     * <p>根据业主查询房屋信息</p>
     *
     * @param roomDto 数据对象分享
     * @return RoomDto 对象数据
     */
    @RequestMapping(value = "/queryRoomsByOwner", method = RequestMethod.POST)
    List<RoomDto> queryRoomsByOwner(@RequestBody RoomDto roomDto);


    /**
     * <p>根据业主查询房屋信息</p>
     *
     * @param importRoomFees 数据对象分享
     */
    @RequestMapping(value = "/freshRoomIds", method = RequestMethod.POST)
    List<ImportRoomFee> freshRoomIds(@RequestBody List<ImportRoomFee> importRoomFees);

    /**
     * <p>根据业主查询房屋信息</p>
     *
     * @param importCustomCreateFeeDtos 数据对象分享
     */
    @RequestMapping(value = "/freshRoomIdsByImportCustomCreateFee", method = RequestMethod.POST)
    List<ImportCustomCreateFeeDto> freshRoomIdsByImportCustomCreateFee(@RequestBody List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos);
}
