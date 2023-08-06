package com.java110.job.msgNotify;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;

/**
 * 消息通知工具类
 */
public class MsgNotifyFactory {

    public static final String DEFAULT_MSG_NOTIFY_WAY = "DEFAULT_MSG_NOTIFY_WAY";

    public static final String NOTIFY_WAY_WECHAT = "WECHAT";
    public static final String NOTIFY_WAY_ALI = "ALI";
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
        IMsgNotify msgNotify = getMsgNotify();
        return msgNotify.sendApplyReturnFeeMsg(communityId, userId, content);
    }

    /**
     * 发送欠费 账单信息
     *
     * @param communityId 小区
     * @param userId      用户
     * @param content     {
     *                    "feeTypeName",
     *                    "payerObjName",
     *                    "billAmountOwed",
     *                    "date",
     *                    url
     *                    }
     */
    public static ResultVo sendOweFeeMsg(String communityId, String userId, JSONObject content) {
        IMsgNotify msgNotify = getMsgNotify();
        return msgNotify.sendOweFeeMsg(communityId, userId, content);
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
        IMsgNotify msgNotify = getMsgNotify();
        return msgNotify.sendPayFeeMsg(communityId, userId, content, role);
    }

    /**
     * 业主报修时
     * @param communityId 小区
     * @param userId 用户
     * @param content {
     *                repairTypeName，
     *                repairObjName，
     *                repairName，
     *                url
     * }
     * @return
     */
    public static ResultVo sendAddOwnerRepairMsg(String communityId,String userId,JSONObject content){
        IMsgNotify msgNotify = getMsgNotify();
        return msgNotify.sendAddOwnerRepairMsg(communityId, userId, content);
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
    private static IMsgNotify getMsgNotify(String notifyWay) {
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
        }

        return notify;
    }
}
