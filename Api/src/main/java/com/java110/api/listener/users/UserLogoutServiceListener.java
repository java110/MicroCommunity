package com.java110.api.listener.users;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.naming.AuthenticationException;
import java.util.Map;

/**
 * 用户退出登录
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("userLogoutServiceListener")
public class UserLogoutServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(UserLogoutServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_SERVICE_LOGOUT;
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
     * 校验用户登录：
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"用户注册请求参数有误，不是有效的json格式 "+paramIn);
        Assert.jsonObjectHaveKey(paramIn,"token","请求报文中未包含token 节点请检查");
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        ResponseEntity responseEntity= null;
        try {
            //删除 token 信息
            AuthenticationFactory.deleteToken(paramObj.getString("token"));
            responseEntity = new ResponseEntity<String>("退出登录成功", HttpStatus.OK);
        } catch (Exception e) {
            //Invalid signature/claims
            responseEntity = new ResponseEntity<String>("退出登录失败，请联系管理员", HttpStatus.UNAUTHORIZED);
        }
        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 对请求报文处理
     * @param paramIn
     * @return
     */
    private JSONObject refreshParamIn(String paramIn){
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        paramObj.put("userId","-1");
        paramObj.put("levelCd","0");

        return paramObj;
    }





}
