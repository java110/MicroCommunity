package com.java110.job.listener.task;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.job.dao.ITaskServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractTaskBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractTaskBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ITaskServiceDao getTaskServiceDaoImpl();

    /**
     * 刷新 businessTaskInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessTaskInfo
     */
    protected void flushBusinessTaskInfo(Map businessTaskInfo, String statusCd) {
        businessTaskInfo.put("newBId", businessTaskInfo.get("b_id"));
        businessTaskInfo.put("operate", businessTaskInfo.get("operate"));
        businessTaskInfo.put("taskCron", businessTaskInfo.get("task_cron"));
        businessTaskInfo.put("createTime", businessTaskInfo.get("create_time"));
        businessTaskInfo.put("taskName", businessTaskInfo.get("task_name"));
        businessTaskInfo.put("state", businessTaskInfo.get("state"));
        businessTaskInfo.put("templateId", businessTaskInfo.get("template_id"));
        businessTaskInfo.put("taskId", businessTaskInfo.get("task_id"));
        businessTaskInfo.remove("bId");
        businessTaskInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessTask 定时任务信息
     */
    protected void autoSaveDelBusinessTask(Business business, JSONObject businessTask) {
//自动插入DEL
        Map info = new HashMap();
        info.put("taskId", businessTask.getString("taskId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentTaskInfos = getTaskServiceDaoImpl().getTaskInfo(info);
        if (currentTaskInfos == null || currentTaskInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentTaskInfo = currentTaskInfos.get(0);

        currentTaskInfo.put("bId", business.getbId());

        currentTaskInfo.put("operate", currentTaskInfo.get("operate"));
        currentTaskInfo.put("taskCron", currentTaskInfo.get("task_cron"));
        currentTaskInfo.put("createTime", currentTaskInfo.get("create_time"));
        currentTaskInfo.put("taskName", currentTaskInfo.get("task_name"));
        currentTaskInfo.put("state", currentTaskInfo.get("state"));
        currentTaskInfo.put("templateId", currentTaskInfo.get("template_id"));
        currentTaskInfo.put("taskId", currentTaskInfo.get("task_id"));


        currentTaskInfo.put("operate", StatusConstant.OPERATE_DEL);
        getTaskServiceDaoImpl().saveBusinessTaskInfo(currentTaskInfo);
        for (Object key : currentTaskInfo.keySet()) {
            if (businessTask.get(key) == null) {
                businessTask.put(key.toString(), currentTaskInfo.get(key));
            }
        }
    }


}
