package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.acct.bmo.account.IOwnerPrestoreAccountBMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AccountApi
 * @Description TODO
 * @Author wuxw
 * @Date 2021/5/4 12:44
 * @Version 1.0
 * add by wuxw 2021/5/4
 **/

@RestController
@RequestMapping(value = "/account")
public class AccountApi {

    @Autowired
    private IGetAccountBMO getAccountBMOImpl;

    @Autowired
    private IOwnerPrestoreAccountBMO ownerPrestoreAccountBMOImpl;

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /account/queryAccount
     * @path /app/account/queryAccount
     */
    @RequestMapping(value = "/queryAccount", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccount(@RequestHeader(value = "store-id", required = false) String storeId,
                                               @RequestParam(value = "page") int page,
                                               @RequestParam(value = "row") int row) {
        AccountDto accountDto = new AccountDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        accountDto.setObjId(storeId);
        return getAccountBMOImpl.get(accountDto);
    }

    /**
     * 查询业主账户
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /account/queryOwnerAccount
     * @path /app/account/queryOwnerAccount
     */
    @RequestMapping(value = "/queryOwnerAccount", method = RequestMethod.GET)
    public ResponseEntity<String> queryOwnerAccount(
            @RequestParam(value = "communityId") String communityId,
            @RequestParam(value = "ownerId",required = false) String ownerId,
            @RequestParam(value = "ownerName",required = false) String ownerName,
            @RequestParam(value = "tel",required = false) String tel,
            @RequestParam(value = "idCard",required = false) String idCard,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        AccountDto accountDto = new AccountDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        accountDto.setObjId(ownerId);
        accountDto.setObjType(AccountDto.OBJ_TYPE_PERSON);
        accountDto.setAcctName(ownerName);
        accountDto.setPartId(communityId);
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerId);
        ownerDto.setCommunityId(communityId);
        ownerDto.setLink(tel);
        ownerDto.setIdCard(idCard);
        return getAccountBMOImpl.queryOwnerAccount(accountDto,ownerDto);
    }

    /**
     * 查询账户明细
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /account/queryAccountDetail
     * @path /app/account/queryAccountDetail
     */
    @RequestMapping(value = "/queryAccountDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountDetail(@RequestHeader(value = "store-id", required = false) String storeId,
                                                     @RequestParam(value = "acctId", required = false) String acctId,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) {
        AccountDetailDto accountDto = new AccountDetailDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        accountDto.setObjId(storeId);
        accountDto.setAcctId(acctId);
        return getAccountBMOImpl.getDetail(accountDto);
    }

    /**
     * 业主账户预存
     *
     * @param reqJson 小区ID
     * @return
     * @serviceCode /account/ownerPrestoreAccount
     * @path /app/account/ownerPrestoreAccount
     */
    @RequestMapping(value = "/ownerPrestoreAccount", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "ownerId", "业主不能为空");
        Assert.hasKeyAndValue(reqJson, "amount", "金额不能为空");

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setRemark(reqJson.getString("remark"));
        accountDetailPo.setObjId(reqJson.getString("ownerId"));
        accountDetailPo.setAmount(reqJson.getString("amount"));
        return ownerPrestoreAccountBMOImpl.prestore(accountDetailPo,reqJson);
    }
}
