package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IToPayTempCarFeeSMO;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerCarOpenUserDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarOpenUserV1InnerServiceSMO;
import com.java110.po.ownerCarOpenUser.OwnerCarOpenUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service("toPayTempCarFeeSMOImpl")
public class ToPayTempCarFeeSMOImpl extends AppAbstractComponentSMO implements IToPayTempCarFeeSMO {
    private static final Logger logger = LoggerFactory.getLogger(ToPayTempCarFeeSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;


    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private IOwnerCarOpenUserV1InnerServiceSMO ownerCarOpenUserV1InnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> toPay(IPageData pd) {
        return super.businessProcess(pd);
    }

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;


    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "carNum", "请求报文中未包含房屋信息节点");
        Assert.jsonObjectHaveKey(paramIn, "appId", "请求报文中未包含appId节点");
        Assert.jsonObjectHaveKey(paramIn, "openId", "请求报文中未包含openId节点");
        Assert.jsonObjectHaveKey(paramIn, "paId", "请求报文中未包含paId节点");
        Assert.jsonObjectHaveKey(paramIn, "inoutId", "请求报文中未包含inoutId节点");
        Assert.jsonObjectHaveKey(paramIn, "couponList", "请求报文中未包含couponList节点");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {

        ResponseEntity responseEntity = null;

        //根据paId 查询communityId
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(paramIn.getString("paId"));
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);

        Assert.listOnlyOne(parkingAreaDtos,"停车场不存在");
        paramIn.put("communityId",parkingAreaDtos.get(0).getCommunityId());
        SmallWeChatDto smallWeChatDto = getSmallWechat(pd, paramIn);

        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }
        JSONArray couponList = paramIn.getJSONArray("couponList");
        List<String> couponIds = new ArrayList<String>();
        if (couponList != null && couponList.size() > 0) {
            for (int couponIndex = 0; couponIndex < couponList.size(); couponIndex++) {
                couponIds.add(couponList.getJSONObject(couponIndex).getString("couponId"));
            }
        }
        //查询用户ID
        paramIn.put("userId", pd.getUserId());
        String url = "tempCarFee.queryTempCarFeeOrder?paId=" + paramIn.getString("paId")
                + "&carNum=" + paramIn.getString("carNum")
                + "&machineId=" + paramIn.getString("machineId")
                +"&couponIds="+StringUtils.join(couponIds,",");
        responseEntity = super.callCenterService(restTemplate, pd, "", url, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
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
            paramOut.put("payType", TempCarPayOrderDto.PAY_TYPE_WECHAT);
            String urlOut = "tempCarFee.notifyTempCarFeeOrder";
            responseEntity = this.callCenterService(getHeaders("-1", pd.getAppId()), paramOut.toJSONString(), urlOut, HttpMethod.POST);
            JSONObject param = new JSONObject();
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                param.put("code", "101");
                param.put("msg", "扣费为0回调失败");
                return new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK);
            }
            param.put("code", "100");
            param.put("msg", "扣费为0回调成功");
            return new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK);
        }
        String openId = paramIn.getString("openId");
        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAY_ADAPT : payAdapt;
        //支付适配器
        IPayAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPayAdapt.class);
        Map result = tPayAdapt.java110Payment(outRestTemplate, paramIn.getString("feeName"), paramIn.getString("tradeType"),
                orderId, money, openId, smallWeChatDto, wechatAuthProperties.getTempCarFeeNotifyUrl());
        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);
        if (!"0".equals(result.get("code"))) {
            return responseEntity;
        }
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
        ownerCarOpenUserPo.setCarNum(paramIn.getString("carNum"));
        ownerCarOpenUserPo.setNickname("未获取");
        ownerCarOpenUserPo.setHeadimgurl("为获取");
        ownerCarOpenUserPo.setOpenId(openId);
        ownerCarOpenUserPo.setOpenType(OwnerCarOpenUserDto.OPEN_TYPE_WECHAT);
        ownerCarOpenUserPo.setOpenUserId(GenerateCodeFactory.getGeneratorId("10"));
        ownerCarOpenUserV1InnerServiceSMOImpl.saveOwnerCarOpenUser(ownerCarOpenUserPo);
        return responseEntity;
    }

    private Map<String, String> getHeaders(String userId,String APP_ID) {
        Map<String, String> headers = new HashMap<>();
        headers.put(CommonConstant.HTTP_APP_ID.toLowerCase(), APP_ID);
        headers.put(CommonConstant.HTTP_USER_ID.toLowerCase(), userId);
        headers.put(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        headers.put(CommonConstant.HTTP_REQ_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        headers.put(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        return headers;
    }
    private SmallWeChatDto getSmallWechat(IPageData pd, JSONObject paramIn) {

        ResponseEntity responseEntity = null;

        pd = PageData.newInstance().builder(pd.getUserId(), "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?appId="
                        + paramIn.getString("appId") + "&page=1&row=1&communityId=" + paramIn.getString("communityId"), HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");

        if (smallWeChats == null || smallWeChats.size() < 1) {
            return null;
        }

        return BeanConvertUtil.covertBean(smallWeChats.get(0), SmallWeChatDto.class);
    }


}
