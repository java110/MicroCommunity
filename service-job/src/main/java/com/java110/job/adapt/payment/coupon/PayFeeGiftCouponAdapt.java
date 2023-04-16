package com.java110.job.adapt.payment.coupon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.couponPool.CouponPropertyPoolDto;
import com.java110.dto.couponPool.CouponPropertyPoolConfigDto;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.couponPool.CouponRuleCppsDto;
import com.java110.dto.couponPool.CouponRuleFeeDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.entity.order.Business;
import com.java110.intf.acct.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.couponPropertyPool.CouponPropertyPoolPo;
import com.java110.po.couponPropertyPoolDetail.CouponPropertyPoolDetailPo;
import com.java110.po.couponPropertyUser.CouponPropertyUserPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ExceptionUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 缴费 赠送 优惠券
 *
 * @author fqz
 * @date 2020-12-11  18:54
 */
@Component(value = "payFeeGiftCouponAdapt")
public class PayFeeGiftCouponAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(PayFeeGiftCouponAdapt.class);

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private ICouponRuleFeeV1InnerServiceSMO couponRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private ICouponRuleCppsV1InnerServiceSMO couponRuleCppsV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolDetailV1InnerServiceSMO couponPropertyPoolDetailV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolConfigV1InnerServiceSMO couponPropertyPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyUserV1InnerServiceSMO couponPropertyUserV1InnerServiceSMOImpl;


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();

        if (data != null) {
            logger.debug("请求日志:{}", data);
        }
        JSONArray businessPayFeeDetails = null;
        if (data == null) {
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setbId(business.getbId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
            Assert.listOnlyOne(feeDetailDtos, "未查询到缴费记录");
            businessPayFeeDetails = JSONArray.parseArray(JSONArray.toJSONString(feeDetailDtos, SerializerFeature.WriteDateUseDateFormat));
        } else if (data.containsKey(PayFeeDetailPo.class.getSimpleName())) {
            Object bObj = data.get(PayFeeDetailPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(bObj);
            } else if (bObj instanceof Map) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(JSONObject.parseObject(JSONObject.toJSONString(bObj)));
            } else if (bObj instanceof List) {
                businessPayFeeDetails = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessPayFeeDetails = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(data);
            }
        }

        if (businessPayFeeDetails == null) {
            return;
        }
        for (int bPayFeeDetailIndex = 0; bPayFeeDetailIndex < businessPayFeeDetails.size(); bPayFeeDetailIndex++) {
            JSONObject businessPayFeeDetail = businessPayFeeDetails.getJSONObject(bPayFeeDetailIndex);
            doPayFeeDetail(business, businessPayFeeDetail);
        }
    }

    private void doPayFeeDetail(Business business, JSONObject businessPayFeeDetail) {
        try {
            //查询缴费明细
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessPayFeeDetail, PayFeeDetailPo.class);
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(payFeeDetailPo.getFeeId());
            feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

            Assert.listOnlyOne(feeDtos, "未查询到费用信息");

            if(businessPayFeeDetail.containsKey("receivedAmount")
                    && businessPayFeeDetail.getDoubleValue("receivedAmount")<0){
                return ;
            }

            CouponRuleFeeDto couponRuleFeeDto = new CouponRuleFeeDto();
            couponRuleFeeDto.setFeeConfigId(feeDtos.get(0).getConfigId());
            couponRuleFeeDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            couponRuleFeeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            couponRuleFeeDto.setCycle(payFeeDetailPo.getCycles());
            List<CouponRuleFeeDto> couponRuleFeeDtos = couponRuleFeeV1InnerServiceSMOImpl.queryCouponRuleFees(couponRuleFeeDto);

            if (couponRuleFeeDtos == null || couponRuleFeeDtos.size() < 1) {
                return;
            }

            List<String> ruleIds = new ArrayList<>();
            for (CouponRuleFeeDto tmpCouponRuleFeeDto : couponRuleFeeDtos) {
                ruleIds.add(tmpCouponRuleFeeDto.getRuleId());
            }

            CouponRuleCppsDto couponRuleCppsDto = new CouponRuleCppsDto();
            couponRuleCppsDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
            List<CouponRuleCppsDto> couponRuleCppsDtos = couponRuleCppsV1InnerServiceSMOImpl.queryCouponRuleCppss(couponRuleCppsDto);

            if (couponRuleCppsDtos == null || couponRuleCppsDtos.size() < 1) {
                return;
            }
            //赠送优惠券
            giftCoupon(couponRuleCppsDtos, feeDtos.get(0), payFeeDetailPo);
        } catch (Exception e) {
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_COUPON);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            logger.error("通知异常", e);
        }
    }

    /**
     * 赠送优惠券
     *
     * @param couponRuleCppsDtos
     * @param feeDto
     * @param payFeeDetailPo
     */
    private void giftCoupon(List<CouponRuleCppsDto> couponRuleCppsDtos, FeeDto feeDto, PayFeeDetailPo payFeeDetailPo) {

        Date startTime = null;
        Date endTime = null;
        for (CouponRuleCppsDto couponRuleCppsDto : couponRuleCppsDtos) {
            try {
                if(CouponRuleCppsDto.FREQUENCY_ONCE.equals(couponRuleCppsDto.getGiftFrequency())) { // 只赠送一次
                    doGiftCoupon(couponRuleCppsDto, feeDto, DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
                }else if(CouponRuleCppsDto.FREQUENCY_MONTH.equals(couponRuleCppsDto.getGiftFrequency())){ // 每月赠送
                    startTime = DateUtil.getDateFromString(payFeeDetailPo.getStartTime(), DateUtil.DATE_FORMATE_STRING_B);
                    endTime = DateUtil.getDateFromString(payFeeDetailPo.getEndTime(), DateUtil.DATE_FORMATE_STRING_B);
                    double maxMonth = Math.ceil(computeFeeSMOImpl.dayCompare(startTime, endTime));
                    if (maxMonth < 1) {
                        doGiftCoupon(couponRuleCppsDto, feeDto, DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
                        continue;
                    }
                    Calendar calendar = Calendar.getInstance();
                    for (int month = 0; month < maxMonth; month++) {
                        calendar.setTime(startTime);
                        calendar.add(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH,1);
                        doGiftCoupon(couponRuleCppsDto, feeDto, DateUtil.getFormatTimeString(calendar.getTime(),DateUtil.DATE_FORMATE_STRING_B));
                    }
                }
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_COUPON);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("通知异常", e);
            }
        }

    }

    private void doGiftCoupon(CouponRuleCppsDto couponRuleCppsDto, FeeDto feeDto, String startTime) {


        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + couponRuleCppsDto.getCppId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            CouponPropertyPoolDto couponPropertyPoolDto = new CouponPropertyPoolDto();
            couponPropertyPoolDto.setCppId(couponRuleCppsDto.getCppId());
            List<CouponPropertyPoolDto> couponPropertyPoolDtos = couponPropertyPoolV1InnerServiceSMOImpl.queryCouponPropertyPools(couponPropertyPoolDto);

            if (couponPropertyPoolDtos == null || couponPropertyPoolDtos.size() < 1) {
                return;
            }

            int stock = Integer.parseInt(couponPropertyPoolDtos.get(0).getStock());

            int quantity = Integer.parseInt(couponRuleCppsDto.getQuantity());

            if (stock < quantity) {
                return;
            }


            CouponPropertyPoolConfigDto couponPropertyPoolConfigDto = new CouponPropertyPoolConfigDto();
            couponPropertyPoolConfigDto.setCouponId(couponRuleCppsDto.getCppId());
            List<CouponPropertyPoolConfigDto> couponPropertyPoolConfigDtos
                    = couponPropertyPoolConfigV1InnerServiceSMOImpl.queryCouponPropertyPoolConfigs(couponPropertyPoolConfigDto);
            String value = "";
            for (CouponPropertyPoolConfigDto couponPropertyPoolConfigDto1 : couponPropertyPoolConfigDtos) {
                value += (couponPropertyPoolConfigDto1.getName() + ":" + couponPropertyPoolConfigDto1.getColumnValue() + ";");
            }

            //先加明细
            CouponPropertyPoolDetailPo couponPropertyPoolDetailPo = new CouponPropertyPoolDetailPo();
            couponPropertyPoolDetailPo.setCommunityId(couponPropertyPoolDtos.get(0).getCommunityId());
            couponPropertyPoolDetailPo.setCouponName(couponPropertyPoolDtos.get(0).getCouponName());
            couponPropertyPoolDetailPo.setCppId(couponPropertyPoolDtos.get(0).getCppId());
            couponPropertyPoolDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
            couponPropertyPoolDetailPo.setSendCount(quantity + "");
            couponPropertyPoolDetailPo.setUserId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
            couponPropertyPoolDetailPo.setUserName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
            couponPropertyPoolDetailPo.setTel(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
            couponPropertyPoolDetailPo.setValue(value);
            couponPropertyPoolDetailV1InnerServiceSMOImpl.saveCouponPropertyPoolDetail(couponPropertyPoolDetailPo);

            //优惠券扣除账户
            CouponPropertyPoolPo couponPropertyPoolPo = new CouponPropertyPoolPo();
            couponPropertyPoolPo.setCppId(couponPropertyPoolDtos.get(0).getCppId());
            couponPropertyPoolPo.setStock((stock - quantity) + "");
            couponPropertyPoolV1InnerServiceSMOImpl.updateCouponPropertyPool(couponPropertyPoolPo);

            //用户账户写入优惠券
//            CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
//            couponPropertyUserDto.setTel(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
//            couponPropertyUserDto.setCommunityId(couponPropertyPoolDtos.get(0).getCommunityId());
//            couponPropertyUserDto.setCppId(couponPropertyPoolDtos.get(0).getCppId());
//
//            List<CouponPropertyUserDto> couponPropertyUserDtos = couponPropertyUserV1InnerServiceSMOImpl.queryCouponPropertyUsers(couponPropertyUserDto);
            CouponPropertyUserPo couponPropertyUserPo = new CouponPropertyUserPo();
//            if(couponPropertyUserDtos == null || couponPropertyUserDtos.size()< 1){
            couponPropertyUserPo.setCommunityId(couponPropertyPoolDtos.get(0).getCommunityId());
            couponPropertyUserPo.setCppId(couponPropertyPoolDtos.get(0).getCppId());
            couponPropertyUserPo.setState(CouponPropertyUserDto.STATE_WAIT);
            couponPropertyUserPo.setCouponId(GenerateCodeFactory.getGeneratorId("10"));
            couponPropertyUserPo.setCouponName(couponPropertyPoolDtos.get(0).getCouponName());
            couponPropertyUserPo.setStock(quantity + "");
            couponPropertyUserPo.setToType(couponPropertyPoolDtos.get(0).getToType());
            couponPropertyUserPo.setValidityDay(couponPropertyPoolDtos.get(0).getValidityDay());
            couponPropertyUserPo.setUserId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
            couponPropertyUserPo.setUserName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
            couponPropertyUserPo.setTel(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
            couponPropertyUserPo.setValue(value);
            couponPropertyUserPo.setStartTime(startTime);
            couponPropertyUserV1InnerServiceSMOImpl.saveCouponPropertyUser(couponPropertyUserPo);
            //这里更新功能 关闭 因为优惠券有有效期 如果 修改显然不合适 modify by  2022-11-24 wuxw
//            }else{
//                couponPropertyUserPo.setCouponId(couponPropertyUserDtos.get(0).getCouponId());
//                int userStock = Integer.parseInt(couponPropertyUserDtos.get(0).getStock());
//                couponPropertyUserPo.setStock((quantity+userStock)+"");
//                couponPropertyUserV1InnerServiceSMOImpl.updateCouponPropertyUser(couponPropertyUserPo);
//            }


        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

    }


}
