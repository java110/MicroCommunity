package com.java110.store.listener.storehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.store.dao.IStorehouseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractStorehouseBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractStorehouseBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IStorehouseServiceDao getStorehouseServiceDaoImpl();

    /**
     * 刷新 businessStorehouseInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessStorehouseInfo
     */
    protected void flushBusinessStorehouseInfo(Map businessStorehouseInfo, String statusCd) {
        businessStorehouseInfo.put("newBId", businessStorehouseInfo.get("b_id"));
        businessStorehouseInfo.put("shDesc", businessStorehouseInfo.get("sh_desc"));
        businessStorehouseInfo.put("shType", businessStorehouseInfo.get("sh_type"));
        businessStorehouseInfo.put("shObjId", businessStorehouseInfo.get("sh_obj_id"));
        businessStorehouseInfo.put("operate", businessStorehouseInfo.get("operate"));
        businessStorehouseInfo.put("shId", businessStorehouseInfo.get("sh_id"));
        businessStorehouseInfo.put("shName", businessStorehouseInfo.get("sh_name"));
        businessStorehouseInfo.put("storeId", businessStorehouseInfo.get("store_id"));
        businessStorehouseInfo.put("isShow", businessStorehouseInfo.get("is_show"));
        businessStorehouseInfo.remove("bId");
        businessStorehouseInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessStorehouse 仓库信息
     */
    protected void autoSaveDelBusinessStorehouse(Business business, JSONObject businessStorehouse) {
//自动插入DEL
        Map info = new HashMap();
        info.put("shId", businessStorehouse.getString("shId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentStorehouseInfos = getStorehouseServiceDaoImpl().getStorehouseInfo(info);
        if (currentStorehouseInfos == null || currentStorehouseInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentStorehouseInfo = currentStorehouseInfos.get(0);

        currentStorehouseInfo.put("bId", business.getbId());

        currentStorehouseInfo.put("shDesc", currentStorehouseInfo.get("sh_desc"));
        currentStorehouseInfo.put("shType", currentStorehouseInfo.get("sh_type"));
        currentStorehouseInfo.put("shObjId", currentStorehouseInfo.get("sh_obj_id"));
        currentStorehouseInfo.put("operate", currentStorehouseInfo.get("operate"));
        currentStorehouseInfo.put("shId", currentStorehouseInfo.get("sh_id"));
        currentStorehouseInfo.put("shName", currentStorehouseInfo.get("sh_name"));
        currentStorehouseInfo.put("storeId", currentStorehouseInfo.get("store_id"));
        currentStorehouseInfo.put("isShow", currentStorehouseInfo.get("is_show"));

        currentStorehouseInfo.put("operate", StatusConstant.OPERATE_DEL);
        getStorehouseServiceDaoImpl().saveBusinessStorehouseInfo(currentStorehouseInfo);
        for (Object key : currentStorehouseInfo.keySet()) {
            if (businessStorehouse.get(key) == null) {
                businessStorehouse.put(key.toString(), currentStorehouseInfo.get(key));
            }
        }
    }


}
