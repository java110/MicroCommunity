package com.java110.common.listener.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMachineBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMachineBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMachineServiceDao getMachineServiceDaoImpl();

    /**
     * 刷新 businessMachineInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMachineInfo
     */
    protected void flushBusinessMachineInfo(Map businessMachineInfo, String statusCd) {
        businessMachineInfo.put("newBId", businessMachineInfo.get("b_id"));
        businessMachineInfo.put("machineMac", businessMachineInfo.get("machine_mac"));
        businessMachineInfo.put("machineId", businessMachineInfo.get("machine_id"));
        businessMachineInfo.put("machineCode", businessMachineInfo.get("machine_code"));
        businessMachineInfo.put("authCode", businessMachineInfo.get("auth_code"));
        businessMachineInfo.put("operate", businessMachineInfo.get("operate"));
        businessMachineInfo.put("machineVersion", businessMachineInfo.get("machine_version"));
        businessMachineInfo.put("communityId", businessMachineInfo.get("community_id"));
        businessMachineInfo.put("machineName", businessMachineInfo.get("machine_name"));
        businessMachineInfo.put("machineTypeCd", businessMachineInfo.get("machine_type_cd"));
        businessMachineInfo.put("machineIp", businessMachineInfo.get("machine_ip"));
        businessMachineInfo.put("locationTypeCd", businessMachineInfo.get("location_type_cd"));
        businessMachineInfo.put("locationObjId", businessMachineInfo.get("location_obj_id"));
        businessMachineInfo.put("heartbeatTime", businessMachineInfo.get("heartbeat_time"));
        businessMachineInfo.put("state", businessMachineInfo.get("state"));

        businessMachineInfo.remove("bId");
        businessMachineInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMachine 设备信息
     */
    protected void autoSaveDelBusinessMachine(Business business, JSONObject businessMachine) {
//自动插入DEL
        Map info = new HashMap();
        info.put("machineId", businessMachine.getString("machineId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMachineInfos = getMachineServiceDaoImpl().getMachineInfo(info);
        if (currentMachineInfos == null || currentMachineInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMachineInfo = currentMachineInfos.get(0);

        currentMachineInfo.put("bId", business.getbId());

        currentMachineInfo.put("machineMac", currentMachineInfo.get("machine_mac"));
        currentMachineInfo.put("machineId", currentMachineInfo.get("machine_id"));
        currentMachineInfo.put("machineCode", currentMachineInfo.get("machine_code"));
        currentMachineInfo.put("authCode", currentMachineInfo.get("auth_code"));
        currentMachineInfo.put("operate", currentMachineInfo.get("operate"));
        currentMachineInfo.put("machineVersion", currentMachineInfo.get("machine_version"));
        currentMachineInfo.put("communityId", currentMachineInfo.get("community_id"));
        currentMachineInfo.put("machineName", currentMachineInfo.get("machine_name"));
        currentMachineInfo.put("machineTypeCd", currentMachineInfo.get("machine_type_cd"));
        currentMachineInfo.put("machineIp", currentMachineInfo.get("machine_ip"));
        currentMachineInfo.put("locationTypeCd", currentMachineInfo.get("location_type_cd"));
        currentMachineInfo.put("locationObjId", currentMachineInfo.get("location_obj_id"));
        currentMachineInfo.put("state", currentMachineInfo.get("state"));
        currentMachineInfo.put("heartbeatTime", currentMachineInfo.get("heartbeat_time"));



        currentMachineInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMachineServiceDaoImpl().saveBusinessMachineInfo(currentMachineInfo);

        for (Object key : currentMachineInfo.keySet()) {
            if (businessMachine.get(key) == null) {
                businessMachine.put(key.toString(), currentMachineInfo.get(key));
            }
        }
    }


}
