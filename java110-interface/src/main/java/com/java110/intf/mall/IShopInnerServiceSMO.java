package com.java110.intf.mall;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.shop.ShopDto;
import com.java110.po.shop.ShopPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IShopInnerServiceSMO
 * @Description 店铺接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "shop-service", configuration = {FeignConfiguration.class})
@RequestMapping("/shopApi")
public interface IShopInnerServiceSMO {


    @RequestMapping(value = "/saveShop", method = RequestMethod.POST)
    public int saveShop(@RequestBody ShopPo shopPo);

    @RequestMapping(value = "/updateShop", method = RequestMethod.POST)
    public int updateShop(@RequestBody ShopPo shopPo);

    @RequestMapping(value = "/deleteShop", method = RequestMethod.POST)
    public int deleteShop(@RequestBody ShopPo shopPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param shopDto 数据对象分享
     * @return ShopDto 对象数据
     */
    @RequestMapping(value = "/queryShops", method = RequestMethod.POST)
    List<ShopDto> queryShops(@RequestBody ShopDto shopDto);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param
     * @return ShopDto 对象数据
     */
    @RequestMapping(value = "/getOrderShopInfo", method = RequestMethod.POST)
    List<ShopDto> getOrderShopInfo(@RequestBody String [] shopIds);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param shopDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryShopsCount", method = RequestMethod.POST)
    int queryShopsCount(@RequestBody ShopDto shopDto);

    @RequestMapping(value = "/returnStoreOrder", method = RequestMethod.POST)
    int returnStoreOrder(@RequestBody ShopDto shopDto);
}
