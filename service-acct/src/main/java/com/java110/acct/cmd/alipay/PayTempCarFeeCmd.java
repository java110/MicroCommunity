package com.java110.acct.cmd.alipay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.context.IPageData;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.ownerCarOpenUser.OwnerCarOpenUserDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeCreateOrderV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarOpenUserV1InnerServiceSMO;
import com.java110.po.ownerCarOpenUser.OwnerCarOpenUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        SmallWeChatDto smallWeChatDto = getSmallWechat( reqJson);

        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "wechatAppId"));
            smallWeChatDto.setAppSecret(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "wechatAppSecret"));
            smallWeChatDto.setMchId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "mchId"));
            smallWeChatDto.setPayPassword(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "key"));
        }
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
        paramIn.put("paId",reqJson.getString("paId"));
        paramIn.put("carNum",reqJson.getString("carNum"));
        paramIn.put("machineId",reqJson.getString("machineId"));
        paramIn.put("couponIds",StringUtils.join(couponIds, ","));
        responseEntity = tempCarFeeCreateOrderV1InnerServiceSMOImpl.createOrder(paramIn);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(responseEntity);
            return ;
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
                return ;
            }
            param.put("code", "100");
            param.put("msg", "扣费为0回调成功");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return ;
        }
        String openId = reqJson.getString("openId");
        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_ADAPT);
        //payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAY_ADAPT : payAdapt;
        //支付适配器
//        IPayAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPayAdapt.class);
//        Map result = tPayAdapt.java110Payment(outRestTemplate, paramIn.getString("feeName"), paramIn.getString("tradeType"),
//                orderId, money, openId, smallWeChatDto, wechatAuthProperties.getTempCarFeeNotifyUrl());
   //     responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);
//        if (!"0".equals(result.get("code"))) {
//            context.setResponseEntity(responseEntity);
//            return ;
//        }
//        JSONObject saveFees = new JSONObject();
//        saveFees.put("orderId", paramIn.getString("inoutId"));
//        saveFees.put("carNum", paramIn.getString("carNum"));
//        saveFees.put("amount", money);
//        saveFees.put("paId", paramIn.getString("paId"));
//        saveFees.put("payTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
//        saveFees.put("payType", "2");
//        CommonCache.setValue(FeeDto.REDIS_PAY_TEMP_CAR_FEE + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
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
