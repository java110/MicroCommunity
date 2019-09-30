package com.java110.api.listener.parkingSpace;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName SaveParkingSpaceListener
 * @Description 保存小区楼信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("saveParkingSpaceListener")
public class SaveParkingSpaceListener extends AbstractServiceApiDataFlowListener {


    private static Logger logger = LoggerFactory.getLogger(SaveParkingSpaceListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_PARKING_SPACE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();


        //添加小区楼
        businesses.add(addParkingSpace(paramObj));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private JSONObject addParkingSpace(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_SPACE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessParkingSpace = new JSONObject();
        businessParkingSpace.putAll(paramInJson);
        businessParkingSpace.put("state", "F");
        businessParkingSpace.put("psId", "-1");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessParkingSpace", businessParkingSpace);

        return business;
    }


    /**
     * 数据校验
     * <p>
     * name:'',
     * age:'',
     * link:'',
     * sex:'',
     * remark:''
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(paramIn, "num", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(paramIn, "area", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(paramIn, "typeCd", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(paramIn, "userId", "请求报文中未包含userId");

        JSONObject paramObj = JSONObject.parseObject(paramIn);

        if (!"1001".equals(paramObj.getString("typeCd")) && !"2001".equals(paramObj.getString("typeCd"))) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "传入typeCd 非法");
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }

}
