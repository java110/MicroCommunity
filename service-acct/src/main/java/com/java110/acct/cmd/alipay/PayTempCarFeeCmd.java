package com.java110.acct.cmd.alipay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarOpenUserDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeCreateOrderV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarOpenUserV1InnerServiceSMO;
import com.java110.po.ownerCarOpenUser.OwnerCarOpenUserPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "alipay.payTempCarFee")
public class PayTempCarFeeCmd extends Cmd {

    @Autowired
    private IOwnerCarOpenUserV1InnerServiceSMO ownerCarOpenUserV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeCreateOrderV1InnerServiceSMO tempCarFeeCreateOrderV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "carNum", "请求报文中未包含房屋信息节点");
        Assert.jsonObjectHaveKey(reqJson, "appId", "请求报文中未包含appId节点");
        Assert.jsonObjectHaveKey(reqJson, "openId", "请求报文中未包含openId节点");
        Assert.jsonObjectHaveKey(reqJson, "paId", "请求报文中未包含paId节点");
        Assert.jsonObjectHaveKey(reqJson, "inoutId", "请求报文中未包含inoutId节点");
        Assert.jsonObjectHaveKey(reqJson, "couponList", "请求报文中未包含couponList节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResponseEntity responseEntity = null;

        //根据paId 查询communityId
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(reqJson.getString("paId"));
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);

        Assert.listOnlyOne(parkingAreaDtos, "停车场不存在");
        reqJson.put("communityId", parkingAreaDtos.get(0).getCommunityId());

        JSONArray couponList = reqJson.getJSONArray("couponList");
        List<String> couponIds = new ArrayList<String>();
        if (couponList != null && couponList.size() > 0) {
            for (int couponIndex = 0; couponIndex < couponList.size(); couponIndex++) {
                couponIds.add(couponList.getJSONObject(couponIndex).getString("couponId"));
            }
        }

        //查询用户ID
        reqJson.put("userId", "-1");

        JSONObject paramIn = new JSONObject();
        paramIn.put("paId", reqJson.getString("paId"));
        paramIn.put("carNum", reqJson.getString("carNum"));
        paramIn.put("machineId", reqJson.getString("machineId"));
        paramIn.put("couponIds", StringUtils.join(couponIds, ","));
        responseEntity = tempCarFeeCreateOrderV1InnerServiceSMOImpl.createOrder(paramIn);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONObject orderInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        if (orderInfo.getIntValue("code") != 0) {
            throw new IllegalArgumentException("缴费失败");
        }
        JSONObject fee = orderInfo.getJSONObject("data");

        double money = fee.getDouble("receivedAmount");

        String orderId = fee.getString("oId");
        //需要判断金额是否 == 0 等于0 直接掉缴费通知接口
        if (money <= 0) {
            JSONObject paramOut = new JSONObject();
            paramOut.put("oId", orderId);
            responseEntity = tempCarFeeCreateOrderV1InnerServiceSMOImpl.notifyOrder(paramIn);
            JSONObject param = new JSONObject();
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                param.put("code", "101");
                param.put("msg", "扣费为0回调失败");
                context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
                return;
            }
            param.put("code", "100");
            param.put("msg", "扣费为0回调成功");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return;
        }
        String openId = reqJson.getString("openId");
        ResultVo result = doAlipay(reqJson, paramIn.getString("feeName"), orderId, money, openId);

        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
        if (!"0".equals(result.getCode())) {
            return;
        }
        JSONObject saveFees = new JSONObject();
        saveFees.put("orderId", paramIn.getString("inoutId"));
        saveFees.put("carNum", paramIn.getString("carNum"));
        saveFees.put("amount", money);
        saveFees.put("paId", paramIn.getString("paId"));
        saveFees.put("payTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        saveFees.put("payType", "2");
        CommonCache.setValue(FeeDto.REDIS_PAY_TEMP_CAR_FEE + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        CommonCache.setValue(FeeDto.REDIS_PAY_TEMP_CAR_FEE_COMMUNITY + orderId, reqJson.getString("communityId"), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        //记录openId 和车辆关系 以免每次 输入 车牌号麻烦
        OwnerCarOpenUserPo ownerCarOpenUserPo = new OwnerCarOpenUserPo();
        ownerCarOpenUserPo.setCarNum(reqJson.getString("carNum"));
        ownerCarOpenUserPo.setNickname("未获取");
        ownerCarOpenUserPo.setHeadimgurl("为获取");
        ownerCarOpenUserPo.setOpenId(openId);
        ownerCarOpenUserPo.setOpenType(OwnerCarOpenUserDto.OPEN_TYPE_ALIPAY);
        ownerCarOpenUserPo.setOpenUserId(GenerateCodeFactory.getGeneratorId("10"));
        ownerCarOpenUserV1InnerServiceSMOImpl.saveOwnerCarOpenUser(ownerCarOpenUserPo);
    }

    private ResultVo doAlipay(JSONObject reqJson, String feeName, String orderId, double money, String openId) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                CommunitySettingFactory.getValue(reqJson.getString("communityId"), "APP_ID"),
                CommunitySettingFactory.getRemark(reqJson.getString("communityId"), "APP_PRIVATE_KEY"),
                "json",
                "UTF-8",
                CommunitySettingFactory.getRemark(reqJson.getString("communityId"), "ALIPAY_PUBLIC_KEY"),
                "RSA2");
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(MappingCache.getValue("ALIPAY", "temp"));
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", money);
        bizContent.put("subject", feeName);
        bizContent.put("buyer_id", openId);
        bizContent.put("timeout_express", "10m");
        request.setBizContent(bizContent.toString());
        AlipayTradeCreateResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK,orderId);
            } else {
                return new ResultVo(ResultVo.CODE_ERROR, response.getMsg());
            }
        } catch (AlipayApiException e) {
            throw new CmdException("支付宝下单失败" + e);
        }
    }

    private SmallWeChatDto getSmallWechat(JSONObject paramIn) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setAppId(paramIn.getString("appId"));
        smallWeChatDto.setObjId(paramIn.getString("communityId"));
        smallWeChatDto.setRow(1);
        smallWeChatDto.setPage(1);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);


        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            return null;
        }

        return smallWeChatDtos.get(0);
    }
}
