/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.job.adapt.report.asyn.impl;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.report.IReportOwnerPayFeeInnerServiceSMO;
import com.java110.job.adapt.report.ReportOwnerPayFeeAdapt;
import com.java110.job.adapt.report.asyn.ISaveReportOwnerPayFee;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @desc add by 吴学文 16:28
 */
@Service
public class SaveReportOwnerPayFeeImpl implements ISaveReportOwnerPayFee {
    private static Logger logger = LoggerFactory.getLogger(SaveReportOwnerPayFeeImpl.class);


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IReportOwnerPayFeeInnerServiceSMO reportOwnerPayFeeInnerServiceSMOImpl;

    @Override
    @Async
    public void saveData(PayFeeDetailPo payFeeDetailPo, FeeDto feeDto) {
        List<String> dateSub = null;
        List<String> tmpDateSub = null;
        try {
            dateSub = analyseDate(DateUtil.getDateFromString(payFeeDetailPo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A),
                    DateUtil.getDateFromString(payFeeDetailPo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        } catch (ParseException e) {
            logger.error("出错了", e);
            return;
        }

        if (dateSub.size() < 1) {
            return;
        }

        if(FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) { //一次性不离散
            tmpDateSub = dateSub;
            dateSub = new ArrayList<>();
            dateSub.add(tmpDateSub.get(0));
        }

        BigDecimal amount = new BigDecimal(payFeeDetailPo.getReceivedAmount());
        amount = amount.divide(new BigDecimal(dateSub.size()), BigDecimal.ROUND_HALF_UP);

//        if("bailefu".equals(MappingCache.getValue("payFeeDetailToMonth"))){
//            doBailefuCommonOwnerPayFee(payFeeDetailPo, feeDto, dateSub, amount);
//        }else{
            doCommonOwnerPayFee(payFeeDetailPo, feeDto, dateSub, amount);
//        }


    }

    private void doBailefuCommonOwnerPayFee(PayFeeDetailPo payFeeDetailPo, FeeDto feeDto, List<String> dateSub, BigDecimal amount) {
        Date createTime = null;
        Date startTime = null;
        try {
            createTime = DateUtil.getDateFromString(payFeeDetailPo.getCreateTime(), DateUtil.DATE_FORMATE_STRING_B);
        } catch (ParseException e) {
            logger.error("出错了", e);
            return;
        }
        BigDecimal oweFee = new BigDecimal(0);

        for (String dateStr : dateSub) {
            try {
                Calendar startTimeCal = Calendar.getInstance();
                startTimeCal.setTime(DateUtil.getDateFromStringB(dateStr));
                startTimeCal.set(Calendar.DAY_OF_MONTH, 1);
                startTime = startTimeCal.getTime();

                Calendar createTimeCal = Calendar.getInstance();
                createTimeCal.setTime(createTime);
                createTimeCal.set(Calendar.DAY_OF_MONTH, 1);
                createTime = createTimeCal.getTime();

                if (startTime.getTime() < createTime.getTime()) {
                    dealOwnerPayFee(dateStr, payFeeDetailPo, feeDto, 0.0);
                    oweFee = oweFee.add(amount);
                } else if (startTime.getTime() == createTime.getTime()) {
                    oweFee = oweFee.add(amount);
                    dealOwnerPayFee(dateStr, payFeeDetailPo, feeDto, oweFee.doubleValue());
                } else {
                    dealOwnerPayFee(dateStr, payFeeDetailPo, feeDto, amount.doubleValue());
                }
            } catch (Exception e) {
                logger.error("出错了", e);
                return;
            }
        }
    }

    private void doCommonOwnerPayFee(PayFeeDetailPo payFeeDetailPo, FeeDto feeDto, List<String> dateSub, BigDecimal amount) {
        for (String dateStr : dateSub) {
            try {
                dealOwnerPayFee(dateStr, payFeeDetailPo, feeDto, amount.doubleValue());
            } catch (Exception e) {
                logger.error("出错了", e);
                return;
            }
        }
    }


    private void dealOwnerPayFee(String dateStr, PayFeeDetailPo payFeeDetailPo, FeeDto feeDto, double amount) throws Exception {
        ReportOwnerPayFeePo reportOwnerPayFeePo = new ReportOwnerPayFeePo();
        reportOwnerPayFeePo.setCommunityId(payFeeDetailPo.getCommunityId());
        reportOwnerPayFeePo.setAmount(amount + "");
        reportOwnerPayFeePo.setConfigId(feeDto.getConfigId());
        reportOwnerPayFeePo.setConfigName(feeDto.getFeeName());
        reportOwnerPayFeePo.setFeeName(feeDto.getFeeName());
        reportOwnerPayFeePo.setDetailId(payFeeDetailPo.getDetailId());
        reportOwnerPayFeePo.setFeeId(feeDto.getFeeId());
        //获取缴费用户楼栋单元房间号
        String payerObjName = computeFeeSMOImpl.getFeeObjName(feeDto);
        reportOwnerPayFeePo.setObjId(feeDto.getPayerObjId());
        reportOwnerPayFeePo.setObjName(payerObjName);
        reportOwnerPayFeePo.setObjType(feeDto.getPayerObjType());
        reportOwnerPayFeePo.setOwnerId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
        reportOwnerPayFeePo.setOwnerName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
        reportOwnerPayFeePo.setPfDate(dateStr);
        reportOwnerPayFeePo.setPfMonth(getMonth(dateStr) + "");
        reportOwnerPayFeePo.setPfId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_pfId));
        reportOwnerPayFeePo.setPfYear(getYear(dateStr) + "");
        reportOwnerPayFeeInnerServiceSMOImpl.saveReportOwnerPayFee(reportOwnerPayFeePo);
    }

    public static int getMonth(String dateStr) throws Exception {
        Date date = DateUtil.getDateFromString(dateStr, DateUtil.DATE_FORMATE_STRING_B);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        return c1.get(Calendar.MONTH) + 1;
    }

    public static int getYear(String dateStr) throws Exception {
        Date date = DateUtil.getDateFromString(dateStr, DateUtil.DATE_FORMATE_STRING_B);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        return c1.get(Calendar.YEAR);
    }

    public static List<String> analyseDate(Date begintime, Date endtime) {
        List<String> results = new ArrayList<String>();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(begintime);//2014-02-24
        c2.setTime(endtime);//2015-04-30
        String[] str = null;
        String sd = "";
        while (c1.compareTo(c2) < 0) {
//            System.out.println(c1.getTime());
            str = new String[2];
            sd = c1.get(Calendar.YEAR) + "-" + (c1.get(Calendar.MONTH) + 1) + "-01";


            if (c1.get(Calendar.MONTH) == 2) {
                c1.add(Calendar.DAY_OF_YEAR, 1);
            }
            c1.add(Calendar.MONTH, 1);
//            System.out.println("str[1]:"+str[1]);
            results.add(sd);
        }
        return results;
    }
}
