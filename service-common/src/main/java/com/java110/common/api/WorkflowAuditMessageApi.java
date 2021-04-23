package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.workflowAuditMessage.IDeleteWorkflowAuditMessageBMO;
import com.java110.common.bmo.workflowAuditMessage.IGetWorkflowAuditMessageBMO;
import com.java110.common.bmo.workflowAuditMessage.ISaveWorkflowAuditMessageBMO;
import com.java110.common.bmo.workflowAuditMessage.IUpdateWorkflowAuditMessageBMO;
import com.java110.dto.workflowAuditMessage.WorkflowAuditMessageDto;
import com.java110.po.workflowAuditMessage.WorkflowAuditMessagePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/workflowAuditMessage")
public class WorkflowAuditMessageApi {

    @Autowired
    private ISaveWorkflowAuditMessageBMO saveWorkflowAuditMessageBMOImpl;
    @Autowired
    private IUpdateWorkflowAuditMessageBMO updateWorkflowAuditMessageBMOImpl;
    @Autowired
    private IDeleteWorkflowAuditMessageBMO deleteWorkflowAuditMessageBMOImpl;

    @Autowired
    private IGetWorkflowAuditMessageBMO getWorkflowAuditMessageBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /workflowAuditMessage/saveWorkflowAuditMessage
     * @path /app/workflowAuditMessage/saveWorkflowAuditMessage
     */
    @RequestMapping(value = "/saveWorkflowAuditMessage", method = RequestMethod.POST)
    public ResponseEntity<String> saveWorkflowAuditMessage(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "businessKey", "请求报文中未包含businessKey");
        Assert.hasKeyAndValue(reqJson, "auditPersonId", "请求报文中未包含auditPersonId");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "message", "请求报文中未包含message");


        WorkflowAuditMessagePo workflowAuditMessagePo = BeanConvertUtil.covertBean(reqJson, WorkflowAuditMessagePo.class);
        return saveWorkflowAuditMessageBMOImpl.save(workflowAuditMessagePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /workflowAuditMessage/updateWorkflowAuditMessage
     * @path /app/workflowAuditMessage/updateWorkflowAuditMessage
     */
    @RequestMapping(value = "/updateWorkflowAuditMessage", method = RequestMethod.POST)
    public ResponseEntity<String> updateWorkflowAuditMessage(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "businessKey", "请求报文中未包含businessKey");
        Assert.hasKeyAndValue(reqJson, "auditPersonId", "请求报文中未包含auditPersonId");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "message", "请求报文中未包含message");
        Assert.hasKeyAndValue(reqJson, "auditId", "auditId不能为空");


        WorkflowAuditMessagePo workflowAuditMessagePo = BeanConvertUtil.covertBean(reqJson, WorkflowAuditMessagePo.class);
        return updateWorkflowAuditMessageBMOImpl.update(workflowAuditMessagePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /workflowAuditMessage/deleteWorkflowAuditMessage
     * @path /app/workflowAuditMessage/deleteWorkflowAuditMessage
     */
    @RequestMapping(value = "/deleteWorkflowAuditMessage", method = RequestMethod.POST)
    public ResponseEntity<String> deleteWorkflowAuditMessage(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "auditId", "auditId不能为空");


        WorkflowAuditMessagePo workflowAuditMessagePo = BeanConvertUtil.covertBean(reqJson, WorkflowAuditMessagePo.class);
        return deleteWorkflowAuditMessageBMOImpl.delete(workflowAuditMessagePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /workflowAuditMessage/queryWorkflowAuditMessage
     * @path /app/workflowAuditMessage/queryWorkflowAuditMessage
     */
    @RequestMapping(value = "/queryWorkflowAuditMessage", method = RequestMethod.GET)
    public ResponseEntity<String> queryWorkflowAuditMessage(
                                                            @RequestHeader(value = "store-id",required = false) String storeId,
                                                            @RequestParam(value = "communityId") String communityId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        WorkflowAuditMessageDto workflowAuditMessageDto = new WorkflowAuditMessageDto();
        workflowAuditMessageDto.setPage(page);
        workflowAuditMessageDto.setRow(row);
        //workflowAuditMessageDto.setCommunityId(communityId);
        return getWorkflowAuditMessageBMOImpl.get(workflowAuditMessageDto);
    }
}
