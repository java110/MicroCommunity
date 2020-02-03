package com.java110.report.smo.fee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.report.smo.fee.IListFeeSummarySMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询app服务类
 */
@Service("listFeeSummarySMOImpl")
public class ListFeeSummarySMOImpl extends AbstractComponentSMO implements IListFeeSummarySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> list(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);
        Assert.hasKeyAndValue(paramIn, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(paramIn, "feeSummaryType", "未包含小区信息");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        int page = paramIn.getInteger("page");
        int row = paramIn.getInteger("row");
        paramIn.put("page", (page - 1) * row);
        paramIn.put("row", page * row);

        if("1001".equals(paramIn.getString("feeSummaryType"))){//日
            paramIn.put("formatStr","%Y-%m-%d");
        }else if("1101".equals(paramIn.getString("feeSummaryType"))){
            paramIn.put("formatStr","%Y-%m");
        }else{
            paramIn.put("formatStr","%Y");
        }

        String apiUrl = "";
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/api.queryFeeSummary" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
