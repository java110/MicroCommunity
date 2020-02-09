package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询员工信息query.staff.byName
 * Created by Administrator on 2019/4/2.
 */
@Java110Listener("queryStaffByUserNameServiceListener")
public class QueryStaffByUserNameServiceListener extends AbstractServiceApiDataFlowListener {
    private final static Logger logger = LoggerFactory.getLogger(QueryStaffByUserNameServiceListener.class);

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_STAFF_BY_NAME;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) throws ListenerExecuteException{


        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        JSONObject data = dataFlowContext.getReqJson();
        logger.debug("请求信息：{}",JSONObject.toJSONString(dataFlowContext));
        Assert.hasKeyAndValue(data,"storeId","请求报文中未包含storeId节点");
        Assert.hasKeyAndValue(data,"name","请求报文中未包含name节点");
        ResponseEntity<String> responseEntity = null;

        JSONObject resultJson = JSONObject.parseObject("{\"total:\":10,\"datas\":[]}");
        //根据名称查询用户信息
        responseEntity = super.callService(dataFlowContext,ServiceCodeConstant.SERVICE_CODE_QUERY_USER_BY_NAME,data);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }


        String useIds = getUserIds(responseEntity,dataFlowContext);
        if(StringUtil.isEmpty(useIds)){
            responseEntity = new ResponseEntity<String>(resultJson.toJSONString(),HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        JSONArray userInfos = getUserInfos(responseEntity);
        Map<String,String> paramIn = new HashMap<>();
        paramIn.put("userIds",useIds);
        paramIn.put("storeId",data.getString("storeId"));
        //查询是商户员工的userId
        responseEntity = super.callService(dataFlowContext,ServiceCodeConstant.SERVICE_CODE_QUERY_STOREUSER_BYUSERIDS,paramIn);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return ;
        }
        resultJson.put("datas",getStaffUsers(userInfos,responseEntity));
        responseEntity = new ResponseEntity<String>(resultJson.toJSONString(),HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 查询商户员工
     * @param userInfos 用户信息
     * @param responseEntity 商户返回的用户ID信息
     * @return
     */
    private JSONArray getStaffUsers(JSONArray userInfos,ResponseEntity<String> responseEntity ){


        JSONObject storeUserInfo = null;
        JSONArray newStaffUsers = new JSONArray();
        JSONArray storeUsers = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("storeUsers");
        if(storeUsers == null || storeUsers.size() < 1){
            return newStaffUsers;
        }

        for(int storeUserIndex = 0 ;storeUserIndex < storeUsers.size();storeUserIndex++){
            storeUserInfo = storeUsers.getJSONObject(storeUserIndex);

            for(int userIndex = 0; userIndex < userInfos.size();userIndex ++){
                if(userInfos.getJSONObject(userIndex).getString("userId").equals(storeUserInfo.getString("userId"))){
                    newStaffUsers.add(userInfos.getJSONObject(userIndex));
                }
            }
        }


        return newStaffUsers;
    }


    /**
     * 获取用ID
     * 如：
     *     123,456,567
     * @param responseEntity
     * @param dataFlowContext
     * @return
     */
    private String getUserIds(ResponseEntity<String> responseEntity,DataFlowContext dataFlowContext){
        JSONObject userInfo = null;
        String userId = "";
        JSONArray resultInfo = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("users");
        if(resultInfo == null || resultInfo.size() < 1){
            return userId;
        }

        for(int userIndex = 0 ;userIndex < resultInfo.size();userIndex++){
            userInfo = resultInfo.getJSONObject(userIndex);
            userId += (userInfo.getString("userId") +",");
        }

        userId = userId.length()>0?userId.substring(0,userId.lastIndexOf(",")):userId;

        return userId;
    }


    /**
     * 获取用户
     * @param responseEntity
     * @return
     */
    private JSONArray getUserInfos(ResponseEntity<String> responseEntity){
        JSONArray resultInfo = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("users");
        if(resultInfo == null || resultInfo.size() < 1){
            return null;
        }

        return resultInfo;
    }

    /**
     * 查询用户信息
     * @param tmpObj
     */
    private void queryUserInfoByUserId( DataFlowContext dataFlowContext,JSONObject tmpObj,AppService appService){

        String userId = tmpObj.getString("userId");

        if(StringUtil.isEmpty(userId)){
            return ;
        }

        ResponseEntity responseEntity= null;

        String requestUrl = appService.getUrl();
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(),ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);

        //先查询商户服务查询员工userId
        requestUrl = requestUrl + "?userId="+userId;
        dataFlowContext.getRequestHeaders().put("REQUEST_URL",requestUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext,appService,httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            throw new ListenerExecuteException(1999,"查询用户信息异常 "+responseEntity.getBody());
        }
        tmpObj.putAll(JSONObject.parseObject(responseEntity.getBody().toString()));
    }
}
