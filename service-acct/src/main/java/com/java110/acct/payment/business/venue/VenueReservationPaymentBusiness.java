package com.java110.acct.payment.business.venue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.communitySpace.CommunitySpaceDto;
import com.java110.dto.communitySpacePerson.CommunitySpacePersonDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.intf.community.ICommunitySpacePersonTimeV1InnerServiceSMO;
import com.java110.intf.community.ICommunitySpacePersonV1InnerServiceSMO;
import com.java110.intf.community.ICommunitySpaceV1InnerServiceSMO;
import com.java110.po.communitySpacePerson.CommunitySpacePersonPo;
import com.java110.po.communitySpacePersonTime.CommunitySpacePersonTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 场地预约
 */
@Service("venueReservation")
public class VenueReservationPaymentBusiness implements IPaymentBusiness {

    @Autowired
    private ICommunitySpaceV1InnerServiceSMO communitySpaceV1InnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";


    @Autowired
    private ICommunitySpacePersonV1InnerServiceSMO communitySpacePersonV1InnerServiceSMOImpl;

    @Autowired
    private ICommunitySpacePersonTimeV1InnerServiceSMO communitySpacePersonTimeV1InnerServiceSMOImpl;

    /**
     *
     * @param context
     * @param reqJson{
     *          personName:"",
     *          personTel:"",
     *          appointmentTime:"",
     *          payWay:"",
     *          communityId:"",
     *          spaces:[{spaceId:'123',openTimes:[{hours:1},{hours:2}]}]
     * }
     * @return
     */
    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        //Assert.hasKeyAndValue(reqJson, "spaceId", "请求报文中未包含spaceId");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "请求报文中未包含appointmentTime");
        Assert.hasKeyAndValue(reqJson, "payWay", "请求报文中未包含payWay");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        if (!reqJson.containsKey("spaces")) {
            throw new IllegalArgumentException("未包含 场地");
        }

        JSONArray spaces = reqJson.getJSONArray("spaces");

        JSONArray openTimes = null;
        BigDecimal money = new BigDecimal(0);
        for (int spaceIndex = 0; spaceIndex < spaces.size(); spaceIndex++) {
            openTimes = spaces.getJSONObject(spaceIndex).getJSONArray("openTimes");

            if (openTimes == null || openTimes.size() < 1) {
                throw new IllegalArgumentException("未包含 预约时间");
            }

            CommunitySpaceDto communitySpaceDto = new CommunitySpaceDto();
            communitySpaceDto.setSpaceId(spaces.getJSONObject(spaceIndex).getString("spaceId"));
            List<CommunitySpaceDto> communitySpaceDtos = communitySpaceV1InnerServiceSMOImpl.queryCommunitySpaces(communitySpaceDto);

            Assert.listOnlyOne(communitySpaceDtos, "场地不存在" + communitySpaceDto.getSpaceId());

            int openTime = 0;

            for (int timeIndex = 0; timeIndex < openTimes.size(); timeIndex++) {
                openTime += 1;
            }
            money = money.add(new BigDecimal(openTime).multiply(new BigDecimal(communitySpaceDtos.get(0).getFeeMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(money.doubleValue());
        paymentOrderDto.setName("预约费用");

        reqJson.put("receivableAmount",money.doubleValue());
        reqJson.put("receivedAmount",money.doubleValue());
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {
        JSONArray spaces = reqJson.getJSONArray("spaces");
        JSONObject spaceObj = null;
        for (int spaceIndex = 0; spaceIndex < spaces.size(); spaceIndex++) {
            spaceObj = spaces.getJSONObject(spaceIndex);
            CommunitySpacePersonPo communitySpacePersonPo = BeanConvertUtil.covertBean(reqJson, CommunitySpacePersonPo.class);
            communitySpacePersonPo.setSpaceId(spaceObj.getString("spaceId"));
            communitySpacePersonPo.setCspId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            if (StringUtil.isEmpty(communitySpacePersonPo.getState())) {
                communitySpacePersonPo.setState(CommunitySpacePersonDto.STATE_W);
            }
            int flag = communitySpacePersonV1InnerServiceSMOImpl.saveCommunitySpacePerson(communitySpacePersonPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }

            if (!spaceObj.containsKey("openTimes")) {
                return;
            }

            JSONArray openTimes = spaceObj.getJSONArray("openTimes");

            if (openTimes == null || openTimes.size() < 1) {
                return;
            }
            CommunitySpacePersonTimePo communitySpacePersonTimePo = null;
            for (int timeIndex = 0; timeIndex < openTimes.size(); timeIndex++) {
                communitySpacePersonTimePo = new CommunitySpacePersonTimePo();
                communitySpacePersonTimePo.setCommunityId(communitySpacePersonPo.getCommunityId());
                communitySpacePersonTimePo.setCspId(communitySpacePersonPo.getCspId());
                communitySpacePersonTimePo.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
                communitySpacePersonTimePo.setSpaceId(communitySpacePersonPo.getSpaceId());
                communitySpacePersonTimePo.setTimeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
                communitySpacePersonTimeV1InnerServiceSMOImpl.saveCommunitySpacePersonTime(communitySpacePersonTimePo);
            }
        }

    }
}
