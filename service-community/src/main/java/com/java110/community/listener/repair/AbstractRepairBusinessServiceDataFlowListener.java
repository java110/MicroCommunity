package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修信息 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractRepairBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractRepairBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IRepairServiceDao getRepairServiceDaoImpl();

    /**
     * 刷新 businessRepairInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessRepairInfo
     */
    protected void flushBusinessRepairInfo(Map businessRepairInfo, String statusCd) {
        businessRepairInfo.put("newBId", businessRepairInfo.get("b_id"));
        businessRepairInfo.put("operate", businessRepairInfo.get("operate"));
        businessRepairInfo.put("repairName", businessRepairInfo.get("repair_name"));
        businessRepairInfo.put("appointmentTime", businessRepairInfo.get("appointment_time"));
        businessRepairInfo.put("repairType", businessRepairInfo.get("repair_type"));
        businessRepairInfo.put("context", businessRepairInfo.get("context"));
        businessRepairInfo.put("repairId", businessRepairInfo.get("repair_id"));
        businessRepairInfo.put("tel", businessRepairInfo.get("tel"));
        businessRepairInfo.put("state", businessRepairInfo.get("state"));
        businessRepairInfo.put("communityId", businessRepairInfo.get("community_id"));
        businessRepairInfo.put("roomId", businessRepairInfo.get("room_id"));
        businessRepairInfo.remove("bId");
        businessRepairInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessRepair 报修信息信息
     */
    protected void autoSaveDelBusinessRepair(Business business, JSONObject businessRepair) {
//自动插入DEL
        Map info = new HashMap();
        info.put("repairId", businessRepair.getString("repairId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentRepairInfos = getRepairServiceDaoImpl().getRepairInfo(info);
        if (currentRepairInfos == null || currentRepairInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentRepairInfo = currentRepairInfos.get(0);

        currentRepairInfo.put("bId", business.getbId());

        currentRepairInfo.put("operate", currentRepairInfo.get("operate"));
        currentRepairInfo.put("repairName", currentRepairInfo.get("repair_name"));
        currentRepairInfo.put("appointmentTime", currentRepairInfo.get("appointment_time"));
        currentRepairInfo.put("repairType", currentRepairInfo.get("repair_type"));
        currentRepairInfo.put("context", currentRepairInfo.get("context"));
        currentRepairInfo.put("repairId", currentRepairInfo.get("repair_id"));
        currentRepairInfo.put("tel", currentRepairInfo.get("tel"));
        currentRepairInfo.put("state", currentRepairInfo.get("state"));
        currentRepairInfo.put("communityId", currentRepairInfo.get("community_id"));
        currentRepairInfo.put("roomId", currentRepairInfo.get("room_id"));


        currentRepairInfo.put("operate", StatusConstant.OPERATE_DEL);
        getRepairServiceDaoImpl().saveBusinessRepairInfo(currentRepairInfo);

        for (Object key : currentRepairInfo.keySet()) {
            if (businessRepair.get(key) == null) {
                businessRepair.put(key.toString(), currentRepairInfo.get(key));
            }
        }
    }


}
