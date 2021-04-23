package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.roomRenovationDetail.RoomRenovationDetailDto;
import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRoomRenovationDetailInnerServiceSMO
 * @Description 装修明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/roomRenovationDetailApi")
public interface IRoomRenovationDetailInnerServiceSMO {


    @RequestMapping(value = "/saveRoomRenovationDetail", method = RequestMethod.POST)
    public int saveRoomRenovationDetail(@RequestBody RoomRenovationDetailPo roomRenovationDetailPo);

    @RequestMapping(value = "/updateRoomRenovationDetail", method = RequestMethod.POST)
    public int updateRoomRenovationDetail(@RequestBody  RoomRenovationDetailPo roomRenovationDetailPo);

    @RequestMapping(value = "/deleteRoomRenovationDetail", method = RequestMethod.POST)
    public int deleteRoomRenovationDetail(@RequestBody  RoomRenovationDetailPo roomRenovationDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param roomRenovationDetailDto 数据对象分享
     * @return RoomRenovationDetailDto 对象数据
     */
    @RequestMapping(value = "/queryRoomRenovationDetails", method = RequestMethod.POST)
    List<RoomRenovationDetailDto> queryRoomRenovationDetails(@RequestBody RoomRenovationDetailDto roomRenovationDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param roomRenovationDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRoomRenovationDetailsCount", method = RequestMethod.POST)
    int queryRoomRenovationDetailsCount(@RequestBody RoomRenovationDetailDto roomRenovationDetailDto);
}
