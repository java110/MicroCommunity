package com.java110.store.listener.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.store.dao.IResourceStoreTypeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品类型 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractResourceStoreTypeBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractResourceStoreTypeBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IResourceStoreTypeServiceDao getResourceStoreTypeServiceDaoImpl();

    /**
     * 刷新 businessResourceStoreTypeInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessResourceStoreTypeInfo
     */
    protected void flushBusinessResourceStoreTypeInfo(Map businessResourceStoreTypeInfo, String statusCd) {
        businessResourceStoreTypeInfo.put("newBId", businessResourceStoreTypeInfo.get("b_id"));
        businessResourceStoreTypeInfo.put("rstId", businessResourceStoreTypeInfo.get("rst_id"));
        businessResourceStoreTypeInfo.put("name", businessResourceStoreTypeInfo.get("name"));
        businessResourceStoreTypeInfo.put("description", businessResourceStoreTypeInfo.get("description"));
        businessResourceStoreTypeInfo.put("storeId", businessResourceStoreTypeInfo.get("store_id"));
        businessResourceStoreTypeInfo.put("parentId", businessResourceStoreTypeInfo.get("parent_id"));
        businessResourceStoreTypeInfo.remove("bId");
        businessResourceStoreTypeInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessResourceStoreType 物品类型信息
     */
    protected void autoSaveDelBusinessResourceStoreType(Business business, JSONObject businessResourceStoreType) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("rstId", businessResourceStoreType.getString("rstId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentResourceStoreTypeInfos = getResourceStoreTypeServiceDaoImpl().getResourceStoreTypeInfo(info);
        if (currentResourceStoreTypeInfos == null || currentResourceStoreTypeInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentResourceStoreTypeInfo = currentResourceStoreTypeInfos.get(0);

        currentResourceStoreTypeInfo.put("bId", business.getbId());

        currentResourceStoreTypeInfo.put("rstId", currentResourceStoreTypeInfo.get("rst_id"));
        currentResourceStoreTypeInfo.put("name", currentResourceStoreTypeInfo.get("name"));
        currentResourceStoreTypeInfo.put("description", currentResourceStoreTypeInfo.get("description"));
        currentResourceStoreTypeInfo.put("storeId", currentResourceStoreTypeInfo.get("store_id"));
        currentResourceStoreTypeInfo.put("parentId", currentResourceStoreTypeInfo.get("parent_id"));
        currentResourceStoreTypeInfo.put("operate", StatusConstant.OPERATE_DEL);
        getResourceStoreTypeServiceDaoImpl().saveBusinessResourceStoreTypeInfo(currentResourceStoreTypeInfo);
        for (Object key : currentResourceStoreTypeInfo.keySet()) {
            if (businessResourceStoreType.get(key) == null) {
                businessResourceStoreType.put(key.toString(), currentResourceStoreTypeInfo.get(key));
            }
        }
    }


}
