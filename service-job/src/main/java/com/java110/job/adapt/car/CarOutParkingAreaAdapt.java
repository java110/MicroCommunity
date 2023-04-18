package com.java110.job.adapt.car;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.smallWeChat.TemplateDataDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.common.ICarInoutInnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component(value = "carOutParkingAreaAdapt")
public class CarOutParkingAreaAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(CarOutParkingAreaAdapt.class);

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;


    @Autowired
    private RestTemplate outRestTemplate;


    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";


    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessCarInoutDetailPos = new JSONArray();

        if (data instanceof JSONObject) {
            businessCarInoutDetailPos.add(data);
        }

        //JSONObject businessCarBlackWhite = data.getJSONObject("businessCarBlackWhite");
        for (int bCarInoutDetailPoIndex = 0; bCarInoutDetailPoIndex < businessCarInoutDetailPos.size(); bCarInoutDetailPoIndex++) {
            JSONObject businessCarInoutDetailPo = businessCarInoutDetailPos.getJSONObject(bCarInoutDetailPoIndex);
            doSendCarOutParkingAreaToWechat(business, businessCarInoutDetailPo);
        }
    }

    /**
     * 发送出厂通知
     *
     * @param business
     * @param businessCarInoutDetailPo 开门记录信息
     */
    private void doSendCarOutParkingAreaToWechat(Business business, JSONObject businessCarInoutDetailPo) {
        CarInoutDetailPo carInoutDetailPo = BeanConvertUtil.covertBean(businessCarInoutDetailPo, CarInoutDetailPo.class);

        //没有手机号 说明  没法发送营销信息 所以 放弃 不推送
        if (StringUtil.isEmpty(carInoutDetailPo.getCarNum())) {
            return;
        }

        //不是出厂不发信息
        if (!CarInoutDetailDto.CAR_INOUT_OUT.equals(carInoutDetailPo.getCarInout())) {
            return;
        }

        String communityId = carInoutDetailPo.getCommunityId();

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(carInoutDetailPo.getCarNum());
        ownerCarDto.setCommunityId(communityId);
        ownerCarDto.setLeaseTypes(new String[]{OwnerCarDto.LEASE_TYPE_INNER,
                OwnerCarDto.LEASE_TYPE_MONTH,
                OwnerCarDto.LEASE_TYPE_NO_MONEY,
                OwnerCarDto.LEASE_TYPE_SALE});
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return;
        }

        if (StringUtil.isEmpty(ownerCarDtos.get(0).getOwnerId())) {
            return;
        }
        if (ownerCarDtos.get(0).getOwnerId().startsWith("-")) {
            return;
        }

        TemplateDataDto templateDataDto = getOwnerTemplateId(communityId, "");

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
        ownerAppUserDto.setAppType("WECHAT");
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            return;
        }

        String openId = ownerAppUserDtos.get(0).getOpenId();
        String url = sendMsgUrl + templateDataDto.getAccessToken();
        Data data = new Data();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateDataDto.getTemplateId());
        templateMessage.setTouser(openId);
        data.setFirst(new Content("亲爱的月租车用户，一路平安"));
        data.setKeyword1(new Content(carInoutDetailPo.getCarNum()));
        data.setKeyword2(new Content(ownerCarDtos.get(0).getAreaNum()));

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setInoutId(carInoutDetailPo.getInoutId());
        List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto);

        if (carInoutDtos == null || carInoutDtos.size() < 1) {
            return;
        }

        data.setKeyword3(new Content(carInoutDtos.get(0).getInTime()));
        data.setKeyword4(new Content(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A)));

        double day = DateUtil.dayCompare(DateUtil.getCurrentDate(), ownerCarDtos.get(0).getEndTime());
        data.setKeyword5(new Content(day + ""));

        String remark = getRemark(communityId, ownerCarDtos.get(0).getOwnerId());
        data.setRemark(new Content(remark));
        templateMessage.setData(data);
        //获取业主公众号地址
        String wechatUrl = UrlCache.getOwnerUrl();
        wechatUrl += "/#/pages/fee/oweFee";
        if (wechatUrl.contains("?")) {
            wechatUrl += ("&wAppId=" + templateDataDto.getAppId());
        } else {
            wechatUrl += ("?wAppId=" + templateDataDto.getAppId());
        }
        templateMessage.setUrl(wechatUrl);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

    }

    private String getRemark(String communityId, String ownerId) {
        Map info = new HashMap();
        info.put("communityId", communityId);
        info.put("ownerIds", new String[]{ownerId});
        List<Map> oweFees = reportOweFeeInnerServiceSMOImpl.queryOweFeesByOwnerIds(info);
        String remark = "感谢您的使用,如有疑问请联系相关物业人员";
        if (oweFees == null || oweFees.size() < 1) {
            remark = "感谢您的使用,如有疑问请联系相关物业人员";
            return remark;
        }
        double oweFee = Double.parseDouble(oweFees.get(0).get("oweFee").toString());
        if (oweFee < 0) {
            remark = "感谢您的使用,如有疑问请联系相关物业人员";
            return remark;
        }

        remark = "物业费欠费" + oweFee + "元，请点击详情交费！";

        return remark;
    }

}
