package com.java110.acct.payment.business.reserve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.community.CommunitySpacePersonTimeDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.reserve.ReserveGoodsDto;
import com.java110.dto.reserve.ReserveGoodsOrderDto;
import com.java110.dto.reserve.ReserveGoodsOrderTimeDto;
import com.java110.dto.reserve.ReserveParamsDto;
import com.java110.intf.store.IReserveGoodsOrderTimeV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsOrderV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsV1InnerServiceSMO;
import com.java110.po.reserveGoodsOrder.ReserveGoodsOrderPo;
import com.java110.po.reserveGoodsOrderTime.ReserveGoodsOrderTimePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


@Java110CmdDoc(title = "商品预约",
        description = "商品预约手机端发起支付",
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
@Service("reserveGoods")
public class ReserveGoodsPaymentBusiness implements IPaymentBusiness {


    public static final String CODE_PREFIX_ID = "10";
    @Autowired
    private IReserveGoodsOrderV1InnerServiceSMO reserveGoodsOrderV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsOrderTimeV1InnerServiceSMO reserveGoodsOrderTimeV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsV1InnerServiceSMO reserveGoodsV1InnerServiceSMOImpl;

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

        if (!reqJson.containsKey("goodss")) {
            throw new IllegalArgumentException("未包含 场地");
        }

        JSONArray goodss = reqJson.getJSONArray("goodss");


        JSONObject goodsInfo = null;
        BigDecimal money = new BigDecimal(0);
        for (int goodsIndex = 0; goodsIndex < goodss.size(); goodsIndex++) {
            goodsInfo = goodss.getJSONObject(goodsIndex);
            assertGoodsAndComputeMoney(reqJson, goodsInfo);
            money = money.add(new BigDecimal(goodsInfo.getDoubleValue("moneyDec")));
        }

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(money.doubleValue());
        paymentOrderDto.setName("预约费用");

        reqJson.put("receivableAmount", money.doubleValue());
        reqJson.put("receivedAmount", money.doubleValue());
        return paymentOrderDto;
    }

    private void assertGoodsAndComputeMoney(JSONObject reqJson, JSONObject goodsInfo) {

        ReserveGoodsDto reserveGoodsDto = new ReserveGoodsDto();
        reserveGoodsDto.setGoodsId(goodsInfo.getString("goodsId"));
        List<ReserveGoodsDto> reserveGoodsDtos = reserveGoodsV1InnerServiceSMOImpl.queryReserveGoodss(reserveGoodsDto);
        if ("1001".equals(goodsInfo.getString("type"))) {
            Assert.listOnlyOne(reserveGoodsDtos, "就餐不存在");
        } else {
            Assert.listOnlyOne(reserveGoodsDtos, "服务不存在");
        }

        checkAppointmentTime(reqJson, reserveGoodsDtos.get(0));

        if (!goodsInfo.containsKey("hours")) {
            throw new IllegalArgumentException("未包含 预约时间");
        }

        JSONArray hours = goodsInfo.getJSONArray("hours");

        if (hours == null || hours.size() < 1) {
            throw new IllegalArgumentException("未包含 预约时间");
        }
        ReserveGoodsOrderTimeDto reserveGoodsOrderTimeDto = null;
        int flag = 0;
        int quantity = 0;
        quantity = Integer.parseInt(goodsInfo.getString("quantity"));
        for (int timeIndex = 0; timeIndex < hours.size(); timeIndex++) {
            reserveGoodsOrderTimeDto = new ReserveGoodsOrderTimeDto();
            reserveGoodsOrderTimeDto.setCommunityId(reqJson.getString("communityId"));
            reserveGoodsOrderTimeDto.setAppointmentTime(reqJson.getString("appointmentTime"));
            reserveGoodsOrderTimeDto.setHours(hours.getString(timeIndex));
            reserveGoodsOrderTimeDto.setGoodsId(goodsInfo.getString("goodsId"));
            flag = reserveGoodsOrderTimeV1InnerServiceSMOImpl.queryReserveGoodsOrderTimesCount(reserveGoodsOrderTimeDto);
            if (flag > 0) {
                throw new CmdException(reqJson.getString("appointmentTime") + "," + hours.getString(timeIndex) + "已经被预约");
            }
            if (quantity > Integer.parseInt(reserveGoodsDtos.get(0).getHoursMaxQuantity())) {
                throw new CmdException("预约数量超过设定数量");
            }
        }

        BigDecimal moneyDec = new BigDecimal(hours.size())
                .multiply(new BigDecimal(quantity))
                .multiply(new BigDecimal(Double.parseDouble(reserveGoodsDtos.get(0).getPrice())))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        goodsInfo.put("moneyDec", moneyDec.doubleValue());
    }

    private void checkAppointmentTime(JSONObject reqJson, ReserveGoodsDto reserveGoodsDto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringB(reqJson.getString("appointmentTime")));
        int day;
        String[] days = reserveGoodsDto.getParamWayText().split(",");

        if (ReserveParamsDto.PARAM_WAY_DAY.equals(reserveGoodsDto.getParamWay())) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            if (!Arrays.asList(days).contains(day + "")) {
                throw new CmdException(reqJson.getString("appointmentTime") + "不能预约");
            }
        } else {
            day = calendar.get(Calendar.DAY_OF_WEEK);
            boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
            //获取周几
            //若一周第一天为星期天，则-1
            if (isFirstSunday) {
                day = day - 1;
                if (day == 0) {
                    day = 7;
                }
            }
            if (!Arrays.asList(days).contains(day + "")) {
                throw new CmdException(reqJson.getString("appointmentTime") + "不能预约");
            }
        }
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {
        JSONArray goodss = reqJson.getJSONArray("goodss");
        JSONObject goodsObj = null;
        for (int goodsIndex = 0; goodsIndex < goodss.size(); goodsIndex++) {
            goodsObj = goodss.getJSONObject(goodsIndex);
            ReserveGoodsOrderPo reserveGoodsOrderPo = BeanConvertUtil.covertBean(reqJson, ReserveGoodsOrderPo.class);
            reserveGoodsOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            reserveGoodsOrderPo.setExtOrderId(paymentOrderDto.getOrderId());
            reserveGoodsOrderPo.setGoodsId(goodsObj.getString("goodsId"));

            reserveGoodsOrderPo.setState(ReserveGoodsOrderDto.STATE_W);

            int flag = reserveGoodsOrderV1InnerServiceSMOImpl.saveReserveGoodsOrder(reserveGoodsOrderPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }

            JSONArray hours = goodsObj.getJSONArray("hours");

            ReserveGoodsOrderTimePo reserveGoodsOrderTimePo = null;
            for (int timeIndex = 0; timeIndex < hours.size(); timeIndex++) {
                reserveGoodsOrderTimePo = new ReserveGoodsOrderTimePo();
                reserveGoodsOrderTimePo.setCommunityId(reserveGoodsOrderPo.getCommunityId());
                reserveGoodsOrderTimePo.setGoodsId(reserveGoodsOrderPo.getGoodsId());
                reserveGoodsOrderTimePo.setOrderId(reserveGoodsOrderPo.getOrderId());
                reserveGoodsOrderTimePo.setHours(hours.getString(timeIndex));
                reserveGoodsOrderTimePo.setQuantity(goodss.getJSONObject(goodsIndex).getString("quantity"));
                reserveGoodsOrderTimePo.setTimeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
                reserveGoodsOrderTimePo.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
                reserveGoodsOrderTimeV1InnerServiceSMOImpl.saveReserveGoodsOrderTime(reserveGoodsOrderTimePo);
            }
        }

    }
}