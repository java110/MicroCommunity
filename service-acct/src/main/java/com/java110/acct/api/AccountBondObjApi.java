package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.accountBondObj.IDeleteAccountBondObjBMO;
import com.java110.acct.bmo.accountBondObj.IGetAccountBondObjBMO;
import com.java110.acct.bmo.accountBondObj.ISaveAccountBondObjBMO;
import com.java110.acct.bmo.accountBondObj.IUpdateAccountBondObjBMO;
import com.java110.dto.account.AccountBondObjDto;
import com.java110.po.accountBondObj.AccountBondObjPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/accountBondObj")
public class AccountBondObjApi {

    @Autowired
    private ISaveAccountBondObjBMO saveAccountBondObjBMOImpl;
    @Autowired
    private IUpdateAccountBondObjBMO updateAccountBondObjBMOImpl;
    @Autowired
    private IDeleteAccountBondObjBMO deleteAccountBondObjBMOImpl;

    @Autowired
    private IGetAccountBondObjBMO getAccountBondObjBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /accountBondObj/saveAccountBondObj
     * @path /app/accountBondObj/saveAccountBondObj
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveAccountBondObj", method = RequestMethod.POST)
    public ResponseEntity<String> saveAccountBondObj(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bondId", "请求报文中未包含bondId");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "bondType", "请求报文中未包含bondType");
        Assert.hasKeyAndValue(reqJson, "receivableAmount", "请求报文中未包含receivableAmount");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");


        AccountBondObjPo accountBondObjPo = BeanConvertUtil.covertBean(reqJson, AccountBondObjPo.class);
        return saveAccountBondObjBMOImpl.save(accountBondObjPo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /accountBondObj/updateAccountBondObj
     * @path /app/accountBondObj/updateAccountBondObj
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateAccountBondObj", method = RequestMethod.POST)
    public ResponseEntity<String> updateAccountBondObj(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bobjId", "bobjId不能为空");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");


        AccountBondObjPo accountBondObjPo = BeanConvertUtil.covertBean(reqJson, AccountBondObjPo.class);
        return updateAccountBondObjBMOImpl.update(accountBondObjPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountBondObj/deleteAccountBondObj
     * @path /app/accountBondObj/deleteAccountBondObj
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteAccountBondObj", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAccountBondObj(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "bobjId", "bobjId不能为空");
        Assert.hasKeyAndValue(reqJson, "objId", "objId不能为空");


        AccountBondObjPo accountBondObjPo = BeanConvertUtil.covertBean(reqJson, AccountBondObjPo.class);
        return deleteAccountBondObjBMOImpl.delete(accountBondObjPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountBondObj/queryAccountBondObj
     * @path /app/accountBondObj/queryAccountBondObj
     * @param
     * @return
     */
    @RequestMapping(value = "/queryAccountBondObj", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountBondObj(@RequestParam(value = "bobjId",required = false) String bobjId,
                                                      @RequestParam(value = "state",required = false) String state,
                                                      @RequestParam(value = "objId",required = false) String objId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        AccountBondObjDto accountBondObjDto = new AccountBondObjDto();
        accountBondObjDto.setPage(page);
        accountBondObjDto.setRow(row);
        accountBondObjDto.setBobjId(bobjId);
        accountBondObjDto.setState(state);
        accountBondObjDto.setObjId( objId );
        return getAccountBondObjBMOImpl.get(accountBondObjDto);
    }
}
