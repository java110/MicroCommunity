package com.java110.common.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.dto.workflow.WorkflowDto;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/workflow")
public class WorkflowApi {

    @Autowired
    private IQueryWorkFlowFirstStaffBMO queryWorkFlowFirstStaffBMOImpl;

    @RequestMapping(value = "/getFirstStaff", method = RequestMethod.GET)
    public ResponseEntity<String> getFirstStaff(@RequestParam(name = "flowType") String flowType,
                                                @RequestParam(name = "communityId") String communityId,
                                                @RequestHeader(value = "store-id") String storeId) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(communityId);
        workflowDto.setFlowType(flowType);
        workflowDto.setStoreId(storeId);
        return queryWorkFlowFirstStaffBMOImpl.query(workflowDto);
    }

    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 创建模型
     * @param response
     * @param name 模型名称
     * @param key 模型key
     */
    @RequestMapping("/create")
    @ResponseBody
    public String create(HttpServletResponse response, String name, String key) throws IOException {
        logger.info("创建模型入参name：{},key:{}",name,key);
        Model model = repositoryService.newModel();
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        createObjectNode(model.getId());
        logger.info("创建模型结束，返回模型ID：{}",model.getId());
        return model.getId();
    }

    /**
     * 创建模型时完善ModelEditorSource
     * @param modelId
     */
    @SuppressWarnings("deprecation")
    private void createObjectNode(String modelId){
        logger.info("创建模型完善ModelEditorSource入参模型ID：{}",modelId);
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace","http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(modelId,editorNode.toString().getBytes("utf-8"));
        } catch (Exception e) {
            logger.info("创建模型时完善ModelEditorSource服务异常：{}",e);
        }
        logger.info("创建模型完善ModelEditorSource结束");
    }
}
