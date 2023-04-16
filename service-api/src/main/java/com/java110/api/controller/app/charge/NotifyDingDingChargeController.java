package com.java110.api.controller.app.charge;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.chargeMachine.NotifyChargeOrderDto;
import com.java110.intf.common.INotifyChargeV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
@RequestMapping(path = "/app/equipments")
public class NotifyDingDingChargeController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(NotifyDingDingChargeController.class);

    public static final String DING_DING_DOMAIN = "DING_DING_CHARGE";


    public static final String DING_DING_APP_ID = "APP_ID";
    public static final String DING_DING_APP_SECURE = "APP_SECURE";
    @Autowired
    private INotifyChargeV1InnerServiceSMO notifyChargeV1InnerServiceSMOImpl;

    /**
     * <p>充电结束通知</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/{id}/{port}/finish", method = RequestMethod.POST)
    public ResponseEntity<String> finishCharge(
            @PathVariable String id,
            @PathVariable String port,
            @RequestBody String postInfo,
            HttpServletRequest request) {
        if (!validateSign(request, "/equipments/" + id + "/" + port + "/finish", postInfo)) {
            return new ResponseEntity<>("{\n" +
                    "\"code\" : -1,\n" +
                    "\"msg\" : \"鉴权失败\"\n" +
                    "}", HttpStatus.OK);
        }

        JSONObject param = JSONObject.parseObject(postInfo);
        NotifyChargeOrderDto notifyChargeOrderDto = new NotifyChargeOrderDto();
        notifyChargeOrderDto.setOrderId(param.getString("chargeId"));
        notifyChargeOrderDto.setMachineCode(id);
        notifyChargeOrderDto.setPortCode(port);
        notifyChargeOrderDto.setBodyParam(postInfo);
        notifyChargeOrderDto.setReason(param.getString("typeName"));
        notifyChargeOrderDto.setEnergy(param.getString("energy"));

        ResultVo resultVo = null;
        resultVo = notifyChargeV1InnerServiceSMOImpl.finishCharge(notifyChargeOrderDto);

        if (resultVo.getCode() == ResultVo.CODE_OK) {
            resultVo.setCode(200);
            resultVo.setMsg("success");
        }

        return ResultVo.createResponseEntity(resultVo);
    }

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/{id}/power", method = RequestMethod.POST)
    public ResponseEntity<String> workHeartbeat(
            @PathVariable String id,
            @RequestBody String postInfo,
            HttpServletRequest request) {
        if (!validateSign(request, "/equipments/" + id + "/power", postInfo)) {
            return new ResponseEntity<>("{\n" +
                    "\"code\" : -1,\n" +
                    "\"msg\" : \"鉴权失败\"\n" +
                    "}", HttpStatus.OK);
        }

        NotifyChargeOrderDto notifyChargeOrderDto = new NotifyChargeOrderDto();
        notifyChargeOrderDto.setMachineCode(id);
        notifyChargeOrderDto.setBodyParam(postInfo);
        ResultVo resultVo = null;
        resultVo = notifyChargeV1InnerServiceSMOImpl.workHeartbeat(notifyChargeOrderDto);
        if (resultVo.getCode() == ResultVo.CODE_OK) {
            resultVo.setCode(200);
            resultVo.setMsg("success");
        }

        return ResultVo.createResponseEntity(resultVo);
    }

    private boolean validateSign(HttpServletRequest request, String url, String postInfo) {
        String appId = request.getHeader("appid");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String secret = MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_SECURE);
        String data = "appid=" + appId + "&content=" + postInfo + "&timestamp=" + timestamp + "&uri=" + url;
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "HmacMD5");
        Mac mac = null;
        try {
            mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String result =
                Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
        if (sign.equals(result)) {
            return true;
        }

        return false;
    }

}
