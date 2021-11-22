package com.java110.acct.smo;

import com.java110.vo.ResultVo;

/**
 * 扫码枪支付
 */
public interface IQrCodePaymentSMO {

    /**
     * 扫码支付
     *
     * @return
     */
    ResultVo pay(String communityId, String orderNum, double money, String authCode, String feeName) throws Exception;

    /**
     * 检查是否支付完成
     *
     * @param communityId
     * @param orderNum
     * @return
     */
    ResultVo checkPayFinish(String communityId, String orderNum);

}
