package com.java110.api.listener.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.common.IAuditUserInnerServiceSMO;
import com.java110.dto.auditUser.AuditUserDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAuditUserConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.auditUser.ApiAuditUserDataVo;
import com.java110.vo.api.auditUser.ApiAuditUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listAuditUsersListener")
public class ListAuditUsersListener extends AbstractServiceApiListener {

    @Autowired
    private IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAuditUserConstant.LIST_AUDITUSERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAuditUserInnerServiceSMO getAuditUserInnerServiceSMOImpl() {
        return auditUserInnerServiceSMOImpl;
    }

    public void setAuditUserInnerServiceSMOImpl(IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl) {
        this.auditUserInnerServiceSMOImpl = auditUserInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");

        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AuditUserDto auditUserDto = BeanConvertUtil.covertBean(reqJson, AuditUserDto.class);

        int count = auditUserInnerServiceSMOImpl.queryAuditUsersCount(auditUserDto);

        List<ApiAuditUserDataVo> auditUsers = null;

        if (count > 0) {
            auditUsers = BeanConvertUtil.covertBeanList(auditUserInnerServiceSMOImpl.queryAuditUsers(auditUserDto), ApiAuditUserDataVo.class);
        } else {
            auditUsers = new ArrayList<>();
        }

        ApiAuditUserVo apiAuditUserVo = new ApiAuditUserVo();

        apiAuditUserVo.setTotal(count);
        apiAuditUserVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAuditUserVo.setAuditUsers(auditUsers);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAuditUserVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
