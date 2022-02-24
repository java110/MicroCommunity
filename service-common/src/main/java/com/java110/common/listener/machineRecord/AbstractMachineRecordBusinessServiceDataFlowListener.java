package com.java110.common.listener.machineRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineRecordServiceDao;
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
 * 设备上报 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMachineRecordBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMachineRecordBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMachineRecordServiceDao getMachineRecordServiceDaoImpl();

    /**
     * 刷新 businessMachineRecordInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMachineRecordInfo
     */
    protected void flushBusinessMachineRecordInfo(Map businessMachineRecordInfo, String statusCd) {
        businessMachineRecordInfo.put("newBId", businessMachineRecordInfo.get("b_id"));
        businessMachineRecordInfo.put("fileTime", businessMachineRecordInfo.get("file_time"));
        businessMachineRecordInfo.put("machineCode", businessMachineRecordInfo.get("machine_code"));
        businessMachineRecordInfo.put("openTypeCd", businessMachineRecordInfo.get("open_type_cd"));
        businessMachineRecordInfo.put("idCard", businessMachineRecordInfo.get("id_card"));
        businessMachineRecordInfo.put("machineRecordId", businessMachineRecordInfo.get("machine_record_id"));
        businessMachineRecordInfo.put("machineId", businessMachineRecordInfo.get("machine_id"));
        businessMachineRecordInfo.put("operate", businessMachineRecordInfo.get("operate"));
        businessMachineRecordInfo.put("name", businessMachineRecordInfo.get("name"));
        businessMachineRecordInfo.put("tel", businessMachineRecordInfo.get("tel"));
        businessMachineRecordInfo.put("communityId", businessMachineRecordInfo.get("community_id"));
        businessMachineRecordInfo.put("fileId", businessMachineRecordInfo.get("file_id"));
        businessMachineRecordInfo.put("recordTypeCd", businessMachineRecordInfo.get("record_type_cd"));
        businessMachineRecordInfo.remove("bId");
        businessMachineRecordInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMachineRecord 设备上报信息
     */
    protected void autoSaveDelBusinessMachineRecord(Business business, JSONObject businessMachineRecord) {
//自动插入DEL
        Map info = new HashMap();
        info.put("machineRecordId", businessMachineRecord.getString("machineRecordId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMachineRecordInfos = getMachineRecordServiceDaoImpl().getMachineRecordInfo(info);
        if (currentMachineRecordInfos == null || currentMachineRecordInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMachineRecordInfo = currentMachineRecordInfos.get(0);

        currentMachineRecordInfo.put("bId", business.getbId());

        currentMachineRecordInfo.put("fileTime", currentMachineRecordInfo.get("file_time"));
        currentMachineRecordInfo.put("machineCode", currentMachineRecordInfo.get("machine_code"));
        currentMachineRecordInfo.put("openTypeCd", currentMachineRecordInfo.get("open_type_cd"));
        currentMachineRecordInfo.put("idCard", currentMachineRecordInfo.get("id_card"));
        currentMachineRecordInfo.put("machineRecordId", currentMachineRecordInfo.get("machine_record_id"));
        currentMachineRecordInfo.put("machineId", currentMachineRecordInfo.get("machine_id"));
        currentMachineRecordInfo.put("operate", currentMachineRecordInfo.get("operate"));
        currentMachineRecordInfo.put("name", currentMachineRecordInfo.get("name"));
        currentMachineRecordInfo.put("tel", currentMachineRecordInfo.get("tel"));
        currentMachineRecordInfo.put("communityId", currentMachineRecordInfo.get("community_id"));
        currentMachineRecordInfo.put("fileId", currentMachineRecordInfo.get("file_id"));
        currentMachineRecordInfo.put("recordTypeCd", currentMachineRecordInfo.get("record_type_cd"));


        currentMachineRecordInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMachineRecordServiceDaoImpl().saveBusinessMachineRecordInfo(currentMachineRecordInfo);

        for (Object key : currentMachineRecordInfo.keySet()) {
            if (businessMachineRecord.get(key) == null) {
                businessMachineRecord.put(key.toString(), currentMachineRecordInfo.get(key));
            }
        }
    }


}
