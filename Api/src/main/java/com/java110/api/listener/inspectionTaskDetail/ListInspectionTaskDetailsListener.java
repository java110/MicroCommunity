package com.java110.api.listener.inspectionTaskDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionTaskDetail.IInspectionTaskDetailInnerServiceSMO;
import com.java110.dto.inspectionTaskDetail.InspectionTaskDetailDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionTaskDetailConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailDataVo;
import com.java110.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listInspectionTaskDetailsListener")
public class ListInspectionTaskDetailsListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionTaskDetailConstant.LIST_INSPECTIONTASKDETAILS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IInspectionTaskDetailInnerServiceSMO getInspectionTaskDetailInnerServiceSMOImpl() {
        return inspectionTaskDetailInnerServiceSMOImpl;
    }

    public void setInspectionTaskDetailInnerServiceSMOImpl(IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl) {
        this.inspectionTaskDetailInnerServiceSMOImpl = inspectionTaskDetailInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        InspectionTaskDetailDto inspectionTaskDetailDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDetailDto.class);

        int count = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetailsCount(inspectionTaskDetailDto);

        List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails = null;

        if (count > 0) {
            inspectionTaskDetails = BeanConvertUtil.covertBeanList(inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto), ApiInspectionTaskDetailDataVo.class);
        } else {
            inspectionTaskDetails = new ArrayList<>();
        }

        ApiInspectionTaskDetailVo apiInspectionTaskDetailVo = new ApiInspectionTaskDetailVo();

        apiInspectionTaskDetailVo.setTotal(count);
        apiInspectionTaskDetailVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionTaskDetailVo.setInspectionTaskDetails(inspectionTaskDetails);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionTaskDetailVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
