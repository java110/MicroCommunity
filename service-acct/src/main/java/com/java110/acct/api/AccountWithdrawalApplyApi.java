package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.accountWithdrawalApply.IDeleteAccountWithdrawalApplyBMO;
import com.java110.acct.bmo.accountWithdrawalApply.IGetAccountWithdrawalApplyBMO;
import com.java110.acct.bmo.accountWithdrawalApply.ISaveAccountWithdrawalApplyBMO;
import com.java110.acct.bmo.accountWithdrawalApply.IUpdateAccountWithdrawalApplyBMO;
import com.java110.dto.account.AccountWithdrawalApplyDto;
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
        Assert.hasKeyAndValue(reqJson, "bankId", "请求报文中未包含bankId");

        AccountWithdrawalApplyPo accountWithdrawalApplyPo = BeanConvertUtil.covertBean(reqJson, AccountWithdrawalApplyPo.class);
        return saveAccountWithdrawalApplyBMOImpl.save(accountWithdrawalApplyPo,userId,reqJson);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /accountWithdrawalApply/upAccountWithdrawalApply
     * @path /app/accountWithdrawalApply/upAccountWithdrawalApply
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/upAccountWithdrawalApply", method = RequestMethod.POST)
    public ResponseEntity<String> updateAccountWithdrawalApply(@RequestBody JSONObject reqJson) {

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
        Assert.hasKeyAndValue(reqJson, "acctId", "账户ID不能为空");

        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");


        AccountWithdrawalApplyPo accountWithdrawalApplyPo = BeanConvertUtil.covertBean(reqJson, AccountWithdrawalApplyPo.class);
        return deleteAccountWithdrawalApplyBMOImpl.delete(accountWithdrawalApplyPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountWithdrawalApply/listAccountWithdrawalApply
     * @path /app/accountWithdrawalApply/listAccountWithdrawalApply
     * @param applyUserName: '',
     *                     applyUserTel: '',
     *                     state: '486'
     * @return
     */
    @RequestMapping(value = "/listAccountWithdrawalApply", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountWithdrawalApply(@RequestParam(value = "page") int page,
                                                              @RequestParam(value = "row") int row,
                                                              @RequestParam(value = "applyUserName",required = false) String applyUserName,
                                                              @RequestParam(value = "bankCode",required = false) String bankCode,
                                                              @RequestParam(value = "applyUserTel",required = false) String applyUserTel,
                                                              @RequestParam(value = "objId",required = false) String objId,
                                                              @RequestParam(value = "state",required = false) String state) {
        AccountWithdrawalApplyDto accountWithdrawalApplyDto = new AccountWithdrawalApplyDto();
        accountWithdrawalApplyDto.setPage(page);
        accountWithdrawalApplyDto.setRow(row);
        accountWithdrawalApplyDto.setApplyUserName(applyUserName);
        accountWithdrawalApplyDto.setApplyUserTel(applyUserTel);
        accountWithdrawalApplyDto.setBankCode(bankCode);
        accountWithdrawalApplyDto.setObjId( objId );
        if(null == state || "".equals( state )){
            state = "";
        }
        accountWithdrawalApplyDto.setState(state);
        return getAccountWithdrawalApplyBMOImpl.get(accountWithdrawalApplyDto);
    }


    @RequestMapping(value = "/listStateWithdrawalApplys", method = RequestMethod.GET)
    public ResponseEntity<String> listStateWithdrawalApplys(@RequestParam(value = "page") int page,
                                                              @RequestParam(value = "row") int row,
                                                              @RequestParam(value = "state") String state) {
        String [] states = state.split( "," );
        return getAccountWithdrawalApplyBMOImpl.listStateWithdrawalApplys( states, page,row);
    }
}
