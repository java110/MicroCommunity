package com.java110.community.cmd.inspection;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.intf.community.IInspectionTaskV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询巡检点对应的巡检任务
 */
@Java110Cmd(serviceCode = "inspection.queryPointInspectionTask")
public class QueryPointInspectionTaskCmd extends Cmd {

    @Autowired
    private IInspectionTaskV1InnerServiceSMO inspectionTaskV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);

        Assert.hasKeyAndValue(reqJson, "inspectionId", "未包含巡检点");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        InspectionTaskDto inspectionTaskDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDto.class);

        int count = inspectionTaskV1InnerServiceSMOImpl.queryPointInspectionTasksCount(inspectionTaskDto);

        List<InspectionTaskDto> inspectionTaskDtos = null;

        if (count > 0) {
            inspectionTaskDtos = BeanConvertUtil.covertBeanList(inspectionTaskV1InnerServiceSMOImpl.queryPointInspectionTasks(inspectionTaskDto), InspectionTaskDto.class);
        } else {
            inspectionTaskDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, inspectionTaskDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
