package com.java110.store.listener.resourceStoreUseRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.store.dao.IResourceStoreUseRecordServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品使用记录 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractResourceStoreUseRecordBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractResourceStoreUseRecordBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IResourceStoreUseRecordServiceDao getResourceStoreUseRecordServiceDaoImpl();

    /**
     * 刷新 businessResourceStoreUseRecordInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessResourceStoreUseRecordInfo
     */
    protected void flushBusinessResourceStoreUseRecordInfo(Map businessResourceStoreUseRecordInfo, String statusCd) {
        businessResourceStoreUseRecordInfo.put("newBId", businessResourceStoreUseRecordInfo.get("b_id"));
        businessResourceStoreUseRecordInfo.put("unitPrice", businessResourceStoreUseRecordInfo.get("unit_price"));
        businessResourceStoreUseRecordInfo.put("createUserId", businessResourceStoreUseRecordInfo.get("create_user_id"));
        businessResourceStoreUseRecordInfo.put("quantity", businessResourceStoreUseRecordInfo.get("quantity"));
        businessResourceStoreUseRecordInfo.put("rsurId", businessResourceStoreUseRecordInfo.get("rsur_id"));
        businessResourceStoreUseRecordInfo.put("repairId", businessResourceStoreUseRecordInfo.get("repair_id"));
        businessResourceStoreUseRecordInfo.put("createUserName", businessResourceStoreUseRecordInfo.get("create_user_name"));
        businessResourceStoreUseRecordInfo.put("remark", businessResourceStoreUseRecordInfo.get("remark"));
        businessResourceStoreUseRecordInfo.put("storeId", businessResourceStoreUseRecordInfo.get("store_id"));
        businessResourceStoreUseRecordInfo.put("resId", businessResourceStoreUseRecordInfo.get("res_id"));
        businessResourceStoreUseRecordInfo.put("operate", businessResourceStoreUseRecordInfo.get("operate"));
        businessResourceStoreUseRecordInfo.put("communityId", businessResourceStoreUseRecordInfo.get("community_id"));
        businessResourceStoreUseRecordInfo.put("resourceStoreName", businessResourceStoreUseRecordInfo.get("resource_store_name"));
        businessResourceStoreUseRecordInfo.put("state", businessResourceStoreUseRecordInfo.get("state"));
        businessResourceStoreUseRecordInfo.remove("bId");
        businessResourceStoreUseRecordInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessResourceStoreUseRecord 物品使用记录信息
     */
    protected void autoSaveDelBusinessResourceStoreUseRecord(Business business, JSONObject businessResourceStoreUseRecord) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("rsurId", businessResourceStoreUseRecord.getString("rsurId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentResourceStoreUseRecordInfos = getResourceStoreUseRecordServiceDaoImpl().getResourceStoreUseRecordInfo(info);
        if (currentResourceStoreUseRecordInfos == null || currentResourceStoreUseRecordInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentResourceStoreUseRecordInfo = currentResourceStoreUseRecordInfos.get(0);
        currentResourceStoreUseRecordInfo.put("bId", business.getbId());
        currentResourceStoreUseRecordInfo.put("unitPrice", currentResourceStoreUseRecordInfo.get("unit_price"));
        currentResourceStoreUseRecordInfo.put("createUserId", currentResourceStoreUseRecordInfo.get("create_user_id"));
        currentResourceStoreUseRecordInfo.put("quantity", currentResourceStoreUseRecordInfo.get("quantity"));
        currentResourceStoreUseRecordInfo.put("rsurId", currentResourceStoreUseRecordInfo.get("rsur_id"));
        currentResourceStoreUseRecordInfo.put("repairId", currentResourceStoreUseRecordInfo.get("repair_id"));
        currentResourceStoreUseRecordInfo.put("createUserName", currentResourceStoreUseRecordInfo.get("create_user_name"));
        currentResourceStoreUseRecordInfo.put("remark", currentResourceStoreUseRecordInfo.get("remark"));
        currentResourceStoreUseRecordInfo.put("storeId", currentResourceStoreUseRecordInfo.get("store_id"));
        currentResourceStoreUseRecordInfo.put("resId", currentResourceStoreUseRecordInfo.get("res_id"));
        currentResourceStoreUseRecordInfo.put("operate", currentResourceStoreUseRecordInfo.get("operate"));
        currentResourceStoreUseRecordInfo.put("communityId", currentResourceStoreUseRecordInfo.get("community_id"));
        currentResourceStoreUseRecordInfo.put("resourceStoreName", currentResourceStoreUseRecordInfo.get("resource_store_name"));
        currentResourceStoreUseRecordInfo.put("state", currentResourceStoreUseRecordInfo.get("state"));
        currentResourceStoreUseRecordInfo.put("operate", StatusConstant.OPERATE_DEL);
        getResourceStoreUseRecordServiceDaoImpl().saveBusinessResourceStoreUseRecordInfo(currentResourceStoreUseRecordInfo);
        for (Object key : currentResourceStoreUseRecordInfo.keySet()) {
            if (businessResourceStoreUseRecord.get(key) == null) {
                businessResourceStoreUseRecord.put(key.toString(), currentResourceStoreUseRecordInfo.get(key));
            }
        }
    }
}
