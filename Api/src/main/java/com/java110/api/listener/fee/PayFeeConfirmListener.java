package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.dto.FeeConfigDto;
import com.java110.dto.FeeDto;
import com.java110.dto.RoomDto;
import com.java110.entity.center.AppService;
import com.java110.entity.order.Orders;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PayFeeListener
 * @Description TODO 交费通知侦听
 * @Author wuxw
 * @Date 2019/6/3 13:46
 * @Version 1.0
 * add by wuxw 2019/6/3
 **/
@Java110Listener("PayFeeConfirmListener")
public class PayFeeConfirmListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(PayFeeConfirmListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_PAY_CONFIRM_PRE;
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
        dataFlowContext.getRequestCurrentHeaders().put("oId", paramObj.getString("oId"));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 刷入order信息
     *
     * @param orders  订单信息
     * @param headers 头部信息
     */
    protected void freshOrderProtocol(JSONObject orders, Map<String, String> headers) {
        super.freshOrderProtocol(orders, headers);
        orders.put("orderProcess", Orders.ORDER_PROCESS_ORDER_CONFIRM_SUBMIT);
        orders.put("oId", headers.get("oId"));
    }

    /**
     * 数据校验
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "oId", "请求报文中未包含订单信息");
        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramInObj.getString("oId"), "订单信息不能为空");

    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
