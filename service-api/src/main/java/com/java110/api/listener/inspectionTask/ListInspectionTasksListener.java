package com.java110.api.listener.inspectionTask;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IInspectionTaskInnerServiceSMO;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionTaskConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionTask.ApiInspectionTaskDataVo;
import com.java110.vo.api.inspectionTask.ApiInspectionTaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listInspectionTasksListener")
public class ListInspectionTasksListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionTaskConstant.LIST_INSPECTIONTASKS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IInspectionTaskInnerServiceSMO getInspectionTaskInnerServiceSMOImpl() {
        return inspectionTaskInnerServiceSMOImpl;
    }

    public void setInspectionTaskInnerServiceSMOImpl(IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl) {
        this.inspectionTaskInnerServiceSMOImpl = inspectionTaskInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        InspectionTaskDto inspectionTaskDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDto.class);

        int count = inspectionTaskInnerServiceSMOImpl.queryInspectionTasksCount(inspectionTaskDto);

        List<ApiInspectionTaskDataVo> inspectionTasks = null;

        if (count > 0) {
            inspectionTasks = BeanConvertUtil.covertBeanList(inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto), ApiInspectionTaskDataVo.class);
        } else {
            inspectionTasks = new ArrayList<>();
        }

        ApiInspectionTaskVo apiInspectionTaskVo = new ApiInspectionTaskVo();

        apiInspectionTaskVo.setTotal(count);
        apiInspectionTaskVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionTaskVo.setInspectionTasks(inspectionTasks);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionTaskVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
