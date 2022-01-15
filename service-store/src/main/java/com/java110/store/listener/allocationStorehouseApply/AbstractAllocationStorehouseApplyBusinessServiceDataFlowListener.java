package com.java110.store.listener.allocationStorehouseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.store.dao.IAllocationStorehouseApplyServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调拨申请 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAllocationStorehouseApplyBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractAllocationStorehouseApplyBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAllocationStorehouseApplyServiceDao getAllocationStorehouseApplyServiceDaoImpl();

    /**
     * 刷新 businessAllocationStorehouseApplyInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAllocationStorehouseApplyInfo
     */
    protected void flushBusinessAllocationStorehouseApplyInfo(Map businessAllocationStorehouseApplyInfo, String statusCd) {
        businessAllocationStorehouseApplyInfo.put("newBId", businessAllocationStorehouseApplyInfo.get("b_id"));
        businessAllocationStorehouseApplyInfo.put("applyId", businessAllocationStorehouseApplyInfo.get("apply_id"));
        businessAllocationStorehouseApplyInfo.put("startUserId", businessAllocationStorehouseApplyInfo.get("start_user_id"));
        businessAllocationStorehouseApplyInfo.put("operate", businessAllocationStorehouseApplyInfo.get("operate"));
        businessAllocationStorehouseApplyInfo.put("startUserName", businessAllocationStorehouseApplyInfo.get("start_user_name"));
        businessAllocationStorehouseApplyInfo.put("applyCount", businessAllocationStorehouseApplyInfo.get("apply_count"));
        businessAllocationStorehouseApplyInfo.put("communityId", businessAllocationStorehouseApplyInfo.get("community_id"));
        businessAllocationStorehouseApplyInfo.put("remark", businessAllocationStorehouseApplyInfo.get("remark"));
        businessAllocationStorehouseApplyInfo.put("state", businessAllocationStorehouseApplyInfo.get("state"));
        businessAllocationStorehouseApplyInfo.put("storeId", businessAllocationStorehouseApplyInfo.get("store_id"));
        businessAllocationStorehouseApplyInfo.put("applyType", businessAllocationStorehouseApplyInfo.get("apply_type"));
        businessAllocationStorehouseApplyInfo.remove("bId");
        businessAllocationStorehouseApplyInfo.put("statusCd", statusCd);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAllocationStorehouseApply 调拨申请信息
     */
    protected void autoSaveDelBusinessAllocationStorehouseApply(Business business, JSONObject businessAllocationStorehouseApply) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("applyId", businessAllocationStorehouseApply.getString("applyId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAllocationStorehouseApplyInfos = getAllocationStorehouseApplyServiceDaoImpl().getAllocationStorehouseApplyInfo(info);
        if (currentAllocationStorehouseApplyInfos == null || currentAllocationStorehouseApplyInfos.size() < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        for (int i = 0; i < currentAllocationStorehouseApplyInfos.size(); i++) {
            Map currentAllocationStorehouseApplyInfo = currentAllocationStorehouseApplyInfos.get(i);
            currentAllocationStorehouseApplyInfo.put("bId", business.getbId());
            currentAllocationStorehouseApplyInfo.put("applyId", currentAllocationStorehouseApplyInfo.get("apply_id"));
            currentAllocationStorehouseApplyInfo.put("startUserId", currentAllocationStorehouseApplyInfo.get("start_user_id"));
            currentAllocationStorehouseApplyInfo.put("operate", currentAllocationStorehouseApplyInfo.get("operate"));
            currentAllocationStorehouseApplyInfo.put("startUserName", currentAllocationStorehouseApplyInfo.get("start_user_name"));
            currentAllocationStorehouseApplyInfo.put("applyCount", currentAllocationStorehouseApplyInfo.get("apply_count"));
            currentAllocationStorehouseApplyInfo.put("remark", currentAllocationStorehouseApplyInfo.get("remark"));
            currentAllocationStorehouseApplyInfo.put("communityId", currentAllocationStorehouseApplyInfo.get("community_id"));
            currentAllocationStorehouseApplyInfo.put("state", currentAllocationStorehouseApplyInfo.get("state"));
            currentAllocationStorehouseApplyInfo.put("storeId", currentAllocationStorehouseApplyInfo.get("store_id"));
            currentAllocationStorehouseApplyInfo.put("applyType", currentAllocationStorehouseApplyInfo.get("apply_type"));
            currentAllocationStorehouseApplyInfo.put("operate", StatusConstant.OPERATE_DEL);
            getAllocationStorehouseApplyServiceDaoImpl().saveBusinessAllocationStorehouseApplyInfo(currentAllocationStorehouseApplyInfo);
            for (Object key : currentAllocationStorehouseApplyInfo.keySet()) {
                if (businessAllocationStorehouseApply.get(key) == null) {
                    businessAllocationStorehouseApply.put(key.toString(), currentAllocationStorehouseApplyInfo.get(key));
                }
            }
        }
    }
}
