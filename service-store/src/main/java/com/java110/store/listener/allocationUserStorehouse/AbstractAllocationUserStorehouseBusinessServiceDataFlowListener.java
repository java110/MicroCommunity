package com.java110.store.listener.allocationUserStorehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.store.dao.IAllocationUserStorehouseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品供应商 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAllocationUserStorehouseBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAllocationUserStorehouseBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAllocationUserStorehouseServiceDao getAllocationUserStorehouseServiceDaoImpl();

    /**
     * 刷新 businessAllocationUserStorehouseInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAllocationUserStorehouseInfo
     */
    protected void flushBusinessAllocationUserStorehouseInfo(Map businessAllocationUserStorehouseInfo, String statusCd) {
        businessAllocationUserStorehouseInfo.put("newBId", businessAllocationUserStorehouseInfo.get("b_id"));
        businessAllocationUserStorehouseInfo.put("acceptUserId", businessAllocationUserStorehouseInfo.get("accept_user_id"));
        businessAllocationUserStorehouseInfo.put("acceptUserName", businessAllocationUserStorehouseInfo.get("accept_user_name"));
        businessAllocationUserStorehouseInfo.put("remark", businessAllocationUserStorehouseInfo.get("remark"));
        businessAllocationUserStorehouseInfo.put("storeId", businessAllocationUserStorehouseInfo.get("store_id"));
        businessAllocationUserStorehouseInfo.put("resId", businessAllocationUserStorehouseInfo.get("res_id"));
        businessAllocationUserStorehouseInfo.put("resCode", businessAllocationUserStorehouseInfo.get("res_code"));
        businessAllocationUserStorehouseInfo.put("resName", businessAllocationUserStorehouseInfo.get("res_name"));
        businessAllocationUserStorehouseInfo.put("startUserId", businessAllocationUserStorehouseInfo.get("start_user_id"));
        businessAllocationUserStorehouseInfo.put("operate", businessAllocationUserStorehouseInfo.get("operate"));
        businessAllocationUserStorehouseInfo.put("startUserName", businessAllocationUserStorehouseInfo.get("start_user_name"));
        businessAllocationUserStorehouseInfo.put("ausId", businessAllocationUserStorehouseInfo.get("aus_id"));
        businessAllocationUserStorehouseInfo.put("stock", businessAllocationUserStorehouseInfo.get("stock"));
        businessAllocationUserStorehouseInfo.put("giveQuantity", businessAllocationUserStorehouseInfo.get("give_quantity"));
        businessAllocationUserStorehouseInfo.remove("bId");
        businessAllocationUserStorehouseInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAllocationUserStorehouse 物品供应商信息
     */
    protected void autoSaveDelBusinessAllocationUserStorehouse(Business business, JSONObject businessAllocationUserStorehouse) {
//自动插入DEL
        Map info = new HashMap();
        info.put("ausId", businessAllocationUserStorehouse.getString("ausId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAllocationUserStorehouseInfos = getAllocationUserStorehouseServiceDaoImpl().getAllocationUserStorehouseInfo(info);
        if (currentAllocationUserStorehouseInfos == null || currentAllocationUserStorehouseInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAllocationUserStorehouseInfo = currentAllocationUserStorehouseInfos.get(0);

        currentAllocationUserStorehouseInfo.put("bId", business.getbId());

        currentAllocationUserStorehouseInfo.put("acceptUserId", currentAllocationUserStorehouseInfo.get("accept_user_id"));
        currentAllocationUserStorehouseInfo.put("acceptUserName", currentAllocationUserStorehouseInfo.get("accept_user_name"));
        currentAllocationUserStorehouseInfo.put("remark", currentAllocationUserStorehouseInfo.get("remark"));
        currentAllocationUserStorehouseInfo.put("storeId", currentAllocationUserStorehouseInfo.get("store_id"));
        currentAllocationUserStorehouseInfo.put("resId", currentAllocationUserStorehouseInfo.get("res_id"));
        currentAllocationUserStorehouseInfo.put("resCode", currentAllocationUserStorehouseInfo.get("res_code"));
        currentAllocationUserStorehouseInfo.put("resName", currentAllocationUserStorehouseInfo.get("res_name"));
        currentAllocationUserStorehouseInfo.put("startUserId", currentAllocationUserStorehouseInfo.get("start_user_id"));
        currentAllocationUserStorehouseInfo.put("operate", currentAllocationUserStorehouseInfo.get("operate"));
        currentAllocationUserStorehouseInfo.put("startUserName", currentAllocationUserStorehouseInfo.get("start_user_name"));
        currentAllocationUserStorehouseInfo.put("ausId", currentAllocationUserStorehouseInfo.get("aus_id"));
        currentAllocationUserStorehouseInfo.put("stock", currentAllocationUserStorehouseInfo.get("stock"));
        currentAllocationUserStorehouseInfo.put("giveQuantity", currentAllocationUserStorehouseInfo.get("give_quantity"));
        currentAllocationUserStorehouseInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAllocationUserStorehouseServiceDaoImpl().saveBusinessAllocationUserStorehouseInfo(currentAllocationUserStorehouseInfo);
        for (Object key : currentAllocationUserStorehouseInfo.keySet()) {
            if (businessAllocationUserStorehouse.get(key) == null) {
                businessAllocationUserStorehouse.put(key.toString(), currentAllocationUserStorehouseInfo.get(key));
            }
        }
    }


}
