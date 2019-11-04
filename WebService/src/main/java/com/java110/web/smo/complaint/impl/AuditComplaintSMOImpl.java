package com.java110.web.smo.complaint.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.web.smo.complaint.IAuditComplaintSMO;
import com.java110.web.smo.complaint.IListComplaintsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询complaint服务类
 */
@Service("auditComplaintSMOImpl")
public class AuditComplaintSMOImpl extends AbstractComponentSMO implements IAuditComplaintSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> auditComplaint(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "必填，请求报文中未包含小区信息");
        Assert.hasKeyAndValue(paramIn, "complaintId", "投诉ID不能为空");
        Assert.hasKeyAndValue(paramIn, "taskId", "必填，请求报文中未包含任务ID");
        Assert.hasKeyAndValue(paramIn, "state", "必填，请求报文中未包含状态");
        Assert.hasKeyAndValue(paramIn, "remark", "必填，请求报文中未包含审核信息");


        super.validatePageInfo(pd);

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_COMPLAINT);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/complaint.auditComplaint" ;


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                apiUrl,
                HttpMethod.POST);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
