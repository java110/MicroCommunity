package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.staff.ApiStaffDataVo;
import com.java110.vo.api.staff.ApiStaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询员工信息
 * Created by Administrator on 2019/4/2.
 */
@Java110Listener("queryStaffServiceListener")
public class QueryStaffServiceListener extends AbstractServiceApiListener {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public int getOrder() {
        return 0;
    }



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_STAFF_INFOS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     *
     * @param event
     */

    public void soService1(ServiceDataFlowEvent event) {


        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        /*Map<String,String> headers = dataFlowContext.getRequestHeaders();

        Assert.hasKeyAndValue(headers,"page","请求报文中未包含page节点");
        Assert.hasKeyAndValue(headers,"rows","请求报文中未包含rows节点");
        Assert.hasKeyAndValue(headers,"storeId","请求报文中未包含storeId节点");

        ResponseEntity responseEntity= null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_STORE_USERS);
        if(appService == null){
            responseEntity = new ResponseEntity<String>("当前没有权限访问"+ServiceCodeConstant.SERVICE_CODE_QUERY_STORE_USERS, HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }
        String requestUrl = appService.getUrl();
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(),ServiceCodeConstant.SERVICE_CODE_QUERY_STORE_USERS);

        //先查询商户服务查询员工userId
        requestUrl = requestUrl + "?page="+headers.get("page")+"&rows="+headers.get("rows")+"&storeId="+headers.get("storeId");
        dataFlowContext.getRequestHeaders().put("REQUEST_URL",requestUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext,appService,httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return ;
        }

        //然后根据userId 在用户服务查询用户信息
        JSONObject staffs = JSONObject.parseObject(responseEntity.getBody().toString());

        JSONArray rows = staffs.getJSONArray("datas");

        appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        if(appService == null){
            responseEntity = new ResponseEntity<String>("当前没有权限访问"+ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO, HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        for(int rowIndex = 0 ;rowIndex < rows.size();rowIndex ++){
            JSONObject tmpObj = rows.getJSONObject(rowIndex);
            queryUserInfoByUserId(dataFlowContext,tmpObj,appService);
        }
        dataFlowContext.setResponseEntity(new ResponseEntity(staffs.toJSONString(),HttpStatus.OK));

*/

    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson,"page","请求报文中未包含page节点");
        Assert.hasKeyAndValue(reqJson,"row","请求报文中未包含rows节点");
        Assert.hasKeyAndValue(reqJson,"storeId","请求报文中未包含storeId节点");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        UserDto userDto = BeanConvertUtil.covertBean(reqJson, UserDto.class);

        int count = userInnerServiceSMOImpl.getStaffCount(userDto);

        List<ApiStaffDataVo> staffs = null;

        if (count > 0) {
            staffs = BeanConvertUtil.covertBeanList(userInnerServiceSMOImpl.getStaffs(userDto), ApiStaffDataVo.class);
        } else {
            staffs = new ArrayList<>();
        }

        ApiStaffVo apiStaffVo = new ApiStaffVo();

        apiStaffVo.setTotal(count);
        apiStaffVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiStaffVo.setStaffs(staffs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiStaffVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
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


    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
