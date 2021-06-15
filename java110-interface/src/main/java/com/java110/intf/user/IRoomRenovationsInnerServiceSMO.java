package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 房屋装修
 *
 * @author fqz
 * @date 2021-02-25 8:41
 */
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/roomRenovationsApi")
public interface IRoomRenovationsInnerServiceSMO {

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
