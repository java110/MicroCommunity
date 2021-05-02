package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.shopVipAccount.IDeleteShopVipAccountBMO;
import com.java110.acct.bmo.shopVipAccount.IGetShopVipAccountBMO;
import com.java110.acct.bmo.shopVipAccount.ISaveShopVipAccountBMO;
import com.java110.acct.bmo.shopVipAccount.IUpdateShopVipAccountBMO;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/shopVipAccount")
public class ShopVipAccountApi {

    @Autowired
    private ISaveShopVipAccountBMO saveShopVipAccountBMOImpl;
    @Autowired
    private IUpdateShopVipAccountBMO updateShopVipAccountBMOImpl;
    @Autowired
    private IDeleteShopVipAccountBMO deleteShopVipAccountBMOImpl;

    @Autowired
    private IGetShopVipAccountBMO getShopVipAccountBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /shopVipAccount/saveShopVipAccount
     * @path /app/shopVipAccount/saveShopVipAccount
     */
    @RequestMapping(value = "/saveShopVipAccount", method = RequestMethod.POST)
    public ResponseEntity<String> saveShopVipAccount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "vipId", "请求报文中未包含vipId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");


        ShopVipAccountPo shopVipAccountPo = BeanConvertUtil.covertBean(reqJson, ShopVipAccountPo.class);
        return saveShopVipAccountBMOImpl.save(shopVipAccountPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /shopVipAccount/updateShopVipAccount
     * @path /app/shopVipAccount/updateShopVipAccount
     */
    @RequestMapping(value = "/updateShopVipAccount", method = RequestMethod.POST)
    public ResponseEntity<String> updateShopVipAccount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "vipId", "请求报文中未包含vipId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "vipAcctId", "vipAcctId不能为空");


        ShopVipAccountPo shopVipAccountPo = BeanConvertUtil.covertBean(reqJson, ShopVipAccountPo.class);
        return updateShopVipAccountBMOImpl.update(shopVipAccountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /shopVipAccount/deleteShopVipAccount
     * @path /app/shopVipAccount/deleteShopVipAccount
     */
    @RequestMapping(value = "/deleteShopVipAccount", method = RequestMethod.POST)
    public ResponseEntity<String> deleteShopVipAccount(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "vipAcctId", "vipAcctId不能为空");


        ShopVipAccountPo shopVipAccountPo = BeanConvertUtil.covertBean(reqJson, ShopVipAccountPo.class);
        return deleteShopVipAccountBMOImpl.delete(shopVipAccountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param shopId 小区ID
     * @return
     * @serviceCode /shopVipAccount/queryShopVipAccount
     * @path /app/shopVipAccount/queryShopVipAccount
     */
    @RequestMapping(value = "/queryShopVipAccount", method = RequestMethod.GET)
    public ResponseEntity<String> queryShopVipAccount(@RequestParam(value = "shopId", required = false) String shopId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ShopVipAccountDto shopVipAccountDto = new ShopVipAccountDto();
        shopVipAccountDto.setPage(page);
        shopVipAccountDto.setRow(row);
        shopVipAccountDto.setShopId(shopId);
        return getShopVipAccountBMOImpl.get(shopVipAccountDto);
    }
}
