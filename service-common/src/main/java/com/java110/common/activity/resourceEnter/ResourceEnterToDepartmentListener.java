package com.java110.common.activity.resourceEnter;

import com.java110.intf.common.IAuditUserInnerServiceSMO;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

/**
 * 部门领导 监听类
 */
public class ResourceEnterToDepartmentListener implements TaskListener , ExecutionListener {
    private final static Logger logger = LoggerFactory.getLogger(ResourceEnterToDepartmentListener.class);

    private IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl;

    @Override
    public void notify(DelegateTask delegateTask) {
        logger.info("查询部门审核人员");

        String nextAuditStaffId = delegateTask.getVariable("nextAuditStaffId").toString();

        delegateTask.setAssignee(nextAuditStaffId);
        logger.info("设置部门审核人员："+nextAuditStaffId);
    }


    @Override
    public void notify(DelegateExecution execution) {

    }
}
