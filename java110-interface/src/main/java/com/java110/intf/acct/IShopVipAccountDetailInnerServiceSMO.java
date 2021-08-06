package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.shopVipAccount.ShopVipAccountDetailDto;
import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IShopVipAccountDetailInnerServiceSMO
 * @Description 会员账户交易接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/shopVipAccountDetailApi")
public interface IShopVipAccountDetailInnerServiceSMO {


    @RequestMapping(value = "/saveShopVipAccountDetail", method = RequestMethod.POST)
    public int saveShopVipAccountDetail(@RequestBody ShopVipAccountDetailPo shopVipAccountDetailPo);

    @RequestMapping(value = "/updateShopVipAccountDetail", method = RequestMethod.POST)
    public int updateShopVipAccountDetail(@RequestBody  ShopVipAccountDetailPo shopVipAccountDetailPo);

    @RequestMapping(value = "/deleteShopVipAccountDetail", method = RequestMethod.POST)
    public int deleteShopVipAccountDetail(@RequestBody  ShopVipAccountDetailPo shopVipAccountDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param shopVipAccountDetailDto 数据对象分享
     * @return ShopVipAccountDetailDto 对象数据
     */
    @RequestMapping(value = "/queryShopVipAccountDetails", method = RequestMethod.POST)
    List<ShopVipAccountDetailDto> queryShopVipAccountDetails(@RequestBody ShopVipAccountDetailDto shopVipAccountDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param shopVipAccountDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryShopVipAccountDetailsCount", method = RequestMethod.POST)
    int queryShopVipAccountDetailsCount(@RequestBody ShopVipAccountDetailDto shopVipAccountDetailDto);



}
