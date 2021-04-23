package com.java110.common.bmo.workflowAuditMessage.impl;

import com.java110.common.bmo.workflowAuditMessage.IDeleteWorkflowAuditMessageBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.java110.po.workflowAuditMessage.WorkflowAuditMessagePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteWorkflowAuditMessageBMOImpl")
public class DeleteWorkflowAuditMessageBMOImpl implements IDeleteWorkflowAuditMessageBMO {

    @Autowired
    private IWorkflowAuditMessageInnerServiceSMO workflowAuditMessageInnerServiceSMOImpl;

    /**
     * @param workflowAuditMessagePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(WorkflowAuditMessagePo workflowAuditMessagePo) {

        int flag = workflowAuditMessageInnerServiceSMOImpl.deleteWorkflowAuditMessage(workflowAuditMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
