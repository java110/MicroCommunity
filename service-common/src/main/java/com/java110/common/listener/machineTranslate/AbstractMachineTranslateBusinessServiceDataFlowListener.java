package com.java110.common.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineTranslateServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备同步 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMachineTranslateBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMachineTranslateBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMachineTranslateServiceDao getMachineTranslateServiceDaoImpl();

    /**
     * 刷新 businessMachineTranslateInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMachineTranslateInfo
     */
    protected void flushBusinessMachineTranslateInfo(Map businessMachineTranslateInfo, String statusCd) {
        businessMachineTranslateInfo.put("newBId", businessMachineTranslateInfo.get("b_id"));
        businessMachineTranslateInfo.put("machineId", businessMachineTranslateInfo.get("machine_id"));
        businessMachineTranslateInfo.put("machineCode", businessMachineTranslateInfo.get("machine_code"));
        businessMachineTranslateInfo.put("operate", businessMachineTranslateInfo.get("operate"));
        businessMachineTranslateInfo.put("typeCd", businessMachineTranslateInfo.get("type_cd"));
        businessMachineTranslateInfo.put("machineTranslateId", businessMachineTranslateInfo.get("machine_translate_id"));
        businessMachineTranslateInfo.put("objId", businessMachineTranslateInfo.get("obj_id"));
        businessMachineTranslateInfo.put("objName", businessMachineTranslateInfo.get("obj_name"));
        businessMachineTranslateInfo.put("state", businessMachineTranslateInfo.get("state"));
        businessMachineTranslateInfo.put("communityId", businessMachineTranslateInfo.get("community_id"));
        businessMachineTranslateInfo.put("machineCmd", businessMachineTranslateInfo.get("machine_cmd"));
        businessMachineTranslateInfo.put("objBId", businessMachineTranslateInfo.get("obj_b_id"));
        businessMachineTranslateInfo.remove("bId");
        businessMachineTranslateInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMachineTranslate 设备同步信息
     */
    protected void autoSaveDelBusinessMachineTranslate(Business business, JSONObject businessMachineTranslate) {
//自动插入DEL
        Map info = new HashMap();
        info.put("machineTranslateId", businessMachineTranslate.getString("machineTranslateId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMachineTranslateInfos = getMachineTranslateServiceDaoImpl().getMachineTranslateInfo(info);
        if (currentMachineTranslateInfos == null || currentMachineTranslateInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMachineTranslateInfo = currentMachineTranslateInfos.get(0);

        currentMachineTranslateInfo.put("bId", business.getbId());

        currentMachineTranslateInfo.put("machineId", currentMachineTranslateInfo.get("machine_id"));
        currentMachineTranslateInfo.put("machineCode", currentMachineTranslateInfo.get("machine_code"));
        currentMachineTranslateInfo.put("operate", currentMachineTranslateInfo.get("operate"));
        currentMachineTranslateInfo.put("typeCd", currentMachineTranslateInfo.get("type_cd"));
        currentMachineTranslateInfo.put("machineTranslateId", currentMachineTranslateInfo.get("machine_translate_id"));
        currentMachineTranslateInfo.put("objId", currentMachineTranslateInfo.get("obj_id"));
        currentMachineTranslateInfo.put("objName", currentMachineTranslateInfo.get("obj_name"));
        currentMachineTranslateInfo.put("state", currentMachineTranslateInfo.get("state"));
        currentMachineTranslateInfo.put("communityId", currentMachineTranslateInfo.get("community_id"));
        currentMachineTranslateInfo.put("machineCmd", currentMachineTranslateInfo.get("machine_cmd"));
        currentMachineTranslateInfo.put("objBId", currentMachineTranslateInfo.get("obj_b_id"));


        currentMachineTranslateInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMachineTranslateServiceDaoImpl().saveBusinessMachineTranslateInfo(currentMachineTranslateInfo);

        for (Object key : currentMachineTranslateInfo.keySet()) {
            if (businessMachineTranslate.get(key) == null) {
                businessMachineTranslate.put(key.toString(), currentMachineTranslateInfo.get(key));
            }
        }
    }


}
