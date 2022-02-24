package com.java110.store.listener.resourceStoreSpecification;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.store.dao.IResourceStoreSpecificationServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品规格 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractResourceStoreSpecificationBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractResourceStoreSpecificationBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IResourceStoreSpecificationServiceDao getResourceStoreSpecificationServiceDaoImpl();

    /**
     * 刷新 businessResourceStoreSpecificationInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessResourceStoreSpecificationInfo
     */
    protected void flushBusinessResourceStoreSpecificationInfo(Map businessResourceStoreSpecificationInfo, String statusCd) {
        businessResourceStoreSpecificationInfo.put("newBId", businessResourceStoreSpecificationInfo.get("b_id"));
        businessResourceStoreSpecificationInfo.put("rssId", businessResourceStoreSpecificationInfo.get("rss_id"));
        businessResourceStoreSpecificationInfo.put("rstId", businessResourceStoreSpecificationInfo.get("rst_id"));
        businessResourceStoreSpecificationInfo.put("parentRstId", businessResourceStoreSpecificationInfo.get("parent_rst_id"));
        businessResourceStoreSpecificationInfo.put("operate", businessResourceStoreSpecificationInfo.get("operate"));
        businessResourceStoreSpecificationInfo.put("specName", businessResourceStoreSpecificationInfo.get("spec_name"));
        businessResourceStoreSpecificationInfo.put("description", businessResourceStoreSpecificationInfo.get("description"));
        businessResourceStoreSpecificationInfo.put("storeId", businessResourceStoreSpecificationInfo.get("store_id"));
        businessResourceStoreSpecificationInfo.remove("bId");
        businessResourceStoreSpecificationInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessResourceStoreSpecification 物品规格信息
     */
    protected void autoSaveDelBusinessResourceStoreSpecification(Business business, JSONObject businessResourceStoreSpecification) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("rssId", businessResourceStoreSpecification.getString("rssId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentResourceStoreSpecificationInfos = getResourceStoreSpecificationServiceDaoImpl().getResourceStoreSpecificationInfo(info);
        if (currentResourceStoreSpecificationInfos == null || currentResourceStoreSpecificationInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentResourceStoreSpecificationInfo = currentResourceStoreSpecificationInfos.get(0);

        currentResourceStoreSpecificationInfo.put("bId", business.getbId());

        currentResourceStoreSpecificationInfo.put("rssId", currentResourceStoreSpecificationInfo.get("rss_id"));
        currentResourceStoreSpecificationInfo.put("rstId", currentResourceStoreSpecificationInfo.get("rst_id"));
        currentResourceStoreSpecificationInfo.put("parentRstId", currentResourceStoreSpecificationInfo.get("parent_rst_id"));
        currentResourceStoreSpecificationInfo.put("operate", currentResourceStoreSpecificationInfo.get("operate"));
        currentResourceStoreSpecificationInfo.put("specName", currentResourceStoreSpecificationInfo.get("spec_name"));
        currentResourceStoreSpecificationInfo.put("description", currentResourceStoreSpecificationInfo.get("description"));
        currentResourceStoreSpecificationInfo.put("storeId", currentResourceStoreSpecificationInfo.get("store_id"));


        currentResourceStoreSpecificationInfo.put("operate", StatusConstant.OPERATE_DEL);
        getResourceStoreSpecificationServiceDaoImpl().saveBusinessResourceStoreSpecificationInfo(currentResourceStoreSpecificationInfo);
        for (Object key : currentResourceStoreSpecificationInfo.keySet()) {
            if (businessResourceStoreSpecification.get(key) == null) {
                businessResourceStoreSpecification.put(key.toString(), currentResourceStoreSpecificationInfo.get(key));
            }
        }
    }


}
