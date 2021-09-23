package com.java110.fee.bmo.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.fee.bmo.ApiBaseBMO;
import com.java110.fee.bmo.account.ISaveAccountBMO;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

@Service("saveAccountBMOImpl")
public class SaveAccountBMOImpl extends ApiBaseBMO implements ISaveAccountBMO {

    /**
     * 添加账户余额
     *
     * @author fqz
     * @date 2021-09-09
     */
    @Override
    public void save(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AccountPo accountPo = BeanConvertUtil.covertBean(paramInJson, AccountPo.class);
        super.insert(dataFlowContext, accountPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT);
    }

    /**
     * 添加账户明细
     *
     * @author fqz
     * @date 2021-09-09
     */
    @Override
    public void saveDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AccountDetailPo accountDetailPo = BeanConvertUtil.covertBean(paramInJson, AccountDetailPo.class);
        super.insert(dataFlowContext, accountDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACCT_DETAIL);
    }

}
