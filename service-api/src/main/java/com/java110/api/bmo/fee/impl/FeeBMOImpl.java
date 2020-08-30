package com.java110.api.bmo.fee.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.common.ICarInoutInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.po.car.CarInoutPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FeeBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:24
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("feeBMOImpl")
public class FeeBMOImpl extends ApiBaseBMO implements IFeeBMO {


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(paramInJson, PayFeeConfigPo.class);
        super.delete(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_CONFIG);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", paramInJson.getString("feeId"));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
        super.delete(dataFlowContext, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO);
    }

    public void updateFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        PayFeePo payFeePo = BeanConvertUtil.covertBean(paramInJson, PayFeePo.class);

        super.update(dataFlowContext, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_INFO);
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", "-1");
        businessFeeDetail.put("primeRate", "1.00");
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }


        feeDto = feeDtos.get(0);
        businessFeeDetail.put("startTime", DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        Date endTime = feeDto.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        int hours = 0;
        if ("-101".equals(paramInJson.getString("cycles"))) {
            hours = new Double(Double.parseDouble(paramInJson.getString("tmpCycles")) * DateUtil.getCurrentMonthDay() * 24).intValue();
            endCalender.add(Calendar.HOUR, hours);
        } else {
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        }
        businessFeeDetail.put("endTime", DateUtil.getFormatTimeString(endCalender.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        paramInJson.put("feeInfo", feeDto);

        BigDecimal feePrice = new BigDecimal("0.00");

        if ("3333".equals(feeDto.getPayerObjType())) { //房屋相关
            String computingFormula = feeDto.getComputingFormula();
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                RoomDto roomDto = new RoomDto();
                roomDto.setRoomId(feeDto.getPayerObjId());
                roomDto.setCommunityId(feeDto.getCommunityId());
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                if (roomDtos == null || roomDtos.size() != 1) {
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到房屋信息，查询多条数据");
                }
                roomDto = roomDtos.get(0);
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDtos.get(0).getBuiltUpArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        } else if ("6666".equals(feeDto.getPayerObjType())) {//车位相关
            String computingFormula = feeDto.getComputingFormula();
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
                parkingSpaceDto.setPsId(feeDto.getPayerObjId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

                if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到停车位信息，查询多条数据");
                }
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(parkingSpaceDtos.get(0).getArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(parkingSpaceDtos.get(0).getArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        }
        BigDecimal cycles = null;
        //BigDecimal receivableAmount = feePrice;
        if ("-101".equals(paramInJson.getString("cycles"))) {//自定义缴费
            BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            cycles = receivedAmount.divide(feePrice, 2, BigDecimal.ROUND_HALF_EVEN);
            paramInJson.put("tmpCycles", cycles);
            businessFeeDetail.put("cycles", cycles.doubleValue());
            businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
        } else {
            cycles = new BigDecimal(Double.parseDouble(paramInJson.getString("cycles")));
            double tmpReceivableAmount = cycles.multiply(feePrice).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            businessFeeDetail.put("receivableAmount", tmpReceivableAmount);
        }

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailPo.class.getSimpleName(), businessFeeDetail);

        return business;
    }


    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Date endTime = feeInfo.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        int hours = 0;
        if ("-101".equals(paramInJson.getString("cycles"))) {
            hours = new Double(Double.parseDouble(paramInJson.getString("tmpCycles")) * DateUtil.getCurrentMonthDay() * 24).intValue();
            endCalender.add(Calendar.HOUR, hours);
        } else {
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        }
        feeInfo.setEndTime(endCalender.getTime());
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(feeInfo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("cycles", paramInJson.getString("cycles"));
        feeMap.put("configEndTime", feeInfo.getConfigEndTime());

        businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessFee);

        //为停车费单独处理
        paramInJson.put("carFeeEndTime",feeInfo.getEndTime());
        paramInJson.put("carPayerObjType",feeInfo.getPayerObjType());
        paramInJson.put("carPayerObjId",feeInfo.getPayerObjId());

        return business;
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeePreDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", "-1");
        businessFeeDetail.put("primeRate", "1.00");
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }

        feeDto = feeDtos.get(0);
        paramInJson.put("feeInfo", feeDto);
        businessFeeDetail.put("startTime", DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        Date endTime = feeDto.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        int hours = 0;
        if ("-101".equals(paramInJson.getString("cycles"))) {
            hours = new Double(Double.parseDouble(paramInJson.getString("tmpCycles")) * DateUtil.getCurrentMonthDay() * 24).intValue();
            endCalender.add(Calendar.HOUR, hours);
        } else {
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        }
        businessFeeDetail.put("endTime", DateUtil.getFormatTimeString(endCalender.getTime(), DateUtil.DATE_FORMATE_STRING_A));

        BigDecimal feePrice = new BigDecimal("0.00");

        if ("3333".equals(feeDto.getPayerObjType())) { //房屋相关
            String computingFormula = feeDto.getComputingFormula();
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                RoomDto roomDto = new RoomDto();
                roomDto.setRoomId(feeDto.getPayerObjId());
                roomDto.setCommunityId(feeDto.getCommunityId());
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                if (roomDtos == null || roomDtos.size() != 1) {
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到房屋信息，查询多条数据");
                }
                roomDto = roomDtos.get(0);
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDtos.get(0).getBuiltUpArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        } else if ("6666".equals(feeDto.getPayerObjType())) {//车位相关
            String computingFormula = feeDto.getComputingFormula();
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
                parkingSpaceDto.setPsId(feeDto.getPayerObjId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

                if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到停车位信息，查询多条数据");
                }
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(parkingSpaceDtos.get(0).getArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(parkingSpaceDtos.get(0).getArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        }

        BigDecimal receivableAmount = feePrice;
        BigDecimal cycles = new BigDecimal(Double.parseDouble(paramInJson.getString("cycles")));
        double tmpReceivableAmount = cycles.multiply(receivableAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        businessFeeDetail.put("receivableAmount", tmpReceivableAmount);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailPo.class.getSimpleName(), businessFeeDetail);
        paramInJson.put("receivableAmount", tmpReceivableAmount);
        return business;
    }


    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyPreFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Date endTime = feeInfo.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        feeInfo.setEndTime(endCalender.getTime());
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(feeInfo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessFee);

        return business;
    }

    public JSONObject modifyTempCarInout(JSONObject reqJson, DataFlowContext context) {

        FeeDto feeDto = (FeeDto) reqJson.get("feeInfo");
        CarInoutDto tempCarInoutDto = new CarInoutDto();
        tempCarInoutDto.setCommunityId(reqJson.getString("communityId"));
        tempCarInoutDto.setInoutId(feeDto.getPayerObjId());
        List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(tempCarInoutDto);

        Assert.listOnlyOne(carInoutDtos, "根据费用信息反差车辆进场记录未查到 或查到多条");

        CarInoutDto carInoutDto = carInoutDtos.get(0);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_INOUT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCarInout = new JSONObject();
        businessCarInout.putAll(BeanConvertUtil.beanCovertMap(carInoutDto));
        businessCarInout.put("state", "100400");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(CarInoutPo.class.getSimpleName(), businessCarInout);
        return business;
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeTempDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", "-1");
        businessFeeDetail.put("primeRate", "1.00");
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }
        feeDto = feeDtos.get(0);
        paramInJson.put("feeInfo", feeDto);
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeDto.getFeeTypeCd());
        feeConfigDto.setConfigId(feeDto.getConfigId());
        feeConfigDto.setCommunityId(feeDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }
        feeConfigDto = feeConfigDtos.get(0);
        Date nowTime = new Date();

        long diff = nowTime.getTime() - feeDto.getStartTime().getTime();
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        double day = 0;
        double hour = 0;
        double min = 0;
        day = diff / nd;// 计算差多少天
        hour = diff % nd / nh + day * 24;// 计算差多少小时
        min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
        double money = 0.00;
        double newHour = hour;
        if (min > 0) { //一小时超过
            newHour += 1;
        }
        if (newHour <= 2) {
            money = Double.parseDouble(feeConfigDto.getAdditionalAmount());
        } else {
            BigDecimal lastHour = new BigDecimal(newHour - 2);
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            money = squarePrice.multiply(lastHour).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }

        double receivableAmount = money;

        businessFeeDetail.put("receivableAmount", receivableAmount);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailPo.class.getSimpleName(), businessFeeDetail);
        paramInJson.put("receivableAmount", receivableAmount);
        return business;
    }


    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyTempFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("amount", paramInJson.getString("receivableAmount"));
        feeMap.put("state", "2009001");
        businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessFee);

        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        paramInJson.put("configId", "-1");
        paramInJson.put("isDefault", "F");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(paramInJson, PayFeeConfigPo.class);
        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFee(OwnerCarDto ownerCarDto, JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", "-1");
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", ownerCarDto.getCarId());
        businessUnit.put("payerObjType", FeeDto.PAYER_OBJ_TYPE_CAR);
        businessUnit.put("feeFlag", paramInJson.getString("feeFlag"));
        businessUnit.put("state", "2008001");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessUnit);

        return business;
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addRoomFee(RoomDto roomDto, JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", "-1");
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("endTime", paramInJson.containsKey("feeEndDate") ? paramInJson.getString("feeEndDate")
                : DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", roomDto.getRoomId());
        businessUnit.put("payerObjType", "3333");
        businessUnit.put("feeFlag", paramInJson.getString("feeFlag"));
        businessUnit.put("state", "2008001");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessUnit);

        return business;
    }

    /**
     * 添加费用项信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(paramInJson.getString("communityId"));
        feeConfigDto.setConfigId(paramInJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "未找到该费用项");

        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("isDefault", feeConfigDtos.get(0).getIsDefault());
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);

        super.update(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_CONFIG);
    }

}
