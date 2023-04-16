package com.java110.acct.payment.business.oweFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 欠费缴费
 */
@Service("oweFee")
public class OweFeePaymentBusiness implements IPaymentBusiness {


    private final static Logger logger = LoggerFactory.getLogger(OweFeePaymentBusiness.class);

    public static final String CODE_PREFIX_ID = "10";


    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String TOTAL_FEE_PRICE = "TOTAL_FEE_PRICE";

    //键
    public static final String RECEIVED_AMOUNT_SWITCH = "RECEIVED_AMOUNT_SWITCH";

    //禁用电脑端提交收费按钮
    public static final String OFFLINE_PAY_FEE_SWITCH = "OFFLINE_PAY_FEE_SWITCH";

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;


    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {
        String userId = context.getReqHeaders().get("user-id");

        String ownerId = reqJson.getString("ownerId");
        String roomId = reqJson.getString("roomId");

        String payObjType = "3333";
        if (reqJson.containsKey("payObjType")) {
            payObjType = reqJson.getString("payObjType");
        }

        FeeDto feeDto = new FeeDto();
        if (!StringUtil.isEmpty(ownerId)) {
            feeDto.setOwnerId(ownerId);
        } else {
            feeDto.setPayerObjId(roomId);
            feeDto.setPayerObjType(payObjType);
        }
        feeDto.setCommunityId(reqJson.getString("communityId"));


        //查询费用信息arrearsEndTime
        feeDto.setArrearsEndTime(DateUtil.getCurrentDate());
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            throw new IllegalArgumentException("未包含欠费费用");
        }

        String val = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(), TOTAL_FEE_PRICE);
        if (StringUtil.isEmpty(val)) {
            val = MappingCache.getValue(DOMAIN_COMMON, TOTAL_FEE_PRICE);
        }
        List<FeeDto> tmpFeeDtos = new ArrayList<>();
        double money = 0.0;
        BigDecimal tmpMoney = new BigDecimal(money);
        BigDecimal feeTotalPrice = null;
        for (FeeDto tmpFeeDto : feeDtos) {
            try {
                computeFeeSMOImpl.computeEveryOweFee(tmpFeeDto);//计算欠费金额
                //如果金额为0 就排除
                //if (tmpFeeDto.getFeePrice() > 0 && tmpFeeDto.getEndTime().getTime() <= DateUtil.getCurrentDate().getTime()) {
                tmpFeeDto.setVal(val);
                if (tmpFeeDto.getFeeTotalPrice() > 0 && "Y".equals(tmpFeeDto.getPayOnline())) {
                    tmpFeeDtos.add(tmpFeeDto);
                    feeTotalPrice = new BigDecimal(tmpFeeDto.getFeeTotalPrice());
                    tmpMoney = tmpMoney.add(feeTotalPrice);
                }
            } catch (Exception e) {
                logger.error("可能费用资料有问题导致算费失败", e);
            }
        }

        String feeName = getFeeName(feeDtos.get(0));

        String orderId = GenerateCodeFactory.getOId();

        money = tmpMoney.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(money);
        paymentOrderDto.setName(feeName + "欠费费用");

        JSONObject saveFees = new JSONObject();
        saveFees.put("orderId", orderId);
        saveFees.put("userId", userId);
        saveFees.put("money", money);
        saveFees.put("roomId", roomId);
        saveFees.put("communityId", reqJson.getString("communityId"));
        saveFees.put("fees", tmpFeeDtos);
        CommonCache.setValue(FeeDto.REDIS_PAY_OWE_FEE + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {

        String order = CommonCache.getAndRemoveValue(FeeDto.REDIS_PAY_OWE_FEE + paymentOrderDto.getOrderId());

        JSONObject paramIn = JSONObject.parseObject(order);
        paramIn.put("oId", paymentOrderDto.getOrderId());
        freshFees(paramIn);
        JSONObject paramOut = CallApiServiceFactory.postForApi(paymentOrderDto.getAppId(), paramIn, "fee.payOweFee", JSONObject.class, paramIn.getString("userId"));

    }


    private void freshFees(JSONObject paramIn) {
        if (!paramIn.containsKey("fees")) {
            return;
        }

        JSONArray fees = paramIn.getJSONArray("fees");
        JSONObject fee = null;
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            fee = fees.getJSONObject(feeIndex);
            if (fee.containsKey("deadlineTime")) {
                fee.put("startTime", DateUtil.getFormatTimeString(fee.getDate("endTime"), DateUtil.DATE_FORMATE_STRING_A));
                fee.put("endTime", DateUtil.getFormatTimeString(fee.getDate("deadlineTime"), DateUtil.DATE_FORMATE_STRING_A));
                fee.put("receivedAmount", fee.getString("feeTotalPrice"));
                fee.put("state", "");
            }
        }
    }

    private String getFeeName(FeeDto feeDto) {
        //查询小区名称
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(feeDto.getCommunityId());
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");

        List<FeeAttrDto> feeAttrDtos = feeDto.getFeeAttrDtos();
        if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
            return communityDtos.get(0).getName() + "-" + feeDto.getFeeName();
        }

        for (FeeAttrDto feeAttrDto : feeAttrDtos) {
            if (FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME.equals(feeAttrDto.getSpecCd())) {
                return communityDtos.get(0).getName() + "-" + feeAttrDto.getValue() + "-" + feeDto.getFeeName();
            }
        }

        return communityDtos.get(0).getName() + "-" + feeDto.getFeeName();
    }
}
