package com.java110.api.listener.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.utils.constant.ServiceCodeBasePrivilegeConstant;

/**
 * 保存权限侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateBasePrivilegeListener")
public class UpdateBasePrivilegeListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "pId", "权限ID不能为空");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写权限名称");
        Assert.hasKeyAndValue(reqJson, "domain", "必填，请选择商户类型");
        Assert.hasKeyAndValue(reqJson, "resource", "必填，请选择资源路径");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        ResponseEntity<String> responseEntity = null;

        BasePrivilegeDto basePrivilegeDto = BeanConvertUtil.covertBean(reqJson, BasePrivilegeDto.class);


        int saveFlag = menuInnerServiceSMOImpl.updateBasePrivilege(basePrivilegeDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeBasePrivilegeConstant.UPDATE_BASEPRIVILEGE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMenuInnerServiceSMO getMenuInnerServiceSMOImpl() {
        return menuInnerServiceSMOImpl;
    }

    public void setMenuInnerServiceSMOImpl(IMenuInnerServiceSMO menuInnerServiceSMOImpl) {
        this.menuInnerServiceSMOImpl = menuInnerServiceSMOImpl;
    }
}
