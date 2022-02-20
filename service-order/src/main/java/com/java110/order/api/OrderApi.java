package com.java110.order.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.trace.Java110TraceLog;
import com.java110.entity.order.Orders;
import com.java110.order.smo.IOrderProcessServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.util.Assert;
import com.java110.core.base.controller.BaseController;
import com.java110.core.event.center.DataFlowEventPublishing;
import com.java110.order.smo.IOrderServiceSMO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wux
 * @create 2019-02-05 上午10:29
 * @desc 订单接口服务类
 **/
@RestController
@RequestMapping(path = "/orderApi")
public class OrderApi extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(OrderApi.class);

    @Autowired
    private IOrderServiceSMO orderServiceSMOImpl;

    @Autowired
    private IOrderProcessServiceSMO orderProcessServiceSMOImpl;

    /**
     * 订单请求服务
     * @param orderInfo 订单信息
     * @param request 请求对象
     * @return ResponseEntity 对象
     */
    @RequestMapping(path = "/service", method = RequestMethod.POST)
    @ApiOperation(value = "中心服务订单受理", notes = "test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType = "query", name = "orderInfo", value = "订单受理信息", required = true, dataType = "String")
    @Java110TraceLog
    public ResponseEntity<String> servicePost(@RequestBody String orderInfo, HttpServletRequest request) {

        ResponseEntity<String> responseEntity = null;

        try {
            Map<String, String> headers = new HashMap<String, String>();
            getRequestInfo(request, headers);
            logger.debug("订单服务请求报文为: {},请求头为:{}", orderInfo, headers);
            //接受请求事件
            DataFlowEventPublishing.receiveRequest(orderInfo, headers);
            //预校验
            preValiateOrderInfo(orderInfo);
            JSONObject order = JSONObject.parseObject(orderInfo).getJSONObject("orders");

            if(!order.containsKey("orderProcess")){
                responseEntity = orderServiceSMOImpl.service(orderInfo, headers);
            }else if(Orders.ORDER_PROCESS_ORDER_PRE_SUBMIT.equals(order.getString("orderProcess"))){
                responseEntity = orderProcessServiceSMOImpl.preService(orderInfo, headers);
            }else if(Orders.ORDER_PROCESS_ORDER_CONFIRM_SUBMIT.equals(order.getString("orderProcess"))){
                responseEntity = orderProcessServiceSMOImpl.confirmService(orderInfo, headers);
            }else{
                responseEntity = orderServiceSMOImpl.service(orderInfo, headers);
            }

        } catch (Exception e) {
            logger.error("请求订单异常", e);
            responseEntity = new ResponseEntity<String>("请求中心服务发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("订单服务返回报文为: {}", responseEntity);
        }
        return responseEntity;
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     *
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo) {

        Assert.jsonObjectHaveKey(orderInfo, "orders", "请求报文中未包含orders节点，" + orderInfo);

        Assert.jsonObjectHaveKey(orderInfo, "business", "请求报文中未包含business节点，" + orderInfo);

        if (JSONObject.parseObject(orderInfo).getJSONObject("orders").containsKey("dataFlowId")) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_ERROR, "报文中不能存在dataFlowId节点");
        }
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     *
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo, Map<String, String> headers) {

        Assert.hasKey(headers, "serviceCode", "没有包含serviceCode");

        Assert.hasLength(headers.get("serviceCode"), "serviceCode 不能为空");

        Assert.hasKey(headers, "appId", "没有包含appId");

        Assert.hasLength(headers.get("appId"), "appId 不能为空");
    }

    /**
     * 获取请求信息
     *
     * @param request
     * @param headers
     * @throws RuntimeException
     */
    private void getRequestInfo(HttpServletRequest request, Map headers) throws Exception {
        try {
            super.initHeadParam(request, headers);
            super.initUrlParam(request, headers);
        } catch (Exception e) {
            logger.error("加载头信息失败", e);
            throw e;
        }
    }


    public IOrderServiceSMO getOrderServiceSMOImpl() {
        return orderServiceSMOImpl;
    }

    public void setOrderServiceSMOImpl(IOrderServiceSMO orderServiceSMOImpl) {
        this.orderServiceSMOImpl = orderServiceSMOImpl;
    }
}
