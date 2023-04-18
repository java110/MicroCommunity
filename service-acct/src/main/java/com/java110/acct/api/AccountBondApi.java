package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.accountBond.IDeleteAccountBondBMO;
import com.java110.acct.bmo.accountBond.IGetAccountBondBMO;
import com.java110.acct.bmo.accountBond.ISaveAccountBondBMO;
import com.java110.acct.bmo.accountBond.IUpdateAccountBondBMO;
import com.java110.dto.account.AccountBondDto;
import com.java110.po.accountBond.AccountBondPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/accountBond")
public class AccountBondApi {

    @Autowired
    private ISaveAccountBondBMO saveAccountBondBMOImpl;
    @Autowired
    private IUpdateAccountBondBMO updateAccountBondBMOImpl;
    @Autowired
    private IDeleteAccountBondBMO deleteAccountBondBMOImpl;

    @Autowired
    private IGetAccountBondBMO getAccountBondBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /accountBond/saveAccountBond
     * @path /app/accountBond/saveAccountBond
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveAccountBond", method = RequestMethod.POST)
    public ResponseEntity<String> saveAccountBond(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bondName", "请求报文中未包含bondName");
        Assert.hasKeyAndValue(reqJson, "bondType", "请求报文中未包含bondType");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "bondMonth", "请求报文中未包含bondMonth");


        AccountBondPo accountBondPo = BeanConvertUtil.covertBean(reqJson, AccountBondPo.class);
        //保证金类型默认未店铺类型，为6006，暂时写死方便后期扩展其他类型。
        accountBondPo.setBondType( "6006" );
        return saveAccountBondBMOImpl.save(accountBondPo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /accountBond/updateAccountBond
     * @path /app/accountBond/updateAccountBond
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateAccountBond", method = RequestMethod.POST)
    public ResponseEntity<String> updateAccountBond(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bondName", "请求报文中未包含bondName");
        Assert.hasKeyAndValue(reqJson, "bondType", "请求报文中未包含bondType");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "bondMonth", "请求报文中未包含bondMonth");
        Assert.hasKeyAndValue(reqJson, "bondId", "bondId不能为空");


        AccountBondPo accountBondPo = BeanConvertUtil.covertBean(reqJson, AccountBondPo.class);
        return updateAccountBondBMOImpl.update(accountBondPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountBond/deleteAccountBond
     * @path /app/accountBond/deleteAccountBond
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteAccountBond", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAccountBond(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bondId", "bondId不能为空");
        Assert.hasKeyAndValue(reqJson, "objId", "objId不能为空");


        AccountBondPo accountBondPo = BeanConvertUtil.covertBean(reqJson, AccountBondPo.class);
        return deleteAccountBondBMOImpl.delete(accountBondPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountBond/queryAccountBond
     * @path /app/accountBond/queryAccountBond
     * @param
     * @return
     */
        @RequestMapping(value = "/queryAccountBond", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountBond(@RequestParam(value = "bondName",required = false) String bondName,
                                                      @RequestParam(value = "objId",required = false) String objId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        AccountBondDto accountBondDto = new AccountBondDto();
        accountBondDto.setPage(page);
        accountBondDto.setRow(row);
        accountBondDto.setBondName(bondName);
        accountBondDto.setObjId(objId);
        return getAccountBondBMOImpl.get(accountBondDto);
    }
}
