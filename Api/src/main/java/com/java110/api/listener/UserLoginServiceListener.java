package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户注册 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("userLoginServiceListener")
public class UserLoginServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(UserLoginServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_SERVICE_LOGIN;
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
     * 请求参数格式：
     * {
        "username":"admin",
        "passwd":"1234565"
     }
     返回报文：
     {
        "userId":"",
        "token":"12dddd"
     }
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"用户注册请求参数有误，不是有效的json格式 "+paramIn);
        Assert.jsonObjectHaveKey(paramIn,"username","用户登录，未包含username节点，请检查" + paramIn);
        Assert.jsonObjectHaveKey(paramIn,"passwd","用户登录，未包含passwd节点，请检查" + paramIn);
        RestTemplate restTemplate = super.getRestTemplate();
        ResponseEntity responseEntity= null;
        JSONObject paramInJson = JSONObject.parseObject(paramIn);
        //根据AppId 查询 是否有登录的服务，查询登录地址调用
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_LOGIN);
        if(appService == null){
            responseEntity = new ResponseEntity<String>("当前没有权限访问"+ServiceCodeConstant.SERVICE_CODE_QUERY_USER_LOGIN,HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }
        String requestUrl = appService.getUrl() + "?userCode="+paramInJson.getString("username");
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(),ServiceCodeConstant.SERVICE_CODE_QUERY_USER_LOGIN);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        try{
            responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
        }catch (HttpStatusCodeException e){ //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>("请求登录查询异常，"+e.getResponseBodyAsString(),e.getStatusCode());
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        String resultBody = responseEntity.getBody().toString();

        Assert.isJsonObject(resultBody,"调用登录查询异常,返回报文有误，不是有效的json格式 "+resultBody);

        JSONObject resultInfo = JSONObject.parseObject(resultBody);
        if(!resultInfo.containsKey("user") || !resultInfo.getJSONObject("user").containsKey("userPwd")
                || !resultInfo.getJSONObject("user").containsKey("userId")){
            responseEntity = new ResponseEntity<String>("用户或密码错误", HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        JSONObject userInfo = resultInfo.getJSONObject("user");
        String userPwd = userInfo.getString("userPwd");
        if(!userPwd.equals(paramInJson.getString("passwd"))){
            responseEntity = new ResponseEntity<String>("密码错误", HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID,userInfo.getString("userId"));
            userMap.put(CommonConstant.LOGIN_USER_NAME,userInfo.getString("userName"));
            String token = AuthenticationFactory.createAndSaveToken(userMap);
            userInfo.remove("userPwd");
            userInfo.put("token",token);
            responseEntity = new ResponseEntity<String>(userInfo.toJSONString(), HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
        }catch (Exception e){
            logger.error("登录异常：",e);
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"系统内部错误，请联系管理员");
        }
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
