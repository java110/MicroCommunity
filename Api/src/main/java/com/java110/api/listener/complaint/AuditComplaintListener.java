package com.java110.api.listener.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.complaint.IComplaintBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.complaint.IComplaintInnerServiceSMO;
import com.java110.core.smo.complaintUser.IComplaintUserInnerServiceSMO;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeComplaintConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.complaint.ApiComplaintDataVo;
import com.java110.vo.api.complaint.ApiComplaintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("auditComplaintListener")
public class AuditComplaintListener extends AbstractServiceApiListener {

    @Autowired
    private IComplaintBMO complaintBMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;


    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeComplaintConstant.AUDIT_COMPLAINT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IComplaintUserInnerServiceSMO getComplaintUserInnerServiceSMOImpl() {
        return complaintUserInnerServiceSMOImpl;
    }

    public void setComplaintUserInnerServiceSMOImpl(IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl) {
        this.complaintUserInnerServiceSMOImpl = complaintUserInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "complaintId", "投诉ID不能为空");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "taskId", "必填，请填写任务ID");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写批注");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户信息");
        //super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setTaskId(reqJson.getString("taskId"));
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setStoreId(reqJson.getString("storeId"));
        complaintDto.setAuditCode(reqJson.getString("state"));
        complaintDto.setAuditMessage(reqJson.getString("remark"));
        complaintDto.setCurrentUserId(reqJson.getString("userId"));

        boolean isLastTask = complaintUserInnerServiceSMOImpl.completeTask(complaintDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        if (isLastTask) {
            HttpHeaders header = new HttpHeaders();
            context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
            JSONArray businesses = new JSONArray();
            AppService service = event.getAppService();
            //添加单元信息
            businesses.add(complaintBMOImpl.updateComplaint(reqJson, context));

            responseEntity = complaintBMOImpl.callService(context, service.getServiceCode(), businesses);
        }
        context.setResponseEntity(responseEntity);

    }

    public IComplaintInnerServiceSMO getComplaintInnerServiceSMOImpl() {
        return complaintInnerServiceSMOImpl;
    }

    public void setComplaintInnerServiceSMOImpl(IComplaintInnerServiceSMO complaintInnerServiceSMOImpl) {
        this.complaintInnerServiceSMOImpl = complaintInnerServiceSMOImpl;
    }
}
