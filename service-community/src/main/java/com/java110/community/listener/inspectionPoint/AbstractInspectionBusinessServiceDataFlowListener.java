package com.java110.community.listener.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡检点 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractInspectionBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractInspectionBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IInspectionServiceDao getInspectionServiceDaoImpl();

    /**
     * 刷新 businessInspectionInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessInspectionInfo
     */
    protected void flushBusinessInspectionInfo(Map businessInspectionInfo, String statusCd) {
        businessInspectionInfo.put("newBId", businessInspectionInfo.get("b_id"));
        businessInspectionInfo.put("inspectionId", businessInspectionInfo.get("inspection_id"));
        businessInspectionInfo.put("machineId", businessInspectionInfo.get("machine_id"));
        businessInspectionInfo.put("operate", businessInspectionInfo.get("operate"));
        businessInspectionInfo.put("remark", businessInspectionInfo.get("remark"));
        businessInspectionInfo.put("inspectionName", businessInspectionInfo.get("inspection_name"));
        businessInspectionInfo.put("communityId", businessInspectionInfo.get("community_id"));
        businessInspectionInfo.remove("bId");
        businessInspectionInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessInspection 巡检点信息
     */
    protected void autoSaveDelBusinessInspection(Business business, JSONObject businessInspection) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("inspectionId", businessInspection.getString("inspectionId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentInspectionInfos = getInspectionServiceDaoImpl().getInspectionInfo(info);
        if (currentInspectionInfos == null || currentInspectionInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentInspectionInfo = currentInspectionInfos.get(0);
        currentInspectionInfo.put("bId", business.getbId());
        currentInspectionInfo.put("inspectionId", currentInspectionInfo.get("inspection_id"));
        currentInspectionInfo.put("machineId", currentInspectionInfo.get("machine_id"));
        currentInspectionInfo.put("operate", currentInspectionInfo.get("operate"));
        currentInspectionInfo.put("remark", currentInspectionInfo.get("remark"));
        currentInspectionInfo.put("inspectionName", currentInspectionInfo.get("inspection_name"));
        currentInspectionInfo.put("communityId", currentInspectionInfo.get("community_id"));
        currentInspectionInfo.put("operate", StatusConstant.OPERATE_DEL);
        getInspectionServiceDaoImpl().saveBusinessInspectionInfo(currentInspectionInfo);

        for (Object key : currentInspectionInfo.keySet()) {
            if (businessInspection.get(key) == null) {
                businessInspection.put(key.toString(), currentInspectionInfo.get(key));
            }
        }
    }


}
