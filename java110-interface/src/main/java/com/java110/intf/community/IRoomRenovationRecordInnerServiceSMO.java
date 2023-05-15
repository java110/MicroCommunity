package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/roomRenovationRecordApi")
public interface IRoomRenovationRecordInnerServiceSMO {

    @RequestMapping(value = "/saveRoomRenovationRecord", method = RequestMethod.POST)
    public int saveRoomRenovationRecord(@RequestBody RoomRenovationRecordPo roomRenovationRecordPo);

    /**
     * <p>查询装修记录信息(与文件表关联)</p>
     *
     * @param roomRenovationRecordPo 数据对象分享
     * @return RoomRenovationDto 对象数据
     */
    @RequestMapping(value = "/queryRoomRenovationRecords", method = RequestMethod.POST)
    List<RoomRenovationRecordPo> queryRoomRenovationRecords(@RequestBody RoomRenovationRecordPo roomRenovationRecordPo);

    /**
     * 查询装修记录信息
     *
     * @param roomRenovationRecordPo 数据对象分享
     * @return RoomRenovationDto 对象数据
     */
    @RequestMapping(value = "/getRoomRenovationRecords", method = RequestMethod.POST)
    List<RoomRenovationRecordPo> getRoomRenovationRecords(@RequestBody RoomRenovationRecordPo roomRenovationRecordPo);

    /**
     * 查询<p>装修</p>总记录数(与文件表关联)
     *
     * @param roomRenovationRecordPo 数据对象分享
     * @return 装修记录数
     */
    @RequestMapping(value = "/queryRoomRenovationRecordsCount", method = RequestMethod.POST)
    int queryRoomRenovationRecordsCount(@RequestBody RoomRenovationRecordPo roomRenovationRecordPo);

    /**
     * 查询装修记录数
     *
     * @param roomRenovationRecordPo
     * @return
     */
    @RequestMapping(value = "/getRoomRenovationRecordsCount", method = RequestMethod.POST)
    int getRoomRenovationRecordsCount(@RequestBody RoomRenovationRecordPo roomRenovationRecordPo);

    /**
     * 删除装修记录
     *
     * @param roomRenovationRecordPo
     * @return
     */
    @RequestMapping(value = "/deleteRoomRenovationRecord", method = RequestMethod.POST)
    int deleteRoomRenovationRecord(@RequestBody RoomRenovationRecordPo roomRenovationRecordPo);

}
