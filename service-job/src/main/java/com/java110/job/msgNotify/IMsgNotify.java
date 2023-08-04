package com.java110.job.msgNotify;

import com.alibaba.fastjson.JSONObject;
import com.java110.vo.ResultVo;

/**
 * 消息通知 接口类
 */
public interface IMsgNotify {

    /**
     * 发送退费申请 消息
     *
     * @param userId
     * @param content
     * @return
     */
    ResultVo sendApplyReturnFeeMsg(String communityId, String userId, JSONObject content);

    /**
     * 发送欠费 账单信息
     *
     * @param communityId 小区
     * @param userId 用户
     * @param content {
     *                    "feeTypeName",
     *                     "payerObjName",
     *                     "billAmountOwed",
     *                     "date",
     *                url
     * }
     * @return
     */
    ResultVo sendOweFeeMsg(String communityId, String userId, JSONObject content);

    /**
     * 发送缴费成功提醒
     *
     * @param communityId 小区
     * @param userId 用户
     * @param content {
     *                    "payFeeRoom",
     *                     "feeTypeCdName",
     *                     "payFeeTime",
     *                     "receivedAmount",
     *                url
     * }
     */
    ResultVo sendPayFeeMsg(String communityId, String userId, JSONObject content,String role);
}
