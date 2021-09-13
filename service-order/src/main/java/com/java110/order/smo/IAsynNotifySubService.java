package com.java110.order.smo;

import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.order.OrderItemDto;

import java.util.List;
import java.util.Map;

/**
 * 异步通知子服务生成business 表
 */
public interface IAsynNotifySubService {

    void notifySubService(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto);

    /**
     * 通知databus
     * @param orderItemMaps
     * @param orderDto
     */
    void notifyDatabus(List<Map> orderItemMaps, OrderDto orderDto);
}
