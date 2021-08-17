package com.java110.common.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.dto.workflow.WorkflowDto;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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


    /**
     * 更新流程
     *
     * @param modelId     模型ID
     * @param name        流程模型名称
     * @param description
     * @param json_xml    流程文件
     * @param svg_xml     图片
     */
    @RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveModel(@PathVariable String modelId
            , String name, String description
            , String json_xml, String svg_xml) {
        try {
            Model model = repositoryService.getModel(modelId);
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put(MODEL_NAME, name);
            modelJson.put(MODEL_DESCRIPTION, description);
            modelJson.put(ModelDataJsonConstants.MODEL_REVISION, model.getVersion() + 1);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));

            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();
        } catch (Exception e) {
            logger.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }


    /**
     * 获取model的节点信息，编辑器根据返回的json进行绘图
     * @ServiceCode /workflow/getEditorJson
     * @param modelId
     * @return
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

    /**
     * 获取编辑器组件及配置项信息
     * 获取流程json文件
     *
     * @return
     */
    @RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        try {
            return IOUtils.toString(stencilsetStream, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }


}
