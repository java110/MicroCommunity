package com.java110.store.listener.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.IPurchaseApplyServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购申请 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractPurchaseApplyBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractPurchaseApplyBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPurchaseApplyServiceDao getPurchaseApplyServiceDaoImpl();

    /**
     * 刷新 businessPurchaseApplyInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPurchaseApplyInfo
     */
    protected void flushBusinessPurchaseApplyInfo(Map businessPurchaseApplyInfo, String statusCd) {
        businessPurchaseApplyInfo.put("newBId", businessPurchaseApplyInfo.get("b_id"));
        businessPurchaseApplyInfo.put("operate", businessPurchaseApplyInfo.get("operate"));
        businessPurchaseApplyInfo.put("applyDetailId", businessPurchaseApplyInfo.get("apply_detail_id"));
        businessPurchaseApplyInfo.put("resOrderType", businessPurchaseApplyInfo.get("res_order_type"));
        businessPurchaseApplyInfo.put("description", businessPurchaseApplyInfo.get("description"));
        businessPurchaseApplyInfo.put("applyOrderId", businessPurchaseApplyInfo.get("apply_order_id"));
        businessPurchaseApplyInfo.put("state", businessPurchaseApplyInfo.get("state"));
        businessPurchaseApplyInfo.put("storeId", businessPurchaseApplyInfo.get("store_id"));
        businessPurchaseApplyInfo.put("entryPerson", businessPurchaseApplyInfo.get("entry_person"));
        businessPurchaseApplyInfo.put("userId", businessPurchaseApplyInfo.get("user_id"));
        businessPurchaseApplyInfo.put("endUserName", businessPurchaseApplyInfo.get("end_user_name"));
        businessPurchaseApplyInfo.put("endUserTel", businessPurchaseApplyInfo.get("end_user_tel"));
        businessPurchaseApplyInfo.put("communityId", businessPurchaseApplyInfo.get("community_id"));
        businessPurchaseApplyInfo.remove("bId");
        businessPurchaseApplyInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPurchaseApply 采购申请信息
     */
    protected void autoSaveDelBusinessPurchaseApply(Business business, JSONObject businessPurchaseApply) {
//自动插入DEL
        Map info = new HashMap();
        info.put("applyOrderId", businessPurchaseApply.getString("applyOrderId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentPurchaseApplyInfos = getPurchaseApplyServiceDaoImpl().getPurchaseApplyInfo(info);
        if (currentPurchaseApplyInfos == null || currentPurchaseApplyInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentPurchaseApplyInfo = currentPurchaseApplyInfos.get(0);

        currentPurchaseApplyInfo.put("bId", business.getbId());
        currentPurchaseApplyInfo.put("operate", currentPurchaseApplyInfo.get("operate"));
        currentPurchaseApplyInfo.put("applyDstaffetailId", currentPurchaseApplyInfo.get("applyDetailId"));
        currentPurchaseApplyInfo.put("resOrderType", currentPurchaseApplyInfo.get("resOrderType"));
        currentPurchaseApplyInfo.put("description", currentPurchaseApplyInfo.get("description"));
        currentPurchaseApplyInfo.put("applyOrderId", currentPurchaseApplyInfo.get("applyOrderId"));
        currentPurchaseApplyInfo.put("state", currentPurchaseApplyInfo.get("state"));
        currentPurchaseApplyInfo.put("storeId", currentPurchaseApplyInfo.get("storeId"));
        currentPurchaseApplyInfo.put("userId", currentPurchaseApplyInfo.get("userId"));
        currentPurchaseApplyInfo.put("endUserName", currentPurchaseApplyInfo.get("end_user_name"));
        currentPurchaseApplyInfo.put("endUserTel", currentPurchaseApplyInfo.get("end_user_tel"));
        currentPurchaseApplyInfo.put("userName", currentPurchaseApplyInfo.get("userName"));
        currentPurchaseApplyInfo.put("createTime", currentPurchaseApplyInfo.get("createTime"));
        currentPurchaseApplyInfo.put("communityId", currentPurchaseApplyInfo.get("community_id"));
        currentPurchaseApplyInfo.put("operate", StatusConstant.OPERATE_DEL);
        getPurchaseApplyServiceDaoImpl().saveBusinessPurchaseApplyInfo(currentPurchaseApplyInfo);

        for (Object key : currentPurchaseApplyInfo.keySet()) {
            if (businessPurchaseApply.get(key) == null) {
                businessPurchaseApply.put(key.toString(), currentPurchaseApplyInfo.get(key));
            }
        }
    }


}
