package com.java110.api.bmo.fee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.ICarInoutInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.po.car.CarInoutPo;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
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


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

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

    private static Calendar getTargetEndTime(Calendar endCalender, Double cycles) {
        if (StringUtil.isInteger(cycles.toString())) {
            endCalender.add(Calendar.MONTH, new Double(cycles).intValue());
            return endCalender;
        }
        if (cycles >= 1) {
            endCalender.add(Calendar.MONTH, new Double(Math.floor(cycles)).intValue());
            cycles = cycles - Math.floor(cycles);
        }
        int futureDay = endCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        int hours = new Double(cycles * futureDay * 24).intValue();
        endCalender.add(Calendar.HOUR, hours);
        return endCalender;
    }

    /**
     * 添加费用明细信息
     *
     * @param payFeeDetailPo  费用明细
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addSimpleFeeDetail(PayFeeDetailPo payFeeDetailPo, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = JSONObject.parseObject(JSONObject.toJSONString(payFeeDetailPo));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailPo.class.getSimpleName(), businessFeeDetail);
        return business;
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext, FeeReceiptDetailPo feeReceiptDetailPo, FeeReceiptPo feeReceiptPo) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        //支付方式
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }
        if (!businessFeeDetail.containsKey("state") || StringUtil.isEmpty(businessFeeDetail.getString("state"))) {
            businessFeeDetail.put("state", "1400");
        }
        feeDto = feeDtos.get(0);
        businessFeeDetail.put("startTime", DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        int hours = 0;
        Date targetEndTime = null;
        BigDecimal cycles = null;
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        BigDecimal feePrice = new BigDecimal(feePriceAll.get("feePrice").toString());
        if ("-101".equals(paramInJson.getString("cycles"))) {
            Date endTime = feeDto.getEndTime();
            Calendar endCalender = Calendar.getInstance();
            endCalender.setTime(endTime);
            BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            cycles = receivedAmount.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
            endCalender = getTargetEndTime(endCalender, cycles.doubleValue());
            targetEndTime = endCalender.getTime();
            paramInJson.put("tmpCycles", cycles.doubleValue());
            businessFeeDetail.put("cycles", cycles.doubleValue());
            businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
        } else {
            targetEndTime = computeFeeSMOImpl.getFeeEndTimeByCycles(feeDto, paramInJson.getString("cycles"));
            cycles = new BigDecimal(Double.parseDouble(paramInJson.getString("cycles")));
            double tmpReceivableAmount = cycles.multiply(feePrice).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            businessFeeDetail.put("receivableAmount", tmpReceivableAmount);
        }
        businessFeeDetail.put("endTime", DateUtil.getFormatTimeString(targetEndTime, DateUtil.DATE_FORMATE_STRING_A));
        paramInJson.put("feeInfo", feeDto);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailPo.class.getSimpleName(), businessFeeDetail);
        paramInJson.put("detailId", businessFeeDetail.getString("detailId"));

        return business;
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOweFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext,
                                      List<FeeReceiptDetailPo> feeReceiptDetailPos,
                                      List<FeeReceiptPo> feeReceiptPos) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }
        if (!businessFeeDetail.containsKey("state") || StringUtil.isEmpty(businessFeeDetail.getString("state"))) {
            businessFeeDetail.put("state", "1400");
        }
        feeDto = feeDtos.get(0);
        businessFeeDetail.put("startTime", paramInJson.getString("startTime"));
        BigDecimal cycles = null;
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        BigDecimal feePrice = new BigDecimal(feePriceAll.get("feePrice").toString());
        Date endTime = feeDto.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
        cycles = receivedAmount.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
        businessFeeDetail.put("cycles", cycles.doubleValue());
        businessFeeDetail.put("receivableAmount", paramInJson.getString("receivedAmount"));
        businessFeeDetail.put("receivedAmount", paramInJson.getString("receivedAmount"));
        businessFeeDetail.put("endTime", paramInJson.getString("endTime"));
        paramInJson.put("feeInfo", feeDto);
        paramInJson.put("cycles", cycles.doubleValue());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailPo.class.getSimpleName(), businessFeeDetail);
        paramInJson.put("detailId", businessFeeDetail.getString("detailId"));
        FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
        FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
        feeReceiptDetailPo.setAmount(businessFeeDetail.getString("receivedAmount"));
        feeReceiptDetailPo.setCommunityId(feeDto.getCommunityId());
        feeReceiptDetailPo.setCycle(businessFeeDetail.getString("cycles"));
        feeReceiptDetailPo.setDetailId(businessFeeDetail.getString("detailId"));
        feeReceiptDetailPo.setEndTime(businessFeeDetail.getString("endTime"));
        feeReceiptDetailPo.setFeeId(feeDto.getFeeId());
        feeReceiptDetailPo.setFeeName(StringUtil.isEmpty(feeDto.getImportFeeName()) ? feeDto.getFeeName() : feeDto.getImportFeeName());
        feeReceiptDetailPo.setStartTime(businessFeeDetail.getString("startTime"));
        feeReceiptDetailPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));

        computeFeeSMOImpl.freshFeeReceiptDetail(feeDto, feeReceiptDetailPo);
        //查询业主信息
        OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);

        feeReceiptDetailPos.add(feeReceiptDetailPo);
        feeReceiptPo.setAmount(feeReceiptDetailPo.getAmount());
        feeReceiptPo.setCommunityId(feeReceiptDetailPo.getCommunityId());
        feeReceiptPo.setReceiptId(feeReceiptDetailPo.getReceiptId());
        feeReceiptPo.setObjType(feeDto.getPayerObjType());
        feeReceiptPo.setObjId(feeDto.getPayerObjId());
        feeReceiptPo.setObjName(computeFeeSMOImpl.getFeeObjName(feeDto));
        feeReceiptPo.setPayObjId(ownerDto.getOwnerId());
        feeReceiptPo.setPayObjName(ownerDto.getName());
        feeReceiptPos.add(feeReceiptPo);
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
            endCalender = getTargetEndTime(endCalender, Double.parseDouble(paramInJson.getString("tmpCycles")));
        } else {
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        }
        feeInfo.setEndTime(endCalender.getTime());
        //判断 结束时间 是否大于 费用项 结束时间，这里 容错一下，如果 费用结束时间大于 费用项结束时间 30天 走报错 属于多缴费
        if (feeInfo.getEndTime().getTime() - feeInfo.getConfigEndTime().getTime() > 30 * 24 * 60 * 60 * 1000L) {
            throw new IllegalArgumentException("缴费超过了 费用项结束时间");
        }
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(feeInfo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("cycles", paramInJson.getString("cycles"));
        feeMap.put("configEndTime", feeInfo.getConfigEndTime());
        businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessFee);
        //为停车费单独处理
        paramInJson.put("carFeeEndTime", feeInfo.getEndTime());
        paramInJson.put("carPayerObjType", feeInfo.getPayerObjType());
        paramInJson.put("carPayerObjId", feeInfo.getPayerObjId());
        return business;
    }

    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyOweFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_ONLY_UPDATE_FEE_INFO); //这里走only
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", paramInJson.getString("endTime"));
        feeMap.put("cycles", paramInJson.getString("cycles"));
        feeMap.put("configEndTime", feeInfo.getConfigEndTime());
        if (FeeDto.FEE_FLAG_ONCE.equals(feeInfo.getFeeFlag())) { //缴费结束
            feeMap.put("state", FeeDto.STATE_FINISH);
        }
        Date maxEndTime = feeInfo.getConfigEndTime();
        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeInfo.getFeeFlag())) {
            maxEndTime = feeInfo.getDeadlineTime();
        }
        if(maxEndTime != null) { //这里数据问题的情况下
            Date endTime = DateUtil.getDateFromStringA(paramInJson.getString("endTime"));
            if (endTime.getTime() >= maxEndTime.getTime()) {
                feeMap.put("state", FeeDto.STATE_FINISH);
            }
        }

        businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessFee);
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
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        if (!businessFeeDetail.containsKey("state")) {
            businessFeeDetail.put("state", "1400");
        }
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
//            hours = new Double(Double.parseDouble(paramInJson.getString("tmpCycles")) * DateUtil.getCurrentMonthDay() * 24).intValue();
//            endCalender.add(Calendar.HOUR, hours);
            endCalender = getTargetEndTime(endCalender, Double.parseDouble(paramInJson.getString("tmpCycles")));
        } else {
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
            if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
                if (feeDto.getDeadlineTime() != null) {
                    endCalender.setTime(feeDto.getDeadlineTime());
                } else if (!StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    endCalender.setTime(feeDto.getCurReadingTime());
                } else if (feeDto.getImportFeeEndTime() == null) {
                    endCalender.setTime(feeDto.getConfigEndTime());
                } else {
                    endCalender.setTime(feeDto.getImportFeeEndTime());
                }
            }
        }
        businessFeeDetail.put("endTime", DateUtil.getFormatTimeString(endCalender.getTime(), DateUtil.DATE_FORMATE_STRING_A));

        double feePrice = 0.0;

        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);

        BigDecimal receivableAmount = new BigDecimal(feePriceAll.get("feePrice").toString());
        BigDecimal cycles = new BigDecimal(Double.parseDouble(paramInJson.getString("cycles")));
        double tmpReceivableAmount = cycles.multiply(receivableAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        double discountPrice = paramInJson.getDouble("discountPrice");
        businessFeeDetail.put("receivableAmount", tmpReceivableAmount);
        businessFeeDetail.put("receivedAmount",
                new BigDecimal(tmpReceivableAmount).subtract(new BigDecimal(discountPrice)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailPo.class.getSimpleName(), businessFeeDetail);
        paramInJson.put("receivableAmount", tmpReceivableAmount);
        paramInJson.put("receivedAmount", businessFeeDetail.getString("receivedAmount"));
        paramInJson.put("detailId", businessFeeDetail.getString("detailId"));
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
        //判断 结束时间 是否大于 费用项 结束时间，这里 容错一下，如果 费用结束时间大于 费用项结束时间 30天 走报错 属于多缴费
        if (feeInfo.getEndTime().getTime() - feeInfo.getConfigEndTime().getTime() > 30 * 24 * 60 * 60 * 1000L) {
            throw new IllegalArgumentException("缴费超过了 费用项结束时间" + JSONObject.toJSONString(feeInfo) + "|||" + paramInJson.getString("cycles"));
        }
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(feeInfo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessFee);

        //为停车费单独处理
        paramInJson.put("carFeeEndTime", feeInfo.getEndTime());
        paramInJson.put("carPayerObjType", feeInfo.getPayerObjType());
        paramInJson.put("carPayerObjId", feeInfo.getPayerObjId());
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
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        if (!businessFeeDetail.containsKey("state")) {
            businessFeeDetail.put("state", "1400");
        }
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
        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", time);
        businessUnit.put("endTime", time);
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", ownerCarDto.getCarId());
        businessUnit.put("payerObjType", FeeDto.PAYER_OBJ_TYPE_CAR);
        businessUnit.put("feeFlag", paramInJson.getString("feeFlag"));
        businessUnit.put("state", "2008001");
        businessUnit.put("batchId", paramInJson.getString("batchId"));
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessUnit);
        paramInJson.put("feeId", businessUnit.getString("feeId"));
        return business;
    }

    @Override
    public JSONObject addFeeAttr(JSONObject paramInJson, DataFlowContext dataFlowContext, String specCd, String value) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(paramInJson.getString("communityId"));
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(paramInJson.getString("feeId"));
        feeAttrPo.setAttrId("-1");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(FeeAttrPo.class.getSimpleName(), JSONObject.parseObject(JSONObject.toJSONString(feeAttrPo)));
        return business;

    }

    /**
     * 添加费用
     *
     * @param payFeePo        接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addSimpleFee(PayFeePo payFeePo, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = null;
        businessUnit = JSONObject.parseObject(JSONObject.toJSONString(payFeePo));
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
    public JSONObject addRoomFee(RoomDto roomDto, JSONObject paramInJson, DataFlowContext dataFlowContext) throws ParseException {
        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("feeEndDate")) {
            time = paramInJson.getString("feeEndDate");
        } else if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }
        //获取费用标识
        String feeFlag = paramInJson.getString("feeFlag");
        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(paramInJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        if (paramInJson.containsKey("amount") && !StringUtil.isEmpty(paramInJson.getString("amount"))) {
            businessUnit.put("amount", paramInJson.getString("amount"));
        }
        businessUnit.put("startTime", time);
        businessUnit.put("endTime", time);
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", roomDto.getRoomId());
        businessUnit.put("payerObjType", "3333");
        businessUnit.put("feeFlag", paramInJson.getString("feeFlag"));
        businessUnit.put("state", "2008001");
        businessUnit.put("batchId", paramInJson.getString("batchId"));
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessUnit);
        paramInJson.put("feeId", businessUnit.getString("feeId"));
        return business;
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addContractFee(ContractDto contractDto, JSONObject paramInJson, DataFlowContext dataFlowContext) {
        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("feeEndDate")) {
            time = paramInJson.getString("feeEndDate");
        } else if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        if (paramInJson.containsKey("amount") && !StringUtil.isEmpty(paramInJson.getString("amount"))) {
            businessUnit.put("amount", paramInJson.getString("amount"));
        }
        businessUnit.put("startTime", time);
        businessUnit.put("endTime", time);
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", contractDto.getContractId());
        businessUnit.put("payerObjType", FeeDto.PAYER_OBJ_TYPE_CONTRACT);
        businessUnit.put("feeFlag", paramInJson.getString("feeFlag"));
        businessUnit.put("state", "2008001");
        businessUnit.put("batchId", paramInJson.getString("batchId"));
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeePo.class.getSimpleName(), businessUnit);
        paramInJson.put("feeId", businessUnit.getString("feeId"));
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
