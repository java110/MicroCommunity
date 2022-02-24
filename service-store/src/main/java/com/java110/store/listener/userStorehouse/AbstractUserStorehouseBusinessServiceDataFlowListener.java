package com.java110.store.listener.userStorehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.store.dao.IUserStorehouseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人物品 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractUserStorehouseBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractUserStorehouseBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IUserStorehouseServiceDao getUserStorehouseServiceDaoImpl();

    /**
     * 刷新 businessUserStorehouseInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessUserStorehouseInfo
     */
    protected void flushBusinessUserStorehouseInfo(Map businessUserStorehouseInfo, String statusCd) {
        businessUserStorehouseInfo.put("newBId", businessUserStorehouseInfo.get("b_id"));
        businessUserStorehouseInfo.put("resName", businessUserStorehouseInfo.get("res_name"));
        businessUserStorehouseInfo.put("operate", businessUserStorehouseInfo.get("operate"));
        businessUserStorehouseInfo.put("storeId", businessUserStorehouseInfo.get("store_id"));
        businessUserStorehouseInfo.put("stock", businessUserStorehouseInfo.get("stock"));
        businessUserStorehouseInfo.put("miniStock", businessUserStorehouseInfo.get("mini_stock"));
        businessUserStorehouseInfo.put("resId", businessUserStorehouseInfo.get("res_id"));
        businessUserStorehouseInfo.put("userId", businessUserStorehouseInfo.get("user_id"));
        businessUserStorehouseInfo.put("usId", businessUserStorehouseInfo.get("us_id"));
        businessUserStorehouseInfo.remove("bId");
        businessUserStorehouseInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessUserStorehouse 个人物品信息
     */
    protected void autoSaveDelBusinessUserStorehouse(Business business, JSONObject businessUserStorehouse) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("usId", businessUserStorehouse.getString("usId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentUserStorehouseInfos = getUserStorehouseServiceDaoImpl().getUserStorehouseInfo(info);
        if (currentUserStorehouseInfos == null || currentUserStorehouseInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentUserStorehouseInfo = currentUserStorehouseInfos.get(0);
        currentUserStorehouseInfo.put("bId", business.getbId());
        currentUserStorehouseInfo.put("resName", currentUserStorehouseInfo.get("res_name"));
        currentUserStorehouseInfo.put("operate", currentUserStorehouseInfo.get("operate"));
        currentUserStorehouseInfo.put("storeId", currentUserStorehouseInfo.get("store_id"));
        currentUserStorehouseInfo.put("stock", currentUserStorehouseInfo.get("stock"));
        currentUserStorehouseInfo.put("miniStock", currentUserStorehouseInfo.get("mini_stock"));
        currentUserStorehouseInfo.put("resId", currentUserStorehouseInfo.get("res_id"));
        currentUserStorehouseInfo.put("userId", currentUserStorehouseInfo.get("user_id"));
        currentUserStorehouseInfo.put("usId", currentUserStorehouseInfo.get("us_id"));
        currentUserStorehouseInfo.put("operate", StatusConstant.OPERATE_DEL);
        getUserStorehouseServiceDaoImpl().saveBusinessUserStorehouseInfo(currentUserStorehouseInfo);
        for (Object key : currentUserStorehouseInfo.keySet()) {
            if (businessUserStorehouse.get(key) == null) {
                businessUserStorehouse.put(key.toString(), currentUserStorehouseInfo.get(key));
            }
        }
    }

}
