package com.java110.api.listener.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.complaint.IComplaintBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.smo.complaint.IComplaintInnerServiceSMO;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeComplaintConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存投诉建议侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateComplaintListener")
public class UpdateComplaintListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IComplaintBMO complaintBMOImpl;

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "complaintId", "投诉ID不能为空");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择投诉类型");
        //Assert.hasKeyAndValue(reqJson, "roomId", "必填，请选择房屋编号");
        Assert.hasKeyAndValue(reqJson, "complaintName", "必填，请填写投诉人");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写投诉电话");
        //Assert.hasKeyAndValue(reqJson, "state", "必填，请填写投诉状态");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写投诉内容");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        complaintBMOImpl.upComplaint(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeComplaintConstant.UPDATE_COMPLAINT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IComplaintInnerServiceSMO getComplaintInnerServiceSMOImpl() {
        return complaintInnerServiceSMOImpl;
    }

    public void setComplaintInnerServiceSMOImpl(IComplaintInnerServiceSMO complaintInnerServiceSMOImpl) {
        this.complaintInnerServiceSMOImpl = complaintInnerServiceSMOImpl;
    }
}
