package com.java110.boot.controller.app.charge;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.charge.NotifyChargeOrderDto;
import com.java110.intf.common.INotifyChargeV1InnerServiceSMO;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用充电桩 充电完成回调
 *
 * 主要用于测试 使用
 */
@RestController
@RequestMapping(path = "/app/charge")
public class NotifyCommonChargeController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(NotifyCommonChargeController.class);

    @Autowired
    private INotifyChargeV1InnerServiceSMO notifyChargeV1InnerServiceSMOImpl;

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/finish", method = RequestMethod.POST)
    public ResponseEntity<String> finishCharge(
            @RequestBody String postInfo,
            HttpServletRequest request) {

        JSONObject param = JSONObject.parseObject(postInfo);
        NotifyChargeOrderDto notifyChargeOrderDto = new NotifyChargeOrderDto();
        notifyChargeOrderDto.setMachineCode(param.getString("machineCode"));
        notifyChargeOrderDto.setPortCode(param.getString("portCode"));
        notifyChargeOrderDto.setBodyParam(postInfo);
        notifyChargeOrderDto.setReason(param.getString("reason"));

        ResultVo resultVo = notifyChargeV1InnerServiceSMOImpl.finishCharge(notifyChargeOrderDto);
        return ResultVo.createResponseEntity(resultVo);
    }

}
