package com.java110.api.listener.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.*;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.app.IAppInnerServiceSMO;
import com.java110.dto.app.AppDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存应用侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateAppListener")
public class UpdateAppListener extends AbstractServiceApiListener {

    @Autowired
    private IAppInnerServiceSMO appInnerServiceSMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "appId", "应用Id不能为空");
Assert.hasKeyAndValue(reqJson, "name", "必填，请填写应用名称");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AppDto appDto = BeanConvertUtil.covertBean(reqJson, AppDto.class);

        appDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);

        int count = appInnerServiceSMOImpl.deleteApp(appDto);



        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "删除数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAppConstant.UPDATE_APP;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }



    public IAppInnerServiceSMO getAppInnerServiceSMOImpl() {
        return appInnerServiceSMOImpl;
    }

    public void setAppInnerServiceSMOImpl(IAppInnerServiceSMO appInnerServiceSMOImpl) {
        this.appInnerServiceSMOImpl = appInnerServiceSMOImpl;
    }
}
