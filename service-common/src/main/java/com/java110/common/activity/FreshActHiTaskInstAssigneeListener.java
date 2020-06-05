package com.java110.common.activity;

import com.java110.common.dao.IAuditUserServiceDao;
import com.java110.utils.factory.ApplicationContextFactory;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 刷新 ActHisTaskInst assignee 字段为空的情况
 */
public class FreshActHiTaskInstAssigneeListener implements TaskListener {
    private IAuditUserServiceDao auditUserServiceDaoImpl;

    @Override
    public void notify(DelegateTask delegateTask) {

        auditUserServiceDaoImpl = ApplicationContextFactory.getBean("auditUserServiceDaoImpl", IAuditUserServiceDao.class);

        //ApplicationContextFactory.getBean("");

        String userId = delegateTask.getVariable("currentUserId").toString();

        String taskId = delegateTask.getId();
        Map info = new HashMap();
        info.put("userId", userId);
        info.put("taskId", taskId);
        auditUserServiceDaoImpl.freshActHiTaskInstAssignee(info);

    }
}
