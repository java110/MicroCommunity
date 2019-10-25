package com.java110.common.activity.resourceEnter;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.audit.AuditUser;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 部门领导 监听类
 */
public class ResourceEnterToDepartmentListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

        //查询相应的相应审核人员

        System.out.println("打印 对象 delegateTask：" + JSONObject.toJSONString(delegateTask));

        AuditUser user = new AuditUser();
        user.setUserId("1234567890");
        user.setUserName("吴学文");
        user.setAuditLink("department");
        user.setObjCode("ResourceEnter");
        delegateTask.setVariable(user.getUserId(), user);
    }
}
