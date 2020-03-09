package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

/**
 * 删除员工信息
 * Created by Administrator on 2019/4/4.
 */
@Java110Listener("deleteStaffServiceListener")
public class DeleteStaffServiceListener extends AbstractServiceApiDataFlowListener {
    @Override
    public int getOrder() {
        return 0;
    }
    @Autowired
    private IUserBMO userBMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_DELETE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"删除员工时请求参数有误，不是有效的json格式 "+paramIn);

        JSONObject paramInJson = JSONObject.parseObject(paramIn);
        Assert.jsonObjectHaveKey(paramInJson,"storeId","请求参数中未包含storeId 节点，请确认");
        Assert.jsonObjectHaveKey(paramInJson,"userId","请求参数中未包含userId 节点，请确认");

        JSONArray businesses = new JSONArray();

        //删除商户用户

        businesses.add(deleteStaff(paramInJson));

        //删除用户
        businesses.add(deleteUser(paramInJson));

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID,paramInJson.getString("userId"));
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD,"D");


        String paramInObj = userBMOImpl.restToCenterProtocol(businesses,dataFlowContext.getRequestCurrentHeaders()).toJSONString();

        //将 rest header 信息传递到下层服务中去
        userBMOImpl.freshHttpHeader(header,dataFlowContext.getRequestCurrentHeaders());

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);

        super.doResponse(dataFlowContext);

        //如果不成功直接返回
        if(dataFlowContext.getResponseEntity().getStatusCode() != HttpStatus.OK){
            return ;
        }

        //赋权
        deleteUserPrivilege(dataFlowContext,paramInJson);


        // 删除权限
    }

    /**
     * 删除用户权限
     * @param dataFlowContext
     * @param paramInJson
     */
    private void deleteUserPrivilege(DataFlowContext dataFlowContext, JSONObject paramInJson) {

        ResponseEntity responseEntity= null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_DELETE_USER_ALL_PRIVILEGE);
        if(appService == null){
            responseEntity = new ResponseEntity<String>("当前没有权限访问"+ServiceCodeConstant.SERVICE_CODE_DELETE_USER_ALL_PRIVILEGE,HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }
        String requestUrl = appService.getUrl();
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(),ServiceCodeConstant.SERVICE_CODE_DELETE_USER_ALL_PRIVILEGE);
        userBMOImpl.freshHttpHeader(header,dataFlowContext.getRequestCurrentHeaders());
        JSONObject paramInObj = new JSONObject();
        paramInObj.put("userId",paramInJson.getString("userId"));
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        doRequest(dataFlowContext,appService,httpEntity);
    }


    /**
     * 删除商户
     * @param paramInJson
     * @return
     */
    private JSONObject deleteStaff(JSONObject paramInJson) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_STORE_USER);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessStoreUsers = new JSONArray();
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId",paramInJson.getString("storeId"));
        businessStoreUser.put("userId",paramInJson.getString("userId"));
        businessStoreUsers.add(businessStoreUser);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessStoreUser",businessStoreUsers);

        return business;

    }

    /**
     * 删除商户
     * @param paramInJson
     * @return
     */
    private JSONObject deleteUser(JSONObject paramInJson) {
        //校验json 格式中是否包含 name,email,levelCd,tel


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_REMOVE_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("userId",paramInJson.getString("userId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser",businessStoreUser);

        return business;

    }
}
