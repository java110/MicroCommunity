package com.java110.api.listener.msg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.msg.IMsgBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.common.IMsgInnerServiceSMO;
import com.java110.dto.msg.MsgDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.msg.ApiMsgDataVo;
import com.java110.vo.api.msg.ApiMsgVo;
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
@Java110Listener("readMsgListener")
public class ReadMsgListener extends AbstractServiceApiListener {

    @Autowired
    private IMsgBMO msgBMOImpl;
    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_READ_MSGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        //super.validatePageInfo(reqJson);

        Assert.hasKeyAndValue(reqJson, "userName", "必填，请填写员工名称");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写员工ID");
        Assert.hasKeyAndValue(reqJson, "msgId", "必填，请填写消息ID");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        businesses.add(msgBMOImpl.addReadMsg(reqJson, context));


        ResponseEntity<String> responseEntity = msgBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);

    }
}
