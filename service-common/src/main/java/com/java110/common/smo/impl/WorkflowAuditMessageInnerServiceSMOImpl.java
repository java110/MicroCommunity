package com.java110.common.smo.impl;


import com.java110.common.dao.IWorkflowAuditMessageServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.workflowAuditMessage.WorkflowAuditMessageDto;
import com.java110.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.java110.po.workflowAuditMessage.WorkflowAuditMessagePo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 流程审核表内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class WorkflowAuditMessageInnerServiceSMOImpl extends BaseServiceSMO implements IWorkflowAuditMessageInnerServiceSMO {

    @Autowired
    private IWorkflowAuditMessageServiceDao workflowAuditMessageServiceDaoImpl;


    @Override
    public int saveWorkflowAuditMessage(@RequestBody WorkflowAuditMessagePo workflowAuditMessagePo) {
        int saveFlag = 1;
        workflowAuditMessageServiceDaoImpl.saveWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessagePo));
        return saveFlag;
    }

    @Override
    public int updateWorkflowAuditMessage(@RequestBody WorkflowAuditMessagePo workflowAuditMessagePo) {
        int saveFlag = 1;
        workflowAuditMessageServiceDaoImpl.updateWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessagePo));
        return saveFlag;
    }

    @Override
    public int deleteWorkflowAuditMessage(@RequestBody WorkflowAuditMessagePo workflowAuditMessagePo) {
        int saveFlag = 1;
        workflowAuditMessagePo.setStatusCd("1");
        workflowAuditMessageServiceDaoImpl.updateWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessagePo));
        return saveFlag;
    }

    @Override
    public List<WorkflowAuditMessageDto> queryWorkflowAuditMessages(@RequestBody WorkflowAuditMessageDto workflowAuditMessageDto) {

        //校验是否传了 分页信息

        int page = workflowAuditMessageDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            workflowAuditMessageDto.setPage((page - 1) * workflowAuditMessageDto.getRow());
        }

        List<WorkflowAuditMessageDto> workflowAuditMessages = BeanConvertUtil.covertBeanList(workflowAuditMessageServiceDaoImpl.getWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessageDto)), WorkflowAuditMessageDto.class);

        return workflowAuditMessages;
    }


    @Override
    public int queryWorkflowAuditMessagesCount(@RequestBody WorkflowAuditMessageDto workflowAuditMessageDto) {
        return workflowAuditMessageServiceDaoImpl.queryWorkflowAuditMessagesCount(BeanConvertUtil.beanCovertMap(workflowAuditMessageDto));
    }

    public IWorkflowAuditMessageServiceDao getWorkflowAuditMessageServiceDaoImpl() {
        return workflowAuditMessageServiceDaoImpl;
    }

    public void setWorkflowAuditMessageServiceDaoImpl(IWorkflowAuditMessageServiceDao workflowAuditMessageServiceDaoImpl) {
        this.workflowAuditMessageServiceDaoImpl = workflowAuditMessageServiceDaoImpl;
    }
}
