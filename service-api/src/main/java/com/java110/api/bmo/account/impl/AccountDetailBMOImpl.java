package com.java110.api.bmo.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.account.IAccountDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountDetailBMOImpl")
public class AccountDetailBMOImpl extends ApiBaseBMO implements IAccountDetailBMO {

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAccountDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("detailId", "-1");
        AccountDetailPo accountDetailPo = BeanConvertUtil.covertBean(paramInJson, AccountDetailPo.class);
        super.insert(dataFlowContext, accountDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT_DETAIL);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAccountDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AccountDetailPo accountDetailPo = BeanConvertUtil.covertBean(paramInJson, AccountDetailPo.class);
        super.update(dataFlowContext, accountDetailPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ACCT_DETAIL);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAccountDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AccountDetailPo accountDetailPo = BeanConvertUtil.covertBean(paramInJson, AccountDetailPo.class);
        super.update(dataFlowContext, accountDetailPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ACCT_DETAIL);
    }

    @Override
    public void dealAccount(JSONObject paramObj, DataFlowContext dataFlowContext) {
        //账户金额
        double deductionAmount = paramObj.getDouble("deductionAmount"); //抵扣金额
        double redepositAmount = paramObj.getDouble("redepositAmount"); //转存金额

        if (deductionAmount == 0 || redepositAmount == 0) { //抵扣金额
            return;
        }
        String acctId = paramObj.getString("acctId");
        if (StringUtil.isEmpty(acctId)) {
            throw new IllegalArgumentException("账户id为空！");
        }
        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(acctId);
        //查询账户金额
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        Assert.listOnlyOne(accountDtos, "查询账户金额错误！");
        //获取金额
        double amount = Double.parseDouble(accountDtos.get(0).getAmount());

        if (amount < deductionAmount && deductionAmount > 0) {
            throw new IllegalArgumentException("余额不足");
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        if (deductionAmount > 0) {
            //accountDetailPo.setAmount((deductionAmount * -1) + "");
            accountDetailPo.setAmount(deductionAmount  + "");
            accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_OUT);
            accountDetailPo.setRemark("前台缴费扣款");
        } else {
            accountDetailPo.setAmount(redepositAmount + "");
            accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
            accountDetailPo.setRemark("前台缴费预存");
        }
        accountDetailPo.setOrderId("-1");
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setRelAcctId("-1");
        super.insert(dataFlowContext, accountDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT_DETAIL);

    }

}
