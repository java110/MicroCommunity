package com.java110.common.bmo.workflowAuditMessage.impl;

import com.java110.common.bmo.workflowAuditMessage.IUpdateWorkflowAuditMessageBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.java110.po.workflowAuditMessage.WorkflowAuditMessagePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateWorkflowAuditMessageBMOImpl")
public class UpdateWorkflowAuditMessageBMOImpl implements IUpdateWorkflowAuditMessageBMO {

    @Autowired
    private IWorkflowAuditMessageInnerServiceSMO workflowAuditMessageInnerServiceSMOImpl;

    /**
     * @param workflowAuditMessagePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(WorkflowAuditMessagePo workflowAuditMessagePo) {

        int flag = workflowAuditMessageInnerServiceSMOImpl.updateWorkflowAuditMessage(workflowAuditMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
