package com.java110.api.controller.app.payment.icbc;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.intf.acct.INotifyPaymentV1InnerServiceSMO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/app/payment/notify")
public class IcbcNotifyPaymentController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(IcbcNotifyPaymentController.class);

    @Autowired
    private INotifyPaymentV1InnerServiceSMO notifyPaymentV1InnerServiceSMOImpl;

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/icbc/{appId}/{communityId}", method = RequestMethod.POST)
    public ResponseEntity<String> notify( @PathVariable String appId,@PathVariable String communityId, HttpServletRequest request) {
        JSONObject paramIn = new JSONObject();
        for (String key : request.getParameterMap().keySet()) {
            paramIn.put(key, request.getParameter(key));
            logger.debug("icbc回调报文form" + key + ":: " + request.getParameter(key));
        }
        logger.debug("icbc支付回调报文" + paramIn.toJSONString());

        /*接收参数*/
//        Map<String, String> params = getRequestParams(request);
//        System.out.println("params:" + params);
//        String sign = params.get("sign");
//        System.out.println(sign);
//
//
//        String preStr = buildSignString(params);
//        paramIn.put("preSign", preStr);
//        paramIn.put("sign", sign);

        return notifyPaymentV1InnerServiceSMOImpl.notifyPayment(new NotifyPaymentOrderDto(appId,paramIn.toJSONString(),communityId));

    }


    // 构建签名字符串
    public static String buildSignString(Map<String, String> params) {

        // params.put("Zm","test_test");

        if (params == null || params.size() == 0) {
            return "";
        }

        List<String> keys = new ArrayList<>(params.size());

        for (String key : params.keySet()) {
            if ("sign".equals(key))
                continue;
            if ("wId".equals(key))
                continue;
            if (StringUtils.isEmpty(params.get(key)))
                continue;
            keys.add(key);
        }
        //System.out.println(listToString(keys));

        Collections.sort(keys);

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                buf.append(key + "=" + value);
            } else {
                buf.append(key + "=" + value + "&");
            }
        }

        return buf.toString();
    }
}
