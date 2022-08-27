package com.java110.fee.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.couponUser.CouponUserDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.fee.bmo.tempCarFee.IGetTempCarFeeRules;
import com.java110.intf.acct.ICouponUserDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeCreateOrderV1InnerServiceSMO;
import com.java110.po.couponUser.CouponUserPo;
import com.java110.po.couponUserDetail.CouponUserDetailPo;
import com.java110.po.tempCarFeeConfig.TempCarFeeConfigPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
public class TempCarFeeCreateOrderV1InnerServiceSMOImpl  extends BaseServiceSMO implements ITempCarFeeCreateOrderV1InnerServiceSMO {

    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;
    @Autowired
    private IGetTempCarFeeRules getTempCarFeeRulesImpl;

    @Autowired
    private ICouponUserDetailV1InnerServiceSMO couponUserDetailV1InnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> createOrder(@RequestBody JSONObject reqJson) {

        TempCarPayOrderDto tempCarPayOrderDto = new TempCarPayOrderDto();
        tempCarPayOrderDto.setPaId(reqJson.getString("paId"));
        tempCarPayOrderDto.setCarNum(reqJson.getString("carNum"));
        ResponseEntity<String> responseEntity = getTempCarFeeRulesImpl.getTempCarFeeOrder(tempCarPayOrderDto);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject orderInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        if (orderInfo.getIntValue("code") != 0) {
            return responseEntity;
        }

        JSONObject fee = orderInfo.getJSONObject("data");
        //double money = fee.getDouble("payCharge");
        BigDecimal money = new BigDecimal(fee.getDouble("payCharge"));
        //3.0 考虑优惠卷
        double couponPrice = checkCouponUser(reqJson);
        money = money.subtract(new BigDecimal(couponPrice)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        double receivedAmount = money.doubleValue();
        //所有 优惠折扣计算完后，如果总金额小于等于0，则返回总扣款为0
        if (receivedAmount <= 0) {
            receivedAmount = 0.0;
        }
        fee.put("receivedAmount", receivedAmount);
        fee.put("oId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oId));
        JSONObject outParm = new JSONObject();
        outParm.put("data",fee);
        outParm.put("code","0");
        outParm.put("msg","成功");
        ResponseEntity<String> responseEntitys = new ResponseEntity<>(outParm.toJSONString(), HttpStatus.OK);
        fee.putAll(reqJson);
        CommonCache.setValue("queryTempCarFeeOrder" + fee.getString("oId"), fee.toJSONString(), 24 * 60 * 60);
        return responseEntitys;
    }

    @Override
    public ResponseEntity<String> notifyOrder(@RequestBody JSONObject reqJson) {
        String paramIn = CommonCache.getAndRemoveValue("queryTempCarFeeOrder" + reqJson.getString("oId"));
        if (StringUtil.isEmpty(paramIn)) {
            throw new CmdException("已经处理过了 再不处理");
        }
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        paramObj.putAll(reqJson);
        modifyCouponUser(paramObj);
        TempCarPayOrderDto tempCarPayOrderDto = BeanConvertUtil.covertBean(paramObj, TempCarPayOrderDto.class);
        ResponseEntity<String> responseEntity = getTempCarFeeRulesImpl.notifyTempCarFeeOrder(tempCarPayOrderDto);
        return responseEntity;
    }

    private double checkCouponUser(JSONObject paramObj) {

        BigDecimal couponPrice = new BigDecimal(0.0);
        String couponIds = paramObj.getString("couponIds");
        if (couponIds == null || "".equals(couponIds)) {
            paramObj.put("couponPrice", couponPrice.doubleValue());
            paramObj.put("couponUserDtos", new JSONArray()); //这里考虑空
            return couponPrice.doubleValue();
        }

        List<String> result = Arrays.asList(couponIds.split(","));
        CouponUserDto couponUserDto = new CouponUserDto();
        couponUserDto.setCouponIds(result.toArray(new String[result.size()]));
        List<CouponUserDto> couponUserDtos = couponUserV1InnerServiceSMOImpl.queryCouponUsers(couponUserDto);
        if (couponUserDtos == null || couponUserDtos.size() < 1) {
            paramObj.put("couponPrice", couponPrice.doubleValue());
            return couponPrice.doubleValue();
        }
        for (CouponUserDto couponUser : couponUserDtos) {
            //不计算已过期购物券金额
            if (couponUser.getEndTime().compareTo(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)) >= 0) {
                couponPrice = couponPrice.add(new BigDecimal(Double.parseDouble(couponUser.getActualPrice())));
            }
        }
        paramObj.put("couponPrice", couponPrice.doubleValue());
        paramObj.put("couponUserDtos", BeanConvertUtil.beanCovertJSONArray(couponUserDtos));
        return couponPrice.doubleValue();
    }

    private void modifyCouponUser(JSONObject paramObj) {
        if (!paramObj.containsKey("couponPrice") || paramObj.getDouble("couponPrice") <= 0) {
            return;
        }
        //FeeDto feeInfo = (FeeDto) paramObj.get("feeInfo");
        CouponUserDto couponUserDto = null;
        JSONArray couponUserDtos = paramObj.getJSONArray("couponUserDtos");
        CouponUserDto couponUser = null;
        for (int accountIndex = 0; accountIndex < couponUserDtos.size(); accountIndex++) {
            couponUser = BeanConvertUtil.covertBean(couponUserDtos.getJSONObject(accountIndex), CouponUserDto.class);
            couponUserDto = new CouponUserDto();
            couponUserDto.setCouponId(couponUser.getCouponId());
            couponUserDto.setState(CouponUserDto.COUPON_STATE_RUN);
            List<CouponUserDto> couponUserDtos1 = couponUserV1InnerServiceSMOImpl.queryCouponUsers(couponUserDto);
            if (couponUserDtos1 == null || couponUserDtos1.size() < 1) {
                throw new CmdException("优惠券被使用");
            }
            CouponUserPo couponUserPo = new CouponUserPo();
            couponUserPo.setState(CouponUserDto.COUPON_STATE_STOP);
            couponUserPo.setCouponId(couponUser.getCouponId());
            int fage = couponUserV1InnerServiceSMOImpl.updateCouponUser(couponUserPo);
            if (fage < 1) {
                throw new CmdException("更新优惠卷信息失败");
            }
            CouponUserDetailPo couponUserDetailPo = new CouponUserDetailPo();
            couponUserDetailPo.setUoId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
            couponUserDetailPo.setCouponId(couponUser.getCouponId());
            couponUserDetailPo.setUserId(couponUser.getUserId());
            couponUserDetailPo.setCouponName(couponUser.getCouponName());
            couponUserDetailPo.setUserName(couponUser.getUserName());
            couponUserDetailPo.setObjId(paramObj.getString("carNum"));
            couponUserDetailPo.setObjType("车辆");
            couponUserDetailPo.setOrderId(paramObj.getString("oId"));
            fage = couponUserDetailV1InnerServiceSMOImpl.saveCouponUserDetail(couponUserDetailPo);
            if (fage < 1) {
                throw new CmdException("新增优惠卷使用记录信息失败");
            }
        }

        paramObj.put("remark", paramObj.getString("remark") + "-优惠劵抵扣" + paramObj.getDouble("couponPrice") + "元");

    }
}
