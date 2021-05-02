package com.java110.api.bmo.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.account.IAccountBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountBMOImpl")
public class AccountBMOImpl extends ApiBaseBMO implements IAccountBMO {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAccount(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("acctId", "-1");
        AccountPo accountPo = BeanConvertUtil.covertBean(paramInJson, AccountPo.class);
        super.insert(dataFlowContext, accountPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAccount(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AccountPo accountPo = BeanConvertUtil.covertBean(paramInJson, AccountPo.class);
        super.update(dataFlowContext, accountPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ACCT);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAccount(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AccountPo accountPo = BeanConvertUtil.covertBean(paramInJson, AccountPo.class);
        super.update(dataFlowContext, accountPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ACCT);
    }

}
