package com.java110.fee.listener.detail;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.fee.dao.IFeeDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用明细 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFeeDetailBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractFeeDetailBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IFeeDetailServiceDao getFeeDetailServiceDaoImpl();

    /**
     * 刷新 businessFeeDetailInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessFeeDetailInfo
     */
    protected void flushBusinessFeeDetailInfo(Map businessFeeDetailInfo, String statusCd) {
        businessFeeDetailInfo.put("newBId", businessFeeDetailInfo.get("b_id"));
        businessFeeDetailInfo.put("operate", businessFeeDetailInfo.get("operate"));
        businessFeeDetailInfo.put("primeRate", businessFeeDetailInfo.get("prime_rate"));
        businessFeeDetailInfo.put("detailId", businessFeeDetailInfo.get("detail_id"));
        businessFeeDetailInfo.put("receivableAmount", businessFeeDetailInfo.get("receivable_amount"));
        businessFeeDetailInfo.put("cycles", businessFeeDetailInfo.get("cycles"));
        businessFeeDetailInfo.put("remark", businessFeeDetailInfo.get("remark"));
        businessFeeDetailInfo.put("receivedAmount", businessFeeDetailInfo.get("received_amount"));
        businessFeeDetailInfo.put("payableAmount", businessFeeDetailInfo.get("payable_amount"));
        businessFeeDetailInfo.put("communityId", businessFeeDetailInfo.get("community_id"));
        businessFeeDetailInfo.put("feeId", businessFeeDetailInfo.get("fee_id"));
        businessFeeDetailInfo.put("startTime", businessFeeDetailInfo.get("start_time"));
        businessFeeDetailInfo.put("endTime", businessFeeDetailInfo.get("end_time"));
        businessFeeDetailInfo.remove("bId");
        businessFeeDetailInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessFeeDetail 费用明细信息
     */
    protected void autoSaveDelBusinessFeeDetail(Business business, JSONObject businessFeeDetail) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("detailId", businessFeeDetail.getString("detailId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentFeeDetailInfos = getFeeDetailServiceDaoImpl().getFeeDetailInfo(info);
        if (currentFeeDetailInfos == null || currentFeeDetailInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentFeeDetailInfo = currentFeeDetailInfos.get(0);
        currentFeeDetailInfo.put("bId", business.getbId());
        currentFeeDetailInfo.put("operate", currentFeeDetailInfo.get("operate"));
        currentFeeDetailInfo.put("primeRate", currentFeeDetailInfo.get("prime_rate"));
        currentFeeDetailInfo.put("detailId", currentFeeDetailInfo.get("detail_id"));
        currentFeeDetailInfo.put("receivableAmount", currentFeeDetailInfo.get("receivable_amount"));
        currentFeeDetailInfo.put("cycles", currentFeeDetailInfo.get("cycles"));
        currentFeeDetailInfo.put("remark", currentFeeDetailInfo.get("remark"));
        currentFeeDetailInfo.put("receivedAmount", currentFeeDetailInfo.get("received_amount"));
        currentFeeDetailInfo.put("payableAmount", currentFeeDetailInfo.get("payable_amount"));
        currentFeeDetailInfo.put("communityId", currentFeeDetailInfo.get("community_id"));
        currentFeeDetailInfo.put("feeId", currentFeeDetailInfo.get("fee_id"));
        currentFeeDetailInfo.put("startTime", currentFeeDetailInfo.get("start_time"));
        currentFeeDetailInfo.put("endTime", currentFeeDetailInfo.get("end_time"));
        currentFeeDetailInfo.put("operate", StatusConstant.OPERATE_DEL);
        getFeeDetailServiceDaoImpl().saveBusinessFeeDetailInfo(currentFeeDetailInfo);
        for (Object key : currentFeeDetailInfo.keySet()) {
            if (businessFeeDetail.get(key) == null) {
                businessFeeDetail.put(key.toString(), currentFeeDetailInfo.get(key));
            }
        }
    }
}
