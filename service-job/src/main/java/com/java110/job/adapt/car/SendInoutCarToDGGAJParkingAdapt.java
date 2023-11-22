package com.java110.job.adapt.car;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.client.RestTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.parking.ParkingAreaAttrDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.system.Business;
import com.java110.intf.community.IParkingAreaAttrV1InnerServiceSMO;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImageUtils;
import com.java110.utils.util.StringUtil;
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
 * 停车进出数据向  东莞市公安局社会停车场（道闸卡口）平台同步数据
 * <p>
 * databus 配置
 * <p>
 * 名称 车辆进出databus
 * 业务类型 saveCarInoutDetail
 * 适配器 sendInoutCarToDGGAJParkingAdapt
 * 顺序 80
 * 状态 在用
 *
 *
 * mapping 中配置 DOGUAN_PAKRING_KEY 平台提供的key
 */
@Component(value = "sendInoutCarToDGGAJParkingAdapt")
public class SendInoutCarToDGGAJParkingAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(SendInoutCarToDGGAJParkingAdapt.class);

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaAttrV1InnerServiceSMO parkingAreaAttrV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    public static final String SPEC_CD_GATE_ID = "489905"; // 公安停车场ID
    public static final String SPEC_CD_SECURE = "489906"; //公安停车场秘钥

    private static final String url = "http://59.39.179.74:9070/services/hole";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();

        //todo 获取到 参数信息
        CarInoutDetailPo carInoutDetailPo = BeanConvertUtil.covertBean(data, CarInoutDetailPo.class);

        ParkingAreaAttrDto parkingAreaAttrDto = new ParkingAreaAttrDto();
        parkingAreaAttrDto.setPaId(carInoutDetailPo.getPaId());
        List<ParkingAreaAttrDto> parkingAreaAttrDtos = parkingAreaAttrV1InnerServiceSMOImpl.queryParkingAreaAttrs(parkingAreaAttrDto);
        if (parkingAreaAttrDtos == null || parkingAreaAttrDtos.size() < 1) {
            throw new IllegalArgumentException("公安停车场配置未找到");
        }
        String gateId = getParkingAttr(parkingAreaAttrDtos,SPEC_CD_GATE_ID);
        String secure = getParkingAttr(parkingAreaAttrDtos,SPEC_CD_SECURE);

        //todo 获取到token
        String token = initTrans(carInoutDetailPo,gateId,secure);

        //todo 上传进出场车辆信息
        parkWriteInfoV2(carInoutDetailPo,token,gateId);


    }

    /**
     * 写停车场车辆信息
     * @param carInoutDetailPo
     * @param token
     */
    private void parkWriteInfoV2(CarInoutDetailPo carInoutDetailPo, String token,String gateId) {

        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(carInoutDetailPo.getPaId());
        parkingAreaDto.setCommunityId(carInoutDetailPo.getCommunityId());
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);

        if (parkingAreaDtos == null || parkingAreaDtos.size() < 1) {
            throw new IllegalArgumentException("停车场未找到");
        }


        //进
        String directType = "1";

        if (CarInoutDetailDto.CAR_INOUT_OUT.equals(carInoutDetailPo.getCarInout())) {
            directType = "2"; // todo 出
        }
        String photo = ImageUtils.getBase64ByImgUrl(carInoutDetailPo.getPhotoJpg());
        JSONObject picBase64 = JSONObject.parseObject("{\"SubImageInfo\": [{\"FileFormat\":\"Jpeg\",\"Data\":\"无图片\"}]}");

        if(!StringUtil.isEmpty(photo)){
            picBase64.getJSONArray("SubImageInfo").getJSONObject(0).put("Data",photo.replaceAll("\n", ""));
        }

        String reqParam = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.conflux.sunshine.com\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <ws:vehicleWriteInfoV2>\n" +
                "         <!--Optional:-->\n" +
                "         <gateId>"+gateId+"</gateId>\n" +
                "         <!--Optional:-->\n" +
                "         <directType>"+directType+"</directType>\n" +
                "         <driverWayNo>1</driverWayNo>\n" +
                "         <!--Optional:-->\n" +
                "         <driverWayType>00</driverWayType>\n" +
                "         <!--Optional:-->\n" +
                "         <licenseType>02</licenseType>\n" +
                "         <!--Optional:-->\n" +
                "         <passTime>"+ DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A) +"</passTime>\n" +
                "         <!--Optional:-->\n" +
                "         <licenseColor>9</licenseColor>\n" +
                "         <!--Optional:-->\n" +
                "         <carType>K33</carType>\n" +
                "         <!--Optional:-->\n" +
                "         <license>"+carInoutDetailPo.getCarNum()+"</license>\n" +
                "         <!--Optional:-->\n" +
                "         <backLicense></backLicense>\n" +
                "         <!--Optional:-->\n" +
                "         <backLicenseColor></backLicenseColor>\n" +
                "         <!--Optional:-->\n" +
                "         <identical>1</identical>\n" +
                "         <!--Optional:-->\n" +
                "         <carColor>99</carColor>\n" +
                "         <!--Optional:-->\n" +
                "         <carBrand>0</carBrand>\n" +
                "         <!--Optional:-->\n" +
                "         <carShape>8</carShape>\n" +
                "         <!--Optional:-->\n" +
                "         <picBase64>"+picBase64.toJSONString()+"</picBase64>\n" +
                "         <freeParkingNums>-1</freeParkingNums>\n" +
                "         <!--Optional:-->\n" +
                "         <sendFlag>0</sendFlag>\n" +
                "         <!--Optional:-->\n" +
                "         <inputTime>"+ DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A) +"</inputTime>\n" +
                "         <!--Optional:-->\n" +
                "         <token>"+token+"</token>\n" +
                "      </ws:vehicleWriteInfoV2>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        logger.debug("请求报文：{}",reqParam);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","text/xml;charset=UTF-8");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(reqParam, httpHeaders);
        try {
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            logger.debug("返回报文,{}",responseEntity);
        }catch (HttpStatusCodeException e){
            logger.error("调用异常",e);
        }catch (Exception e){
            logger.error("调用异常",e);
        }
    }

    private String getParkingAttr(List<ParkingAreaAttrDto> parkingAreaAttrDtos, String specCdGateId) {

        for(ParkingAreaAttrDto parkingAreaAttrDto:parkingAreaAttrDtos){
            if(specCdGateId.equals(parkingAreaAttrDto.getSpecCd())){
                return parkingAreaAttrDto.getValue().trim();
            }
        }
        return "";
    }

    /**
     * 初始化连接
     * @param carInoutDetailPo
     * @return
     */
    public String initTrans(CarInoutDetailPo carInoutDetailPo,String gateId,String secure) {

        //进
        String directType = "1";

        if (CarInoutDetailDto.CAR_INOUT_OUT.equals(carInoutDetailPo.getCarInout())) {
            directType = "2"; // todo 出
        }

        String token = CommonCache.getValue("DGGAJ_Parking_token_"+directType+carInoutDetailPo.getPaId());

        if(!StringUtil.isEmpty(token)){
            return token;
        }

        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(carInoutDetailPo.getPaId());
        parkingAreaDto.setCommunityId(carInoutDetailPo.getCommunityId());
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);

        if (parkingAreaDtos == null || parkingAreaDtos.size() < 1) {
            throw new IllegalArgumentException("停车场未找到");
        }

       // String key = MappingCache.getValue("DOGUAN_PAKRING_KEY");



        String paramIn = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.conflux.sunshine.com\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <ws:initTrans>\n" +
                "         <!--Optional:-->\n" +
                "         <gateId>"+gateId+"</gateId>\n" +
                "         <!--Optional:-->\n" +
                "         <directType>" + directType + "</directType>\n" +
                "         <driverWayNo>1</driverWayNo>\n" +
                "         <!--Optional:-->\n" +
                "         <initKey>"+secure+"</initKey>\n" +
                "      </ws:initTrans>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        logger.debug("请求报文：{}",paramIn);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","text/xml;charset=UTF-8");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(paramIn, httpHeaders);
        try {
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            logger.debug("返回报文,{}",responseEntity);
            String body = responseEntity.getBody();
            if(!body.contains("token")){
                return token;
            }
            token = body.substring(body.indexOf("&lt;token&gt;")+13,body.indexOf("&lt;/token&gt;"));
            CommonCache.setValue("DGGAJ_Parking_token_"+directType+carInoutDetailPo.getPaId(),token,CommonCache.TOKEN_EXPIRE_TIME);
        }catch (HttpStatusCodeException e){
            logger.error("调用异常",e);
        }catch (Exception e){
            logger.error("调用异常",e);
        }

        return token;
    }
}
