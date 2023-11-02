package com.java110.fee.bill;

import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeRule.PayFeeRulePo;

import java.util.List;

/**
 * 周期性费用 间接费用转换 一次性费用处理
 */
public interface ICycleConvertOnceFee {

    /**
     * 批量将 周期性 间接性费用 转换一次性
     *
     * @param payFeePos 费用集合
     * @return 大于1 转换成功 0 转换失败
     */
    int convertPayFees(List<PayFeePo> payFeePos);


    /**
     * 将 周期性 间接性费用 转换一次性
     *
     * @param payFeePo 单个费用
     * @return 大于1 转换成功 0 转换失败
     */
    int convertPayFee(PayFeePo payFeePo);

    /**
     * 将小区里的费用批量转换为一次性费用
     *
     * @param communityId 小区ID
     * @return 大于1 转换成功 0 转换失败
     */
    int covertCommunityPayFee(String communityId);

    /**
     * 根据 费用规则转换
     *
     * @param ruleIds 费用规则
     * @return 大于1 转换成功 0 转换失败
     */
    int covertRuleIdsPayFee(List<String> ruleIds);

    /**
     * 根据规则生成 一次性费用
     *
     * @param payFeeRulePos
     * @return
     */
    int rulesGeneratePayFees(List<PayFeeRulePo> payFeeRulePos);

    /**
     * 根据规则生成 一次性费用
     *
     * @param payFeeRulePo
     * @return
     */
    int ruleGeneratePayFee(PayFeeRulePo payFeeRulePo);
}
