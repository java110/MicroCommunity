package com.java110.api.listener.fastuser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.activities.IActivitiesBMO;
import com.java110.api.bmo.fastuser.IFastuserBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeActivitiesConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveFastuserListener")
public class SaveFastuserListener extends AbstractServiceApiListener {

    @Autowired
    private IFastuserBMO fastuserBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "fastUserTitle", "必填，请填写业活动标题");
        Assert.hasKeyAndValue(reqJson, "fastUserContext", "必填，请填写活动内容");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "userName", "必填，请填写用户名称");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        reqJson.put("fastuserId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fastuserId));

        //添加单元信息
        businesses.add(fastuserBMOImpl.addFastuser(reqJson, context));

        ResponseEntity<String> responseEntity = fastuserBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);
    }



    @Override
    public String getServiceCode() {
        return ServiceCodeActivitiesConstant.ADD_FASTUSER;
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
