package com.java110.fee.feeMonth;

/**
 * 费用离散为 月 应收实收数据
 * <p>
 * 主要是为了报表统计好统计 而设计的
 */
public interface IPayFeeMonth {

    /**
     * 单个费用生成或者刷新 离散月
     *
     * @param feeId
     * @param communityId
     */
    void doGeneratorOrRefreshFeeMonth(String feeId, String communityId);

    /**
     * 所有费用 生成月数据
     *
     * @param communityId
     */
    void doGeneratorOrRefreshAllFeeMonth(String communityId);

    /**
     * 缴费 离散数据
     * @param feeId
     * @param detailId
     * @param communityId
     */
    void payFeeDetailRefreshFeeMonth(String feeId,String detailId,String communityId);

    /**
     * 删除 费用离散月
     * @param feeId
     * @param communityId
     */
    void deleteFeeMonth(String feeId,String communityId);

}
