package com.java110.api.listener.inspectionTaskDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspectionTaskDetail.IInspectionTaskDetailBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeInspectionTaskDetailConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveInspectionTaskDetailListener")
public class SaveInspectionTaskDetailListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionTaskDetailBMO inspectionTaskDetailBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "taskDetailId", "请求报文中未包含taskDetailId");
        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "inspectionId", "请求报文中未包含inspectionId");
        Assert.hasKeyAndValue(reqJson, "inspectionName", "请求报文中未包含inspectionName");
        Assert.hasKeyAndValue(reqJson, "signType", "请求报文中未包含signType");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(inspectionTaskDetailBMOImpl.addInspectionTaskDetail(reqJson, context));

        ResponseEntity<String> responseEntity = inspectionTaskDetailBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionTaskDetailConstant.ADD_INSPECTIONTASKDETAIL;
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
