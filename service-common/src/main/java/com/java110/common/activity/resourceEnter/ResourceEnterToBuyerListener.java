package com.java110.common.activity.resourceEnter;

import com.java110.intf.common.IAuditUserInnerServiceSMO;
import com.java110.dto.auditUser.AuditUserDto;
import com.java110.dto.resourceStore.ResourceOrderDto;
import com.java110.entity.audit.AuditUser;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

/**
 * 采购人员采购
 */
public class ResourceEnterToBuyerListener implements TaskListener , ExecutionListener {

    private IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl;

    @Override
    public void notify(DelegateTask delegateTask) {

        auditUserInnerServiceSMOImpl = ApplicationContextFactory.getBean("auditUserInnerServiceSMOImpl", IAuditUserInnerServiceSMO.class);
        AuditUserDto auditUserDto = new AuditUserDto();
        ResourceOrderDto resourceOrderDto = (ResourceOrderDto)delegateTask.getVariable("resourceOrderDto");
        auditUserDto.setStoreId(resourceOrderDto.getStoreId());
        auditUserDto.setObjCode("resourceEntry");
        auditUserDto.setAuditLink("809003");
        List<AuditUserDto> auditUserDtos = auditUserInnerServiceSMOImpl.queryAuditUsers(auditUserDto);

        for (AuditUserDto tmpAuditUser : auditUserDtos) {
            AuditUser auditUser = BeanConvertUtil.covertBean(tmpAuditUser, AuditUser.class);

            delegateTask.setVariable(auditUser.getUserId(), auditUser);

        }
    }

    @Override
    public void notify(DelegateExecution execution) {

    }
}
