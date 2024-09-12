package com.java110.job.adapt.hcIot;

import com.java110.dto.owner.OwnerDto;

public interface IOwnerDataToIot {

    /**
     * 同步业主数据
     * @param ownerDto
     */
    void sendOwnerData(OwnerDto ownerDto);
}
