package com.java110.job.adapt.market;


import com.java110.dto.market.MarketTextDto;

public interface ISendExecutor {

    /**
     * 发送信息
     * @param marketTextDto
     */
    void send(MarketTextDto marketTextDto,String tel,String communityId,String communityName);
}
