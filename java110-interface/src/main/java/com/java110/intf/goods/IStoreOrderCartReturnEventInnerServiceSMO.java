package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeOrder.StoreOrderCartReturnEventDto;
import com.java110.po.storeOrderCartReturnEvent.StoreOrderCartReturnEventPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreOrderCartReturnEventInnerServiceSMO
 * @Description 退货事件接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeOrderCartReturnEventApi")
public interface IStoreOrderCartReturnEventInnerServiceSMO {


    @RequestMapping(value = "/saveStoreOrderCartReturnEvent", method = RequestMethod.POST)
    public int saveStoreOrderCartReturnEvent(@RequestBody StoreOrderCartReturnEventPo storeOrderCartReturnEventPo);

    @RequestMapping(value = "/updateStoreOrderCartReturnEvent", method = RequestMethod.POST)
    public int updateStoreOrderCartReturnEvent(@RequestBody  StoreOrderCartReturnEventPo storeOrderCartReturnEventPo);

    @RequestMapping(value = "/deleteStoreOrderCartReturnEvent", method = RequestMethod.POST)
    public int deleteStoreOrderCartReturnEvent(@RequestBody  StoreOrderCartReturnEventPo storeOrderCartReturnEventPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storeOrderCartReturnEventDto 数据对象分享
     * @return StoreOrderCartReturnEventDto 对象数据
     */
    @RequestMapping(value = "/queryStoreOrderCartReturnEvents", method = RequestMethod.POST)
    List<StoreOrderCartReturnEventDto> queryStoreOrderCartReturnEvents(@RequestBody StoreOrderCartReturnEventDto storeOrderCartReturnEventDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeOrderCartReturnEventDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreOrderCartReturnEventsCount", method = RequestMethod.POST)
    int queryStoreOrderCartReturnEventsCount(@RequestBody StoreOrderCartReturnEventDto storeOrderCartReturnEventDto);
}
