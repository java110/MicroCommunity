package com.java110.common.activity.complaint;

import com.java110.core.smo.audit.IAuditUserInnerServiceSMO;
import com.java110.dto.auditUser.AuditUserDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.resourceStore.ResourceOrderDto;
import com.java110.entity.audit.AuditUser;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

/**
 * 采购人员采购
 */
public class ComplaintUserListener implements TaskListener {

    private IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl;

    @Override
    public void notify(DelegateTask delegateTask) {

        auditUserInnerServiceSMOImpl = ApplicationContextFactory.getBean("auditUserInnerServiceSMOImpl", IAuditUserInnerServiceSMO.class);
        AuditUserDto auditUserDto = new AuditUserDto();
        ComplaintDto complaintDto = (ComplaintDto)delegateTask.getVariable("complaintDto");
        auditUserDto.setStoreId(complaintDto.getStoreId());
        auditUserDto.setObjCode("complaint");
        auditUserDto.setAuditLink("809004");
        List<AuditUserDto> auditUserDtos = auditUserInnerServiceSMOImpl.queryAuditUsers(auditUserDto);

        for (AuditUserDto tmpAuditUser : auditUserDtos) {
            AuditUser auditUser = BeanConvertUtil.covertBean(tmpAuditUser, AuditUser.class);

            delegateTask.setVariable(auditUser.getUserId(), auditUser);

        }
    }
}
