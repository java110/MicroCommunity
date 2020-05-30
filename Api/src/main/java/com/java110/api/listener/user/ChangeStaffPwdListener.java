package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 修改员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("changeStaffPwdServiceListener")
public class ChangeStaffPwdListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(ChangeStaffPwdListener.class);

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    @Autowired
    private IUserBMO userBMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_CHANGE_STAFF_PWD;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
//获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn, "添加员工时请求参数有误，不是有效的json格式 " + paramIn);
        JSONObject paramInJson = JSONObject.parseObject(paramIn);
        Assert.jsonObjectHaveKey(paramInJson, "userId", "请求参数中未包含userId 节点，请确认");
        Assert.jsonObjectHaveKey(paramInJson, "oldPwd", "请求参数中未包含oldPwd 节点，请确认");
        Assert.jsonObjectHaveKey(paramInJson, "newPwd", "请求参数中未包含newPwd 节点，请确认");

        paramInJson.put("oldPwd", AuthenticationFactory.passwdMd5(paramInJson.getString("oldPwd")));
        paramInJson.put("newPwd", AuthenticationFactory.passwdMd5(paramInJson.getString("newPwd")));

        userBMOImpl.modifyStaff(paramInJson, dataFlowContext);
    }

}
