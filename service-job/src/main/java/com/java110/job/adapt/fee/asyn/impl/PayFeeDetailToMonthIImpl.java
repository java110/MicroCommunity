package com.java110.job.adapt.fee.asyn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.entity.order.Business;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.intf.fee.IPayFeeMonthInnerServiceSMO;
import com.java110.intf.report.IGeneratorOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.job.adapt.fee.asyn.IPayFeeDetailToMonth;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 主要用于处理费用离散月 适配器
 * add by wuxw 2023-05-11
 */
@Service
public class PayFeeDetailToMonthIImpl implements IPayFeeDetailToMonth {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IGeneratorOweFeeInnerServiceSMO generatorOweFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    @Override
    @Async
    public void doPayFeeDetail(Business business, JSONObject businessPayFeeDetail) {
        //查询缴费明细
        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessPayFeeDetail, PayFeeDetailPo.class);
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(payFeeDetailPo.getFeeId());
        feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        Assert.listOnlyOne(feeDtos, "未查询到费用信息");
        feeDto = feeDtos.get(0);

        Date endTime = DateUtil.getDateFromStringB(businessPayFeeDetail.getString("endTime"));

        //todo 转换为月（优化版）
        toPayFeeDetailMonth(businessPayFeeDetail, feeDto);

        // 转换为月
        //toMonth(businessPayFeeDetail, feeDto, startTime, endTime, createTime);

        //todo 如果是租金 则延长房屋租期
        toAddRoomRentTime(feeDtos.get(0), endTime);

        //todo 修改欠费
        toDeleteOweFee(feeDtos.get(0));

    }

    /**
     * 调用fee 模块 处理 离散
     * @param businessPayFeeDetail
     * @param feeDto
     */
    private void toPayFeeDetailMonth(JSONObject businessPayFeeDetail, FeeDto feeDto) {
        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(feeDto.getCommunityId());
        payFeeDetailRefreshFeeMonthDto.setDetailId(businessPayFeeDetail.getString("detailId"));
        payFeeDetailRefreshFeeMonthDto.setFeeId(feeDto.getFeeId());
        //todo 调用费用模块 处理
        payFeeMonthInnerServiceSMOImpl.payFeeDetailRefreshFeeMonth(payFeeDetailRefreshFeeMonthDto);
    }

    /**
     * 处理欠费
     *
     * @param feeDto
     */
    private void toDeleteOweFee(FeeDto feeDto) {
        generatorOweFeeInnerServiceSMOImpl.computeOweFee(feeDto);
    }

    private void toAddRoomRentTime(FeeDto feeDto, Date endTime) {

        //todo 不是租金直接返回
        if (!FeeConfigDto.FEE_TYPE_CD_RENT.equals(feeDto.getFeeTypeCd())) {
            return;
        }
        //todo 不是房屋直接返回
        if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {
            return;
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(feeDto.getPayerObjId());
        ownerRoomRelDto.setCommunityId(feeDto.getCommunityId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return;
        }

        Date rentEndDate = ownerRoomRelDtos.get(0).getEndTime();

        if (endTime.getTime() < rentEndDate.getTime()) {
            return;
        }

        OwnerRoomRelPo ownerRoomRelPo = new OwnerRoomRelPo();
        ownerRoomRelPo.setEndTime(DateUtil.getFormatTimeString(endTime, DateUtil.DATE_FORMATE_STRING_B));
        ownerRoomRelPo.setRelId(ownerRoomRelDtos.get(0).getRelId());

        ownerRoomRelV1InnerServiceSMOImpl.updateOwnerRoomRel(ownerRoomRelPo);
    }

    private void toMonth(JSONObject businessPayFeeDetail, FeeDto feeDto, Date startTime, Date endTime, Date createTime) {
        double maxMonth = 1;
        if (!FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            maxMonth = Math.ceil(computeFeeSMOImpl.dayCompare(startTime, endTime));
        }

        if (maxMonth < 1) {
            return;
        }

        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);

        Double feePrice = Double.parseDouble(feePriceAll.get("feePrice").toString());

        BigDecimal totalRecDec = new BigDecimal(businessPayFeeDetail.getDouble("receivedAmount"));
        //每月平均值
        BigDecimal priRecDec = totalRecDec.divide(new BigDecimal(maxMonth), 2, BigDecimal.ROUND_HALF_EVEN);

        List<PayFeeDetailMonthPo> payFeeDetailMonthPos = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        commonPropertyCode(businessPayFeeDetail, feeDto, startTime, createTime, maxMonth, feePrice, priRecDec, payFeeDetailMonthPos, calendar);

        payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonths(payFeeDetailMonthPos);
    }

    private void commonPropertyCode(JSONObject businessPayFeeDetail, FeeDto feeDto, Date startTime, Date createTime, double maxMonth, Double feePrice, BigDecimal priRecDec, List<PayFeeDetailMonthPo> payFeeDetailMonthPos, Calendar calendar) {
        BigDecimal discountAmount;
        PayFeeDetailMonthPo tmpPayFeeDetailMonthPo;


        for (int month = 0; month < maxMonth; month++) {
            calendar.setTime(startTime);
            calendar.add(Calendar.MONTH, month);
            discountAmount = new BigDecimal(feePrice).subtract(priRecDec).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            tmpPayFeeDetailMonthPo = new PayFeeDetailMonthPo();
            tmpPayFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
            tmpPayFeeDetailMonthPo.setCommunityId(feeDto.getCommunityId());
            tmpPayFeeDetailMonthPo.setDetailId(businessPayFeeDetail.getString("detailId"));
            tmpPayFeeDetailMonthPo.setDetailMonth((calendar.get(Calendar.MONTH) + 1) + "");
            tmpPayFeeDetailMonthPo.setDetailYear(calendar.get(Calendar.YEAR) + "");
            tmpPayFeeDetailMonthPo.setDiscountAmount(discountAmount.doubleValue() + "");
            tmpPayFeeDetailMonthPo.setMonthId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_monthId));
            tmpPayFeeDetailMonthPo.setReceivableAmount(feePrice + "");
            tmpPayFeeDetailMonthPo.setReceivedAmount(priRecDec.doubleValue() + "");
            tmpPayFeeDetailMonthPo.setRemark("程序计算生成");
            payFeeDetailMonthPos.add(tmpPayFeeDetailMonthPo);
        }
    }

    private void bailefuPropertyCode(JSONObject businessPayFeeDetail, FeeDto feeDto, Date startTime, Date createTime, double maxMonth, Double feePrice, BigDecimal priRecDec, List<PayFeeDetailMonthPo> payFeeDetailMonthPos, Calendar calendar) {
        BigDecimal discountAmount;
        PayFeeDetailMonthPo tmpPayFeeDetailMonthPo;
        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.setTime(startTime);
        startTimeCal.set(Calendar.DAY_OF_MONTH, 1);
        startTime = startTimeCal.getTime();

        Calendar createTimeCal = Calendar.getInstance();
        createTimeCal.setTime(createTime);
        createTimeCal.set(Calendar.DAY_OF_MONTH, 1);
        createTime = createTimeCal.getTime();

        BigDecimal oweFee = new BigDecimal(0);

        for (int month = 0; month < maxMonth; month++) {
            calendar.setTime(startTime);
            calendar.add(Calendar.MONTH, month);
            discountAmount = new BigDecimal(feePrice).subtract(priRecDec).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            tmpPayFeeDetailMonthPo = new PayFeeDetailMonthPo();
            tmpPayFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
            tmpPayFeeDetailMonthPo.setCommunityId(feeDto.getCommunityId());
            tmpPayFeeDetailMonthPo.setDetailId(businessPayFeeDetail.getString("detailId"));
            tmpPayFeeDetailMonthPo.setDetailMonth((calendar.get(Calendar.MONTH) + 1) + "");
            tmpPayFeeDetailMonthPo.setDetailYear(calendar.get(Calendar.YEAR) + "");
            tmpPayFeeDetailMonthPo.setDiscountAmount(discountAmount.doubleValue() + "");
            tmpPayFeeDetailMonthPo.setMonthId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_monthId));
            tmpPayFeeDetailMonthPo.setReceivableAmount(feePrice + "");
            if (calendar.getTime().getTime() < createTime.getTime()) {
                tmpPayFeeDetailMonthPo.setReceivedAmount("0");
                oweFee = oweFee.add(priRecDec);
            } else if (calendar.getTime().getTime() == createTime.getTime()) {
                oweFee = oweFee.add(priRecDec);
                tmpPayFeeDetailMonthPo.setReceivedAmount(oweFee.doubleValue() + "");
            } else {
                tmpPayFeeDetailMonthPo.setReceivedAmount(priRecDec.doubleValue() + "");
            }
            tmpPayFeeDetailMonthPo.setRemark("程序计算生成");
            payFeeDetailMonthPos.add(tmpPayFeeDetailMonthPo);
        }
    }

}
