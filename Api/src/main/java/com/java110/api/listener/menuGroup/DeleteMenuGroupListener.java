package com.java110.api.listener.menuGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.menu.IMenuInnerServiceSMO;
import com.java110.dto.menuGroup.MenuGroupDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.BusinessTypeConstant;

import com.java110.core.annotation.Java110Listener;
import com.java110.common.constant.ServiceCodeMenuGroupConstant;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteMenuGroupListener")
public class DeleteMenuGroupListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "gId", "组Id不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;

        MenuGroupDto menuGroupDto = BeanConvertUtil.covertBean(reqJson, MenuGroupDto.class);


        int saveFlag = menuInnerServiceSMOImpl.deleteMenuGroup(menuGroupDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMenuGroupConstant.DELETE_MENUGROUP;
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
