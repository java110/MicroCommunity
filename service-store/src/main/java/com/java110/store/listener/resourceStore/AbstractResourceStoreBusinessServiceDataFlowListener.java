package com.java110.store.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.IResourceStoreServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractResourceStoreBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractResourceStoreBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IResourceStoreServiceDao getResourceStoreServiceDaoImpl();

    /**
     * 刷新 businessResourceStoreInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessResourceStoreInfo
     */
    protected void flushBusinessResourceStoreInfo(Map businessResourceStoreInfo, String statusCd) {
        businessResourceStoreInfo.put("newBId", businessResourceStoreInfo.get("b_id"));
        businessResourceStoreInfo.put("resName", businessResourceStoreInfo.get("res_name"));
        businessResourceStoreInfo.put("operate", businessResourceStoreInfo.get("operate"));
        businessResourceStoreInfo.put("price", businessResourceStoreInfo.get("price"));
        businessResourceStoreInfo.put("resCode", businessResourceStoreInfo.get("res_code"));
        businessResourceStoreInfo.put("description", businessResourceStoreInfo.get("description"));
        businessResourceStoreInfo.put("storeId", businessResourceStoreInfo.get("store_id"));
        businessResourceStoreInfo.put("stock", businessResourceStoreInfo.get("stock"));
        businessResourceStoreInfo.put("resId", businessResourceStoreInfo.get("res_id"));
        businessResourceStoreInfo.put("unitCode", businessResourceStoreInfo.get("unit_code"));
        businessResourceStoreInfo.put("remark", businessResourceStoreInfo.get("remark"));
        businessResourceStoreInfo.put("outLowPrice", businessResourceStoreInfo.get("out_low_price"));
        businessResourceStoreInfo.put("outHighPrice", businessResourceStoreInfo.get("out_high_price"));
        businessResourceStoreInfo.put("shId", businessResourceStoreInfo.get("sh_id"));
        businessResourceStoreInfo.put("warningStock", businessResourceStoreInfo.get("warning_stock"));
        businessResourceStoreInfo.put("rstId", businessResourceStoreInfo.get("rst_id"));
        businessResourceStoreInfo.put("rssId", businessResourceStoreInfo.get("rss_id"));
        businessResourceStoreInfo.put("miniUnitCode", businessResourceStoreInfo.get("mini_unit_code"));
        businessResourceStoreInfo.put("miniUnitStock", businessResourceStoreInfo.get("mini_unit_stock"));
        businessResourceStoreInfo.put("miniStock", businessResourceStoreInfo.get("mini_stock"));
        businessResourceStoreInfo.put("parentRstId", businessResourceStoreInfo.get("parent_rst_id"));
        businessResourceStoreInfo.put("isFixed", businessResourceStoreInfo.get("is_fixed"));
        businessResourceStoreInfo.remove("bId");
        businessResourceStoreInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessResourceStore 资源信息
     */
    protected void autoSaveDelBusinessResourceStore(Business business, JSONObject businessResourceStore) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("resId", businessResourceStore.getString("resId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentResourceStoreInfos = getResourceStoreServiceDaoImpl().getResourceStoreInfo(info);
        if (currentResourceStoreInfos == null || currentResourceStoreInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentResourceStoreInfo = currentResourceStoreInfos.get(0);
        currentResourceStoreInfo.put("bId", business.getbId());
        currentResourceStoreInfo.put("resName", currentResourceStoreInfo.get("res_name"));
        currentResourceStoreInfo.put("operate", currentResourceStoreInfo.get("operate"));
        currentResourceStoreInfo.put("price", currentResourceStoreInfo.get("price"));
        currentResourceStoreInfo.put("resCode", currentResourceStoreInfo.get("res_code"));
        currentResourceStoreInfo.put("description", currentResourceStoreInfo.get("description"));
        currentResourceStoreInfo.put("storeId", currentResourceStoreInfo.get("store_id"));
        currentResourceStoreInfo.put("stock", currentResourceStoreInfo.get("stock"));
        currentResourceStoreInfo.put("resId", currentResourceStoreInfo.get("res_id"));
        currentResourceStoreInfo.put("unitCode", currentResourceStoreInfo.get("unit_code"));
        currentResourceStoreInfo.put("remark", currentResourceStoreInfo.get("remark"));
        currentResourceStoreInfo.put("outLowPrice", currentResourceStoreInfo.get("out_low_price"));
        currentResourceStoreInfo.put("outHighPrice", currentResourceStoreInfo.get("out_high_price"));
        currentResourceStoreInfo.put("operate", StatusConstant.OPERATE_DEL);
        currentResourceStoreInfo.put("shId", currentResourceStoreInfo.get("sh_id"));
        currentResourceStoreInfo.put("warningStock", currentResourceStoreInfo.get("warning_stock"));
        currentResourceStoreInfo.put("rstId", currentResourceStoreInfo.get("rst_id"));
        currentResourceStoreInfo.put("rssId", currentResourceStoreInfo.get("rss_id"));
        currentResourceStoreInfo.put("miniUnitCode", currentResourceStoreInfo.get("mini_unit_code"));
        currentResourceStoreInfo.put("miniUnitStock", currentResourceStoreInfo.get("mini_unit_stock"));
        currentResourceStoreInfo.put("miniStock", currentResourceStoreInfo.get("mini_stock"));
        currentResourceStoreInfo.put("parentRstId", currentResourceStoreInfo.get("parent_rst_id"));
        currentResourceStoreInfo.put("isFixed", currentResourceStoreInfo.get("is_fixed"));
        getResourceStoreServiceDaoImpl().saveBusinessResourceStoreInfo(currentResourceStoreInfo);
        for (Object key : currentResourceStoreInfo.keySet()) {
            if (businessResourceStore.get(key) == null) {
                businessResourceStore.put(key.toString(), currentResourceStoreInfo.get(key));
            }
        }
    }

}
