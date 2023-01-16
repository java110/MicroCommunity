package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.utils.util.BeanConvertUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflow")
public class WorkflowApi {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowApi.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    String MODEL_ID = "modelId";
    String MODEL_NAME = "name";
    String MODEL_REVISION = "revision";
    String MODEL_DESCRIPTION = "description";

    @Autowired
    private IQueryWorkFlowFirstStaffBMO queryWorkFlowFirstStaffBMOImpl;

    /**
     * 更新流程
     *
     * @param reqString 模型ID
     * @ServiceCode /workflow/saveModel
     */
    @RequestMapping(value = "/saveModel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> saveModel(@RequestBody String reqString) {
        JSONObject reqJson = JSONObject.parseObject(reqString);
        WorkflowModelDto workflowModelDto = BeanConvertUtil.covertBean(reqJson, WorkflowModelDto.class);
        //部署model
        return queryWorkFlowFirstStaffBMOImpl.saveModel(workflowModelDto);
    }

    /**
     * 部署流程
     *
     * @param reqString 模型ID
     * @ServiceCode /workflow/deployModel
     */
    @RequestMapping(value = "/deployModel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> deployModel(@RequestBody String reqString) {
        JSONObject reqJson = JSONObject.parseObject(reqString);
        WorkflowModelDto workflowModelDto = BeanConvertUtil.covertBean(reqJson, WorkflowModelDto.class);
        //部署model
        return queryWorkFlowFirstStaffBMOImpl.deployModel(workflowModelDto);
    }


    /**
     * 获取model的节点信息，编辑器根据返回的json进行绘图
     *
     * @param modelId
     * @return
     * @ServiceCode /workflow/getEditorJson
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/getEditorJson", method = RequestMethod.GET)
    public ResponseEntity getEditorJson(@RequestParam(value = "modelId") String modelId) {
        ObjectNode modelNode = null;
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(MODEL_NAME, model.getName());
                }
                modelNode.put(MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(
                        new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
                modelNode.put("model", editorJsonNode);
            } catch (Exception e) {
                logger.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return new ResponseEntity(modelNode.toString(), HttpStatus.OK);
    }
}
