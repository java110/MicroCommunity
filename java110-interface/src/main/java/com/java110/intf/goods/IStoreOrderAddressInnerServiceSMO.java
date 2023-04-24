package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeOrder.StoreOrderAddressDto;
import com.java110.po.storeOrderAddress.StoreOrderAddressPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreOrderAddressInnerServiceSMO
 * @Description 发货地址接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeOrderAddressApi")
public interface IStoreOrderAddressInnerServiceSMO {


    @RequestMapping(value = "/saveStoreOrderAddress", method = RequestMethod.POST)
    public int saveStoreOrderAddress(@RequestBody StoreOrderAddressPo storeOrderAddressPo);

    @RequestMapping(value = "/updateStoreOrderAddress", method = RequestMethod.POST)
    public int updateStoreOrderAddress(@RequestBody  StoreOrderAddressPo storeOrderAddressPo);

    @RequestMapping(value = "/deleteStoreOrderAddress", method = RequestMethod.POST)
    public int deleteStoreOrderAddress(@RequestBody  StoreOrderAddressPo storeOrderAddressPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storeOrderAddressDto 数据对象分享
     * @return StoreOrderAddressDto 对象数据
     */
    @RequestMapping(value = "/queryStoreOrderAddresss", method = RequestMethod.POST)
    List<StoreOrderAddressDto> queryStoreOrderAddresss(@RequestBody StoreOrderAddressDto storeOrderAddressDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeOrderAddressDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreOrderAddresssCount", method = RequestMethod.POST)
    int queryStoreOrderAddresssCount(@RequestBody StoreOrderAddressDto storeOrderAddressDto);
}
