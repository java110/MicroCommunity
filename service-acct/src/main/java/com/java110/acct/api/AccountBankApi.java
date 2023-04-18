package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.accountBank.IDeleteAccountBankBMO;
import com.java110.acct.bmo.accountBank.IGetAccountBankBMO;
import com.java110.acct.bmo.accountBank.ISaveAccountBankBMO;
import com.java110.acct.bmo.accountBank.IUpdateAccountBankBMO;
import com.java110.dto.account.AccountBankDto;
import com.java110.po.accountBank.AccountBankPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/accountBank")
public class AccountBankApi {

    @Autowired
    private ISaveAccountBankBMO saveAccountBankBMOImpl;

    @Autowired
    private IUpdateAccountBankBMO updateAccountBankBMOImpl;

    @Autowired
    private IDeleteAccountBankBMO deleteAccountBankBMOImpl;

    @Autowired
    private IGetAccountBankBMO getAccountBankBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /accountBank/saveAccountBank
     * @path /app/accountBank/saveAccountBank
     */
    @RequestMapping(value = "/saveAccountBank", method = RequestMethod.POST)
    public ResponseEntity<String> saveAccountBank(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "bankCode", "请求报文中未包含bankCode");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "bankName", "请求报文中未包含bankName");
        Assert.hasKeyAndValue(reqJson, "shopId", "请求报文中未包含shopId");
        AccountBankPo accountBankPo = BeanConvertUtil.covertBean(reqJson, AccountBankPo.class);
        return saveAccountBankBMOImpl.save(accountBankPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /accountBank/updateAccountBank
     * @path /app/accountBank/updateAccountBank
     */
    @RequestMapping(value = "/updateAccountBank", method = RequestMethod.POST)
    public ResponseEntity<String> updateAccountBank(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "bankCode", "请求报文中未包含bankCode");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "bankName", "请求报文中未包含bankName");
        Assert.hasKeyAndValue(reqJson, "shopId", "请求报文中未包含shopId");
        Assert.hasKeyAndValue(reqJson, "bankId", "bankId不能为空");
        AccountBankPo accountBankPo = BeanConvertUtil.covertBean(reqJson, AccountBankPo.class);
        return updateAccountBankBMOImpl.update(accountBankPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /accountBank/deleteAccountBank
     * @path /app/accountBank/deleteAccountBank
     */
    @RequestMapping(value = "/deleteAccountBank", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAccountBank(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "shopId", "商铺ID不能为空");
        Assert.hasKeyAndValue(reqJson, "bankId", "bankId不能为空");
        AccountBankPo accountBankPo = BeanConvertUtil.covertBean(reqJson, AccountBankPo.class);
        return deleteAccountBankBMOImpl.delete(accountBankPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param shopId 小区ID
     * @return
     * @serviceCode /accountBank/queryAccountBank
     * @path /app/accountBank/queryAccountBank
     */
    @RequestMapping(value = "/queryAccountBank", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountBank(@RequestParam(value = "shopId", required = false) String shopId,
                                                   @RequestParam(value = "bankId", required = false) String bankId,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        AccountBankDto accountBankDto = new AccountBankDto();
        accountBankDto.setPage(page);
        accountBankDto.setRow(row);
        accountBankDto.setShopId(shopId);
        accountBankDto.setBankId(bankId);
        return getAccountBankBMOImpl.get(accountBankDto);
    }
}
