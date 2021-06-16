package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.accountWithdrawalApply.IDeleteAccountWithdrawalApplyBMO;
import com.java110.acct.bmo.accountWithdrawalApply.IGetAccountWithdrawalApplyBMO;
import com.java110.acct.bmo.accountWithdrawalApply.ISaveAccountWithdrawalApplyBMO;
import com.java110.acct.bmo.accountWithdrawalApply.IUpdateAccountWithdrawalApplyBMO;
import com.java110.dto.accountWithdrawalApply.AccountWithdrawalApplyDto;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/accountWithdrawalApply")
public class AccountWithdrawalApplyApi {

    @Autowired
    private ISaveAccountWithdrawalApplyBMO saveAccountWithdrawalApplyBMOImpl;
    @Autowired
    private IUpdateAccountWithdrawalApplyBMO updateAccountWithdrawalApplyBMOImpl;
    @Autowired
    private IDeleteAccountWithdrawalApplyBMO deleteAccountWithdrawalApplyBMOImpl;

    @Autowired
    private IGetAccountWithdrawalApplyBMO getAccountWithdrawalApplyBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /accountWithdrawalApply/saveAccountWithdrawalApply
     * @path /app/accountWithdrawalApply/saveAccountWithdrawalApply
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveAccountWithdrawalApply", method = RequestMethod.POST)
    public ResponseEntity<String> saveAccountWithdrawalApply(@RequestBody JSONObject reqJson,
                                                             @RequestHeader(value="user-id") String userId ) {

        Assert.hasKeyAndValue(reqJson, "acctId", "请求报文中未包含acctId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");

        AccountWithdrawalApplyPo accountWithdrawalApplyPo = BeanConvertUtil.covertBean(reqJson, AccountWithdrawalApplyPo.class);
        return saveAccountWithdrawalApplyBMOImpl.save(accountWithdrawalApplyPo,userId);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /accountWithdrawalApply/updateAccountWithdrawalApply
     * @path /app/accountWithdrawalApply/updateAccountWithdrawalApply
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateAccountWithdrawalApply", method = RequestMethod.POST)
    public ResponseEntity<String> updateAccountWithdrawalApply(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "acctId", "请求报文中未包含acctId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "applyUserId", "请求报文中未包含applyUserId");
        Assert.hasKeyAndValue(reqJson, "applyUserName", "请求报文中未包含applyUserName");
        Assert.hasKeyAndValue(reqJson, "applyUserTel", "请求报文中未包含applyUserTel");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");


        AccountWithdrawalApplyPo accountWithdrawalApplyPo = BeanConvertUtil.covertBean(reqJson, AccountWithdrawalApplyPo.class);
        return updateAccountWithdrawalApplyBMOImpl.update(accountWithdrawalApplyPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountWithdrawalApply/deleteAccountWithdrawalApply
     * @path /app/accountWithdrawalApply/deleteAccountWithdrawalApply
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteAccountWithdrawalApply", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAccountWithdrawalApply(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");


        AccountWithdrawalApplyPo accountWithdrawalApplyPo = BeanConvertUtil.covertBean(reqJson, AccountWithdrawalApplyPo.class);
        return deleteAccountWithdrawalApplyBMOImpl.delete(accountWithdrawalApplyPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountWithdrawalApply/queryAccountWithdrawalApply
     * @path /app/accountWithdrawalApply/queryAccountWithdrawalApply
     * @param
     * @return
     */
    @RequestMapping(value = "/queryAccountWithdrawalApply", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountWithdrawalApply(@RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        AccountWithdrawalApplyDto accountWithdrawalApplyDto = new AccountWithdrawalApplyDto();
        accountWithdrawalApplyDto.setPage(page);
        accountWithdrawalApplyDto.setRow(row);
        return getAccountWithdrawalApplyBMOImpl.get(accountWithdrawalApplyDto);
    }
}
