package com.java110.store.listener.allocationStorehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.store.dao.IAllocationStorehouseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库调拨 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAllocationStorehouseBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractAllocationStorehouseBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAllocationStorehouseServiceDao getAllocationStorehouseServiceDaoImpl();

    /**
     * 刷新 businessAllocationStorehouseInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAllocationStorehouseInfo
     */
    protected void flushBusinessAllocationStorehouseInfo(Map businessAllocationStorehouseInfo, String statusCd) {
        businessAllocationStorehouseInfo.put("newBId", businessAllocationStorehouseInfo.get("b_id"));
        businessAllocationStorehouseInfo.put("asId", businessAllocationStorehouseInfo.get("as_id"));
        businessAllocationStorehouseInfo.put("storeId", businessAllocationStorehouseInfo.get("store_id"));
        businessAllocationStorehouseInfo.put("resId", businessAllocationStorehouseInfo.get("res_id"));
        businessAllocationStorehouseInfo.put("shIdz", businessAllocationStorehouseInfo.get("sh_id_z"));
        businessAllocationStorehouseInfo.put("resName", businessAllocationStorehouseInfo.get("res_name"));
        businessAllocationStorehouseInfo.put("startUserId", businessAllocationStorehouseInfo.get("start_user_id"));
        businessAllocationStorehouseInfo.put("operate", businessAllocationStorehouseInfo.get("operate"));
        businessAllocationStorehouseInfo.put("shIda", businessAllocationStorehouseInfo.get("sh_id_a"));
        businessAllocationStorehouseInfo.put("startUserName", businessAllocationStorehouseInfo.get("start_user_name"));
        businessAllocationStorehouseInfo.put("stock", businessAllocationStorehouseInfo.get("stock"));
        businessAllocationStorehouseInfo.put("originalStock", businessAllocationStorehouseInfo.get("original_stock"));
        businessAllocationStorehouseInfo.put("remark", businessAllocationStorehouseInfo.get("remark"));
        businessAllocationStorehouseInfo.put("applyId", businessAllocationStorehouseInfo.get("apply_id"));
        businessAllocationStorehouseInfo.remove("bId");
        businessAllocationStorehouseInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAllocationStorehouse 仓库调拨信息
     */
    protected void autoSaveDelBusinessAllocationStorehouse(Business business, JSONObject businessAllocationStorehouse) {
//自动插入DEL
        Map info = new HashMap();
        info.put("asId", businessAllocationStorehouse.getString("asId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAllocationStorehouseInfos = getAllocationStorehouseServiceDaoImpl().getAllocationStorehouseInfo(info);
        if (currentAllocationStorehouseInfos == null || currentAllocationStorehouseInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAllocationStorehouseInfo = currentAllocationStorehouseInfos.get(0);

        currentAllocationStorehouseInfo.put("bId", business.getbId());

        currentAllocationStorehouseInfo.put("asId", currentAllocationStorehouseInfo.get("as_id"));
        currentAllocationStorehouseInfo.put("storeId", currentAllocationStorehouseInfo.get("store_id"));
        currentAllocationStorehouseInfo.put("resId", currentAllocationStorehouseInfo.get("res_id"));
        currentAllocationStorehouseInfo.put("shIdz", currentAllocationStorehouseInfo.get("sh_id_z"));
        currentAllocationStorehouseInfo.put("resName", currentAllocationStorehouseInfo.get("res_name"));
        currentAllocationStorehouseInfo.put("startUserId", currentAllocationStorehouseInfo.get("start_user_id"));
        currentAllocationStorehouseInfo.put("operate", currentAllocationStorehouseInfo.get("operate"));
        currentAllocationStorehouseInfo.put("shIda", currentAllocationStorehouseInfo.get("sh_id_a"));
        currentAllocationStorehouseInfo.put("startUserName", currentAllocationStorehouseInfo.get("start_user_name"));
        currentAllocationStorehouseInfo.put("stock", currentAllocationStorehouseInfo.get("stock"));
        currentAllocationStorehouseInfo.put("originalStock", currentAllocationStorehouseInfo.get("original_stock"));
        currentAllocationStorehouseInfo.put("remark", currentAllocationStorehouseInfo.get("remark"));
        currentAllocationStorehouseInfo.put("applyId", currentAllocationStorehouseInfo.get("apply_id"));


        currentAllocationStorehouseInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAllocationStorehouseServiceDaoImpl().saveBusinessAllocationStorehouseInfo(currentAllocationStorehouseInfo);
        for (Object key : currentAllocationStorehouseInfo.keySet()) {
            if (businessAllocationStorehouse.get(key) == null) {
                businessAllocationStorehouse.put(key.toString(), currentAllocationStorehouseInfo.get(key));
            }
        }
    }


}
