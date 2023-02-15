package com.java110.user.bmo.owner;

import com.java110.dto.owner.OwnerDto;

import java.util.List;

/**
 * 查询业主 房屋 车辆等统计信息
 */
public interface IQueryOwnerStatisticsBMO {

    /**
     * 查询业主统计信息
     * @param ownerDtos
     * @return
     */
    List<OwnerDto> query(List<OwnerDto> ownerDtos);
}
