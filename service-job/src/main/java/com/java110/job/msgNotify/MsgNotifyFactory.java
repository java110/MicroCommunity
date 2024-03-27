package com.java110.job.msgNotify;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.log.LoggerFactory;
import com.java110.job.adapt.Repair.MachineAddOwnerRepairAdapt;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;

import java.util.List;

/**
 * 消息通知工具类
 */
public class MsgNotifyFactory {
    private static Logger logger = LoggerFactory.getLogger(MsgNotifyFactory.class);

    public static final String DEFAULT_MSG_NOTIFY_WAY = "DEFAULT_MSG_NOTIFY_WAY";

    public static final String NOTIFY_WAY_WECHAT = "WECHAT";
    public static final String NOTIFY_WAY_ALI = "ALI";
    public static final String NOTIFY_WAY_WORK_LICENSE = "WORK_LICENSE"; // todo 工牌

    public static final String NOTIFY_WAY_TENCENT = "TENCENT";
    public static final String ROLE_OWNER = "OWNER"; // 业主
    public static final String ROLE_STAFF = "STAFF"; // 员工


    /**
     * 发送退费申请 消息
     *
     * @param userId
     * @param content {
     *                detailId:'',
     *                name:''
     *                }
     * @return
     */
    public static ResultVo sendApplyReturnFeeMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendApplyReturnFeeMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 申请退费 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

    /**
     * 发送欠费 账单信息
     *
     * @param communityId 小区
     * @param userId      用户
     * @param contents     [{
     *                    "feeTypeName",
     *                    "payerObjName",
     *                    "billAmountOwed",
     *                    "date",
     *                    url
     *                    }]
     */
    public static ResultVo sendOweFeeMsg(String communityId, String userId, String ownerId, List<JSONObject> contents, String notifyWay) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify(notifyWay);
            resultVo = msgNotify.sendOweFeeMsg(communityId, userId,ownerId, contents);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 发送欠费 账单信息 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

    /**
     * 发送欠费 账单信息
     *
     * @param communityId 小区
     * @param userId      用户
     * @param contents     [{
     *                    "feeTypeName",
     *                    "payerObjName",
     *                    "billAmountOwed",
     *                    "date",
     *                    url
     *                    }]
     */
    public static ResultVo sendOweFeeMsg(String communityId, String userId,String ownerId, List<JSONObject> contents) {
        return sendOweFeeMsg(communityId,userId, ownerId,contents,null);
    }

    /**
     * 发送缴费成功提醒
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    "payFeeRoom",
     *                    "feeTypeCdName",
     *                    "payFeeTime",
     *                    "receivedAmount",
     *                    url
     *                    }
     */
    public static ResultVo sendPayFeeMsg(String communityId, String userId, JSONObject content, String role) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendPayFeeMsg(communityId, userId, content, role);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 发送缴费成功提醒 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

    /**
     * 业主报修时
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairId,
     *                    repairTypeName，
     *                    repairObjName，
     *                    repairName，
     *                    url
     *                    }
     * @return
     */
    public static ResultVo sendAddOwnerRepairMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendAddOwnerRepairMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 业主报修时 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

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
    public static ResultVo sendDistributeRepairStaffMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendDistributeRepairStaffMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 派单给维修师傅 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

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
    public static ResultVo sendDistributeRepairOwnerMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendDistributeRepairOwnerMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 派单给维修师傅 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

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
    public static ResultVo sendFinishRepairOwnerMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendFinishRepairOwnerMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 报修完成给业主通知 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }


    /**
     * 退单给业主发送消息
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    repairId,
     *                    repairTypeName，
     *                    repairObjName，
     *                    repairName，
     *                    url
     *                    }
     * @return
     */
    public static ResultVo sendReturnRepairMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendReturnRepairMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 业主报修时 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }


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
    public static ResultVo sendOaDistributeMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendOaDistributeMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 业主报修时 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

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
    public static ResultVo sendOaCreateStaffMsg(String communityId, String userId, JSONObject content) {
        ResultVo resultVo = null;
        try {
            IMsgNotify msgNotify = getMsgNotify();
            resultVo = msgNotify.sendOaCreateStaffMsg(communityId, userId, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("通知 业主报修时 消息", e);
            resultVo = new ResultVo(ResultVo.CODE_ERROR, e.getMessage());
        }

        return resultVo;
    }

    /**
     * 获取通知适配器
     *
     * @return
     */
    private static IMsgNotify getMsgNotify() {
        return getMsgNotify(null);
    }


    /**
     * 获取通知适配器
     *
     * @param notifyWay
     * @return
     */
    public static IMsgNotify getMsgNotify(String notifyWay) {
        IMsgNotify notify = null;
        if (StringUtil.isEmpty(notifyWay)) {
            notifyWay = MappingCache.getValue(MappingConstant.ENV_DOMAIN, DEFAULT_MSG_NOTIFY_WAY);
        }

        if (StringUtil.isEmpty(notifyWay)) {
            notifyWay = NOTIFY_WAY_WECHAT;
        }

        switch (notifyWay) {
            case NOTIFY_WAY_TENCENT:
                notify = ApplicationContextFactory.getBean("tencentMsgNotifyImpl", IMsgNotify.class);
                break;
            case NOTIFY_WAY_WECHAT:
                notify = ApplicationContextFactory.getBean("wechatMsgNotifyImpl", IMsgNotify.class);
                break;
            case NOTIFY_WAY_ALI:
                notify = ApplicationContextFactory.getBean("aliMsgNotifyImpl", IMsgNotify.class);
                break;
            case NOTIFY_WAY_WORK_LICENSE:
                notify = ApplicationContextFactory.getBean("workLicenseMsgNotifyImpl", IMsgNotify.class);
                break;
        }

        return notify;
    }


}
