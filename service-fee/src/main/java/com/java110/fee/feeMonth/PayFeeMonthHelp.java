package com.java110.fee.feeMonth;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.utils.util.DateUtil;
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

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;


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
//        if (!FeeDto.FEE_FLAG_ONCE.equals(feeDto.getPayerObjType())) {
//            return feePrice;
//        }
//        double maxMonth = Math.ceil(computeFeeSMOImpl.dayCompare(feeDto.getStartTime(), feeDto.getEndTime()));
//        if (maxMonth <= 0) {
//            return feePrice;
//
//        }
//        BigDecimal feePriceDec = new BigDecimal(feePrice).divide(new BigDecimal(maxMonth), 2, BigDecimal.ROUND_HALF_UP);
//        feePrice = feePriceDec.doubleValue();
        return feePrice;
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


    /**
     * 处理已经交过费的记录处理
     *
     * @param feeDto
     * @param payFeeMonthOwnerDto
     */
    @Override
    public void waitDispersedFeeDetail(FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto) {
        PayFeeDetailMonthDto payFeeDetailMonthDto = new PayFeeDetailMonthDto();
        payFeeDetailMonthDto.setCommunityId(feeDto.getCommunityId());
        payFeeDetailMonthDto.setFeeId(feeDto.getFeeId());
        List<FeeDetailDto> feeDetailDtos = payFeeDetailMonthInnerServiceSMOImpl.getWaitDispersedFeeDetail(payFeeDetailMonthDto);

        if (feeDetailDtos == null || feeDetailDtos.size() < 1) {
            return;
        }

        for (FeeDetailDto feeDetailDto : feeDetailDtos) {
            // todo 逐条去离散
            doDispersedFeeDetail(feeDetailDto, feeDto, payFeeMonthOwnerDto);
        }
    }


    /**
     * 处理 欠费 离散
     *
     * @param feeDto
     * @param payFeeMonthOwnerDto
     * @param feePrice
     * @param deadlineTime
     */
    @Override
    public void waitDispersedOweFee(FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto, Double feePrice, Date deadlineTime) {
        // todo 费用已经结束
        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            return;
        }

        // todo 清理 detailId 为-1 的数据
        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setCommunityId(feeDto.getCommunityId());
        payFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
        payFeeDetailMonthPo.setDetailId("-1");
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);

        List<PayFeeDetailMonthPo> payFeeDetailMonthPos = new ArrayList<>();
        // todo 处理 开始时间和结束时间
        Date startTime = DateUtil.timeToDate(feeDto.getEndTime());
        Date endTime = DateUtil.timeToDate(deadlineTime);

        BigDecimal receivableAmount = new BigDecimal(feePrice);

        BigDecimal dayReceivableAmount = null;

        //todo 一次性费用 日应收计算
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            int day = DateUtil.daysBetween(endTime, startTime);
            if (day < 1) {
                day = 1;
            }
            dayReceivableAmount = receivableAmount.divide(new BigDecimal(day), 4, BigDecimal.ROUND_HALF_UP);// 日 应收
        }

        // todo 寻找第一个自然月 一日
        Calendar firstMonthDayCal = Calendar.getInstance();
        firstMonthDayCal.setTime(startTime);
        firstMonthDayCal.add(Calendar.MONTH, 1);
        firstMonthDayCal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstMonthDayTime = firstMonthDayCal.getTime();

        Date startMonthDayTime = startTime;
        // todo  循环，只到 firstMonthDayTime 大于 endTime
        int curDay = 0;
        int curMonthMaxDay = 30;
        BigDecimal curMonthReceivableAmount = null;

        while (firstMonthDayTime.getTime() < endTime.getTime()) {
            curDay = DateUtil.daysBetween(firstMonthDayTime, startMonthDayTime);

            //todo 周期性费用 日应收重新算
            if (!FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
                // todo 计算当月天数
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startMonthDayTime);
                curMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                dayReceivableAmount = receivableAmount.divide(new BigDecimal(curMonthMaxDay), 4, BigDecimal.ROUND_HALF_UP);// 日 实收
            }
            // todo 计算 应收
            curMonthReceivableAmount = new BigDecimal(curDay).multiply(dayReceivableAmount).setScale(4, BigDecimal.ROUND_HALF_UP);

            // todo 保存数据到pay_fee_detail_month
            toSavePayFeeDetailMonth(curMonthReceivableAmount.doubleValue(), 0, null, feeDto, payFeeMonthOwnerDto, payFeeDetailMonthPos, startMonthDayTime,deadlineTime);

            // todo 将startTime 修改为 下月1日时间
            startMonthDayTime = firstMonthDayTime;
            firstMonthDayCal.add(Calendar.MONTH, 1);
            firstMonthDayTime = firstMonthDayCal.getTime();
        }

        //todo 最后处理 最后 startMonthDayTime 到endTime 的
        if (startMonthDayTime.getTime() >= endTime.getTime()) {
            payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonths(payFeeDetailMonthPos);
            return;
        }

        curDay = DateUtil.daysBetween(endTime, startMonthDayTime);
        //todo 周期性费用 日应收重新算
        if (!FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            // todo 计算当月天数
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startMonthDayTime);
            curMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            dayReceivableAmount = receivableAmount.divide(new BigDecimal(curMonthMaxDay), 4, BigDecimal.ROUND_HALF_UP);// 日 实收
        }
        // todo 计算 应收
        curMonthReceivableAmount = new BigDecimal(curDay).multiply(dayReceivableAmount).setScale(4, BigDecimal.ROUND_HALF_UP);

        // todo 保存数据到pay_fee_detail_month
        toSavePayFeeDetailMonth(curMonthReceivableAmount.doubleValue(), 0, null, feeDto, payFeeMonthOwnerDto, payFeeDetailMonthPos, startMonthDayTime,deadlineTime);
        payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonths(payFeeDetailMonthPos);

    }

    private void doDispersedFeeDetail(FeeDetailDto feeDetailDto, FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto) {
        List<PayFeeDetailMonthPo> payFeeDetailMonthPos = new ArrayList<>();

        // todo 去除 开始时间和 结束时间的 小时 分钟 秒
        Date startTime = DateUtil.timeToDate(feeDetailDto.getStartTime());
        Date endTime = DateUtil.timeToDate(feeDetailDto.getEndTime());

        int day = DateUtil.daysBetween(endTime, startTime);
        if (day < 1) {
            day = 1;
        }

        BigDecimal receivableAmount = new BigDecimal(Double.parseDouble(feeDetailDto.getReceivableAmount()));
        BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(feeDetailDto.getReceivedAmount()));

        BigDecimal dayReceivableAmount = receivableAmount.divide(new BigDecimal(day), 4, BigDecimal.ROUND_HALF_UP);// 日 应收
        BigDecimal dayReceivedAmount = receivedAmount.divide(new BigDecimal(day), 4, BigDecimal.ROUND_HALF_UP);// 日 实收

        // todo 寻找第一个自然月 一日
        Calendar firstMonthDayCal = Calendar.getInstance();
        firstMonthDayCal.setTime(startTime);
        firstMonthDayCal.add(Calendar.MONTH, 1);
        firstMonthDayCal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstMonthDayTime = firstMonthDayCal.getTime();

        Date startMonthDayTime = startTime;
        // todo  循环，只到 firstMonthDayTime 大于 endTime
        int curDay = 0;
        BigDecimal curMonthReceivableAmount = null;
        BigDecimal curMonthReceivedAmount = null;
        while (firstMonthDayTime.getTime() < endTime.getTime()) {
            curDay = DateUtil.daysBetween(firstMonthDayTime, startMonthDayTime);
            // todo 计算 应收
            curMonthReceivableAmount = new BigDecimal(curDay).multiply(dayReceivableAmount).setScale(4, BigDecimal.ROUND_HALF_UP);
            // todo 计算 实收
            curMonthReceivedAmount = new BigDecimal(curDay).multiply(dayReceivedAmount).setScale(4, BigDecimal.ROUND_HALF_UP);

            // todo 保存数据到pay_fee_detail_month
            toSavePayFeeDetailMonth(curMonthReceivableAmount.doubleValue(), curMonthReceivedAmount.doubleValue(), feeDetailDto, feeDto, payFeeMonthOwnerDto, payFeeDetailMonthPos, startMonthDayTime,endTime);

            // todo 将startTime 修改为 下月1日时间
            startMonthDayTime = firstMonthDayTime;
            firstMonthDayCal.add(Calendar.MONTH, 1);
            firstMonthDayTime = firstMonthDayCal.getTime();
        }

        //todo 最后处理 最后 startMonthDayTime 到endTime 的
        if (startMonthDayTime.getTime() >= endTime.getTime()) {
            payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonths(payFeeDetailMonthPos);
            return;
        }

        curDay = DateUtil.daysBetween(endTime, startMonthDayTime);
        // todo 计算 应收
        curMonthReceivableAmount = new BigDecimal(curDay).multiply(dayReceivableAmount).setScale(4, BigDecimal.ROUND_HALF_UP);
        // todo 计算 实收
        curMonthReceivedAmount = new BigDecimal(curDay).multiply(dayReceivedAmount).setScale(4, BigDecimal.ROUND_HALF_UP);

        // todo 保存数据到pay_fee_detail_month
        toSavePayFeeDetailMonth(curMonthReceivableAmount.doubleValue(), curMonthReceivedAmount.doubleValue(), feeDetailDto, feeDto, payFeeMonthOwnerDto, payFeeDetailMonthPos, startMonthDayTime,endTime);
        payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonths(payFeeDetailMonthPos);

    }

    /**
     * 保存数据
     *
     * @param receivableAmount
     * @param receivedAmount
     * @param feeDetailDto
     * @param feeDto
     */
    private void toSavePayFeeDetailMonth(double receivableAmount,
                                         double receivedAmount,
                                         FeeDetailDto feeDetailDto,
                                         FeeDto feeDto,
                                         PayFeeMonthOwnerDto payFeeMonthOwnerDto,
                                         List<PayFeeDetailMonthPo> payFeeDetailMonthPos,
                                         Date curTime,
                                         Date deadlineTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curTime);
        PayFeeDetailMonthPo tmpPayFeeDetailMonthPo = new PayFeeDetailMonthPo();
        tmpPayFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
        tmpPayFeeDetailMonthPo.setCommunityId(feeDto.getCommunityId());
        if (feeDetailDto == null) {
            tmpPayFeeDetailMonthPo.setDetailId("-1");
        } else { // todo 交费记录 保存时
            tmpPayFeeDetailMonthPo.setDetailId(feeDetailDto.getDetailId());
        }
        tmpPayFeeDetailMonthPo.setDetailYear(calendar.get(Calendar.YEAR) + "");
        tmpPayFeeDetailMonthPo.setDetailMonth((calendar.get(Calendar.MONTH) + 1) + "");

        tmpPayFeeDetailMonthPo.setReceivableAmount(receivableAmount + "");
        tmpPayFeeDetailMonthPo.setReceivedAmount(receivedAmount + "");
        tmpPayFeeDetailMonthPo.setDiscountAmount(
                getDiscountAmount(Double.parseDouble(tmpPayFeeDetailMonthPo.getReceivableAmount()),
                        Double.parseDouble(tmpPayFeeDetailMonthPo.getReceivedAmount()),
                        calendar.getTime(), feeDto) + "");
        tmpPayFeeDetailMonthPo.setMonthId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_monthId,true));
        tmpPayFeeDetailMonthPo.setRemark("程序计算生成");
        tmpPayFeeDetailMonthPo.setObjName(payFeeMonthOwnerDto.getObjName());
        tmpPayFeeDetailMonthPo.setObjId(payFeeMonthOwnerDto.getObjId());
        tmpPayFeeDetailMonthPo.setOwnerId(payFeeMonthOwnerDto.getOwnerId());
        tmpPayFeeDetailMonthPo.setOwnerName(payFeeMonthOwnerDto.getOwnerName());
        tmpPayFeeDetailMonthPo.setLink(payFeeMonthOwnerDto.getLink());
        tmpPayFeeDetailMonthPo.setCurMonthTime(DateUtil.getFormatTimeStringB(calendar.getTime()));
        tmpPayFeeDetailMonthPo.setDeadlineTime(DateUtil.getFormatTimeStringA(deadlineTime));
        if (feeDetailDto == null) {
            tmpPayFeeDetailMonthPo.setPayFeeTime(null);
        } else { // todo 交费记录 保存时
            tmpPayFeeDetailMonthPo.setPayFeeTime(DateUtil.getFormatTimeStringA(feeDetailDto.getCreateTime()));
        }
        tmpPayFeeDetailMonthPo.setState("W"); // todo 这里暂时写死，目前用不到，算是预留字段
        tmpPayFeeDetailMonthPo.setFeeName(feeDto.getFeeName());
        tmpPayFeeDetailMonthPo.setConfigId(feeDto.getConfigId());
        payFeeDetailMonthPos.add(tmpPayFeeDetailMonthPo);

    }


}
