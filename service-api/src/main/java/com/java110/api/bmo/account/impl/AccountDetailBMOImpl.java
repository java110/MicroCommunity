package com.java110.api.bmo.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.account.IAccountDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountDetailBMOImpl")
public class AccountDetailBMOImpl extends ApiBaseBMO implements IAccountDetailBMO {

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

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

}
