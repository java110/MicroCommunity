package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 修改员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("changeStaffPwdServiceListener")
public class ChangeStaffPwdListener extends AbstractServiceApiDataFlowListener {

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


    /**
     * 添加员工信息
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
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

        JSONArray businesses = new JSONArray();
        //判断请求报文中包含 userId 并且 不为-1时 将已有用户添加为员工，反之，则添加用户再将用户添加为员工
        JSONObject staffBusiness = userBMOImpl.modifyStaff(paramInJson, dataFlowContext);
        businesses.add(staffBusiness);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID, paramInJson.getString("userId"));
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");

        String paramInObj = userBMOImpl.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders()).toJSONString();

        //将 rest header 信息传递到下层服务中去
        userBMOImpl.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);

        super.doResponse(dataFlowContext);
    }

}
