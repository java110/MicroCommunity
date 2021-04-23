package com.java110.common.bmo.workflowAuditMessage.impl;

import com.java110.common.bmo.workflowAuditMessage.ISaveWorkflowAuditMessageBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.java110.po.workflowAuditMessage.WorkflowAuditMessagePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveWorkflowAuditMessageBMOImpl")
public class SaveWorkflowAuditMessageBMOImpl implements ISaveWorkflowAuditMessageBMO {

    @Autowired
    private IWorkflowAuditMessageInnerServiceSMO workflowAuditMessageInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param workflowAuditMessagePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(WorkflowAuditMessagePo workflowAuditMessagePo) {

        workflowAuditMessagePo.setAuditId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auditId));
        int flag = workflowAuditMessageInnerServiceSMOImpl.saveWorkflowAuditMessage(workflowAuditMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
