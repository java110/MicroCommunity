package com.java110.fee.convertOnce;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.fee.dao.IPayFeeConfigV1ServiceDao;
import com.java110.fee.dao.impl.PayFeeV1ServiceDaoImpl;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeRule.PayFeeRulePo;
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
        return 0;
    }

    @Override
    public int covertRuleIdsPayFee(List<String> ruleIds) {
        return 0;
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
     * @param tmpPayFeeRulePo
     */
    public int ruleGeneratePayFee(PayFeeRulePo tmpPayFeeRulePo) {

        //todo 查询费用项信息
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(tmpPayFeeRulePo.getConfigId());
        feeConfigDto.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "费用项不存在，configId=" + tmpPayFeeRulePo.getConfigId());

        //todo 计算目标 结束时间
        Date targetEndTime = computeTargetEndTime(tmpPayFeeRulePo, feeConfigDtos.get(0));

        //todo 创建 pay_fee 和 attrs 数据

        List<PayFeePo> payFeePos = new ArrayList<>();
        for()


        return 0;
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
