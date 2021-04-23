package com.java110.community.listener.inspectionTaskDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionTaskDetailServiceDao;
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
 * 巡检任务明细 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractInspectionTaskDetailBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractInspectionTaskDetailBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IInspectionTaskDetailServiceDao getInspectionTaskDetailServiceDaoImpl();

    /**
     * 刷新 businessInspectionTaskDetailInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessInspectionTaskDetailInfo
     */
    protected void flushBusinessInspectionTaskDetailInfo(Map businessInspectionTaskDetailInfo, String statusCd) {
        businessInspectionTaskDetailInfo.put("newBId", businessInspectionTaskDetailInfo.get("b_id"));
        businessInspectionTaskDetailInfo.put("inspectionId", businessInspectionTaskDetailInfo.get("inspection_id"));
        businessInspectionTaskDetailInfo.put("operate", businessInspectionTaskDetailInfo.get("operate"));
        businessInspectionTaskDetailInfo.put("inspectionName", businessInspectionTaskDetailInfo.get("inspection_name"));
        businessInspectionTaskDetailInfo.put("state", businessInspectionTaskDetailInfo.get("state"));
        businessInspectionTaskDetailInfo.put("communityId", businessInspectionTaskDetailInfo.get("community_id"));
        businessInspectionTaskDetailInfo.put("taskId", businessInspectionTaskDetailInfo.get("task_id"));
        businessInspectionTaskDetailInfo.put("taskDetailId", businessInspectionTaskDetailInfo.get("task_detail_id"));
        businessInspectionTaskDetailInfo.put("patrolType", businessInspectionTaskDetailInfo.get("patrol_type"));
        businessInspectionTaskDetailInfo.put("description", businessInspectionTaskDetailInfo.get("description"));
        businessInspectionTaskDetailInfo.remove("bId");
        businessInspectionTaskDetailInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessInspectionTaskDetail 巡检任务明细信息
     */
    protected void autoSaveDelBusinessInspectionTaskDetail(Business business, JSONObject businessInspectionTaskDetail) {
//自动插入DEL
        Map info = new HashMap();
        info.put("taskDetailId", businessInspectionTaskDetail.getString("taskDetailId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentInspectionTaskDetailInfos = getInspectionTaskDetailServiceDaoImpl().getInspectionTaskDetailInfo(info);
        if (currentInspectionTaskDetailInfos == null || currentInspectionTaskDetailInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentInspectionTaskDetailInfo = currentInspectionTaskDetailInfos.get(0);

        currentInspectionTaskDetailInfo.put("bId", business.getbId());

        currentInspectionTaskDetailInfo.put("inspectionId", currentInspectionTaskDetailInfo.get("inspection_id"));
        currentInspectionTaskDetailInfo.put("operate", currentInspectionTaskDetailInfo.get("operate"));
        currentInspectionTaskDetailInfo.put("inspectionName", currentInspectionTaskDetailInfo.get("inspection_name"));
        currentInspectionTaskDetailInfo.put("state", currentInspectionTaskDetailInfo.get("state"));
        currentInspectionTaskDetailInfo.put("communityId", currentInspectionTaskDetailInfo.get("community_id"));
        currentInspectionTaskDetailInfo.put("taskId", currentInspectionTaskDetailInfo.get("task_id"));
        currentInspectionTaskDetailInfo.put("taskDetailId", currentInspectionTaskDetailInfo.get("task_detail_id"));

        currentInspectionTaskDetailInfo.put("patrolType", currentInspectionTaskDetailInfo.get("patrol_type"));
        currentInspectionTaskDetailInfo.put("description", currentInspectionTaskDetailInfo.get("description"));


        currentInspectionTaskDetailInfo.put("operate", StatusConstant.OPERATE_DEL);
        getInspectionTaskDetailServiceDaoImpl().saveBusinessInspectionTaskDetailInfo(currentInspectionTaskDetailInfo);
        for (Object key : currentInspectionTaskDetailInfo.keySet()) {
            if (businessInspectionTaskDetail.get(key) == null) {
                businessInspectionTaskDetail.put(key.toString(), currentInspectionTaskDetailInfo.get(key));
            }
        }
    }


}
