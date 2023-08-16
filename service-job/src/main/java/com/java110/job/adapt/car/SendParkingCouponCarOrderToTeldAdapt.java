package com.java110.job.adapt.car;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingCouponCarDto;
import com.java110.dto.system.Business;
import com.java110.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.po.parking.ParkingCouponCarOrderPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.utils.util.TeldUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

/**
 * 特来电 2.7　推送车辆离场通知
 * <p>
 * databus 配置
 * <p>
 * 名称 车辆进出databus
 * 业务类型 saveParkingCouponCarOrder
 * 适配器 sendParkingCouponCarOrderToTeldAdapt
 * 顺序 81
 * 状态 在用
 *
 *
 * mapping 中配置
 *
 *   String signKey = MappingCache.getValue("TELD_DOMAIN", "SIGN_KEY");
 *   String aesKey = MappingCache.getValue("TELD_DOMAIN", "AES_KEY");
 *   String aesIv = MappingCache.getValue("TELD_DOMAIN", "AES_IV");
 *   String OperatorID = MappingCache.getValue("TELD_DOMAIN","Teld_OperatorID");
 *   String OperatorSecret = MappingCache.getValue("TELD_DOMAIN","Teld_OperatorSecret");
 */

@Component(value = "sendParkingCouponCarOrderToTeldAdapt")
public class SendParkingCouponCarOrderToTeldAdapt extends DatabusAdaptImpl {
    private static Logger logger = LoggerFactory.getLogger(SendParkingCouponCarOrderToTeldAdapt.class);

    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    private static final String query_token = "/query_token";

    private static final String notify_url = "/notification_park_order_consumed";


    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();

        //todo 获取到 参数信息
        ParkingCouponCarOrderPo parkingCouponCarOrderPo = BeanConvertUtil.covertBean(data, ParkingCouponCarOrderPo.class);


        ParkingCouponCarDto parkingCouponCarDto = new ParkingCouponCarDto();
        parkingCouponCarDto.setPccId(parkingCouponCarOrderPo.getPccId());
        List<ParkingCouponCarDto> parkingCouponCarDtos = parkingCouponCarV1InnerServiceSMOImpl.queryParkingCouponCars(parkingCouponCarDto);

        if (parkingCouponCarDtos == null || parkingCouponCarDtos.size() < 1) {
            return;
        }

        String remark = parkingCouponCarDtos.get(0).getRemark();
        if (!StringUtil.isJsonObject(remark)) {
            return;
        }

        JSONObject remarkObj = JSONObject.parseObject(remark);
        String StartChargeSeq = remarkObj.getString("StartChargeSeq");

        try {
            String token = getAccessToken();
            notification_park_order_consumed(token, StartChargeSeq, parkingCouponCarDtos.get(0).getCarNum());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 通知 数据
     *
     * @param token
     * @param startChargeSeq
     * @param carNum
     */
    private void notification_park_order_consumed(String token, String startChargeSeq, String carNum) throws Exception {

        JSONObject paramIn = new JSONObject();
        paramIn.put("StartChargeSeq", startChargeSeq);
        paramIn.put("PlateNum", carNum);
        paramIn.put("LeavingTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        paramIn.put("CalculationMethod", 3);

        String signKey = MappingCache.getValue("TELD_DOMAIN", "SIGN_KEY");
        String aesKey = MappingCache.getValue("TELD_DOMAIN", "AES_KEY");
        String aesIv = MappingCache.getValue("TELD_DOMAIN", "AES_IV");
        String OperatorID = MappingCache.getValue("TELD_DOMAIN","Teld_OperatorID");
        String teldUrl = MappingCache.getValue("TELD_DOMAIN","Teld_Url");// http://hlht.teld.cc:7777/evcs/v20191230

        String param = TeldUtil.generateSecurityParam(paramIn.toJSONString(), aesKey, aesIv, signKey, OperatorID);

        logger.debug("请求报文：{}", paramIn);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        httpHeaders.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(param, httpHeaders);
        try {
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(teldUrl+notify_url, HttpMethod.POST, httpEntity, String.class);
            logger.debug("返回报文,{}", responseEntity);

        } catch (HttpStatusCodeException e) {
            logger.error("调用异常", e);
        } catch (Exception e) {
            logger.error("调用异常", e);
        }
    }

    public String getAccessToken() throws Exception {

        String token = CommonCache.getValue("Teld_Parking_token");

        if (!StringUtil.isEmpty(token)) {
            return token;
        }
        String signKey = MappingCache.getValue("TELD_DOMAIN", "SIGN_KEY");
        String aesKey = MappingCache.getValue("TELD_DOMAIN", "AES_KEY");
        String aesIv = MappingCache.getValue("TELD_DOMAIN", "AES_IV");
        String OperatorID = MappingCache.getValue("TELD_DOMAIN","Teld_OperatorID");
        String OperatorSecret = MappingCache.getValue("TELD_DOMAIN","Teld_OperatorSecret");
        String teldUrl = MappingCache.getValue("TELD_DOMAIN","Teld_Url");// http://hlht.teld.cc:7777/evcs/v20191230

        JSONObject paramIn = new JSONObject();
        paramIn.put("OperatorID", OperatorID);
        paramIn.put("OperatorSecret", OperatorSecret);
        String param = TeldUtil.generateSecurityParam(paramIn.toJSONString(), aesKey, aesIv, signKey, OperatorID);


        logger.debug("请求报文：{}", paramIn);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(param, httpHeaders);
        try {
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(teldUrl+query_token, HttpMethod.POST, httpEntity, String.class);
            logger.debug("返回报文,{}", responseEntity);
            String body = responseEntity.getBody();
            JSONObject paramOut = JSONObject.parseObject(body);
            String data = TeldUtil.Decrypt(paramOut.getString("Data"), aesKey, aesIv);
            JSONObject dataObj = JSONObject.parseObject(data);
            token = dataObj.getString("AccessToken");
            CommonCache.setValue("Teld_Parking_token", token, CommonCache.TOKEN_EXPIRE_TIME);
        } catch (HttpStatusCodeException e) {
            logger.error("调用异常", e);
        } catch (Exception e) {
            logger.error("调用异常", e);
        }

        return token;
    }
}
