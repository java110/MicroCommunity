package com.java110.fee.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parkingCoupon.ParkingCouponCarDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.fee.bmo.tempCarFee.IGetTempCarFeeRules;
import com.java110.intf.acct.ICouponUserDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.intf.acct.IParkingCouponCarOrderV1InnerServiceSMO;
import com.java110.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeCreateOrderV1InnerServiceSMO;
import com.java110.po.parkingCouponCar.ParkingCouponCarPo;
import com.java110.po.parkingCouponCarOrder.ParkingCouponCarOrderPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class TempCarFeeCreateOrderV1InnerServiceSMOImpl  extends BaseServiceSMO implements ITempCarFeeCreateOrderV1InnerServiceSMO {

    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;
    @Autowired
    private IGetTempCarFeeRules getTempCarFeeRulesImpl;

    @Autowired
    private ICouponUserDetailV1InnerServiceSMO couponUserDetailV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponCarOrderV1InnerServiceSMO parkingCouponCarOrderV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;


    @Override
    public ResponseEntity<String> createOrder(@RequestBody JSONObject reqJson) {

        TempCarPayOrderDto tempCarPayOrderDto = new TempCarPayOrderDto();
        tempCarPayOrderDto.setPaId(reqJson.getString("paId"));
        tempCarPayOrderDto.setCarNum(reqJson.getString("carNum"));
        if(reqJson.containsKey("couponIds")&& !StringUtil.isEmpty(reqJson.getString("couponIds"))) {
            tempCarPayOrderDto.setPccIds(reqJson.getString("couponIds").split(","));
        }
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
        BigDecimal money = new BigDecimal(fee.getDouble("amount"));
        //3.0 考虑优惠卷 这里不考虑了 因为 物联网平台已经 考虑过了

        // double couponPrice = checkCouponUser(reqJson);
        //money = money.subtract(new BigDecimal(couponPrice)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

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

        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(paramObj.getString("paId"));
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        if(parkingAreaDtos == null  || parkingAreaDtos.size()<1){
            paramObj.put("communityId","-1");
        }else{
            paramObj.put("communityId",parkingAreaDtos.get(0).getCommunityId());
        }
        paramObj.putAll(reqJson);
        TempCarPayOrderDto tempCarPayOrderDto = BeanConvertUtil.covertBean(paramObj, TempCarPayOrderDto.class);
        dealParkingCouponCar(paramObj,tempCarPayOrderDto);
        ResponseEntity<String> responseEntity = getTempCarFeeRulesImpl.notifyTempCarFeeOrder(tempCarPayOrderDto);
        return responseEntity;
    }



    private void dealParkingCouponCar(JSONObject reqJson,TempCarPayOrderDto tempCarPayOrderDto) {
        //处理停车劵

        if(!reqJson.containsKey("couponIds") || StringUtil.isEmpty(reqJson.getString("couponIds"))) {
            return ;
        }

        String[] pccIds = reqJson.getString("couponIds").split(",");
        ParkingCouponCarPo parkingCouponCarPo = null;
        ParkingCouponCarOrderPo parkingCouponCarOrderPo = null;
        for(String pccId: pccIds){
            parkingCouponCarPo = new ParkingCouponCarPo();
            parkingCouponCarPo.setPccId(pccId);
            parkingCouponCarPo.setState(ParkingCouponCarDto.STATE_FINISH);
            parkingCouponCarV1InnerServiceSMOImpl.updateParkingCouponCar(parkingCouponCarPo);

            parkingCouponCarOrderPo = new ParkingCouponCarOrderPo();
            parkingCouponCarOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId("11"));
            parkingCouponCarOrderPo.setCarNum(reqJson.getString("carNum"));
            parkingCouponCarOrderPo.setCarOutId("-1");
            parkingCouponCarOrderPo.setCommunityId(reqJson.getString("communityId"));
            parkingCouponCarOrderPo.setMachineId("-1");
            parkingCouponCarOrderPo.setMachineName("未知");
            parkingCouponCarOrderPo.setPaId(reqJson.getString("paId"));
            parkingCouponCarOrderPo.setPccId(pccId);
            parkingCouponCarOrderPo.setRemark("手机端支付停车劵抵扣");

            parkingCouponCarOrderV1InnerServiceSMOImpl.saveParkingCouponCarOrder(parkingCouponCarOrderPo);
        }

        tempCarPayOrderDto.setPccIds(pccIds);
    }
}
