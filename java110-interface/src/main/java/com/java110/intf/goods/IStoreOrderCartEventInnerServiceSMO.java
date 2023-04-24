package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeOrder.StoreOrderCartEventDto;
import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreOrderCartEventInnerServiceSMO
 * @Description 购物车事件接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeOrderCartEventApi")
public interface IStoreOrderCartEventInnerServiceSMO {


    @RequestMapping(value = "/saveStoreOrderCartEvent", method = RequestMethod.POST)
    public int saveStoreOrderCartEvent(@RequestBody StoreOrderCartEventPo storeOrderCartEventPo);

    @RequestMapping(value = "/updateStoreOrderCartEvent", method = RequestMethod.POST)
    public int updateStoreOrderCartEvent(@RequestBody  StoreOrderCartEventPo storeOrderCartEventPo);

    @RequestMapping(value = "/deleteStoreOrderCartEvent", method = RequestMethod.POST)
    public int deleteStoreOrderCartEvent(@RequestBody  StoreOrderCartEventPo storeOrderCartEventPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storeOrderCartEventDto 数据对象分享
     * @return StoreOrderCartEventDto 对象数据
     */
    @RequestMapping(value = "/queryStoreOrderCartEvents", method = RequestMethod.POST)
    List<StoreOrderCartEventDto> queryStoreOrderCartEvents(@RequestBody StoreOrderCartEventDto storeOrderCartEventDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeOrderCartEventDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreOrderCartEventsCount", method = RequestMethod.POST)
    int queryStoreOrderCartEventsCount(@RequestBody StoreOrderCartEventDto storeOrderCartEventDto);
}
