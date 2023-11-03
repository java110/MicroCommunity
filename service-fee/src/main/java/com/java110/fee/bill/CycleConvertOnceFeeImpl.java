package com.java110.fee.bill;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.payFeeRule.PayFeeRuleDto;
import com.java110.fee.dao.impl.PayFeeV1ServiceDaoImpl;
import com.java110.intf.fee.*;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeRule.PayFeeRulePo;
import com.java110.po.payFeeRuleBill.PayFeeRuleBillPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 转换一次性费用
 */

@Service
public class CycleConvertOnceFeeImpl implements ICycleConvertOnceFee {

    private static Logger logger = LoggerFactory.getLogger(PayFeeV1ServiceDaoImpl.class);


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IPayFeeRuleBillV1InnerServiceSMO payFeeRuleBillV1InnerServiceSMOImpl;

    @Override
    public int convertPayFees(List<PayFeePo> payFeePos) {

        List<PayFeePo> tmpPayFeePos = new ArrayList<>();
        for (PayFeePo tmpPayFeePo : payFeePos) {
            //todo 一次性费用 直接跳过
            if (FeeDto.FEE_FLAG_ONCE.equals(tmpPayFeePo.getFeeFlag())) {
                continue;
            }

            tmpPayFeePos.add(tmpPayFeePo);
        }
        if (tmpPayFeePos.isEmpty()) {
            return 0;
        }

        //todo 业务处理

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();

        PayFeeRulePo tmpPayFeeRulePo = null;
        String curYearMonth = "";
        for (PayFeePo tmpPayFeePo : payFeePos) {
            tmpPayFeeRulePo = BeanConvertUtil.covertBean(tmpPayFeePo, PayFeeRulePo.class);
            tmpPayFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
            curYearMonth = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringB(tmpPayFeePo.getEndTime()));
            tmpPayFeeRulePo.setCurYearMonth(curYearMonth);
            payFeeRulePos.add(tmpPayFeeRulePo);
        }

        if (payFeeRulePos.isEmpty()) {
            return 0;
        }

        //todo 保存规则
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        //todo 根据规则生成费用
        rulesGeneratePayFees(payFeeRulePos);

        return 0;
    }

    @Override
    public int convertPayFee(PayFeePo payFeePo) {
        List<PayFeePo> payFeePos = new ArrayList<>();
        payFeePos.add(payFeePo);
        return convertPayFees(payFeePos);
    }

    @Override
    public int covertCommunityPayFee(String communityId) {
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setCommunityId(communityId);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (payFeeRuleDtos == null || payFeeRuleDtos.isEmpty()) {
            return 0;
        }

        List<PayFeeRulePo> tmpPayFeeRulePos = BeanConvertUtil.covertBeanList(payFeeRuleDtos, PayFeeRulePo.class);
        return rulesGeneratePayFees(tmpPayFeeRulePos);
    }

    @Override
    public int covertRuleIdsPayFee(List<String> ruleIds) {
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (payFeeRuleDtos == null || payFeeRuleDtos.isEmpty()) {
            return 0;
        }

        List<PayFeeRulePo> tmpPayFeeRulePos = BeanConvertUtil.covertBeanList(payFeeRuleDtos, PayFeeRulePo.class);
        return rulesGeneratePayFees(tmpPayFeeRulePos);
    }

    @Override
    public int rulesGeneratePayFees(List<PayFeeRulePo> payFeeRulePos) {

        if (payFeeRulePos == null || payFeeRulePos.isEmpty()) {
            return 0;
        }

        for (PayFeeRulePo tmpPayFeeRulePo : payFeeRulePos) {
            try {
                //todo 单个费用处理
                ruleGeneratePayFee(tmpPayFeeRulePo);
            } catch (Exception e) {
                logger.error("处理异常 ruleId" + tmpPayFeeRulePo.getRuleId(), e);
            }
        }
        return payFeeRulePos.size();
    }

    /**
     * 规则生成费用
     *
     * @param tmpPayFeeRulePo
     */
    public int ruleGeneratePayFee(PayFeeRulePo tmpPayFeeRulePo) {
        int ruleCycle = 1;
        String value = CommunitySettingFactory.getValue(tmpPayFeeRulePo.getCommunityId(), "PAY_FEE_MONTH_CYCLE");
        if (StringUtil.isNumber(value)) {
            ruleCycle = Integer.parseInt(value);
        }

        //todo 查询费用项信息
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(tmpPayFeeRulePo.getConfigId());
        feeConfigDto.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "费用项不存在，configId=" + tmpPayFeeRulePo.getConfigId());

        //todo 计算目标 结束时间
        Date targetEndTime = computeTargetEndTime(tmpPayFeeRulePo, feeConfigDtos.get(0));

        //todo 创建 pay_fee 和 attrs 数据
        List<PayFeeRuleBillPo> payFeeRuleBillPos = new ArrayList<>();
        List<PayFeePo> tmpPayFeePos = new ArrayList<>();
        List<FeeAttrPo> tmpFeeAttrPos = new ArrayList<>();

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjType(tmpPayFeeRulePo.getPayerObjType());
        feeDto.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        feeDto.setPayerObjId(tmpPayFeeRulePo.getPayerObjId());
        OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
        String payerObjName = computeFeeSMOImpl.getFeeObjName(feeDto);

        Date startTime = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getCurYearMonth());
        Date endTime = null;

        //todo 没到时间
        if (startTime.getTime() >= targetEndTime.getTime()) {
            return 0;
        }

        do {
            endTime = DateUtil.getNextMonthFirstDate(startTime, ruleCycle);
            if (endTime.getTime() >= targetEndTime.getTime()) {
                endTime = targetEndTime;
            }
            //todo 生成 费用
            doGeneratorPayFee(tmpPayFeeRulePo, startTime, endTime, feeConfigDtos.get(0), tmpPayFeePos, tmpFeeAttrPos, payFeeRuleBillPos, ownerDto, payerObjName,ruleCycle);

            startTime = endTime;

        }
        while (endTime.getTime() < targetEndTime.getTime());
        int saveFlag = 0;
        if (!tmpPayFeePos.isEmpty()) {
            saveFlag = saveFeeAndAttrs(tmpPayFeePos, tmpFeeAttrPos, payFeeRuleBillPos);
        }

        //todo 修改pay_fee_rule 的CurYearMonth

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setRuleId(tmpPayFeeRulePo.getRuleId());
        payFeeRulePo.setCurYearMonth(DateUtil.getFormatTimeStringB(endTime));
        payFeeRulePo.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        payFeeRuleV1InnerServiceSMOImpl.updatePayFeeRule(payFeeRulePo);

        return saveFlag;
    }


    private void doGeneratorPayFee(PayFeeRulePo tmpPayFeeRulePo, Date startTime, Date endTime, FeeConfigDto feeConfigDto,
                                   List<PayFeePo> tmpPayFeePos, List<FeeAttrPo> tmpFeeAttrPos,
                                   List<PayFeeRuleBillPo> payFeeRuleBillPos,
                                   OwnerDto ownerDto,
                                   String payerObjName,
                                   int ruleCycle) {

        PayFeePo payFeePo = BeanConvertUtil.covertBean(tmpPayFeeRulePo, PayFeePo.class);
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setStartTime(DateUtil.getFormatTimeStringB(startTime));
        payFeePo.setEndTime(DateUtil.getFormatTimeStringB(startTime));
        payFeePo.setRuleId(tmpPayFeeRulePo.getRuleId());

        tmpPayFeePos.add(payFeePo);

        //todo 生成ruleBill 数据
        PayFeeRuleBillPo payFeeRuleBillPo = new PayFeeRuleBillPo();
        payFeeRuleBillPo.setFeeId(payFeePo.getFeeId());
        payFeeRuleBillPo.setBillId(GenerateCodeFactory.getGeneratorId("13"));
        payFeeRuleBillPo.setBillName(feeConfigDto.getFeeName());
        payFeeRuleBillPo.setRuleId(tmpPayFeeRulePo.getRuleId());
        payFeeRuleBillPo.setConfigId(tmpPayFeeRulePo.getConfigId());
        payFeeRuleBillPo.setBatchId(payFeePo.getBatchId());
        payFeeRuleBillPo.setCurYearMonth(DateUtil.getFormatTimeStringB(startTime));
        payFeeRuleBillPo.setCommunityId(payFeePo.getCommunityId());
        payFeeRuleBillPo.setMonthCycle(ruleCycle+"");
        payFeeRuleBillPos.add(payFeeRuleBillPo);

        //todo 这里时间少一天，显示，因为是周期性转一次性 所以 这里为了显示合理 结束时间少一天处理
        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTime(endTime);
        endTimeCal.add(Calendar.DAY_OF_MONTH, -1);
        tmpFeeAttrPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME, DateUtil.getFormatTimeStringB(endTimeCal.getTime())));

        if (ownerDto != null) {
            tmpFeeAttrPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_ID, ownerDto.getOwnerId()));
            tmpFeeAttrPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_LINK, ownerDto.getLink()));
            tmpFeeAttrPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_NAME, ownerDto.getName()));
        }

        //todo 付费对象名称
        tmpFeeAttrPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME, payerObjName));


    }

    private FeeAttrPo addFeeAttr(PayFeePo payFeePo, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(payFeePo.getCommunityId());
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        return feeAttrPo;
    }


    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos, List<PayFeeRuleBillPo> payFeeRuleBillPos) {
        if (feePos == null || feePos.isEmpty()) {
            return 1;
        }
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        if (!payFeeRuleBillPos.isEmpty()) {
            payFeeRuleBillV1InnerServiceSMOImpl.savePayFeeRuleBills(payFeeRuleBillPos);
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        // todo 这里异步的方式计算 月数据 和欠费数据
        List<String> feeIds = new ArrayList<>();
        for (PayFeePo feePo : feePos) {
            feeIds.add(feePo.getFeeId());
        }

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(feePos.get(0).getCommunityId());
        payFeeDetailRefreshFeeMonthDto.setFeeIds(feeIds);

        payFeeMonthInnerServiceSMOImpl.doGeneratorFeeMonths(payFeeDetailRefreshFeeMonthDto);

        payFeeMonthInnerServiceSMOImpl.doGeneratorOweFees(payFeeDetailRefreshFeeMonthDto);
        return flag;
    }

    /**
     * 计算目标结束时间
     *
     * @param tmpPayFeeRulePo
     * @param feeConfigDto
     * @return
     */
    private Date computeTargetEndTime(PayFeeRulePo tmpPayFeeRulePo, FeeConfigDto feeConfigDto) {

        Date targetEndDate = null;
        //todo 判断当前费用是否已结束
        if (FeeDto.STATE_FINISH.equals(tmpPayFeeRulePo.getState())) {
            targetEndDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime());
            return targetEndDate;
        }

        Calendar preEndTimeCal = Calendar.getInstance();
        preEndTimeCal.setTime(DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime()));
        if (StringUtil.isNumber(feeConfigDto.getPrepaymentPeriod())) {
            preEndTimeCal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(feeConfigDto.getPrepaymentPeriod()) * -1);
        }
        Date preEndTime = preEndTimeCal.getTime();
        Date maxEndTime = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getMaxTime());

        Date billEndTime = DateUtil.getCurrentDate();
        //建账时间
        Date startDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getStartTime());
        //计费起始时间
        Date endDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime());
        //缴费周期
        long paymentCycle = Long.parseLong(feeConfigDto.getPaymentCycle());
        // 当前时间 - 开始时间  = 月份
        double mulMonth = 0.0;
        mulMonth = DateUtil.dayCompare(startDate, billEndTime);

        // 月份/ 周期 = 轮数（向上取整）
        double round = 0.0;
        if ("1200".equals(feeConfigDto.getPaymentCd())) { // 1200预付费
            round = Math.floor(mulMonth / paymentCycle) + 1;
        } else { //2100后付费
            round = Math.floor(mulMonth / paymentCycle);
        }
        // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
        targetEndDate = DateUtil.getTargetEndTime(round * paymentCycle, startDate);//目标结束时间

        //todo 如果 到了 预付期 产生下个周期的费用
        if (DateUtil.getFormatTimeStringB(targetEndDate).equals(DateUtil.getFormatTimeStringB(endDate))
                && DateUtil.getCurrentDate().getTime() > preEndTime.getTime()
        ) {
            targetEndDate = DateUtil.getTargetEndTime((round + 1) * paymentCycle, startDate);//目标结束时间
        }


        //todo 费用项的结束时间<缴费的结束时间  费用快结束了   取费用项的结束时间
        if (maxEndTime.getTime() < targetEndDate.getTime()) {
            targetEndDate = maxEndTime;
        }

        if (DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime()).getTime() > targetEndDate.getTime()) {
            targetEndDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime());
        }

        return targetEndDate;
    }
}
