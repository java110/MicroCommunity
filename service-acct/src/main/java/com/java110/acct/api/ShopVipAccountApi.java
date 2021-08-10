package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.shopVipAccount.IDeleteShopVipAccountBMO;
import com.java110.acct.bmo.shopVipAccount.IGetShopVipAccountBMO;
import com.java110.acct.bmo.shopVipAccount.ISaveShopVipAccountBMO;
import com.java110.acct.bmo.shopVipAccount.IUpdateShopVipAccountBMO;
import com.java110.acct.bmo.shopVipAccountDetail.IDeleteShopVipAccountDetailBMO;
import com.java110.acct.bmo.shopVipAccountDetail.IGetShopVipAccountDetailBMO;
import com.java110.acct.bmo.shopVipAccountDetail.ISaveShopVipAccountDetailBMO;
import com.java110.acct.bmo.shopVipAccountDetail.IUpdateShopVipAccountDetailBMO;
import com.java110.dto.shopVipAccount.ShopUserAccountVipDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDetailDto;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
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

    @Autowired
    private ISaveShopVipAccountDetailBMO saveShopVipAccountDetailBMOImpl;
    @Autowired
    private IUpdateShopVipAccountDetailBMO updateShopVipAccountDetailBMOImpl;
    @Autowired
    private IDeleteShopVipAccountDetailBMO deleteShopVipAccountDetailBMOImpl;

    @Autowired
    private IGetShopVipAccountDetailBMO getShopVipAccountDetailBMOImpl;

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
                                                      @RequestParam(value = "vipId", required = false) String vipId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ShopVipAccountDto shopVipAccountDto = new ShopVipAccountDto();
        shopVipAccountDto.setPage(page);
        shopVipAccountDto.setRow(row);
        shopVipAccountDto.setShopId(shopId);
        shopVipAccountDto.setVipId(vipId);
        return getShopVipAccountBMOImpl.get(shopVipAccountDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /shopVipAccount/saveShopVipAccountDetail
     * @path /app/shopVipAccount/saveShopVipAccountDetail
     */
    @RequestMapping(value = "/saveShopVipAccountDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveShopVipAccountDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "vipAcctId", "请求报文中未包含vipAcctId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");


        ShopVipAccountDetailPo shopVipAccountDetailPo = BeanConvertUtil.covertBean(reqJson, ShopVipAccountDetailPo.class);
        return saveShopVipAccountDetailBMOImpl.save(shopVipAccountDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /shopVipAccount/updateShopVipAccountDetail
     * @path /app/shopVipAccount/updateShopVipAccountDetail
     */
    @RequestMapping(value = "/updateShopVipAccountDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateShopVipAccountDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "vipAcctId", "请求报文中未包含vipAcctId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ShopVipAccountDetailPo shopVipAccountDetailPo = BeanConvertUtil.covertBean(reqJson, ShopVipAccountDetailPo.class);
        return updateShopVipAccountDetailBMOImpl.update(shopVipAccountDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /shopVipAccount/deleteShopVipAccountDetail
     * @path /app/shopVipAccount/deleteShopVipAccountDetail
     */
    @RequestMapping(value = "/deleteShopVipAccountDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteShopVipAccountDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ShopVipAccountDetailPo shopVipAccountDetailPo = BeanConvertUtil.covertBean(reqJson, ShopVipAccountDetailPo.class);
        return deleteShopVipAccountDetailBMOImpl.delete(shopVipAccountDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param shopId 小区ID
     * @return
     * @serviceCode /shopVipAccount/queryShopVipAccountDetail
     * @path /app/shopVipAccount/queryShopVipAccountDetail
     */
    @RequestMapping(value = "/queryShopVipAccountDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryShopVipAccountDetail(@RequestParam(value = "shopId", required = false) String shopId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        ShopVipAccountDetailDto shopVipAccountDetailDto = new ShopVipAccountDetailDto();
        shopVipAccountDetailDto.setPage(page);
        shopVipAccountDetailDto.setRow(row);
        shopVipAccountDetailDto.setShopId(shopId);
        return getShopVipAccountDetailBMOImpl.get(shopVipAccountDetailDto);
    }



}
