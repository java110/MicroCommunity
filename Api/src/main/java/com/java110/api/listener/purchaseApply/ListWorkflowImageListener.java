package com.java110.api.listener.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.purchaseApply.IPurchaseApplyInnerServiceSMO;
import com.java110.core.smo.purchaseApplyUser.IActivitiWorkflowImageInnerServiceSMO;
import com.java110.core.smo.purchaseApplyUser.IPurchaseApplyUserInnerServiceSMO;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePurchaseApplyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyDataVo;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyVo;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listWorkflowImageListener")
public class ListWorkflowImageListener extends AbstractServiceApiListener {

    @Autowired
    private IActivitiWorkflowImageInnerServiceSMO activitiWorkflowImageInnerServiceSMOImpl;



    @Override
    public String getServiceCode() {
        return ServiceCodePurchaseApplyConstant.LIST_WORKFLOW_IMAGE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskId", "必填，请填写任务ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        String taskId = reqJson.getString("taskId");

        String image = activitiWorkflowImageInnerServiceSMOImpl.getWorkflowImage(taskId);
        MultiValueMap headers = new HttpHeaders();
        headers.add("Accept-Ranges", "bytes");
        headers.add("content-type", "application/octet-stream");

        try {
            byte[] contextByte = Base64Convert.base64ToByte(image
                    .replace("data:image/webp;base64,", "")
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,", "")
            );
            ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(contextByte, headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        }catch (Exception e){
            ResponseEntity<Object> responseEntity = new ResponseEntity<Object>("查询失败", HttpStatus.BAD_REQUEST);
            context.setResponseEntity(responseEntity);
        }

    }
}
