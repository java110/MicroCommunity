package com.java110.fee.bmo.account.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.fee.bmo.ApiBaseBMO;
import com.java110.fee.bmo.account.IUpdateAccountBMO;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 修改账户余额
 *
 * @author fqz
 * @date 2021-09-09
 */
@Service("updateAccountBMOImpl")
public class UpdateAccountBMOImpl extends ApiBaseBMO implements IUpdateAccountBMO {


    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;


    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    public void update(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AccountPo accountPo = BeanConvertUtil.covertBean(paramInJson, AccountPo.class);
        super.update(dataFlowContext, accountPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ACCT);
    }


    @Override
    public void cashBackAccount(JSONObject paramObj, DataFlowContext dataFlowContext, JSONArray businesses) {
        //判断返现金额是否大于0
        BigDecimal cashBackAmount = new BigDecimal(paramObj.getString("cashBackAmount"));
        if (cashBackAmount != null && cashBackAmount.compareTo(BigDecimal.ZERO) == 1) {
            String roomId = paramObj.getString("roomId");
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(roomId);
            //查询业主房屋关系表
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            Assert.listOnlyOne(ownerRoomRelDtos, "查询业主房屋关系表错误！");
            //获取业主id
            String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
            paramObj.put("ownerId", ownerId);


            //根据业主id去查这个业主的账户余额
            AccountDto accountDto = new AccountDto();
            accountDto.setObjId(ownerId);
            accountDto.setPartId(paramObj.getString("communityId"));
            accountDto.setObjType(AccountDto.OBJ_TYPE_PERSON);
            accountDto.setAcctType(AccountDto.ACCT_TYPE_CASH);
            List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
            if (accountDtos != null && accountDtos.size() != 0) {//存在业主账号
                AccountDetailPo accountDetailPo = new AccountDetailPo();
                accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
                int flag1 = cashBackAmount.compareTo(BigDecimal.ZERO);
                if (flag1 == 1) {
                    accountDetailPo.setAmount(cashBackAmount + "");
                    accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
                    accountDetailPo.setRemark("优惠申请返还");
                }
                accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
                accountDetailPo.setObjType(accountDtos.get(0).getObjType());
                accountDetailPo.setObjId(accountDtos.get(0).getObjId());
                accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
                accountDetailPo.setRelAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

                accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);
            } else {
                accountDto = addAccountDto(paramObj);
                //保存交易明细
                AccountDetailPo accountDetail = new AccountDetailPo();
                accountDetail.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
                accountDetail.setAcctId(accountDto.getAcctId());
                accountDetail.setAmount(cashBackAmount + "");
                accountDetail.setObjType(AccountDetailDto.ORDER_TYPE_USER);
                accountDetail.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
                accountDetail.setObjId(accountDto.getObjId());
                accountDetail.setRemark("优惠申请返还");
                accountDetail.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
                if (StringUtil.isEmpty(accountDetail.getRelAcctId())) {
                    accountDetail.setRelAcctId("-1");
                }
                accountDetailInnerServiceSMOImpl.saveAccountDetails(accountDetail);
            }

        }
    }

    private AccountDto addAccountDto(JSONObject reqJson) {


        AccountPo accountPo = new AccountPo();
        accountPo.setAmount(reqJson.getString("cashBackAmount"));
        accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
        accountPo.setObjId(reqJson.getString("ownerId"));
        accountPo.setObjType(AccountDto.OBJ_TYPE_PERSON);
        accountPo.setAcctType(AccountDto.ACCT_TYPE_CASH);
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        accountPo.setAcctName(ownerDtos.get(0).getName());
        accountPo.setPartId(reqJson.getString("communityId"));
        accountPo.setLink(ownerDtos.get(0).getLink());
        accountInnerServiceSMOImpl.saveAccount(accountPo);
        return BeanConvertUtil.covertBean(accountPo, AccountDto.class);
    }
}
