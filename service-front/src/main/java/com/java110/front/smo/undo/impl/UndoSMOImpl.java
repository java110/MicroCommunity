package com.java110.front.smo.undo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.undo.IUndoSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询carInout服务类
 */
@Service("undoSMOImpl")
public class UndoSMOImpl extends AbstractComponentSMO implements IUndoSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listUndos(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        //Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(pd);

        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_CARINOUT);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);


        paramIn.put("storeId", result.getStoreId());
        paramIn.put("userId", result.getUserId());
        paramIn.put("staffId", result.getUserId());

        JSONObject doing = new JSONObject();
        //查询 报修待办
        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/ownerRepair.listStaffRepairs" + mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            doing.put("repair", paramOut.getString("total"));
        } else {
            doing.put("repair", "0");
        }

        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/auditUser.listAuditComplaints" + mapToUrlParam(paramIn);
        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            doing.put("complaint", paramOut.getString("total"));
        } else {
            doing.put("complaint", "0");
        }
        //采购待办
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/auditUser.listAuditOrders" + mapToUrlParam(paramIn);
        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            doing.put("purchase", paramOut.getString("total"));
        } else {
            doing.put("purchase", "0");
        }
        //物品领用待办
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/collection/getCollectionAuditOrder" + mapToUrlParam(paramIn);
        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            doing.put("collection", paramOut.getString("total"));
        } else {
            doing.put("collection", "0");
        }

        //contract/queryContractTask
        //合同起草待办
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/contract/queryContractTask" + mapToUrlParam(paramIn);
        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            doing.put("contractApply", paramOut.getString("total"));
        } else {
            doing.put("contractApply", "0");
        }
        //contract/queryContractTask
        //合同变更
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/contract/queryContractChangeTask" + mapToUrlParam(paramIn);
        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            doing.put("contractChange", paramOut.getString("total"));
        } else {
            doing.put("contractChange", "0");
        }

        //合同变更
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/resourceStore.listAllocationStoreAuditOrders" + mapToUrlParam(paramIn);
        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            doing.put("allocation", paramOut.getString("total"));
        } else {
            doing.put("allocation", "0");
        }

        return ResultVo.createResponseEntity(doing);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
