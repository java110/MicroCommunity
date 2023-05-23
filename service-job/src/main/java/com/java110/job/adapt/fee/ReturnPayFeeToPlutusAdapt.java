package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.PlutusFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.onlinePay.OnlinePayDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.entity.order.Business;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.returnMoney.wechat.ReturnPayFeeMoneyAdapt;
import com.java110.po.onlinePay.OnlinePayPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 退费审核通过后 通知 微信支付平台退款处理
 * 业务功能
 * <p>
 * 商户针对某一笔已经成功支付的订单发起退款。
 * <p>
 * 交互模式
 * <p>
 * 请求：后台请求交互模式
 * <p>
 * 返回结果：后台请求交互模式
 * <p>
 * 说明：支持部份退款；退到银行卡是非实时的，每个银行的处理速度不同，一般发起退款后1-3个工作日内到账。 当调用退款接口返回未知状态，需要调用退款查询接口查询实际退款状态 。 建议 5 秒调一次查询，调用 10 次后状态仍然未知，请在 3 个工作日后重新查询退款状态，或者使用对账单进行确认。
 * <p>
 * http://open.plutuspay.com/Index.html
 *
 * @author wuxw
 * @Date 2021-08-19 10:12
 */
@Component(value = "returnPayFeeToPlutusAdapt")
public class ReturnPayFeeToPlutusAdapt extends DatabusAdaptImpl {
    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;


    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;


    @Autowired
    private RestTemplate outRestTemplate;

    public static final String wechatReturnUrl = "https://api.plutuspay.com/open/v2/refund";

    private static Logger logger = LoggerFactory.getLogger(ReturnPayFeeMoneyAdapt.class);

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;


    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        OnlinePayPo oaWorkflowDataPo = BeanConvertUtil.covertBean(data, OnlinePayPo.class);

        try {
            doPayFeeMoney(oaWorkflowDataPo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 通知退款
     *
     * @param oaWorkflowDataPo
     */
    public void doPayFeeMoney(OnlinePayPo oaWorkflowDataPo) throws Exception {

        //查询小区信息
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setPayId(oaWorkflowDataPo.getPayId());
        onlinePayDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);

        if (onlinePayDtos == null || onlinePayDtos.size() < 1) {
            return;
        }

//        String payPassword = "";
//        String certData = "";

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setMchId(onlinePayDtos.get(0).getMchId());
        smallWeChatDto.setAppId(onlinePayDtos.get(0).getAppId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        String privateKey = "";
        String devId = "";
        String payPassword = "";
        String publicKey = "";
        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            privateKey = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN, "PLUTUS_PRIVATE_KEY");
            devId = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "PLUTUS_DEV_ID");
            payPassword = MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key");
            publicKey = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN, "PLUTUS_PUBLIC_KEY");
        } else {
            privateKey = CommunitySettingFactory.getRemark(smallWeChatDtos.get(0).getObjId(), "PLUTUS_PRIVATE_KEY");
            devId = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "PLUTUS_DEV_ID");
            payPassword = smallWeChatDtos.get(0).getPayPassword();
            publicKey = CommunitySettingFactory.getRemark(smallWeChatDtos.get(0).getObjId(), "PLUTUS_PUBLIC_KEY");
        }


        JSONObject parameters = new JSONObject();
        parameters.put("sn", onlinePayDtos.get(0).getMchId());//商户号
        parameters.put("outTradeId", onlinePayDtos.get(0).getOrderId());//商户号
        parameters.put("outRefundId", onlinePayDtos.get(0).getPayId());//我们自己设定的退款申请号，约束为UK
        parameters.put("refundAmount", PayUtil.moneyToIntegerStr(Double.parseDouble(onlinePayDtos.get(0).getTotalFee())));//订单金额 单位为分！！！这里稍微注意一下


        String param = PlutusFactory.Encryption(parameters.toJSONString(), privateKey, payPassword, devId);
        System.out.println(param);

        String str = PlutusFactory.post(wechatReturnUrl, param);
        System.out.println(str);

        JSONObject json = JSON.parseObject(str);

        String signature = json.getString("signature");
        String content = json.getString("content");


        //验签
        Boolean verify = PlutusFactory.verify256(content, Base64.decode(signature), publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), payPassword);
        //服务器返回内容
        String paramOut = new String(bb);
        System.out.println(paramOut);

        JSONObject paramObj = JSONObject.parseObject(paramOut);

        if (paramObj.getIntValue("status") != 2) {
            doUpdateOnlinePay(onlinePayDtos.get(0).getOrderId(), OnlinePayDto.STATE_FT, paramObj.getString("remark"));
        } else {
            doUpdateOnlinePay(onlinePayDtos.get(0).getOrderId(), OnlinePayDto.STATE_CT, "退款完成");
        }

    }

    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(!StringUtil.isEmpty(message) && message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }

}
