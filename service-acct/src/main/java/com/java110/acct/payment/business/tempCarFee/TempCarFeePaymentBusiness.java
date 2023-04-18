package com.java110.acct.payment.business.tempCarFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerCarOpenUserDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.intf.user.IOwnerCarOpenUserV1InnerServiceSMO;
import com.java110.po.ownerCarOpenUser.OwnerCarOpenUserPo;
import com.java110.utils.util.Assert;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 房屋费 停车费缴费
 */
@Service("tempCarFee")
public class TempCarFeePaymentBusiness implements IPaymentBusiness {


    private final static Logger logger = LoggerFactory.getLogger(TempCarFeePaymentBusiness.class);

    @Autowired
    private IOwnerCarOpenUserV1InnerServiceSMO ownerCarOpenUserV1InnerServiceSMOImpl;




    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        Assert.jsonObjectHaveKey(reqJson, "carNum", "请求报文中未包含房屋信息节点");
        Assert.jsonObjectHaveKey(reqJson, "appId", "请求报文中未包含appId节点");
        Assert.jsonObjectHaveKey(reqJson, "openId", "请求报文中未包含openId节点");
        Assert.jsonObjectHaveKey(reqJson, "paId", "请求报文中未包含paId节点");
        Assert.jsonObjectHaveKey(reqJson, "inoutId", "请求报文中未包含inoutId节点");
        Assert.jsonObjectHaveKey(reqJson, "couponList", "请求报文中未包含couponList节点");

        JSONArray couponList = reqJson.getJSONArray("couponList");
        List<String> couponIds = new ArrayList<String>();
        if (couponList != null && couponList.size() > 0) {
            for (int couponIndex = 0; couponIndex < couponList.size(); couponIndex++) {
                couponIds.add(couponList.getJSONObject(couponIndex).getString("couponId"));
            }
        }

        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        String url = "tempCarFee.queryTempCarFeeOrder?paId=" + reqJson.getString("paId")
                + "&carNum=" + reqJson.getString("carNum")
                + "&machineId=" + reqJson.getString("machineId")
                +"&couponIds="+ StringUtils.join(couponIds,",");
        JSONObject fee = CallApiServiceFactory.getForApi(appId, null, url, JSONObject.class);
        double money = fee.getDouble("receivedAmount");
        String orderId = fee.getString("oId");
        String feeName = reqJson.getString("carNum");

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(money);
        paymentOrderDto.setName(feeName+"停车费");


        OwnerCarOpenUserPo ownerCarOpenUserPo = new OwnerCarOpenUserPo();
        ownerCarOpenUserPo.setCarNum(reqJson.getString("carNum"));
        ownerCarOpenUserPo.setNickname("未获取");
        ownerCarOpenUserPo.setHeadimgurl("未获取");
        ownerCarOpenUserPo.setOpenId(reqJson.getString("openId"));
        ownerCarOpenUserPo.setOpenType(OwnerCarOpenUserDto.OPEN_TYPE_WECHAT);
        ownerCarOpenUserPo.setOpenUserId(GenerateCodeFactory.getGeneratorId("10"));
        ownerCarOpenUserV1InnerServiceSMOImpl.saveOwnerCarOpenUser(ownerCarOpenUserPo);


        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {

        JSONObject paramIn = new JSONObject();
        paramIn.put("oId", paymentOrderDto.getOrderId());
        JSONObject paramOut = CallApiServiceFactory.postForApi(paymentOrderDto.getAppId(), reqJson, "tempCarFee.notifyTempCarFeeOrder", JSONObject.class, "-1");


    }

}
