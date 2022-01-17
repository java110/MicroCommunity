package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.ChinaUmsFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.order.BusinessDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.returnPayFee.ReturnPayFeeDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.fee.ReturnPayFeePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.DomainContant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 退费审核通过后 通知 银联支付平台退款处理
 *
 * @author fqz
 * @Date 2021-08-19 10:12
 */
@Component(value = "returnPayFeeToChinaUmsAdapt")
public class ReturnPayFeeToChinaUmsAdapt extends DatabusAdaptImpl {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    public static final String chinaUmsReturnUrl = "chinaUmsReturnUrl";

    private static Logger logger = LoggerFactory.getLogger(ReturnPayFeeToChinaUmsAdapt.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessReturnPayFees = null;
        if (data.containsKey(ReturnPayFeePo.class.getSimpleName())) {
            Object bObj = data.get(ReturnPayFeePo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessReturnPayFees = new JSONArray();
                businessReturnPayFees.add(bObj);
            } else if (bObj instanceof Map) {
                businessReturnPayFees = new JSONArray();
                businessReturnPayFees.add(JSONObject.parseObject(JSONObject.toJSONString(bObj)));
            } else if (bObj instanceof List) {
                businessReturnPayFees = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessReturnPayFees = (JSONArray) bObj;
            }
        } else {
            return;
        }

        if (businessReturnPayFees == null) {
            return;
        }
        for (int bReturnPayFeeIndex = 0; bReturnPayFeeIndex < businessReturnPayFees.size(); bReturnPayFeeIndex++) {
            JSONObject businessReturnPayFee = businessReturnPayFees.getJSONObject(bReturnPayFeeIndex);
            doPayFeeToChinaUms(business, businessReturnPayFee);
        }
    }

    /**
     * 通知退款
     *
     * @param business
     * @param businessReturnPayFee
     */
    public void doPayFeeToChinaUms(Business business, JSONObject businessReturnPayFee) {

        Assert.hasKeyAndValue(businessReturnPayFee, "returnFeeId", "未包含退费ID");
        Assert.hasKeyAndValue(businessReturnPayFee, "communityId", "未包含小区ID");
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(businessReturnPayFee.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "查询小区错误！");

        //校验退费审核是否为通过
        ReturnPayFeeDto returnPayFeeDto = new ReturnPayFeeDto();
        returnPayFeeDto.setReturnFeeId(businessReturnPayFee.getString("returnFeeId"));
        returnPayFeeDto.setCommunityId(businessReturnPayFee.getString("communityId"));
        returnPayFeeDto.setState("1100");
        List<ReturnPayFeeDto> returnPayFeeDtos = returnPayFeeInnerServiceSMOImpl.queryReturnPayFees(returnPayFeeDto);

        if (returnPayFeeDtos == null || returnPayFeeDtos.size() < 1) {
            return;//说明没有退款成功
        }

        //查询缴费明细表
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(returnPayFeeDtos.get(0).getDetailId());
        feeDetailDto.setCommunityId(returnPayFeeDtos.get(0).getCommunityId());
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        Assert.listOnlyOne(feeDetailDtos, "缴费明细不存在");

        //查询缴费订单号
        BusinessDto businessDto = new BusinessDto();
        businessDto.setbId(feeDetailDtos.get(0).getbId());
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOrderByBId(businessDto);

        Assert.listOnlyOne(orderDtos, "订单不存在");

        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }


        JSONObject paramMap = new JSONObject();
        paramMap.put("requestTimestamp", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        paramMap.put("msgId", GenerateCodeFactory.getUUID()); // 富友分配给二级商户的商户号
        paramMap.put("mid", smallWeChatDtos.get(0).getMchId()); // 富友分配给二级商户的商户号
        paramMap.put("tid", "88880001"); //终端号
        paramMap.put("instMid", "YUEDANDEFAULT");
        paramMap.put("merOrderId", "1017" + orderDtos.get(0).getoId());
        paramMap.put("refundOrderId", feeDetailDtos.get(0).getDetailId());
        paramMap.put("refundAmount", PayUtil.moneyToIntegerStr(Double.parseDouble(feeDetailDtos.get(0).getReceivedAmount())));


        logger.debug("调用支付统一下单接口" + paramMap.toJSONString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", ChinaUmsFactory.getAccessToken(smallWeChatDtos.get(0)));
        HttpEntity httpEntity = new HttpEntity(paramMap.toJSONString(), headers);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(
                MappingCache.getValue(DomainContant.CHINA_UMS_DOMAIN, chinaUmsReturnUrl), HttpMethod.POST, httpEntity, String.class);

        logger.debug("退款 请求报文：" + paramMap.toJSONString() + ",返回报文：" + responseEntity);

    }
}
