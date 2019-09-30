package com.java110.api.listener.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCodeBasePrivilegeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.menu.IMenuInnerServiceSMO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteBasePrivilegeListener")
public class DeleteBasePrivilegeListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "pId", "权限ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        ResponseEntity<String> responseEntity = null;

        BasePrivilegeDto basePrivilegeDto = BeanConvertUtil.covertBean(reqJson, BasePrivilegeDto.class);


        int saveFlag = menuInnerServiceSMOImpl.deleteBasePrivilege(basePrivilegeDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeBasePrivilegeConstant.DELETE_BASEPRIVILEGE;
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
