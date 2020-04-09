package com.java110.api.listener.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.store.IStoreBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

/**
 * 保存商户信息
 * Created by Administrator on 2019/3/29.
 */
@Java110Listener("saveStoreServiceListener")
public class SaveStoreServiceListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IStoreBMO storeBMOImpl;
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
        businesses.add(storeBMOImpl.addStore(paramObj));
        //添加员工
        businesses.add(storeBMOImpl.addStaff(paramObj));
        //添加公司级组织
        businesses.add(storeBMOImpl.addOrg(paramObj));
        //公司总部
        businesses.add(storeBMOImpl.addOrgHeadCompany(paramObj));
        //总部办公室
        businesses.add(storeBMOImpl.addOrgHeadPart(paramObj));
        businesses.add(storeBMOImpl.addStaffOrg(paramObj));


//        String paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders()).toJSONString();
//
//        //将 rest header 信息传递到下层服务中去
//        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());
//
//        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj, header);
//        //http://user-service/test/sayHello
//        super.doRequest(dataFlowContext, service, httpEntity);

        //super.doResponse(dataFlowContext);
        ResponseEntity<String> responseEntity = storeBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);

//        if (dataFlowContext.getResponseEntity().getStatusCode() != HttpStatus.OK) {
//            return;
//        }
//        String resData = dataFlowContext.getResponseEntity().getBody().toString();
//       responseEntity = new ResponseEntity<String>(JSONArray.parseArray(resData).get(0).toString(), HttpStatus.OK);
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
        storeBMOImpl.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());
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
}
