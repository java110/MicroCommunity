package com.java110.acct.payment.adapt.spdb;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.onlinePay.OnlinePayDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.po.onlinePay.OnlinePayPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 浦发银行支付厂家类
 * <p>
 * App支付交易接口
 * <p>
 * App支付退货
 * <p>
 *
 * 1.0 sql 配置说明：
 *
 * INSERT INTO `TT`.`t_dict` ( `status_cd`, `name`, `description`, `create_time`, `table_name`, `table_columns`)
 * VALUES ( '9070', 'SPDB', 'SPDB', '2022-08-16 15:51:55', 'community_setting_key', 'setting_type');
 *
 * INSERT INTO `TT`.`community_setting_key` (`key_id`, `setting_type`, `setting_name`, `setting_key`, `remark`, `create_time`, `status_cd`)
 * VALUES ('82', '9070', 'SPDB_CLIENT_ID', 'SPDB_CLIENT_ID', '应用id', '2021-10-10 21:25:46', '0');
 * INSERT INTO `TT`.`community_setting_key` (`key_id`, `setting_type`, `setting_name`, `setting_key`, `remark`, `create_time`, `status_cd`)
 * VALUES ('88', '9070', 'ICBC_DECIVE_INFO', 'SPDB_SECRET', '秘钥', '2021-10-10 21:25:46', '0');
 * INSERT INTO `TT`.`community_setting_key` (`key_id`, `setting_type`, `setting_name`, `setting_key`, `remark`, `create_time`, `status_cd`)
 * VALUES ('83', '9070', 'ICBC_PRIVATE_KEY(私钥)', 'SPDB_PRIVATE_KEY', '值请填写1 私钥 请填写在备注中', '2021-10-10 21:25:46', '0');
 * INSERT INTO `TT`.`community_setting_key` (`key_id`, `setting_type`, `setting_name`, `setting_key`, `remark`, `create_time`, `status_cd`)
 * VALUES ('84', '9070', 'ICBC_PUBLIC_KEY(公钥)', 'SPDB_PUBLIC_KEY', '值请填写1 公钥 请填写在备注中 ', '2021-10-10 21:25:46', '0');
 * INSERT INTO `TT`.`community_setting_key` (`key_id`, `setting_type`, `setting_name`, `setting_key`, `remark`, `create_time`, `status_cd`)
 * VALUES ('85', '9070', 'ICBC_MER_ID', 'SPDB_MRCH_NO', '值请填写商家ID', '2021-10-10 21:25:46', '0');
 * INSERT INTO `TT`.`community_setting_key` (`key_id`, `setting_type`, `setting_name`, `setting_key`, `remark`, `create_time`, `status_cd`)
 * VALUES ('86', '9070', 'ICBC_MER_PRTCL_NO', 'SPDB_TERMINAL_NO', '值请填写终端', '2021-10-10 21:25:46', '0');
 * // 以下数据先查询是否存在 存在则修改 不存在添加
 * INSERT INTO `TT`.`c_mapping` (`domain`, `name`, `key`, `value`, `remark`, `create_time`, `status_cd`)
 *  VALUES ('WECHAT', '被扫支付厂家', 'PAY_QR_ADAPT', 'qrCodeSpdbPaymentAdapt', '', '2023-02-18 18:47:14', '0');
 *
 * INSERT INTO `TT`.`c_mapping` (`domain`, `name`, `key`, `value`, `remark`, `create_time`, `status_cd`)
 *  VALUES ('WECHAT', '线上支付厂家', 'PAYMENT_ADAPT', 'pufaPaymentFactory', '', '2023-02-18 18:47:14', '0');
 *
 *  2.0 开发代码
 *   包含类为：service-acct模块 com.java110.acct.payment.adapt.spdb 下的所有类
 *  service-acct 模块下 com.java110.acct.smo.impl.QrCodeSpdbPaymentAdapt 类
 *  3.0 pom 配置
 *  在 service-acct 下的pom.xml 中加入以下
 *         <dependency>
 *           <groupId>cn.hutool</groupId>
 *           <artifactId>hutool-all</artifactId>
 *           <version>5.6.5</version>
 *         </dependency>
 *
 *
 *         <dependency>
 *           <groupId>org.bouncycastle</groupId>
 *           <artifactId>bcprov-jdk15to18</artifactId>
 *           <version>1.68</version>
 *         </dependency>
 *  4.0 登录物业系统后在 设置下的系统下的小区配置中配置 相关信息
 *
 */
@Service("pufaPaymentFactory")
public class SpdbPaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(SpdbPaymentFactoryAdapt.class);


    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    public static final String TRADE_TYPE_NATIVE = "NATIVE";
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    public static final String TRADE_TYPE_APP = "APP";


    public static final String wxPayUnifiedOrder = "https://api.spdb.com.cn/spdb/prd/api/acquiring/appPay/initiation";

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;


    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;


    @Override
    public Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {

        SmallWeChatDto smallWeChatDto = getSmallWechat(reqJson);


        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        String tradeType = reqJson.getString("tradeType");
        String notifyUrl = UrlCache.getOwnerUrl() + "/app/payment/notify/wechat/992020011134400001";

        String openId = reqJson.getString("openId");

        if (StringUtil.isEmpty(openId)) {
            String appType = OwnerAppUserDto.APP_TYPE_WECHAT_MINA;
            if (AppDto.WECHAT_OWNER_APP_ID.equals(appId)) {
                appType = OwnerAppUserDto.APP_TYPE_WECHAT;
            } else if (AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) {
                appType = OwnerAppUserDto.APP_TYPE_WECHAT_MINA;
            } else {
                appType = OwnerAppUserDto.APP_TYPE_APP;
            }

            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setUserId(userId);
            ownerAppUserDto.setAppType(appType);
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

            if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
                throw new IllegalArgumentException("未找到开放账号信息");
            }
            openId = ownerAppUserDtos.get(0).getOpenId();
        }


        logger.debug("【小程序支付】 统一下单开始, 订单编号=" + paymentOrderDto.getOrderId());
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), paymentOrderDto.getMoney());
        //添加或更新支付记录(参数跟进自己业务需求添加)

        JSONObject resMap = null;
        resMap = this.java110UnifieldOrder(paymentOrderDto.getName(),
                paymentOrderDto.getOrderId(),
                tradeType,
                payAmount,
                openId,
                smallWeChatDto,
                notifyUrl
        );


        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            if (TRADE_TYPE_JSAPI.equals(tradeType)) {

                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("package", "prepay_id=" + resMap.get("prepay_id"));
                resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (TRADE_TYPE_APP.equals(tradeType)) {
                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("partnerid", smallWeChatDto.getMchId());
                resultMap.put("prepayid", resMap.getString("prepay_id"));
                //resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (TRADE_TYPE_NATIVE.equals(tradeType)) {
                resultMap.put("prepayId", resMap.getString("prepay_id"));
                resultMap.put("codeUrl", resMap.getString("code_url"));
            }
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap + "===notifyUrl===" + notifyUrl);
        } else {
            resultMap.put("code", resMap.getString("return_code"));
            resultMap.put("msg", resMap.getString("return_msg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("return_msg") + "===code===" + resMap.get("return_code") + "===notifyUrl===" + notifyUrl);
        }
        return resultMap;
    }


    /**
     * {
     * "cmdtyCd": "",
     * "terminalNo": "98009459",
     * "subMechNoAcctID": "wx13de10893e064009",
     * "channelNo": "00",
     * "spdbMrchNo": "310999880620003",
     * "cmdtyDtl": "[993137072803]自助生活缴费",
     * "terminaIP": "223.104.247.218",
     * "frmrMrchOrdrNo": "20113017050000041231",
     * "cmdtyDsc": "[993137072803]自助生活缴费",
     * "tranType": "OA",
     * "tranAmt": 0.01,
     * "mrchlInfmAdr": "http://paycenter-channel.hw-qa.eslink.net.cn/eslink/pay/spdb/callback",
     * "usrChildFlg": "oaX8s04YCscDovz8Re-c7GxyWZWY"
     * }
     *
     * @param feeName
     * @param orderNum
     * @param tradeType
     * @param payAmount
     * @param openid
     * @param smallWeChatDto
     * @param notifyUrl
     * @return
     * @throws Exception
     */
    private JSONObject java110UnifieldOrder(String feeName, String orderNum,
                                            String tradeType, double payAmount, String openid,
                                            SmallWeChatDto smallWeChatDto, String notifyUrl) throws Exception {

        String clientId = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "SPDB_CLIENT_ID");
        String secret = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "SPDB_SECRET");
        String privateKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(), "SPDB_PRIVATE_KEY");
        String apiPublicKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(), "SPDB_PUBLIC_KEY");
        String terminalNo = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "SPDB_TERMINAL_NO");
        String spdbMrchNo = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "SPDB_MRCH_NO");

        SPDBSecurity spdbSecurity = new SPDBSecurity(clientId, secret, privateKey, apiPublicKey);
        SPDBApiClient spdbApiClient = new SPDBApiClient(spdbSecurity);

        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("terminalNo", terminalNo);
        paramIn.put("subMechNoAcctID", smallWeChatDto.getAppId());
        paramIn.put("channelNo", "00");
        paramIn.put("spdbMrchNo", spdbMrchNo);
        paramIn.put("cmdtyDtl", "[" + spdbMrchNo + "]" + feeName);
        paramIn.put("terminaIP", PayUtil.getLocalIp());
        paramIn.put("frmrMrchOrdrNo", orderNum);
        paramIn.put("cmdtyDsc", "[" + spdbMrchNo + "]" + feeName);
        if (TRADE_TYPE_JSAPI.equals(tradeType)) {
            paramIn.put("tranType", "OA");
        } else if (TRADE_TYPE_APP.equals(tradeType)) {
            paramIn.put("tranType", "OM");
        } else {
            throw new IllegalArgumentException("不支持的支付类型");
        }
        paramIn.put("tranAmt", payAmount);
        paramIn.put("mrchlInfmAdr", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramIn.put("usrChildFlg", openid);


        SPDBApiResponse response = spdbApiClient.post(wxPayUnifiedOrder, paramIn.toJSONString());

        if (response.getHttpStatus() != 0000) {
            throw new IllegalArgumentException(response.getResBody());
        }

        JSONObject paramOut = JSONObject.parseObject(response.getResBody());

        if (!"0000".equals(paramOut.getString("statusCode"))) {
            throw new IllegalArgumentException(paramOut.getString("statusMsg"));
        }


        logger.debug("统一下单返回" + response.getResBody());
//请求微信后台，获取预支付ID

        doSaveOnlinePay(smallWeChatDto, openid, orderNum, feeName, payAmount, OnlinePayDto.STATE_WAIT, "待支付");
        return JSONObject.parseObject(paramOut.getString("sgnData")); //signature
    }


    @Override
    public PaymentOrderDto java110NotifyPayment(String param) {
        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        JSONObject json = JSONObject.parseObject(param);


        //更新数据
        paymentOrderDto.setOrderId(json.getString("MrchOrdrNo3"));
        paymentOrderDto.setTransactionId(json.getString("ThdPtySeq"));

        JSONObject paramOut = new JSONObject();
        paramOut.put("AcceptFlag", "0");

        paymentOrderDto.setResponseEntity(new ResponseEntity<String>(paramOut.toJSONString(), HttpStatus.OK));
        doUpdateOnlinePay(json.getString("MrchOrdrNo3"), OnlinePayDto.STATE_COMPILE, "支付成功");
        return paymentOrderDto;
    }


    private SmallWeChatDto getSmallWechat(JSONObject paramIn) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(paramIn.getString("communityId"));
        smallWeChatDto.setAppId(paramIn.getString("appId"));
        smallWeChatDto.setPage(1);
        smallWeChatDto.setRow(1);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);

        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId"));
            smallWeChatDto.setAppSecret(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret"));
            smallWeChatDto.setMchId(MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "mchId"));
            smallWeChatDto.setPayPassword(MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key"));
            return smallWeChatDto;
        }

        return BeanConvertUtil.covertBean(smallWeChatDtos.get(0), SmallWeChatDto.class);
    }


    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }

    private void doSaveOnlinePay(SmallWeChatDto smallWeChatDto, String openId, String orderId, String feeName, double money, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setAppId(smallWeChatDto.getAppId());
        onlinePayPo.setMchId(smallWeChatDto.getMchId());
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOpenId(openId);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setPayId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        onlinePayPo.setPayName(feeName);
        onlinePayPo.setRefundFee("0");
        onlinePayPo.setState(state);
        onlinePayPo.setTotalFee(money + "");
        onlinePayPo.setTransactionId(orderId);
        onlinePayV1InnerServiceSMOImpl.saveOnlinePay(onlinePayPo);
    }

}
