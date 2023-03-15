package com.java110.oa.bmo.oaWorkflow.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflow.ISaveOaWorkflowBMO;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BpmnXml;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveOaWorkflowBMOImpl")
public class SaveOaWorkflowBMOImpl implements ISaveOaWorkflowBMO {

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param oaWorkflowPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(OaWorkflowPo oaWorkflowPo) {
        oaWorkflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));

        //创建model
        WorkflowModelDto workflowModelDto = new WorkflowModelDto();
        workflowModelDto.setName(oaWorkflowPo.getFlowName());
        workflowModelDto.setKey(oaWorkflowPo.getFlowId());
        workflowModelDto = workflowInnerServiceSMOImpl.createModel(workflowModelDto);

        oaWorkflowPo.setModelId(workflowModelDto.getModelId());
        oaWorkflowPo.setFlowKey(workflowModelDto.getKey());
        oaWorkflowPo.setState(OaWorkflowDto.STATE_WAIT);
        int flag = oaWorkflowInnerServiceSMOImpl.saveOaWorkflow(oaWorkflowPo);

        if (flag <1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        //默认 流程图以防画错
        OaWorkflowXmlPo oaWorkflowXmlPo = new OaWorkflowXmlPo();
        oaWorkflowXmlPo.setStoreId(oaWorkflowPo.getStoreId());
        oaWorkflowXmlPo.setFlowId(oaWorkflowPo.getFlowId());
        oaWorkflowXmlPo.setXmlId(GenerateCodeFactory.getGeneratorId("79"));
        oaWorkflowXmlPo.setSvgXml("");
        oaWorkflowXmlPo.setBpmnXml(BpmnXml.getDefaultBpmnXml(oaWorkflowPo.getFlowId()));

        flag = oaWorkflowXmlInnerServiceSMOImpl.saveOaWorkflowXml(oaWorkflowXmlPo);
        if (flag < 1) {
            throw new CmdException("保存模型数据失败");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
