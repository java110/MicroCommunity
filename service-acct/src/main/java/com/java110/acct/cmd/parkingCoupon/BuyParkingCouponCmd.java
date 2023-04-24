package com.java110.acct.cmd.parkingCoupon;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.parkingCoupon.ParkingCouponDto;
import com.java110.dto.parkingCoupon.ParkingCouponShopDto;
import com.java110.dto.shop.ShopDto;
import com.java110.dto.storeShop.StoreShopDto;
import com.java110.intf.acct.IParkingCouponOrderV1InnerServiceSMO;
import com.java110.intf.acct.IParkingCouponShopV1InnerServiceSMO;
import com.java110.intf.acct.IParkingCouponV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.mall.IShopInnerServiceSMO;
import com.java110.intf.store.IStoreShopV1InnerServiceSMO;
import com.java110.po.parkingCouponOrder.ParkingCouponOrderPo;
import com.java110.po.parkingCouponShop.ParkingCouponShopPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;


@Java110CmdDoc(title = "购买停车卷",
        description = "可供第三平台购买停车卷使用，费用 需要第三平台 收取",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/parkingCoupon.buyParkingCoupon",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "parkingCoupon.buyParkingCoupon"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "couponId", length = 30, remark = "优惠券ID"),
        @Java110ParamDoc(name = "quantity", length = 30, remark = "优惠券数量"),
        @Java110ParamDoc(name = "shopId", length = 30, remark = "店铺ID"),
        @Java110ParamDoc(name = "payType", length = 30, remark = "支付方式"),
        @Java110ParamDoc(name = "receivedAmount", length = 30, remark = "实收金额"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),

        }
)

@Java110ExampleDoc(
        reqBody = "http://{ip}:{port}/app/parkingCoupon.listParkingCoupon?page=1&row=100&communityId=2022081539020475",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
/**
 * 购买 停车卷
 */
@Java110Cmd(serviceCode = "parkingCoupon.buyParkingCoupon")
public class BuyParkingCouponCmd extends Cmd {

    @Autowired
    private IParkingCouponV1InnerServiceSMO parkingCouponV1InnerServiceSMOImpl;
    @Autowired
    private IParkingCouponShopV1InnerServiceSMO parkingCouponShopV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponOrderV1InnerServiceSMO parkingCouponOrderV1InnerServiceSMOImpl;

    @Autowired(required = false)
    private IShopInnerServiceSMO shopInnerServiceSMOImpl;

    @Autowired
    private IStoreShopV1InnerServiceSMO storeShopV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "couponId", "未包含购买优惠券");
        Assert.hasKeyAndValue(reqJson, "quantity", "未包含购买数量");
        Assert.hasKeyAndValue(reqJson, "shopId", "未包含店铺");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "未包含购买实收");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "payType", "未包含支付方式");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        //查询优惠券
        ParkingCouponDto parkingCouponDto = new ParkingCouponDto();
        parkingCouponDto.setCouponId(reqJson.getString("couponId"));
        parkingCouponDto.setCommunityId(reqJson.getString("communityId"));
        List<ParkingCouponDto> parkingCouponDtos = parkingCouponV1InnerServiceSMOImpl.queryParkingCoupons(parkingCouponDto);

        Assert.listOnlyOne(parkingCouponDtos, "停车卷不存在");

        // 查询商户信息
        List<ShopDto> storeShopDtos = null;
        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            ShopDto storeShopDto = new ShopDto();
            storeShopDto.setShopId(reqJson.getString("shopId"));
            storeShopDtos = shopInnerServiceSMOImpl.queryShops(storeShopDto);
        } else {
            StoreShopDto storeShopDto = new StoreShopDto();
            storeShopDto.setShopId(reqJson.getString("shopId"));
            storeShopDtos = storeShopV1InnerServiceSMOImpl.queryStoreShops(storeShopDto);
        }

        Assert.listOnlyOne(storeShopDtos, "商家店铺不存在");

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");

        ParkingCouponShopDto parkingCouponShopDto = new ParkingCouponShopDto();
        parkingCouponShopDto.setShopId(reqJson.getString("shopId"));
        parkingCouponShopDto.setCommunityId(reqJson.getString("communityId"));
        parkingCouponShopDto.setCouponId(reqJson.getString("couponId"));
        List<ParkingCouponShopDto> parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShops(parkingCouponShopDto);
        ParkingCouponShopPo parkingCouponShopPo = null;
        int flag = 0;
        if (parkingCouponShopDtos == null || parkingCouponShopDtos.size() < 1) {
            parkingCouponShopPo = new ParkingCouponShopPo();
            parkingCouponShopPo.setCommunityId(reqJson.getString("communityId"));
            parkingCouponShopPo.setCouponShopId(GenerateCodeFactory.getGeneratorId("10"));
            parkingCouponShopPo.setShopId(reqJson.getString("shopId"));
            parkingCouponShopPo.setShopName(storeShopDtos.get(0).getShopName());
            parkingCouponShopPo.setEndTime("2050-01-01");
            parkingCouponShopPo.setCommunityName(communityDtos.get(0).getName());
            parkingCouponShopPo.setCouponId(reqJson.getString("couponId"));
            parkingCouponShopPo.setPaId(parkingCouponDtos.get(0).getPaId());
            parkingCouponShopPo.setPaName(parkingCouponDtos.get(0).getPaName());
            parkingCouponShopPo.setQuantity(reqJson.getString("quantity"));
            parkingCouponShopPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            flag = parkingCouponShopV1InnerServiceSMOImpl.saveParkingCouponShop(parkingCouponShopPo);
        } else {
            String quantityStr = parkingCouponShopDtos.get(0).getQuantity();
            int quantity = Integer.parseInt(quantityStr) + Integer.parseInt(reqJson.getString("quantity"));
            parkingCouponShopPo = new ParkingCouponShopPo();
            parkingCouponShopPo.setCouponShopId(parkingCouponShopDtos.get(0).getCouponShopId());
            parkingCouponShopPo.setQuantity(quantity + "");
            flag = parkingCouponShopV1InnerServiceSMOImpl.updateParkingCouponShop(parkingCouponShopPo);
        }

        if (flag < 1) {
            throw new CmdException("增加停车卷失败");
        }

        //写停车卷购买订单表

        BigDecimal quantityDec = new BigDecimal(reqJson.getString("quantity"));
        quantityDec = quantityDec.multiply(new BigDecimal(parkingCouponDtos.get(0).getValuePrice())).setScale(2, BigDecimal.ROUND_HALF_UP);

        ParkingCouponOrderPo parkingCouponOrderPo = new ParkingCouponOrderPo();
        parkingCouponOrderPo.setCouponId(reqJson.getString("couponId"));
        parkingCouponOrderPo.setCommunityId(reqJson.getString("communityId"));
        parkingCouponOrderPo.setCommunityName(communityDtos.get(0).getName());
        parkingCouponOrderPo.setQuantity(reqJson.getString("quantity"));
        parkingCouponOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId("11"));
        parkingCouponOrderPo.setPaId(parkingCouponDtos.get(0).getPaId());
        parkingCouponOrderPo.setPaName(parkingCouponDtos.get(0).getPaName());
        parkingCouponOrderPo.setPrice(parkingCouponDtos.get(0).getValuePrice());
        parkingCouponOrderPo.setReceivableAmount(quantityDec.doubleValue() + "");
        parkingCouponOrderPo.setReceivedAmount(reqJson.getString("receivedAmount"));
        parkingCouponOrderPo.setRemark(reqJson.getString("remark"));
        parkingCouponOrderPo.setShopId(reqJson.getString("shopId"));
        parkingCouponOrderPo.setShopName(storeShopDtos.get(0).getShopName());
        parkingCouponOrderPo.setPayType(reqJson.getString("payType"));

        flag = parkingCouponOrderV1InnerServiceSMOImpl.saveParkingCouponOrder(parkingCouponOrderPo);
        if (flag < 1) {
            throw new CmdException("支付订单失败");
        }

    }
}
