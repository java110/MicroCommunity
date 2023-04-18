package com.java110.acct.payment.business.venue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.community.CommunitySpaceDto;
import com.java110.dto.community.CommunitySpacePersonDto;
import com.java110.dto.community.CommunitySpacePersonTimeDto;
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


@Java110CmdDoc(title = "场地预约",
        description = "场地预约手机端发起支付",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/payment.unifiedPayment",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "payment.unifiedPayment.venueReservation"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "business", length = 64, remark = "支付场景，比如场地预约 为 venueReservation"),
        @Java110ParamDoc(name = "payAdapt", length = 64, remark = "支付适配器，非必填"),
        @Java110ParamDoc(name = "tradeType", length = 64, remark = "支付类型 NATIVE JSAPI APP"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "tradeType", length = 30, remark = "支付类型 NATIVE JSAPI APP"),
        @Java110ParamDoc(name = "personName", length = 30, remark = "预约人"),
        @Java110ParamDoc(name = "personTel", length = 30, remark = "预约电话"),
        @Java110ParamDoc(name = "appointmentTime", length = 30, remark = "预约时间 YYYY-MM-DD"),
        @Java110ParamDoc(name = "payWay", length = 30, remark = "支付方式"),
        @Java110ParamDoc(name = "spaces", type = "Array", length = 0, remark = "场地"),
        @Java110ParamDoc(parentNodeName = "spaces", name = "spaceId", length = 30, remark = "场地"),
        @Java110ParamDoc(parentNodeName = "spaces", name = "openTimes", type = "Array", length = 0, remark = "预约时间"),
        @Java110ParamDoc(parentNodeName = "openTimes", name = "hours", length = 10, remark = "预约小时"),

})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 100 成功不需要唤起支付窗口，直接支付成功，可能从账户等做了扣款，其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{       \"business\":\"venueReservation\",\"communityId\":\"123123\",\n" +
                "         personName:\"张三\",\n" +
                "         personTel:\"18909711111\",\n" +
                "         appointmentTime:\"2022-12-12\",\n" +
                "         payWay:\"2\",\n" +
                "         communityId:\"123123\",          spaces:[{spaceId:'123',openTimes:[{hours:1},{hours:2}]}]\n" +
                " }",
        resBody = "{'code':0,'msg':'成功'}"
)

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
     * @param context
     * @param reqJson{ personName:"",
     *                 personTel:"",
     *                 appointmentTime:"",
     *                 payWay:"",
     *                 communityId:"",
     *                 spaces:[{spaceId:'123',openTimes:[{hours:1},{hours:2}]}]
     *                 }
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
        CommunitySpacePersonTimeDto communitySpaceOpenTimeDto = null;
        List<CommunitySpacePersonTimeDto> communitySpacePersonTimeDtos = null;
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
                communitySpaceOpenTimeDto = new CommunitySpacePersonTimeDto();
                communitySpaceOpenTimeDto.setSpaceId(spaces.getJSONObject(spaceIndex).getString("spaceId"));
                communitySpaceOpenTimeDto.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
                communitySpaceOpenTimeDto.setAppointmentTime(reqJson.getString("appointmentTime"));
                communitySpacePersonTimeDtos = communitySpacePersonTimeV1InnerServiceSMOImpl.queryCommunitySpacePersonTimes(communitySpaceOpenTimeDto);
                if (communitySpacePersonTimeDtos != null && communitySpacePersonTimeDtos.size() > 0) {
                    throw new IllegalArgumentException(openTimes.getJSONObject(timeIndex).getString("hours") + "已经被预约，不能重复预约");
                }
                openTime += 1;
            }
            money = money.add(new BigDecimal(openTime).multiply(new BigDecimal(communitySpaceDtos.get(0).getFeeMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(money.doubleValue());
        paymentOrderDto.setName("预约费用");

        reqJson.put("receivableAmount", money.doubleValue());
        reqJson.put("receivedAmount", money.doubleValue());
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
            communitySpacePersonPo.setOrderId(paymentOrderDto.getOrderId());
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
                communitySpacePersonTimePo.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
                communitySpacePersonTimeV1InnerServiceSMOImpl.saveCommunitySpacePersonTime(communitySpacePersonTimePo);
            }
        }

    }
}
