package com.java110.community.cmd.inspectionTask;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskV1InnerServiceSMO;
import com.java110.po.inspection.InspectionTaskPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "inspectionTask.updateInspectionTask")
public class UpdateInspectionTaskCmd extends Cmd {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskV1InnerServiceSMO inspectionTaskV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "请求报文中未包含inspectionPlanId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionTaskDto inspectionTaskDto1 = new InspectionTaskDto();
        inspectionTaskDto1.setCommunityId(reqJson.getString("communityId"));
        inspectionTaskDto1.setTaskId(reqJson.getString("taskId"));
        List<InspectionTaskDto> inspectionTaskDtoList = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto1);
        if (inspectionTaskDtoList.size()!= 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "未找到巡检任务信息或找到多条！");
            context.setResponseEntity(responseEntity);
            return;
        }

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setTaskId(reqJson.getString("taskId"));
        inspectionTaskDto.setCommunityId(reqJson.getString("communityId"));
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        Assert.listOnlyOne(inspectionTaskDtos, "未找到需要修改的巡检任务 或多条数据");

        JSONObject businessInspectionTask = new JSONObject();
        businessInspectionTask.putAll(BeanConvertUtil.beanCovertMap(inspectionTaskDtos.get(0)));

        InspectionTaskPo inspectionTaskPo = BeanConvertUtil.covertBean(businessInspectionTask, InspectionTaskPo.class);
        //这里时间不做修改，有可能还没有巡检
        //inspectionTaskPo.setActInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        inspectionTaskPo.setActUserId(reqJson.getString("userId"));
        inspectionTaskPo.setActUserName(reqJson.getString("userName"));
        inspectionTaskPo.setState(reqJson.getString("state"));
        if(!StringUtil.isEmpty(reqJson.getString("taskType")) && reqJson.getString("taskType").equals("2000") ){
//            inspectionTaskPo.setOriginalPlanUserId(inspectionTaskPo.getPlanUserId());
//            inspectionTaskPo.setOriginalPlanUserName(inspectionTaskPo.getPlanUserName());
            inspectionTaskPo.setPlanUserId(reqJson.getString("staffId"));
            inspectionTaskPo.setPlanUserName(reqJson.getString("staffName"));
            inspectionTaskPo.setTaskType(reqJson.getString("taskType"));
            inspectionTaskPo.setTransferDesc(reqJson.getString("transferDesc"));
        }

        int flag = inspectionTaskV1InnerServiceSMOImpl.updateInspectionTask(inspectionTaskPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }
    }
}
