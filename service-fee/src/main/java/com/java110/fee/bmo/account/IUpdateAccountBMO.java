package com.java110.fee.bmo.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.fee.bmo.IApiBaseBMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;

import java.math.BigDecimal;
import java.util.List;

public interface IUpdateAccountBMO extends IApiBaseBMO {

    /**
     * 修改账户余额
     * add by fqz
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return
     */
    void update(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 账户处理
     * @param paramObj
     * @param dataFlowContext
     */
    void cashBackAccount(JSONObject paramObj, DataFlowContext dataFlowContext, JSONArray businesses);
}
