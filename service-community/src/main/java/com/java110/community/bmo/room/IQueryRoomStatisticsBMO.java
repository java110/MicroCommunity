package com.java110.community.bmo.room;

import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerDto;

import java.util.List;

/**
 * 查询业主 房屋 车辆等统计信息
 */
public interface IQueryRoomStatisticsBMO {

    /**
     * 查询房屋统计信息
     * @param roomDtos
     * @return
     */
    List<RoomDto> query(List<RoomDto> roomDtos);

    /**
     * 查询房屋统计信息
     * @param roomDtos
     * @return
     */
    List<RoomDto> queryRoomOweFee(List<RoomDto> roomDtos);
}
