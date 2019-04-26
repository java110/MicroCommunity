package com.java110.api.listener.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.*;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.*;

/**
 * 保存商户信息
 * Created by Administrator on 2019/3/29.
 */
@Java110Listener("saveStoreServiceListener")
public class SaveStoreServiceListener extends AbstractServiceApiDataFlowListener {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_STORE_INFO;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    /**
     * 协议：
     * {
     * <p>
     * "businessStore": {
     * "storeId": "-1",
     * "userId": "用户ID",
     * "name": "齐天超时（王府井店）",
     * "address": "青海省西宁市城中区129号",
     * "password": "ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK",
     * "tel": "15897089471",
     * "storeTypeCd": "M",
     * "nearbyLandmarks": "王府井内",
     * "mapX": "101.801909",
     * "mapY": "36.597263"
     * },
     * "businessStoreAttr": [{
     * "storeId": "-1",
     * "attrId":"-1",
     * "specCd":"1001",
     * "value":"01"
     * }],
     * "businessStorePhoto":[{
     * "storePhotoId":"-1",
     * "storeId":"-1",
     * "storePhotoTypeCd":"T",
     * "photo":"12345678.jpg"
     * }],
     * "businessStoreCerdentials":[{
     * "storeCerdentialsId":"-1",
     * "storeId":"-1",
     * "credentialsCd":"1",
     * "value":"632126XXXXXXXX2011",
     * "validityPeriod":"有效期，长期有效请写3000/01/01",
     * "positivePhoto":"正面照片地址，1234567.jpg",
     * "negativePhoto":"反面照片地址，没有不填写"
     * }]
     * }
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

        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(paramIn, "businessStore", "请求参数中未包含businessStore 节点，请确认");
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID, "-1");
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        //添加商户
        businesses.add(addStore(paramObj));
        //添加员工
        businesses.add(addStaff(paramObj));


        String paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders()).toJSONString();

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);

        //super.doResponse(dataFlowContext);

        if (dataFlowContext.getResponseEntity().getStatusCode() != HttpStatus.OK) {
            return;
        }
        String resData = dataFlowContext.getResponseEntity().getBody().toString();
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONArray.parseArray(resData).get(0).toString(), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
        //如果不成功直接返回
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }

        //赋权
        privilegeUserDefault(dataFlowContext, paramObj);


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
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());
        JSONObject paramInObj = new JSONObject();
        paramInObj.put("userId", paramObj.getJSONObject("businessStore").getString("userId"));
        paramInObj.put("storeTypeCd", paramObj.getJSONObject("businessStore").getString("storeTypeCd"));
        paramInObj.put("userFlag", "admin");
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        doRequest(dataFlowContext, appService, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
        }
    }

    /**
     * 添加商户
     *
     * @param paramInJson
     * @return
     */
    private JSONObject addStore(JSONObject paramInJson) {
        JSONObject business = JSONObject.parseObject("{}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_INFO);
        business.put(CommonConstant.HTTP_SEQ, 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);

        business.put(CommonConstant.HTTP_BUSINESS_DATAS, refreshParamIn(paramInJson));

        return business;

    }

    /**
     * 添加员工
     *
     * @param paramInJson
     * @return
     */
    private JSONObject addStaff(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_USER);
        business.put(CommonConstant.HTTP_SEQ, 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessStoreUsers = new JSONArray();
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId", "-1");
        businessStoreUser.put("storeUserId", "-1");
        businessStoreUser.put("userId", paramInJson.getJSONObject("businessStore").getString("userId"));
        businessStoreUser.put("relCd", StoreUserRelConstant.REL_ADMIN);
        businessStoreUsers.add(businessStoreUser);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessStoreUser", businessStoreUsers);

        return business;
    }

    /**
     * 对请求报文处理
     *
     * @param paramObj
     * @return
     */
    private JSONObject refreshParamIn(JSONObject paramObj) {

        if (paramObj.containsKey("businessStore")) {
            JSONObject businessStoreObj = paramObj.getJSONObject("businessStore");
            businessStoreObj.put("storeId", "-1");
            if (!businessStoreObj.containsKey("password")) {
                String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
                Assert.hasLength(staffDefaultPassword, "映射表中未设置员工默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
                businessStoreObj.put("password", staffDefaultPassword);
            }

            if (!businessStoreObj.containsKey("mapX")) {
                businessStoreObj.put("mapX", "");
            }

            if (!businessStoreObj.containsKey("mapY")) {
                businessStoreObj.put("mapY", "");
            }
        }

        if (paramObj.containsKey("businessStoreAttr")) {
            JSONArray attrs = paramObj.getJSONArray("businessStoreAttr");

            for (int businessStoreAttrIndex = 0; businessStoreAttrIndex < attrs.size(); businessStoreAttrIndex++) {
                JSONObject attr = attrs.getJSONObject(businessStoreAttrIndex);
                attr.put("storeId", "-1");
                attr.put("attrId", "-" + (businessStoreAttrIndex + 1));
            }
        }

        if (paramObj.containsKey("businessStorePhoto")) {
            JSONArray photos = paramObj.getJSONArray("businessStorePhoto");

            for (int businessStorePhotoIndex = 0; businessStorePhotoIndex < photos.size(); businessStorePhotoIndex++) {
                JSONObject attr = photos.getJSONObject(businessStorePhotoIndex);
                attr.put("storeId", "-1");
                attr.put("storePhotoId", "-" + (businessStorePhotoIndex + 1));
            }
        }

        if (paramObj.containsKey("businessStoreCerdentials")) {
            JSONArray cerdentials = paramObj.getJSONArray("businessStoreCerdentials");

            for (int businessStoreCerdentialsIndex = 0; businessStoreCerdentialsIndex < cerdentials.size(); businessStoreCerdentialsIndex++) {
                JSONObject attr = cerdentials.getJSONObject(businessStoreCerdentialsIndex);
                attr.put("storeId", "-1");
                attr.put("storeCerdentialsId", "-" + (businessStoreCerdentialsIndex + 1));
            }
        }

        return paramObj;
    }

}
