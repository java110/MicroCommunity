package com.java110.fee.listener.config;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.fee.dao.IFeeConfigServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用配置 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFeeConfigBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractFeeConfigBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IFeeConfigServiceDao getFeeConfigServiceDaoImpl();

    /**
     * 刷新 businessFeeConfigInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessFeeConfigInfo
     */
    protected void flushBusinessFeeConfigInfo(Map businessFeeConfigInfo, String statusCd) {
        businessFeeConfigInfo.put("newBId", businessFeeConfigInfo.get("b_id"));
        businessFeeConfigInfo.put("feeTypeCd", businessFeeConfigInfo.get("fee_type_cd"));
        businessFeeConfigInfo.put("computingFormula", businessFeeConfigInfo.get("computing_formula"));
        businessFeeConfigInfo.put("computingFormulaText", businessFeeConfigInfo.get("computing_formula_text"));
        businessFeeConfigInfo.put("additionalAmount", businessFeeConfigInfo.get("additional_amount"));
        businessFeeConfigInfo.put("squarePrice", businessFeeConfigInfo.get("square_price"));
        businessFeeConfigInfo.put("isDefault", businessFeeConfigInfo.get("is_default"));
        businessFeeConfigInfo.put("operate", businessFeeConfigInfo.get("operate"));
        businessFeeConfigInfo.put("configId", businessFeeConfigInfo.get("config_id"));
        businessFeeConfigInfo.put("feeFlag", businessFeeConfigInfo.get("fee_flag"));
        businessFeeConfigInfo.put("feeName", businessFeeConfigInfo.get("fee_name"));
        businessFeeConfigInfo.put("startTime", businessFeeConfigInfo.get("start_time"));
        businessFeeConfigInfo.put("endTime", businessFeeConfigInfo.get("end_time"));
        businessFeeConfigInfo.put("communityId", businessFeeConfigInfo.get("community_id"));
        businessFeeConfigInfo.put("paymentCd", businessFeeConfigInfo.get("payment_cd"));
        businessFeeConfigInfo.put("paymentCycle", businessFeeConfigInfo.get("payment_cycle"));
        businessFeeConfigInfo.put("billType", businessFeeConfigInfo.get("bill_type"));
        businessFeeConfigInfo.put("deductFrom", businessFeeConfigInfo.get("deduct_from"));
        businessFeeConfigInfo.remove("bId");
        businessFeeConfigInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessFeeConfig 费用配置信息
     */
    protected void autoSaveDelBusinessFeeConfig(Business business, JSONObject businessFeeConfig) {
//自动插入DEL
        Map info = new HashMap();
        info.put("configId", businessFeeConfig.getString("configId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentFeeConfigInfos = getFeeConfigServiceDaoImpl().getFeeConfigInfo(info);
        if (currentFeeConfigInfos == null || currentFeeConfigInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentFeeConfigInfo = currentFeeConfigInfos.get(0);

        currentFeeConfigInfo.put("bId", business.getbId());

        currentFeeConfigInfo.put("feeTypeCd", currentFeeConfigInfo.get("fee_type_cd"));
        currentFeeConfigInfo.put("computingFormula", currentFeeConfigInfo.get("computing_formula"));
        currentFeeConfigInfo.put("computingFormulaText", currentFeeConfigInfo.get("computing_formula_text"));
        currentFeeConfigInfo.put("additionalAmount", currentFeeConfigInfo.get("additional_amount"));
        currentFeeConfigInfo.put("squarePrice", currentFeeConfigInfo.get("square_price"));
        currentFeeConfigInfo.put("isDefault", currentFeeConfigInfo.get("is_default"));
        currentFeeConfigInfo.put("operate", currentFeeConfigInfo.get("operate"));
        currentFeeConfigInfo.put("configId", currentFeeConfigInfo.get("config_id"));
        currentFeeConfigInfo.put("feeFlag", currentFeeConfigInfo.get("fee_flag"));
        currentFeeConfigInfo.put("feeName", currentFeeConfigInfo.get("fee_name"));
        currentFeeConfigInfo.put("startTime", currentFeeConfigInfo.get("start_time"));
        currentFeeConfigInfo.put("endTime", currentFeeConfigInfo.get("end_time"));
        currentFeeConfigInfo.put("communityId", currentFeeConfigInfo.get("community_id"));
        currentFeeConfigInfo.put("paymentCd", currentFeeConfigInfo.get("payment_cd"));
        currentFeeConfigInfo.put("paymentCycle", currentFeeConfigInfo.get("payment_cycle"));
        currentFeeConfigInfo.put("billType", currentFeeConfigInfo.get("bill_type"));
        currentFeeConfigInfo.put("deductFrom", currentFeeConfigInfo.get("deduct_from"));


        currentFeeConfigInfo.put("operate", StatusConstant.OPERATE_DEL);
        getFeeConfigServiceDaoImpl().saveBusinessFeeConfigInfo(currentFeeConfigInfo);
        for(Object key : currentFeeConfigInfo.keySet()) {
            if(businessFeeConfig.get(key) == null) {
                businessFeeConfig.put(key.toString(), currentFeeConfigInfo.get(key));
            }
        }
    }


}
