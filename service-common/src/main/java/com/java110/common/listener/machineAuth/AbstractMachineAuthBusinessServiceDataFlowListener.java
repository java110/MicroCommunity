package com.java110.common.listener.machineAuth;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineAuthServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备权限 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMachineAuthBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMachineAuthBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMachineAuthServiceDao getMachineAuthServiceDaoImpl();

    /**
     * 刷新 businessMachineAuthInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMachineAuthInfo
     */
    protected void flushBusinessMachineAuthInfo(Map businessMachineAuthInfo, String statusCd) {
        businessMachineAuthInfo.put("newBId", businessMachineAuthInfo.get("b_id"));
        businessMachineAuthInfo.put("personName", businessMachineAuthInfo.get("person_name"));
        businessMachineAuthInfo.put("machineId", businessMachineAuthInfo.get("machine_id"));
        businessMachineAuthInfo.put("operate", businessMachineAuthInfo.get("operate"));
        businessMachineAuthInfo.put("personId", businessMachineAuthInfo.get("person_id"));
        businessMachineAuthInfo.put("startTime", businessMachineAuthInfo.get("start_time"));
        businessMachineAuthInfo.put("state", businessMachineAuthInfo.get("state"));
        businessMachineAuthInfo.put("endTime", businessMachineAuthInfo.get("end_time"));
        businessMachineAuthInfo.put("communityId", businessMachineAuthInfo.get("community_id"));
        businessMachineAuthInfo.put("personType", businessMachineAuthInfo.get("person_type"));
        businessMachineAuthInfo.put("authId", businessMachineAuthInfo.get("auth_id"));
        businessMachineAuthInfo.remove("bId");
        businessMachineAuthInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMachineAuth 设备权限信息
     */
    protected void autoSaveDelBusinessMachineAuth(Business business, JSONObject businessMachineAuth) {
//自动插入DEL
        Map info = new HashMap();
        info.put("authId", businessMachineAuth.getString("authId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMachineAuthInfos = getMachineAuthServiceDaoImpl().getMachineAuthInfo(info);
        if (currentMachineAuthInfos == null || currentMachineAuthInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMachineAuthInfo = currentMachineAuthInfos.get(0);

        currentMachineAuthInfo.put("bId", business.getbId());

        currentMachineAuthInfo.put("personName", currentMachineAuthInfo.get("person_name"));
        currentMachineAuthInfo.put("machineId", currentMachineAuthInfo.get("machine_id"));
        currentMachineAuthInfo.put("operate", currentMachineAuthInfo.get("operate"));
        currentMachineAuthInfo.put("personId", currentMachineAuthInfo.get("person_id"));
        currentMachineAuthInfo.put("startTime", currentMachineAuthInfo.get("start_time"));
        currentMachineAuthInfo.put("state", currentMachineAuthInfo.get("state"));
        currentMachineAuthInfo.put("endTime", currentMachineAuthInfo.get("end_time"));
        currentMachineAuthInfo.put("communityId", currentMachineAuthInfo.get("community_id"));
        currentMachineAuthInfo.put("personType", currentMachineAuthInfo.get("person_type"));
        currentMachineAuthInfo.put("authId", currentMachineAuthInfo.get("auth_id"));


        currentMachineAuthInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMachineAuthServiceDaoImpl().saveBusinessMachineAuthInfo(currentMachineAuthInfo);
        for (Object key : currentMachineAuthInfo.keySet()) {
            if (businessMachineAuth.get(key) == null) {
                businessMachineAuth.put(key.toString(), currentMachineAuthInfo.get(key));
            }
        }
    }


}
