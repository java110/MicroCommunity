package com.java110.fee.feeMonth;

import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PayFeeMonthHelp implements IPayFeeMonthHelp {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;


    public PayFeeMonthOwnerDto generatorOwnerRoom(FeeDto feeDto) {

        PayFeeMonthOwnerDto payFeeMonthOwnerDto = new PayFeeMonthOwnerDto();
        payFeeMonthOwnerDto.setOwnerId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
        payFeeMonthOwnerDto.setOwnerName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
        payFeeMonthOwnerDto.setLink(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
        payFeeMonthOwnerDto.setObjName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME));
        payFeeMonthOwnerDto.setObjId(feeDto.getPayerObjId());
        return payFeeMonthOwnerDto;
    }

    /**
     * 计算每月单价
     *
     * @param feeDto
     * @return
     */
    public Double getMonthFeePrice(FeeDto feeDto) {
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);

        Double feePrice = Double.parseDouble(feePriceAll.get("feePrice").toString());
        //todo 如果是一次性费用 除以
        if (!FeeDto.FEE_FLAG_ONCE.equals(feeDto.getPayerObjType())) {
            return feePrice;
        }
        double maxMonth = Math.ceil(computeFeeSMOImpl.dayCompare(feeDto.getStartTime(), feeDto.getEndTime()));
        if (maxMonth <= 0) {
            return feePrice;

        }
        BigDecimal feePriceDec = new BigDecimal(feePrice).divide(new BigDecimal(maxMonth), 2, BigDecimal.ROUND_HALF_UP);
        feePrice = feePriceDec.doubleValue();
        return feePrice;
    }


    public Double getReceivableAmount(List<FeeDetailDto> feeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto) {
        FeeDetailDto feeDetailDto = getCurFeeDetail(feeDetailDtos, curDate);

        if (feeDetailDto == null && curDate.getTime() < feeDto.getEndTime().getTime()) {
            return 0.00;
        }

        return feePrice;
    }

    @Override
    public Double getReceivedAmount(List<FeeDetailDto> feeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto) {
        //todo 这种情况下应该 实收为0
        if (curDate.getTime() >= feeDto.getEndTime().getTime()) {
            return 0.00;
        }
        //todo 如果 fee 为空
        if (feeDetailDtos == null) {
            return feePrice;
        }
        FeeDetailDto feeDetailDto = getCurFeeDetail(feeDetailDtos, curDate);

        if (feeDetailDto == null && curDate.getTime() < feeDto.getEndTime().getTime()) {
            return 0.00;
        }

        if (feeDetailDto == null) {
            return feePrice;
        }

        double maxMonth = Math.ceil(computeFeeSMOImpl.dayCompare(feeDetailDto.getStartTime(), feeDetailDto.getEndTime()));

        if (maxMonth < 1) {
            return Double.parseDouble(feeDetailDto.getReceivedAmount());
        }

        BigDecimal totalRecDec = new BigDecimal(feeDetailDto.getReceivedAmount());
        //每月平均值
        BigDecimal priRecDec = totalRecDec.divide(new BigDecimal(maxMonth), 4, BigDecimal.ROUND_HALF_UP);

        return priRecDec.doubleValue();
    }

    @Override
    public Double getDiscountAmount(Double feePrice, double receivedAmount, Date curDate, FeeDto feeDto) {

        //todo 这种情况下应该 优惠为0
        if (curDate.getTime() >= feeDto.getEndTime().getTime()) {
            return 0.00;
        }

        BigDecimal discountAmountDec = new BigDecimal(feePrice).subtract(new BigDecimal(receivedAmount)).setScale(4, BigDecimal.ROUND_HALF_UP);
        return discountAmountDec.doubleValue();
    }

    @Override
    public String getFeeDetailId(List<FeeDetailDto> feeDetailDtos, Date curDate) {
        FeeDetailDto feeDetailDto = getCurFeeDetail(feeDetailDtos, curDate);

        if (feeDetailDto == null) {
            return "-1";
        }
        return feeDetailDto.getDetailId();
    }

    @Override
    public String getFeeFeeTime(List<FeeDetailDto> feeDetailDtos, String detailId) {

        if (feeDetailDtos == null || feeDetailDtos.size() < 1) {
            return null;
        }
        for (FeeDetailDto feeDetailDto : feeDetailDtos) {
            if (feeDetailDto.getDetailId().equals(detailId)) {
                return DateUtil.getFormatTimeStringA(feeDetailDto.getCreateTime());
            }
        }
        return null;
    }

    /**
     * 获取当前缴费记录
     *
     * @param feeDetailDtos
     * @param curDate
     * @return
     */
    private FeeDetailDto getCurFeeDetail(List<FeeDetailDto> feeDetailDtos, Date curDate) {
        if (feeDetailDtos == null || feeDetailDtos.size() < 1) {
            return null;
        }
        List<FeeDetailDto> tFeeDetailDtos = new ArrayList<>();
        for (FeeDetailDto feeDetailDto : feeDetailDtos) {
            if (feeDetailDto.getStartTime().getTime() <= curDate.getTime() && feeDetailDto.getEndTime().getTime() > curDate.getTime()) {
                tFeeDetailDtos.add(feeDetailDto);
            }
        }

        if (tFeeDetailDtos.size() < 1) {
            return null;
        }
        if (tFeeDetailDtos.size() == 1) {
            return tFeeDetailDtos.get(0);
        }
        //todo 这种应该是数据异常 也就是一个月费用变更后重复缴费
        BigDecimal cReceivedAmount = new BigDecimal(0.00);
        for (FeeDetailDto feeDetailDto : tFeeDetailDtos) {
            cReceivedAmount = cReceivedAmount.add(new BigDecimal(Double.parseDouble(feeDetailDto.getReceivedAmount()))).setScale(4, BigDecimal.ROUND_HALF_UP);
        }

        tFeeDetailDtos.get(0).setReceivedAmount(cReceivedAmount.doubleValue() + "");

        return tFeeDetailDtos.get(0);
    }


}
