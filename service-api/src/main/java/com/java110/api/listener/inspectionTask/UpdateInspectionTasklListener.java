package com.java110.api.listener.inspectionTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspectionTask.IInspectionTaskBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.inspectionPlan.InspectionTaskDto;
import com.java110.entity.center.AppService;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeInspectionTaskConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

/**
 * 保存巡检任务明细侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateInspectionTaskListener")
public class UpdateInspectionTasklListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionTaskBMO inspectionTaskBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "请求报文中未包含inspectionPlanId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {

        InspectionTaskDto inspectionTaskDto1 = new InspectionTaskDto();
        inspectionTaskDto1.setCommunityId(reqJson.getString("communityId"));
        inspectionTaskDto1.setTaskId(reqJson.getString("taskId"));
        List<InspectionTaskDto>  inspectionTaskDtoList = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto1);
        if (inspectionTaskDtoList.size()!= 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "未找到巡检任务信息或找到多条！");
            context.setResponseEntity(responseEntity);
            return;
        }
        inspectionTaskBMOImpl.updateInspectionTask(reqJson, context);

    }


    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionTaskConstant.UPDATE_INSPECTIONTASK;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
