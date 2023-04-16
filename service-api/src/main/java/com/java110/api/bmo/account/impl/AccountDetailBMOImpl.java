package com.java110.api.bmo.account.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.account.IAccountDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public void dealAccount(JSONObject paramObj, DataFlowContext dataFlowContext, JSONArray businesses) {
        //判断选择的账号
        JSONArray jsonArray = paramObj.getJSONArray("selectUserAccount");
        if (jsonArray != null) {
            //应收款 totalFeePrice
            BigDecimal totalFeePrice = new BigDecimal(paramObj.getString("totalFeePrice")); //应收款
            //实收款 receivedAmount
            BigDecimal receivedAmount = new BigDecimal(paramObj.getString("receivedAmount")); //实收款（扣款金额）

            BigDecimal redepositAmount = new BigDecimal("0.00");//抵扣金额
            for (int columnIndex = 0; columnIndex < jsonArray.size(); columnIndex++) {
                JSONObject param = jsonArray.getJSONObject(columnIndex);
                //账户金额
                BigDecimal amount = new BigDecimal(param.getString("amount")); //账户金额
                int flag = amount.compareTo(receivedAmount);
                if (flag == -1) {//账户金额小于实收款
                    receivedAmount = receivedAmount.subtract(amount);
                    redepositAmount = amount;//抵扣金额
                } else {
                    redepositAmount = receivedAmount;//抵扣金额
                }
                //剩余金额
                amount.compareTo(receivedAmount);
                String acctId = param.getString("acctId");
                if (StringUtil.isEmpty(acctId)) {
                    throw new IllegalArgumentException("账户id为空！");
                }
                AccountDto accountDto = new AccountDto();
                accountDto.setAcctId(acctId);
                //查询账户金额
                List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
                Assert.listOnlyOne(accountDtos, "查询账户金额错误！");
                if (accountDtos != null && accountDtos.size() > 0) {
                    AccountDto accountDto1 = accountDtos.get(0);
                    BigDecimal accountDto1Amount = new BigDecimal(accountDto1.getAmount());
                    if (accountDto1Amount.compareTo(redepositAmount) == -1) {
                        throw new UnsupportedOperationException("账户金额抵扣不足，请您确认账户金额！");
                    }
                }


                AccountDetailPo accountDetailPo = new AccountDetailPo();
                accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
                int flag1 = redepositAmount.compareTo(BigDecimal.ZERO);
                if (flag1 == 1) {
                    accountDetailPo.setAmount(redepositAmount + "");
                    accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_OUT);
                    accountDetailPo.setRemark("前台缴费扣款");
                }
                accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
                accountDetailPo.setObjType(accountDtos.get(0).getObjType());
                accountDetailPo.setObjId(accountDtos.get(0).getObjId());
                accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
                accountDetailPo.setRelAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

                JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
                business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT_DETAIL);
                business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
                business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);

                business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(accountDetailPo.getClass().getSimpleName(), BeanConvertUtil.beanCovertMap(accountDetailPo));
                businesses.add(business);
            }

        }


    }

}
