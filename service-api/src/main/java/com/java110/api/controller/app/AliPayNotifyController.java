package com.java110.api.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.payment.ITempCarFeeToNotifySMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

// 支付宝 文档 https://opendocs.alipay.com/support/01rbcv
@RestController
@RequestMapping(path = "/app/alipay/notify")
public class AliPayNotifyController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private ITempCarFeeToNotifySMO tempCarFeeToNotifySMOImpl;

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/receive", method = RequestMethod.POST)
    public ResponseEntity<String> receive(HttpServletRequest request) {
        JSONObject paramIn = new JSONObject();
        for (String key : request.getParameterMap().keySet()) {
            paramIn.put(key, request.getParameter(key));
            logger.debug("支付宝回调报文form" + key + ":: " + request.getParameter(key));
        }
        logger.debug("支付宝支付回调报文" + paramIn.toJSONString());

        /*接收参数*/
        Map<String, String> params = getRequestParams(request);
        System.out.println("params:" + params);
        String sign = params.get("sign");
        System.out.println(sign);

        paramIn.put("resultInfo", request.getQueryString());


        // 收到通知后记得返回SUCCESS
        return tempCarFeeToNotifySMOImpl.aliPayToNotify(paramIn.toJSONString(), request);


    }
}
