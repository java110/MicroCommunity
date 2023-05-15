package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.RoomDto;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.po.roomRenovation.RoomRenovationPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRoomRenovationInnerServiceSMO
 * @Description 装修申请接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/roomRenovationApi")
public interface IRoomRenovationInnerServiceSMO {

    @RequestMapping(value = "/saveRoomRenovation", method = RequestMethod.POST)
    public int saveRoomRenovation(@RequestBody RoomRenovationPo roomRenovationPo);

    @RequestMapping(value = "/updateRoomRenovation", method = RequestMethod.POST)
    public int updateRoomRenovation(@RequestBody RoomRenovationPo roomRenovationPo);

    @RequestMapping(value = "/updateRoom", method = RequestMethod.POST)
    public int updateRoom(@RequestBody RoomDto roomDto);

    @RequestMapping(value = "/deleteRoomRenovation", method = RequestMethod.POST)
    public int deleteRoomRenovation(@RequestBody RoomRenovationPo roomRenovationPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param roomRenovationDto 数据对象分享
     * @return RoomRenovationDto 对象数据
     */
    @RequestMapping(value = "/queryRoomRenovations", method = RequestMethod.POST)
    List<RoomRenovationDto> queryRoomRenovations(@RequestBody RoomRenovationDto roomRenovationDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomRenovationDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRoomRenovationsCount", method = RequestMethod.POST)
    int queryRoomRenovationsCount(@RequestBody RoomRenovationDto roomRenovationDto);
}
