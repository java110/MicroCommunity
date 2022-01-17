package com.java110.fee.listener.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.fee.dao.IReturnPayFeeServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退费表 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractReturnPayFeeBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractReturnPayFeeBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IReturnPayFeeServiceDao getReturnPayFeeServiceDaoImpl();

    /**
     * 刷新 businessReturnPayFeeInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessReturnPayFeeInfo
     */
    protected void flushBusinessReturnPayFeeInfo(Map businessReturnPayFeeInfo, String statusCd) {
        businessReturnPayFeeInfo.put("newBId", businessReturnPayFeeInfo.get("b_id"));
        businessReturnPayFeeInfo.put("reason", businessReturnPayFeeInfo.get("reason"));
        businessReturnPayFeeInfo.put("primeRate", businessReturnPayFeeInfo.get("prime_rate"));
        businessReturnPayFeeInfo.put("feeTypeCd", businessReturnPayFeeInfo.get("fee_type_cd"));
        businessReturnPayFeeInfo.put("payTime", businessReturnPayFeeInfo.get("pay_time"));
        businessReturnPayFeeInfo.put("detailId", businessReturnPayFeeInfo.get("detail_id"));
        businessReturnPayFeeInfo.put("receivableAmount", businessReturnPayFeeInfo.get("receivable_amount"));
        businessReturnPayFeeInfo.put("cycles", businessReturnPayFeeInfo.get("cycles"));
        businessReturnPayFeeInfo.put("remark", businessReturnPayFeeInfo.get("remark"));
        businessReturnPayFeeInfo.put("receivedAmount", businessReturnPayFeeInfo.get("received_amount"));
        businessReturnPayFeeInfo.put("feeId", businessReturnPayFeeInfo.get("fee_id"));
        businessReturnPayFeeInfo.put("returnFeeId", businessReturnPayFeeInfo.get("return_fee_id"));
        businessReturnPayFeeInfo.put("operate", businessReturnPayFeeInfo.get("operate"));
        businessReturnPayFeeInfo.put("configId", businessReturnPayFeeInfo.get("config_id"));
        businessReturnPayFeeInfo.put("state", businessReturnPayFeeInfo.get("state"));
        businessReturnPayFeeInfo.put("communityId", businessReturnPayFeeInfo.get("community_id"));
        businessReturnPayFeeInfo.remove("bId");
        businessReturnPayFeeInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessReturnPayFee 退费表信息
     */
    protected void autoSaveDelBusinessReturnPayFee(Business business, JSONObject businessReturnPayFee) {
//自动插入DEL
        Map info = new HashMap();
        info.put("returnFeeId", businessReturnPayFee.getString("returnFeeId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentReturnPayFeeInfos = getReturnPayFeeServiceDaoImpl().getReturnPayFeeInfo(info);
        if (currentReturnPayFeeInfos == null || currentReturnPayFeeInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentReturnPayFeeInfo = currentReturnPayFeeInfos.get(0);
        currentReturnPayFeeInfo.put("bId", business.getbId());
        currentReturnPayFeeInfo.put("reason", currentReturnPayFeeInfo.get("reason"));
        currentReturnPayFeeInfo.put("primeRate", currentReturnPayFeeInfo.get("prime_rate"));
        currentReturnPayFeeInfo.put("feeTypeCd", currentReturnPayFeeInfo.get("fee_type_cd"));
        currentReturnPayFeeInfo.put("payTime", currentReturnPayFeeInfo.get("pay_time"));
        currentReturnPayFeeInfo.put("detailId", currentReturnPayFeeInfo.get("detail_id"));
        currentReturnPayFeeInfo.put("receivableAmount", currentReturnPayFeeInfo.get("receivable_amount"));
        currentReturnPayFeeInfo.put("cycles", currentReturnPayFeeInfo.get("cycles"));
        currentReturnPayFeeInfo.put("remark", currentReturnPayFeeInfo.get("remark"));
        currentReturnPayFeeInfo.put("receivedAmount", currentReturnPayFeeInfo.get("received_amount"));
        currentReturnPayFeeInfo.put("createTime", currentReturnPayFeeInfo.get("create_time"));
        currentReturnPayFeeInfo.put("feeId", currentReturnPayFeeInfo.get("fee_id"));
        currentReturnPayFeeInfo.put("returnFeeId", currentReturnPayFeeInfo.get("return_fee_id"));
        currentReturnPayFeeInfo.put("operate", currentReturnPayFeeInfo.get("operate"));
        currentReturnPayFeeInfo.put("configId", currentReturnPayFeeInfo.get("config_id"));
        currentReturnPayFeeInfo.put("state", currentReturnPayFeeInfo.get("state"));
        currentReturnPayFeeInfo.put("communityId", currentReturnPayFeeInfo.get("community_id"));


        currentReturnPayFeeInfo.put("operate", StatusConstant.OPERATE_DEL);
        getReturnPayFeeServiceDaoImpl().saveBusinessReturnPayFeeInfo(currentReturnPayFeeInfo);

        for(Object key : currentReturnPayFeeInfo.keySet()) {
            if(businessReturnPayFee.get(key) == null) {
                businessReturnPayFee.put(key.toString(), currentReturnPayFeeInfo.get(key));
            }
        }
    }


}
