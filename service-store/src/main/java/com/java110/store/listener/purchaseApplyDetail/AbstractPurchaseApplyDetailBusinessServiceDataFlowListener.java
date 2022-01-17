package com.java110.store.listener.purchaseApplyDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.IPurchaseApplyDetailServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单明细 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractPurchaseApplyDetailBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractPurchaseApplyDetailBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPurchaseApplyDetailServiceDao getPurchaseApplyDetailServiceDaoImpl();

    /**
     * 刷新 businessPurchaseApplyDetailInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPurchaseApplyDetailInfo
     */
    protected void flushBusinessPurchaseApplyDetailInfo(Map businessPurchaseApplyDetailInfo, String statusCd) {
        businessPurchaseApplyDetailInfo.put("newBId", businessPurchaseApplyDetailInfo.get("b_id"));
        businessPurchaseApplyDetailInfo.put("operate", businessPurchaseApplyDetailInfo.get("operate"));
        businessPurchaseApplyDetailInfo.put("applyOrderId", businessPurchaseApplyDetailInfo.get("apply_order_id"));
        businessPurchaseApplyDetailInfo.put("id", businessPurchaseApplyDetailInfo.get("id"));
        businessPurchaseApplyDetailInfo.put("resId", businessPurchaseApplyDetailInfo.get("res_id"));
        businessPurchaseApplyDetailInfo.put("quantity", businessPurchaseApplyDetailInfo.get("quantity"));
        businessPurchaseApplyDetailInfo.remove("bId");
        businessPurchaseApplyDetailInfo.put("statusCd", statusCd);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPurchaseApplyDetail 订单明细信息
     */
    protected void autoSaveDelBusinessPurchaseApplyDetail(Business business, JSONObject businessPurchaseApplyDetail) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("applyOrderId", businessPurchaseApplyDetail.getString("applyOrderId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentPurchaseApplyDetailInfos = getPurchaseApplyDetailServiceDaoImpl().getPurchaseApplyDetailInfo(info);
        if (currentPurchaseApplyDetailInfos == null || currentPurchaseApplyDetailInfos.size() < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        for (int i = 0; i < currentPurchaseApplyDetailInfos.size(); i++) {
            Map currentPurchaseApplyDetailInfo = currentPurchaseApplyDetailInfos.get(i);
            currentPurchaseApplyDetailInfo.put("bId", business.getbId());
            currentPurchaseApplyDetailInfo.put("operate", currentPurchaseApplyDetailInfo.get("operate"));
            currentPurchaseApplyDetailInfo.put("applyOrderId", currentPurchaseApplyDetailInfo.get("applyOrderId"));
            currentPurchaseApplyDetailInfo.put("id", currentPurchaseApplyDetailInfo.get("id"));
            currentPurchaseApplyDetailInfo.put("resId", currentPurchaseApplyDetailInfo.get("resId"));
            currentPurchaseApplyDetailInfo.put("remark", currentPurchaseApplyDetailInfo.get("remark"));
            currentPurchaseApplyDetailInfo.put("quantity", currentPurchaseApplyDetailInfo.get("quantity"));
            currentPurchaseApplyDetailInfo.put("operate", StatusConstant.OPERATE_DEL);
            getPurchaseApplyDetailServiceDaoImpl().saveBusinessPurchaseApplyDetailInfo(currentPurchaseApplyDetailInfo);
            for (Object key : currentPurchaseApplyDetailInfo.keySet()) {
                if (businessPurchaseApplyDetail.get(key) == null) {
                    businessPurchaseApplyDetail.put(key.toString(), currentPurchaseApplyDetailInfo.get(key));
                }
            }
        }
    }
}
