package com.java110.fee.feeMonth;

import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.MonthFeeDetailDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
        List<String> months = DateUtil.getMonthBetweenDate(feeDto.getStartTime(), feeDto.getEndTime());
        //double maxMonth = Math.ceil(computeFeeSMOImpl.dayCompare(feeDto.getStartTime(), feeDto.getEndTime()));
        if (months == null || months.size() <= 0) {
            return feePrice;

        }
        BigDecimal feePriceDec = new BigDecimal(feePrice).divide(new BigDecimal(months.size()), 4, BigDecimal.ROUND_HALF_UP);
        feePrice = feePriceDec.doubleValue();
        return feePrice;
    }


    public Double getReceivableAmount(List<FeeDetailDto> feeDetailDtos, Map<String, MonthFeeDetailDto> monthFeeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto) {
        MonthFeeDetailDto monthFeeDetailDto = getCurMonthFeeDetail(monthFeeDetailDtos, curDate);
        if (monthFeeDetailDto == null && curDate.getTime() < feeDto.getEndTime().getTime()) {
            return 0.00;
        }
        return feePrice;
    }


    @Override
    public Double getReceivedAmount(List<FeeDetailDto> feeDetailDtos, Map<String, MonthFeeDetailDto> monthFeeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto) {
        MonthFeeDetailDto monthFeeDetailDto = getCurMonthFeeDetail(monthFeeDetailDtos, curDate);
        if (monthFeeDetailDto == null) {
            return 0.0;
        }
        return monthFeeDetailDto.getReceivedAmount();
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
                tFeeDetailDtos.add(BeanConvertUtil.covertBean(feeDetailDto, FeeDetailDto.class));
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


    @Override
    public Map<String, MonthFeeDetailDto> analysisMonthFeeDetail(List<FeeDetailDto> feeDetailDtos) {

        if (feeDetailDtos == null || feeDetailDtos.size() < 1) {
            return null;
        }

        Map<String, MonthFeeDetailDto> monthFeeDetailDtos = new HashMap<>();

        for (FeeDetailDto feeDetailDto : feeDetailDtos) {
            Date endTime =  feeDetailDto.getEndTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            if(feeDetailDto.getStartTime().getTime()< calendar.getTime().getTime()){
                endTime = calendar.getTime();
            }
            //计算两个日期包含的月份
            List<String> months = DateUtil.getMonthBetweenDate(feeDetailDto.getStartTime(), endTime);

            if (months == null || months.size() < 1) {
                putReceivedAmountToMonthFeeDetailDtos(monthFeeDetailDtos,
                        DateUtil.getFormatTimeString(feeDetailDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_Q),
                        Double.parseDouble(feeDetailDto.getReceivedAmount()),
                        feeDetailDto);
                continue;
            }

            BigDecimal totalRecDec = new BigDecimal(feeDetailDto.getReceivedAmount());
            //每月平均值
            BigDecimal priRecDec = totalRecDec.divide(new BigDecimal(months.size()), 4, BigDecimal.ROUND_HALF_UP);

            for (String month : months) {
                putReceivedAmountToMonthFeeDetailDtos(monthFeeDetailDtos,
                        month,
                        priRecDec.doubleValue(),
                        feeDetailDto);
            }

        }
        return monthFeeDetailDtos;
    }


    /**
     * 月份存放起来
     *
     * @param monthFeeDetailDtos
     * @param month
     * @param receivedAmount
     */
    private void putReceivedAmountToMonthFeeDetailDtos(Map<String, MonthFeeDetailDto> monthFeeDetailDtos,
                                                       String month,
                                                       double receivedAmount,
                                                       FeeDetailDto feeDetailDto) {

        if (!monthFeeDetailDtos.containsKey(month)) {
            monthFeeDetailDtos.put(month, new MonthFeeDetailDto(receivedAmount, feeDetailDto));
            return;
        }

        MonthFeeDetailDto monthFeeDetailDto = monthFeeDetailDtos.get(month);
        BigDecimal recDec = new BigDecimal(monthFeeDetailDto.getReceivedAmount()).add(new BigDecimal(receivedAmount)).setScale(4, BigDecimal.ROUND_HALF_UP);
        monthFeeDetailDto.setReceivedAmount(recDec.doubleValue());
        monthFeeDetailDto.getFeeDetailDtos().add(feeDetailDto);
        monthFeeDetailDtos.put(month, monthFeeDetailDto);
    }

    /**
     * 月离散数据
     *
     * @param monthFeeDetailDtos
     * @param curDate
     * @return
     */
    private MonthFeeDetailDto getCurMonthFeeDetail(Map<String, MonthFeeDetailDto> monthFeeDetailDtos, Date curDate) {
        String month = DateUtil.getFormatTimeString(curDate, DateUtil.DATE_FORMATE_STRING_M);
        if (monthFeeDetailDtos == null) {
            return null;
        }
        if (!monthFeeDetailDtos.containsKey(month)) {
            return null;
        }
        MonthFeeDetailDto monthFeeDetailDto = monthFeeDetailDtos.get(month);
        return monthFeeDetailDto;
    }


}
