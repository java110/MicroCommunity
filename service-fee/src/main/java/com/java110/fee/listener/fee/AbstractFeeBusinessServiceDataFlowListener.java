package com.java110.fee.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.fee.dao.IFeeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFeeBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractFeeBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IFeeServiceDao getFeeServiceDaoImpl();

    /**
     * 刷新 businessFeeInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessFeeInfo
     */
    protected void flushBusinessFeeInfo(Map businessFeeInfo, String statusCd) {
        businessFeeInfo.put("newBId", businessFeeInfo.get("b_id"));
        businessFeeInfo.put("amount", businessFeeInfo.get("amount"));
        businessFeeInfo.put("operate", businessFeeInfo.get("operate"));
        businessFeeInfo.put("incomeObjId", businessFeeInfo.get("income_obj_id"));
        businessFeeInfo.put("feeTypeCd", businessFeeInfo.get("fee_type_cd"));
        businessFeeInfo.put("startTime", businessFeeInfo.get("start_time"));
        businessFeeInfo.put("endTime", businessFeeInfo.get("end_time"));
        businessFeeInfo.put("communityId", businessFeeInfo.get("community_id"));
        businessFeeInfo.put("feeId", businessFeeInfo.get("fee_id"));
        businessFeeInfo.put("userId", businessFeeInfo.get("user_id"));
        businessFeeInfo.put("payerObjId", businessFeeInfo.get("payer_obj_id"));
        businessFeeInfo.put("feeFlag", businessFeeInfo.get("fee_flag"));
        businessFeeInfo.put("state", businessFeeInfo.get("state"));
        businessFeeInfo.put("configId", businessFeeInfo.get("config_id"));
        businessFeeInfo.put("billType", businessFeeInfo.get("bill_type"));
        businessFeeInfo.put("batchId", businessFeeInfo.get("batch_id"));

        businessFeeInfo.remove("bId");
        businessFeeInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessFee 费用信息
     */
    protected void autoSaveDelBusinessFee(Business business, JSONObject businessFee) {
//自动插入DEL
        Map info = new HashMap();
        info.put("feeId", businessFee.getString("feeId"));
        info.put("communityId", businessFee.getString("communityId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentFeeInfos = getFeeServiceDaoImpl().getFeeInfo(info);
        if (currentFeeInfos == null || currentFeeInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentFeeInfo = currentFeeInfos.get(0);

        currentFeeInfo.put("bId", business.getbId());

        currentFeeInfo.put("amount", currentFeeInfo.get("amount"));
        currentFeeInfo.put("operate", currentFeeInfo.get("operate"));
        currentFeeInfo.put("incomeObjId", currentFeeInfo.get("income_obj_id"));
        currentFeeInfo.put("feeTypeCd", currentFeeInfo.get("fee_type_cd"));
        currentFeeInfo.put("startTime", currentFeeInfo.get("start_time"));
        currentFeeInfo.put("endTime", currentFeeInfo.get("end_time"));
        currentFeeInfo.put("communityId", currentFeeInfo.get("community_id"));
        currentFeeInfo.put("feeId", currentFeeInfo.get("fee_id"));
        currentFeeInfo.put("userId", currentFeeInfo.get("user_id"));
        currentFeeInfo.put("payerObjId", currentFeeInfo.get("payer_obj_id"));
        currentFeeInfo.put("feeFlag", currentFeeInfo.get("fee_flag"));
        currentFeeInfo.put("state", currentFeeInfo.get("state"));
        currentFeeInfo.put("configId", currentFeeInfo.get("config_id"));
        currentFeeInfo.put("billType", currentFeeInfo.get("bill_type"));
        currentFeeInfo.put("batchId", currentFeeInfo.get("batch_id"));

        currentFeeInfo.put("operate", StatusConstant.OPERATE_DEL);
        getFeeServiceDaoImpl().saveBusinessFeeInfo(currentFeeInfo);

        for(Object key : currentFeeInfo.keySet()) {
            if(businessFee.get(key) == null) {
                businessFee.put(key.toString(), currentFeeInfo.get(key));
            }
        }
    }


}
