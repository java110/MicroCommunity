package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.StoreUserRelConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 添加员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("addStaffServiceListener")
public class AddStaffServiceListener extends AbstractServiceApiPlusListener {

    private final static Logger logger = LoggerFactory.getLogger(AddStaffServiceListener.class);

    @Autowired
    private IUserBMO userBMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_ADD;
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
        Assert.jsonObjectHaveKey(paramInJson, "storeId", "请求参数中未包含storeId 节点，请确认");
        Assert.jsonObjectHaveKey(paramInJson, "storeTypeCd", "请求参数中未包含storeTypeCd 节点，请确认");
        JSONArray businesses = new JSONArray();
        //判断请求报文中包含 userId 并且 不为-1时 将已有用户添加为员工，反之，则添加用户再将用户添加为员工
        String userId = "";
        String oldUserId = "";

        String relCd = paramInJson.getString("relCd");//员工 组织 岗位

        if (!paramInJson.containsKey("userId") || "-1".equals(paramInJson.getString("userId"))) {
            //将userId 强制写成-1
            oldUserId = "-1";
            userId = GenerateCodeFactory.getUserId();
            paramInJson.put("userId", userId);
            //添加用户
            userBMOImpl.addUser(paramInJson, dataFlowContext);

        }

        paramInJson.put("userId", userId);
        paramInJson.put("relCd", "-1".equals(oldUserId) ? StoreUserRelConstant.REL_COMMON : StoreUserRelConstant.REL_ADMIN);

        userBMOImpl.addStaff(paramInJson, dataFlowContext);

        //重写 员工岗位
        paramInJson.put("relCd", relCd);
        userBMOImpl.addStaffOrg(paramInJson, dataFlowContext);

        commit(dataFlowContext);

        //如果不成功直接返回
        if (dataFlowContext.getResponseEntity().getStatusCode() != HttpStatus.OK) {
            return;
        }

        //赋权
        privilegeUserDefault(dataFlowContext, paramInJson);
    }

    /**
     * 用户赋权
     *
     * @return
     */
    private void privilegeUserDefault(DataFlowContext dataFlowContext, JSONObject paramObj) {
        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE);
        if (appService == null) {
            responseEntity = new ResponseEntity<String>("当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE, HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return;
        }
        String requestUrl = appService.getUrl();
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE);
        userBMOImpl.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());
        JSONObject paramInObj = new JSONObject();
        paramInObj.put("userId", paramObj.getString("userId"));
        paramInObj.put("storeTypeCd", paramObj.getString("storeTypeCd"));
        paramInObj.put("userFlag", "staff");
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        doRequest(dataFlowContext, appService, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
        }
    }


}
