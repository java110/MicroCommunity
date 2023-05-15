package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IShopVipAccountInnerServiceSMO
 * @Description 会员账户接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/shopVipAccountApi")
public interface IShopVipAccountInnerServiceSMO {


    @RequestMapping(value = "/saveShopVipAccount", method = RequestMethod.POST)
    public int saveShopVipAccount(@RequestBody ShopVipAccountPo shopVipAccountPo);

    @RequestMapping(value = "/updateShopVipAccount", method = RequestMethod.POST)
    public int updateShopVipAccount(@RequestBody  ShopVipAccountPo shopVipAccountPo);

    @RequestMapping(value = "/deleteShopVipAccount", method = RequestMethod.POST)
    public int deleteShopVipAccount(@RequestBody  ShopVipAccountPo shopVipAccountPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param shopVipAccountDto 数据对象分享
     * @return ShopVipAccountDto 对象数据
     */
    @RequestMapping(value = "/queryShopVipAccounts", method = RequestMethod.POST)
    List<ShopVipAccountDto> queryShopVipAccounts(@RequestBody ShopVipAccountDto shopVipAccountDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param shopVipAccountDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryShopVipAccountsCount", method = RequestMethod.POST)
    int queryShopVipAccountsCount(@RequestBody ShopVipAccountDto shopVipAccountDto);

    /**
     * 预存金额
     * @param accountDetailPo
     * @return
     */
    @RequestMapping(value = "/prestoreAccount", method = RequestMethod.POST)
    int prestoreAccount(@RequestBody ShopVipAccountDetailPo accountDetailPo);

    /**
     * 扣款金额
     * @param accountDetailPo
     * @return
     */
    @RequestMapping(value = "/withholdAccount", method = RequestMethod.POST)
    int withholdAccount(@RequestBody ShopVipAccountDetailPo accountDetailPo);
}
