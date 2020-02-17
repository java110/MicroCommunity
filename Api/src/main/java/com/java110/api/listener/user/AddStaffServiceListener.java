package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

/**
 * 添加员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("addStaffServiceListener")
public class AddStaffServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(AddStaffServiceListener.class);



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


    /**
     * 添加员工信息
     *
     *
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"添加员工时请求参数有误，不是有效的json格式 "+paramIn);
        JSONObject paramInJson = JSONObject.parseObject(paramIn);
        Assert.jsonObjectHaveKey(paramInJson,"storeId","请求参数中未包含storeId 节点，请确认");
        Assert.jsonObjectHaveKey(paramInJson,"storeTypeCd","请求参数中未包含storeTypeCd 节点，请确认");
        JSONArray businesses = new JSONArray();
        //判断请求报文中包含 userId 并且 不为-1时 将已有用户添加为员工，反之，则添加用户再将用户添加为员工
        String userId = "";
        String oldUserId = "";

        if(!paramInJson.containsKey("userId") || "-1".equals(paramInJson.getString("userId"))){
            //将userId 强制写成-1
            oldUserId = "-1";
            userId = GenerateCodeFactory.getUserId();
            paramInJson.put("userId",userId);
            //添加用户
            JSONObject business = addUser(paramInJson,dataFlowContext);
            businesses.add(business);
        }

        paramInJson.put("userId",userId);
        paramInJson.put("relCd","-1".equals(oldUserId)?StoreUserRelConstant.REL_COMMON:StoreUserRelConstant.REL_ADMIN);

        JSONObject staffBusiness = addStaff(paramInJson);
        businesses.add(staffBusiness);

        JSONObject staffOrgBusiness = addStaffOrg(paramInJson);
        businesses.add(staffOrgBusiness);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID,userId);
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD,"D");

        String paramInObj = super.restToCenterProtocol(businesses,dataFlowContext.getRequestCurrentHeaders()).toJSONString();

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header,dataFlowContext.getRequestCurrentHeaders());

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);

        super.doResponse(dataFlowContext);

        //如果不成功直接返回
        if(dataFlowContext.getResponseEntity().getStatusCode() != HttpStatus.OK){
            return ;
        }

        //赋权
        privilegeUserDefault(dataFlowContext,paramInJson);
    }

    private JSONObject addStaffOrg(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ORG_STAFF_REL);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessOrgStaffRels = new JSONArray();
        JSONObject businessOrgStaffRel = new JSONObject();
        businessOrgStaffRel.put("relId","-1");
        businessOrgStaffRel.put("storeId",paramInJson.getString("storeId"));
        businessOrgStaffRel.put("staffId",paramInJson.getString("userId"));
        businessOrgStaffRel.put("orgId",paramInJson.getString("orgId"));
        businessOrgStaffRel.put("relCd",paramInJson.getString("relCd"));
        businessOrgStaffRels.add(businessOrgStaffRel);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOrgStaffRel",businessOrgStaffRels);

        return business;
    }

    /**
     * 添加员工
     * @param paramInJson
     * @return
     */
    private JSONObject addStaff(JSONObject paramInJson){

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_USER);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessStoreUsers = new JSONArray();
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId",paramInJson.getString("storeId"));
        businessStoreUser.put("storeUserId","-1");
        businessStoreUser.put("userId",paramInJson.getString("userId"));
        businessStoreUser.put("relCd",paramInJson.getString("relCd"));
        businessStoreUsers.add(businessStoreUser);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessStoreUser",businessStoreUsers);

        return business;
    }

    /**
     * 添加用户
     * @param paramObj
     */
    private JSONObject addUser(JSONObject paramObj,DataFlowContext dataFlowContext){

        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(paramObj,"name","请求参数中未包含name 节点，请确认");
        //Assert.jsonObjectHaveKey(paramObj,"email","请求参数中未包含email 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj,"tel","请求参数中未包含tel 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj,"orgId", "请求报文格式错误或未包含部门信息");
        Assert.jsonObjectHaveKey(paramObj,"address", "请求报文格式错误或未包含地址信息");
        Assert.jsonObjectHaveKey(paramObj,"sex", "请求报文格式错误或未包含性别信息");
        Assert.jsonObjectHaveKey(paramObj,"relCd", "请求报文格式错误或未包含员工角色");


        if (paramObj.containsKey("email") && !StringUtil.isEmpty(paramObj.getString("email"))) {
            Assert.isEmail(paramObj, "email", "不是有效的邮箱格式");
        }


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser",refreshParamIn(paramObj));

        return business;
    }

    /**
     * 对请求报文处理
     * @param paramObj
     * @return
     */
    private JSONObject refreshParamIn(JSONObject paramObj){
        //paramObj.put("userId","-1");
        paramObj.put("levelCd", UserLevelConstant.USER_LEVEL_STAFF);
        //设置默认密码
        String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        Assert.hasLength(staffDefaultPassword,"映射表中未设置员工默认密码，请检查"+MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        staffDefaultPassword = AuthenticationFactory.passwdMd5(staffDefaultPassword);
        paramObj.put("password",staffDefaultPassword);
        return paramObj;
    }


    /**
     * 用户赋权
     * @return
     */
    private void privilegeUserDefault(DataFlowContext dataFlowContext,JSONObject paramObj){
        ResponseEntity responseEntity= null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE);
        if(appService == null){
            responseEntity = new ResponseEntity<String>("当前没有权限访问"+ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE,HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }
        String requestUrl = appService.getUrl();
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(),ServiceCodeConstant.SERVICE_CODE_SAVE_USER_DEFAULT_PRIVILEGE);
        super.freshHttpHeader(header,dataFlowContext.getRequestCurrentHeaders());
        JSONObject paramInObj = new JSONObject();
        paramInObj.put("userId",paramObj.getString("userId"));
        paramInObj.put("storeTypeCd",paramObj.getString("storeTypeCd"));
        paramInObj.put("userFlag","staff");
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        doRequest(dataFlowContext,appService,httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            dataFlowContext.setResponseEntity(responseEntity);
        }
    }


}
