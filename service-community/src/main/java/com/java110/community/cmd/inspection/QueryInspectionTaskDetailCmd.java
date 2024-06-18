package com.java110.community.cmd.inspection;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskDetailV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailDataVo;
import com.java110.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询巡检明细
 */
@Java110Cmd(serviceCode = "inspection.queryInspectionTaskDetail")
public class QueryInspectionTaskDetailCmd extends Cmd {

    @Autowired
    private IInspectionTaskDetailV1InnerServiceSMO inspectionTaskDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        InspectionTaskDetailDto inspectionTaskDetailDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDetailDto.class);
        int count = inspectionTaskDetailV1InnerServiceSMOImpl.queryInspectionTaskDetailsCount(inspectionTaskDetailDto);
        List<InspectionTaskDetailDto> inspectionTaskDetails = null;
        if (count > 0) {
            inspectionTaskDetails = BeanConvertUtil.covertBeanList(
                    inspectionTaskDetailV1InnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto),
                    InspectionTaskDetailDto.class);
        } else {
            inspectionTaskDetails = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, inspectionTaskDetails);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
