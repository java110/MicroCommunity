package com.java110.common.activity.resourceEnter;

import com.java110.core.smo.audit.IAuditUserInnerServiceSMO;
import com.java110.dto.auditUser.AuditUserDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceOrderDto;
import com.java110.entity.audit.AuditUser;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 部门领导 监听类
 */
public class ResourceEnterToDepartmentListener implements TaskListener , ExecutionListener {
    private final static Logger logger = LoggerFactory.getLogger(ResourceEnterToDepartmentListener.class);

    private IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl;

    @Override
    public void notify(DelegateTask delegateTask) {
        logger.info("查询部门审核人员");

//        auditUserInnerServiceSMOImpl = ApplicationContextFactory.getBean("auditUserInnerServiceSMOImpl", IAuditUserInnerServiceSMO.class);
//        AuditUserDto auditUserDto = new AuditUserDto();
//        PurchaseApplyDto purchaseApplyDto = (PurchaseApplyDto)delegateTask.getVariable("purchaseApplyDto");
        String nextAuditStaffId = delegateTask.getVariable("nextAuditStaffId").toString();
        /*auditUserDto.setStoreId(purchaseApplyDto.getStoreId());
        auditUserDto.setObjCode("resourceEntry");
        auditUserDto.setAuditLink("809001");
        List<AuditUserDto> auditUserDtos = auditUserInnerServiceSMOImpl.queryAuditUsers(auditUserDto);


        for (AuditUserDto tmpAuditUser : auditUserDtos) {
            AuditUser auditUser = BeanConvertUtil.covertBean(tmpAuditUser, AuditUser.class);
            logger.info("查询到用户："+tmpAuditUser.getUserName());

            delegateTask.setVariable(auditUser.getUserId(), auditUser);

        }

        logger.info("查询审核人员人数："+auditUserDtos.size());

        if (auditUserDtos == null || auditUserDtos.size() < 1) {
            return;
        }*/

        delegateTask.setAssignee(nextAuditStaffId);
        logger.info("设置部门审核人员："+nextAuditStaffId);
    }


    @Override
    public void notify(DelegateExecution execution) {

    }
}
