package com.java110.order.smo;

import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.dto.order.OrderItemDto;

/**
 * 异步通知子服务生成business 表
 */
public interface IAsynNotifySubService {

    void notifySubService(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto);
}
