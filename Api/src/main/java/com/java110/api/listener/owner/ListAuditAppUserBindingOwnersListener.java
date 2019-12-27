package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.owner.IOwnerAppUserInnerServiceSMO;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.auditAppUserBindingOwner.ApiAuditAppUserBindingOwnerDataVo;
import com.java110.vo.api.auditAppUserBindingOwner.ApiAuditAppUserBindingOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listAuditAppUserBindingOwnersListener")
public class ListAuditAppUserBindingOwnersListener extends AbstractServiceApiListener {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS;
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
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);

        int count = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsersCount(ownerAppUserDto);

        List<ApiAuditAppUserBindingOwnerDataVo> auditAppUserBindingOwners = null;

        if (count > 0) {
            auditAppUserBindingOwners = BeanConvertUtil.covertBeanList(ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto), ApiAuditAppUserBindingOwnerDataVo.class);
        } else {
            auditAppUserBindingOwners = new ArrayList<>();
        }

        ApiAuditAppUserBindingOwnerVo apiAuditAppUserBindingOwnerVo = new ApiAuditAppUserBindingOwnerVo();

        apiAuditAppUserBindingOwnerVo.setTotal(count);
        apiAuditAppUserBindingOwnerVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAuditAppUserBindingOwnerVo.setAuditAppUserBindingOwners(auditAppUserBindingOwners);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAuditAppUserBindingOwnerVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IOwnerAppUserInnerServiceSMO getOwnerAppUserInnerServiceSMOImpl() {
        return ownerAppUserInnerServiceSMOImpl;
    }

    public void setOwnerAppUserInnerServiceSMOImpl(IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl) {
        this.ownerAppUserInnerServiceSMOImpl = ownerAppUserInnerServiceSMOImpl;
    }
}
