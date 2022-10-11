package com.java110.intf.mall;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.shopCommunity.ShopCommunityDto;
import com.java110.po.shopCommunity.ShopCommunityPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IShopCommunityInnerServiceSMO
 * @Description 商铺小区接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "shop-service", configuration = {FeignConfiguration.class})
@RequestMapping("/shopCommunityApi")
public interface IShopCommunityInnerServiceSMO {


    @RequestMapping(value = "/saveShopCommunity", method = RequestMethod.POST)
    public int saveShopCommunity(@RequestBody ShopCommunityPo shopCommunityPo);

    @RequestMapping(value = "/updateShopCommunity", method = RequestMethod.POST)
    public int updateShopCommunity(@RequestBody  ShopCommunityPo shopCommunityPo);

    @RequestMapping(value = "/deleteShopCommunity", method = RequestMethod.POST)
    public int deleteShopCommunity(@RequestBody  ShopCommunityPo shopCommunityPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param shopCommunityDto 数据对象分享
     * @return ShopCommunityDto 对象数据
     */
    @RequestMapping(value = "/queryShopCommunitys", method = RequestMethod.POST)
    List<ShopCommunityDto> queryShopCommunitys(@RequestBody ShopCommunityDto shopCommunityDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param shopCommunityDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryShopCommunitysCount", method = RequestMethod.POST)
    int queryShopCommunitysCount(@RequestBody ShopCommunityDto shopCommunityDto);
}
