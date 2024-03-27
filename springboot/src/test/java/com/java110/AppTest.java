package com.java110;

import static org.junit.Assert.assertTrue;

import com.java110.core.factory.PlutusFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.MoneyUtil;
import com.java110.utils.util.StringUtil;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void should() {
//todo 递增时间 不是 费用建账时间的倍数时，修正一下
        Date rateStartTime = DateUtil.getDateFromStringB("2023-03-20");
        FeeDto feeDto =  new FeeDto();
        feeDto.setStartTime(DateUtil.getDateFromStringB("2023-01-01"));
        feeDto.setEndTime(DateUtil.getDateFromStringB("2024-03-01"));
        feeDto.setDeadlineTime(DateUtil.getDateFromStringB("2024-04-01"));
        feeDto.setFeePrice(1000);

        int rateCycle = 12;

        double rate = 0.03;

        rateStartTime = correctByFeeStartTime(rateStartTime,feeDto.getStartTime());

        BigDecimal addTotalAmount = new BigDecimal("0");
        double curOweMonth = 0;
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());

        //todo 递增本轮欠费开始时间
        Date curOweStartTime = null;
        // todo 如果计费起始时间 小于 递增开始时间
        if (feeDto.getEndTime().getTime() < rateStartTime.getTime()) {
            //todo 递增前的欠费
            curOweMonth = DateUtil.dayCompare(feeDto.getEndTime(), rateStartTime);
            addTotalAmount = curFeePrice.multiply(new BigDecimal(curOweMonth)).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            // todo 递增
            curOweStartTime = rateStartTime;
        } else {
            // todo 递增
            curOweStartTime = feeDto.getEndTime();
        }
        double rateMonth = DateUtil.dayCompare(rateStartTime, feeDto.getDeadlineTime());

        // todo 最大周期 递增轮数
        double maxCycle = Math.ceil(rateMonth / rateCycle);

        // todo 增长前的欠费
        BigDecimal rateDec = new BigDecimal(rate + "");
        BigDecimal oweAmountDec = null;
        //todo 递增轮数 循环 curFeePrice 这个是 原始租金
        for (int cycleIndex = 0; cycleIndex < maxCycle; cycleIndex++) {
            //todo 递增月单价
            curFeePrice = new BigDecimal("1").add(rateDec).multiply(curFeePrice).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

            //todo 计算 curCycleRateEneTime 本轮递增结束时间
            Date curCycleRateEneTime = DateUtil.stepMonth(rateStartTime, (cycleIndex + 1) * rateCycle);

            //todo 说明这个已经缴费了
            if (curOweStartTime.getTime() > curCycleRateEneTime.getTime()) {
                continue;
            }
            //todo 本轮 欠费开始时间大于 deadlineTime 跳过
            if(curOweStartTime.getTime() >= feeDto.getDeadlineTime().getTime()){
                continue;
            }

            //todo 本轮递增时间未到 费用deadlineTime
            if (curCycleRateEneTime.getTime() < feeDto.getDeadlineTime().getTime()) {
                curOweMonth = DateUtil.dayCompare(curOweStartTime, curCycleRateEneTime);
                curOweStartTime = curCycleRateEneTime;
            } else {
                curOweMonth = DateUtil.dayCompare(curOweStartTime, feeDto.getDeadlineTime());
                curOweStartTime = feeDto.getDeadlineTime();
            }

            oweAmountDec = curFeePrice.multiply(new BigDecimal(curOweMonth)).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

            addTotalAmount = addTotalAmount.add(oweAmountDec);
        }

        Double amountOwed = MoneyUtil.computePriceScale(addTotalAmount.doubleValue(), feeDto.getScale(), 4);
        System.out.println(amountOwed);
    }

    /**
     * 修正递增 开始时间
     * 如果设置的 递增开始时间和建账时间不是同一天 强制修正下
     * @param rateStartTime
     * @param startTime
     * @return
     */
    private Date correctByFeeStartTime(Date rateStartTime, Date startTime) {
        Calendar rateCalendar = Calendar.getInstance();
        rateCalendar.setTime(rateStartTime);
        int rateDay = rateCalendar.get(Calendar.DAY_OF_MONTH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(rateDay == day){
            return rateStartTime;
        }

        rateCalendar = Calendar.getInstance();
        rateCalendar.setTime(rateStartTime);
        rateCalendar.add(Calendar.MONTH,1);
        rateCalendar.set(Calendar.DAY_OF_MONTH,day);
        return rateCalendar.getTime();
    }

    public Map getTargetEndDateAndOweMonth(FeeDto feeDto, OwnerCarDto ownerCarDto) {
        Date targetEndDate = null;
        double oweMonth = 0.0;

        Map<String, Object> targetEndDateAndOweMonth = new HashMap<>();
        //todo 判断当前费用是否已结束
        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            targetEndDate = feeDto.getEndTime();
            targetEndDateAndOweMonth.put("oweMonth", oweMonth);
            targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
            return targetEndDateAndOweMonth;
        }

        //todo 考虑费用项 费用提前生成
        Calendar preEndTimeCal = Calendar.getInstance();
        preEndTimeCal.setTime(feeDto.getEndTime());
        if (StringUtil.isNumber(feeDto.getPrepaymentPeriod())) {
            preEndTimeCal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(feeDto.getPrepaymentPeriod()) * -1);
        }
        Date preEndTime = preEndTimeCal.getTime();

        //todo 当前费用为一次性费用
        Date maxEndTime = feeDto.getConfigEndTime();
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            //先取 deadlineTime
            if (feeDto.getDeadlineTime() != null) {
                targetEndDate = feeDto.getDeadlineTime();
            } else if (!StringUtil.isEmpty(feeDto.getCurDegrees())) {
                targetEndDate = feeDto.getCurReadingTime();
            } else if (feeDto.getImportFeeEndTime() == null) {
                targetEndDate = maxEndTime;
            } else {
                targetEndDate = feeDto.getImportFeeEndTime();
            }
            //说明欠费
            if (preEndTime.getTime() < DateUtil.getCurrentDate().getTime()) {
                // 目标到期时间 - 到期时间 = 欠费月份
                oweMonth = 1.0;
            }
        } else if (FeeDto.FEE_FLAG_CYCLE_ONCE.equals(feeDto.getFeeFlag())) {
            if (feeDto.getDeadlineTime() != null) {
                maxEndTime = feeDto.getDeadlineTime();
            }
            Date billEndTime = DateUtil.getCurrentDate();
            //建账时间
            Date startDate = feeDto.getStartTime();
            //计费起始时间
            Date endDate = feeDto.getEndTime();
            //缴费周期
            long paymentCycle = Long.parseLong(feeDto.getPaymentCycle());
            // 当前时间 - 开始时间  = 月份
            double mulMonth = 0.0;
            mulMonth = dayCompare(startDate, billEndTime);

            // 月份/ 周期 = 轮数（向上取整）
            double round = 0.0;
            if ("1200".equals(feeDto.getPaymentCd())) { // 1200预付费
                round = Math.floor(mulMonth / paymentCycle) + 1;
            } else { //2100后付费
                round = Math.floor(mulMonth / paymentCycle);
            }
            // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
            targetEndDate = getTargetEndTime(round * paymentCycle, startDate);//目标结束时间

            //todo 如果 到了 预付期 产生下个周期的费用
            if (DateUtil.getFormatTimeStringB(targetEndDate).equals(DateUtil.getFormatTimeStringB(endDate))
                    && DateUtil.getCurrentDate().getTime() > preEndTime.getTime()
            ) {
                targetEndDate = getTargetEndTime((round + 1) * paymentCycle, startDate);//目标结束时间
            }


            //todo 费用项的结束时间<缴费的结束时间  费用快结束了   取费用项的结束时间
            if (maxEndTime.getTime() < targetEndDate.getTime()) {
                targetEndDate = maxEndTime;
            }
            //说明欠费
            if (endDate.getTime() < targetEndDate.getTime()) {
                // 目标到期时间 - 到期时间 = 欠费月份
                oweMonth = dayCompare(endDate, targetEndDate);
            }

            if (feeDto.getEndTime().getTime() > targetEndDate.getTime()) {
                targetEndDate = feeDto.getEndTime();
            }
        } else { // todo 周期性费用
            //当前时间
            Date billEndTime = DateUtil.getCurrentDate();
            //建账时间
            Date startDate = feeDto.getStartTime();
            //计费起始时间
            Date endDate = feeDto.getEndTime();
            //缴费周期
            long paymentCycle = Long.parseLong(feeDto.getPaymentCycle());
            // 当前时间 - 开始时间  = 月份
            double mulMonth = 0.0;
            mulMonth = dayCompare(endDate, billEndTime);

            // 月份/ 周期 = 轮数（向上取整）
            double round = 0.0;
            if ("1200".equals(feeDto.getPaymentCd())) { // 1200预付费
                round = Math.floor(mulMonth / paymentCycle) + 1;
            } else { //2100后付费
                round = Math.floor(mulMonth / paymentCycle);
            }
            // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
            targetEndDate = getTargetEndTime(round * paymentCycle, endDate);//目标结束时间

            //todo 如果 到了 预付期 产生下个周期的费用
            if (DateUtil.getFormatTimeStringB(targetEndDate).equals(DateUtil.getFormatTimeStringB(endDate))
                    && DateUtil.getCurrentDate().getTime() > preEndTime.getTime()
            ) {
                targetEndDate = getTargetEndTime((round + 1) * paymentCycle, startDate);//目标结束时间
            }

            //费用项的结束时间<缴费的结束时间  费用快结束了   取费用项的结束时间
            if (maxEndTime.getTime() < targetEndDate.getTime()) {
                targetEndDate = maxEndTime;
            }
            //说明欠费
            if (endDate.getTime() < targetEndDate.getTime()) {
                // 目标到期时间 - 到期时间 = 欠费月份
                oweMonth = dayCompare(endDate, targetEndDate);
            }

            if (feeDto.getEndTime().getTime() > targetEndDate.getTime()) {
                targetEndDate = feeDto.getEndTime();
            }
        }

        targetEndDateAndOweMonth.put("oweMonth", oweMonth);
        targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
        return targetEndDateAndOweMonth;
    }

    public Date getTargetEndTime(double month, Date startDate) {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDate);

        Double intMonth = Math.floor(month);
        endDate.add(Calendar.MONTH, intMonth.intValue());
        double doubleMonth = month - intMonth;
        if (doubleMonth <= 0) {
            return endDate.getTime();
        }
        int futureDay = endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        Double hour = doubleMonth * futureDay * 24;
        endDate.add(Calendar.HOUR_OF_DAY, hour.intValue());
        return endDate.getTime();
    }


    /**
     * 计算 fromDate 2023-01-12  toDate 2023-09-15
     * 2023-01-12--->2023-01-01        --->  2023-09-01    ------> 2023-09-15
     * fromDate ---> fromDateFirstDate --->  toDateFirstDate ----> toDate
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public double dayCompare(Date fromDate, Date toDate) {


        //todo 需要计算三端时间 相加即可
        Date fromDateFirstDate = fromDate; // 第一个1日

        Date toDateFirstDate = toDate; // 最后一个1日

        boolean firstDay = true;

        //todo 1.0 计算 fromDateFirstDate
        Calendar fromDateCal = Calendar.getInstance();
        fromDateCal.setTime(fromDate);
        fromDateCal.set(Calendar.DAY_OF_MONTH, 1);
        if (fromDate.getTime() > fromDateCal.getTime().getTime()) {
            fromDateCal.add(Calendar.MONTH, 1);
            firstDay = false;
            fromDateFirstDate = fromDateCal.getTime();
        }

        //todo 2.0 计算 toDateFirstDate
        Calendar toDateCal = Calendar.getInstance();
        toDateCal.setTime(toDate);
        toDateCal.set(Calendar.DAY_OF_MONTH, 1);
        if (toDate.getTime() > toDateCal.getTime().getTime()) {
            toDateFirstDate = toDateCal.getTime();
        }

        // todo 3.0 计算整数月  fromDateFirstDate --->  toDateFirstDate
        Calendar from = Calendar.getInstance();
        from.setTime(fromDateFirstDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDateFirstDate);
        //比较月份差 可能有整数 也会负数
        int result = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        //比较年差
        int month = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12;
        //真实 相差月份
        result = result + month;

        //todo 3.1  如果 fromDate 和toDate 是同一天 则直接返回整月，不再计算 4.0 和5.0
        if (DateUtil.sameMonthDay(fromDate, toDate)) {
            return firstDay ? result : result + 1;
        }

        // todo 4.0 计算 fromDate ---> fromDateFirstDate 的月份
        double days = (fromDateFirstDate.getTime() - fromDate.getTime()) * 1.00 / (24 * 60 * 60 * 1000);
        BigDecimal tmpDays = new BigDecimal(days); //相差天数
        BigDecimal monthDay = new BigDecimal(DateUtil.getMonthDay(fromDate));
        BigDecimal resMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result));

        // todo 5.0 计算  toDateFirstDate ----> toDate 月份
        days = (toDate.getTime() - toDateFirstDate.getTime()) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days); //相差天数
        monthDay = new BigDecimal(DateUtil.getMonthDay(toDate));
        resMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(resMonth);

        return resMonth.doubleValue();

    }

    public double dayCompare1(Date fromDate, Date toDate) {
        double resMonth = 0.0;
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        //比较月份差 可能有整数 也会负数
        int result = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        //比较年差
        int month = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12;

        //真实 相差月份
        result = result + month;

        //开始时间  2021-06-01  2021-08-05   result = 2    2021-08-01
        Calendar newFrom = Calendar.getInstance();
        newFrom.setTime(fromDate);
        newFrom.add(Calendar.MONTH, result);
        //如果加月份后 大于了当前时间 默认加 月份 -1 情况 12-19  21-01-10
        //这个是神的逻辑一定好好理解
        if (newFrom.getTime().getTime() > toDate.getTime()) {
            newFrom.setTime(fromDate);
            result = result - 1;
            newFrom.add(Calendar.MONTH, result);
        }

        // t1 2021-08-01   t2 2021-08-05
        long t1 = newFrom.getTime().getTime();
        long t2 = to.getTime().getTime();
        //相差毫秒
        double days = (t2 - t1) * 1.00 / (24 * 60 * 60 * 1000);
        BigDecimal tmpDays = new BigDecimal(days); //相差天数
        BigDecimal monthDay = null;
        Calendar newFromMaxDay = Calendar.getInstance();
        newFromMaxDay.set(newFrom.get(Calendar.YEAR), newFrom.get(Calendar.MONTH), 1, 0, 0, 0);
        newFromMaxDay.add(Calendar.MONTH, 1); //下个月1号
        //在当前月中 这块有问题
        if (toDate.getTime() < newFromMaxDay.getTime().getTime()) {
            monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
            return tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).doubleValue();
        }
        // 上月天数
        days = (newFromMaxDay.getTimeInMillis() - t1) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
        BigDecimal preRresMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP);

        //下月天数
        days = (t2 - newFromMaxDay.getTimeInMillis()) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFromMaxDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        resMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).add(preRresMonth).doubleValue();
        return resMonth;
    }

}
