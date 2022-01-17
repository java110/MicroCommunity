package com.java110.common.listener.machineAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineAttrServiceDao;
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
 * 设备属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMachineAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMachineAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMachineAttrServiceDao getMachineAttrServiceDaoImpl();

    /**
     * 刷新 businessMachineAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMachineAttrInfo
     */
    protected void flushBusinessMachineAttrInfo(Map businessMachineAttrInfo, String statusCd) {
        businessMachineAttrInfo.put("newBId", businessMachineAttrInfo.get("b_id"));
        businessMachineAttrInfo.put("machineId", businessMachineAttrInfo.get("machine_id"));
        businessMachineAttrInfo.put("attrId", businessMachineAttrInfo.get("attr_id"));
        businessMachineAttrInfo.put("operate", businessMachineAttrInfo.get("operate"));
        businessMachineAttrInfo.put("specCd", businessMachineAttrInfo.get("spec_cd"));
        businessMachineAttrInfo.put("communityId", businessMachineAttrInfo.get("community_id"));
        businessMachineAttrInfo.put("value", businessMachineAttrInfo.get("value"));
        businessMachineAttrInfo.remove("bId");
        businessMachineAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMachineAttr 设备属性信息
     */
    protected void autoSaveDelBusinessMachineAttr(Business business, JSONObject businessMachineAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessMachineAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMachineAttrInfos = getMachineAttrServiceDaoImpl().getMachineAttrInfo(info);
        if (currentMachineAttrInfos == null || currentMachineAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMachineAttrInfo = currentMachineAttrInfos.get(0);

        currentMachineAttrInfo.put("bId", business.getbId());

        currentMachineAttrInfo.put("machineId", currentMachineAttrInfo.get("machine_id"));
        currentMachineAttrInfo.put("attrId", currentMachineAttrInfo.get("attr_id"));
        currentMachineAttrInfo.put("operate", currentMachineAttrInfo.get("operate"));
        currentMachineAttrInfo.put("specCd", currentMachineAttrInfo.get("spec_cd"));
        currentMachineAttrInfo.put("communityId", currentMachineAttrInfo.get("community_id"));
        currentMachineAttrInfo.put("value", currentMachineAttrInfo.get("value"));


        currentMachineAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMachineAttrServiceDaoImpl().saveBusinessMachineAttrInfo(currentMachineAttrInfo);
        for (Object key : currentMachineAttrInfo.keySet()) {
            if (businessMachineAttr.get(key) == null) {
                businessMachineAttr.put(key.toString(), currentMachineAttrInfo.get(key));
            }
        }
    }


}
