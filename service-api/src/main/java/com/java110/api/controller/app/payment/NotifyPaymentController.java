package com.java110.api.controller.app.payment;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.intf.acct.INotifyPaymentV1InnerServiceSMO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/app/payment/notify")
public class NotifyPaymentController {

    private final static Logger logger = LoggerFactory.getLogger(NotifyPaymentController.class);

    @Autowired
    private INotifyPaymentV1InnerServiceSMO notifyPaymentV1InnerServiceSMOImpl;

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/common/{appId}/{communityId}", method = RequestMethod.POST)
    public ResponseEntity<String> notify(@RequestBody String postInfo, @PathVariable String appId,@PathVariable String communityId, HttpServletRequest request) {

        logger.debug("微信支付回调报文" + postInfo);

        return notifyPaymentV1InnerServiceSMOImpl.notifyPayment(new NotifyPaymentOrderDto(appId,postInfo,communityId));

    }
}
