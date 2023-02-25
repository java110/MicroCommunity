package com.java110.api.controller.app.smartWeter;

import com.java110.core.base.controller.BaseController;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.meterWater.NotifyMeterWaterOrderDto;
import com.java110.intf.common.INotifySmartMeterV1InnerServiceSMO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/app/smartMeter/notify")
public class NotifySmartMeterController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(NotifySmartMeterController.class);

    @Autowired
    private INotifySmartMeterV1InnerServiceSMO notifySmartWeterV1InnerServiceSMOImpl;

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/{implBean}/{appId}", method = RequestMethod.POST)
    public ResponseEntity<String> notify(
                                          @PathVariable String implBean,
                                          @PathVariable String appId,
                                          @RequestBody String postInfo,
                                          HttpServletRequest request) {

        return notifySmartWeterV1InnerServiceSMOImpl.notifySmartMater(new NotifyMeterWaterOrderDto(appId,postInfo,implBean));

    }

}
