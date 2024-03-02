package com.java110.job.msgNotify;

import com.alibaba.fastjson.JSONObject;
import com.java110.vo.ResultVo;

import java.util.List;

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
     * @param contents [{
     *                    "feeTypeName",
     *                     "payerObjName",
     *                     "billAmountOwed",
     *                     "date",
     *                url
     * }]
     * @return
     */
    ResultVo sendOweFeeMsg(String communityId, String userId,String ownerId, List<JSONObject> contents);

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

    /**
     * 业主报修时
     * @param communityId 小区
     * @param userId 用户
     * @param content {
     *                repairId,
     *                repairTypeName，
     *                repairObjName，
     *                repairName，
     *                url
     * }
     * @return
     */
    ResultVo sendAddOwnerRepairMsg(String communityId, String userId, JSONObject content);

    /**
     * 派单给维修师傅
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairId,
     *                    repairName，
     *                    tel，
     *                    time，
     *                    address
     *                    }
     * @return
     */
    ResultVo sendDistributeRepairStaffMsg(String communityId, String userId, JSONObject content);

    /**
     * 派单给业主通知
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    name，
     *                    tel，
     *                    time，
     *                    url
     *                    }
     * @return
     */
    ResultVo sendDistributeRepairOwnerMsg(String communityId, String userId, JSONObject content);

    /**
     * 报修完成给业主通知
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairObjName，
     *                    staffName，
     *                    time，
     *                    url
     *                    }
     * @return
     */
    ResultVo sendFinishRepairOwnerMsg(String communityId, String userId, JSONObject content);

    /**
     * 退单给业主发送消息
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairTypeName，
     *                    repairObjName，
     *                    repairName，
     *                    url
     *                    }
     * @return
     */
    ResultVo sendReturnRepairMsg(String communityId, String userId, JSONObject content);

    /**
     *  oa 流程待审批通知
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    flowName，
     *                    create_user_name，
     *                    create_time，
     *                    url
     *                    }
     * @return
     */
    ResultVo sendOaDistributeMsg(String communityId, String userId, JSONObject content);

    /**
     *  oa 流程通知发起人
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    flowName，
     *                    staffName，
     *                    url
     *                    }
     * @return
     */
    ResultVo sendOaCreateStaffMsg(String communityId, String userId, JSONObject content);

    /**
     * 投诉通知 员工
     * @param communityId
     * @param userId
     * @param content
     * @return
     */
    ResultVo sendComplaintMsg(String communityId, String userId, JSONObject content);
}
