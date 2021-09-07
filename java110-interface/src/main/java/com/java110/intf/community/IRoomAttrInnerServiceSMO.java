
package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.RoomAttrDto;
import com.java110.po.room.RoomAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRoomAttrInnerServiceSMO
 * @Description 小区房屋属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/roomAttrApi")
public interface IRoomAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param roomAttrDto 数据对象分享
     * @return RoomAttrDto 对象数据
     */
    @RequestMapping(value = "/queryRoomAttrs", method = RequestMethod.POST)
    List<RoomAttrDto> queryRoomAttrs(@RequestBody RoomAttrDto roomAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRoomAttrsCount", method = RequestMethod.POST)
    int queryRoomAttrsCount(@RequestBody RoomAttrDto roomAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveRoomAttr", method = RequestMethod.POST)
    int saveRoomAttr(@RequestBody RoomAttrPo roomAttrPo);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/updateRoomAttrInfoInstance", method = RequestMethod.POST)
    int updateRoomAttrInfoInstance(@RequestBody RoomAttrPo roomAttrPo);
}
