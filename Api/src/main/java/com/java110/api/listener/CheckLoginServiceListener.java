package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import javax.naming.AuthenticationException;
import java.util.Map;

/**
 * 用户注册 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("checkLoginServiceListener")
public class CheckLoginServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(CheckLoginServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_CHECK_SERVICE_LOGIN;
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
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"用户注册请求参数有误，不是有效的json格式 "+paramIn);
        Assert.jsonObjectHaveKey(paramIn,"token","请求报文中未包含token 节点请检查");
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        ResponseEntity responseEntity= null;
        try {
            Map<String, String> claims = AuthenticationFactory.verifyToken(paramObj.getString("token"));
            if(claims == null || claims.isEmpty()){
                throw new AuthenticationException("认证失败，从token中解析到信息为空");
            }
            JSONObject resultInfo = new JSONObject();
            resultInfo.put("userId",claims.get("userId"));
            responseEntity = new ResponseEntity<String>(resultInfo.toJSONString(), HttpStatus.OK);
        } catch (Exception e) {
            //Invalid signature/claims
            responseEntity = new ResponseEntity<String>("认证失败，不是有效的token", HttpStatus.UNAUTHORIZED);
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
