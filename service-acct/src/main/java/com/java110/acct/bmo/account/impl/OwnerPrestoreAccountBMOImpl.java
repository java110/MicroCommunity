package com.java110.acct.bmo.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.account.IOwnerPrestoreAccountBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.fee.IAccountReceiptV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.accountReceipt.AccountReceiptPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ownerPrestoreAccountBMOImpl")
public class OwnerPrestoreAccountBMOImpl implements IOwnerPrestoreAccountBMO {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IAccountReceiptV1InnerServiceSMO accountReceiptV1InnerServiceSMOImpl;

    /**
     * @param accountDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> prestore(AccountDetailPo accountDetailPo, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;

        //查询 业主是否有账户
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(accountDetailPo.getObjId());
        accountDto.setObjType(AccountDto.OBJ_TYPE_PERSON);
        accountDto.setPartId(reqJson.getString("communityId"));
        accountDto.setAcctType(reqJson.getString("acctType"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        if (accountDtos == null || accountDtos.size() < 1) {
            accountDto = addAccountDto(reqJson);
            //保存交易明细
            AccountDetailPo accountDetail = BeanConvertUtil.covertBean(accountDetailPo, AccountDetailPo.class);
            accountDetail.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
            accountDetail.setAcctId(accountDto.getAcctId());
            accountDetail.setObjType(AccountDetailDto.ORDER_TYPE_USER);
            accountDetail.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
            if (StringUtil.isEmpty(accountDetail.getDetailId())) {
                accountDetail.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            }
            if (StringUtil.isEmpty(accountDetail.getRelAcctId())) {
                accountDetail.setRelAcctId("-1");
            }
            accountDetailInnerServiceSMOImpl.saveAccountDetails(accountDetail);
        } else {
            accountDto = accountDtos.get(0);
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
            accountDetailPo.setAcctId(accountDto.getAcctId());
            accountDetailPo.setObjType(AccountDetailDto.ORDER_TYPE_USER);
            int flag = accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);
            if (flag < 1) {
                return ResultVo.error("预存失败");
            }
        }
        // todo 记录账户收款单
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "业主不存在");

        AccountReceiptPo accountReceiptPo = new AccountReceiptPo();
        accountReceiptPo.setOwnerId(reqJson.getString("ownerId"));
        accountReceiptPo.setOwnerName(ownerDtos.get(0).getName());
        accountReceiptPo.setLink(ownerDtos.get(0).getLink());
        accountReceiptPo.setArId(GenerateCodeFactory.getGeneratorId("11"));
        accountReceiptPo.setAcctId(accountDto.getAcctId());
        accountReceiptPo.setPrimeRate(reqJson.getString("primeRate"));
        accountReceiptPo.setReceivableAmount(reqJson.getString("amount"));
        accountReceiptPo.setReceivedAmount(reqJson.getString("amount"));
        accountReceiptPo.setRemark(reqJson.getString("remark"));
        accountReceiptPo.setCommunityId(reqJson.getString("communityId"));
        accountReceiptV1InnerServiceSMOImpl.saveAccountReceipt(accountReceiptPo);
        return ResultVo.success();
    }


    private AccountDto addAccountDto(JSONObject reqJson) {
        AccountPo accountPo = new AccountPo();
        accountPo.setAmount(reqJson.getString("amount"));
        accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
        accountPo.setObjId(reqJson.getString("ownerId"));
        accountPo.setObjType(AccountDto.OBJ_TYPE_PERSON);
        accountPo.setAcctType(reqJson.getString("acctType"));
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "业主不存在");
        accountPo.setAcctName(ownerDtos.get(0).getName());
        accountPo.setPartId(reqJson.getString("communityId"));
        accountPo.setLink(ownerDtos.get(0).getLink());
        accountInnerServiceSMOImpl.saveAccount(accountPo);
        return BeanConvertUtil.covertBean(accountPo, AccountDto.class);
    }

}
