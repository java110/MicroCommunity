package com.java110.user.listener.ownerAppUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IOwnerAppUserServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绑定业主 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOwnerAppUserBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOwnerAppUserBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOwnerAppUserServiceDao getOwnerAppUserServiceDaoImpl();

    /**
     * 刷新 businessOwnerAppUserInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOwnerAppUserInfo
     */
    protected void flushBusinessOwnerAppUserInfo(Map businessOwnerAppUserInfo, String statusCd) {
        businessOwnerAppUserInfo.put("newBId", businessOwnerAppUserInfo.get("b_id"));
        businessOwnerAppUserInfo.put("idCard", businessOwnerAppUserInfo.get("id_card"));
        businessOwnerAppUserInfo.put("openId", businessOwnerAppUserInfo.get("open_id"));
        businessOwnerAppUserInfo.put("link", businessOwnerAppUserInfo.get("link"));
        businessOwnerAppUserInfo.put("remark", businessOwnerAppUserInfo.get("remark"));
        businessOwnerAppUserInfo.put("operate", businessOwnerAppUserInfo.get("operate"));
        businessOwnerAppUserInfo.put("appUserName", businessOwnerAppUserInfo.get("app_user_name"));
        businessOwnerAppUserInfo.put("communityName", businessOwnerAppUserInfo.get("community_name"));
        businessOwnerAppUserInfo.put("state", businessOwnerAppUserInfo.get("state"));
        businessOwnerAppUserInfo.put("appUserId", businessOwnerAppUserInfo.get("app_user_id"));
        businessOwnerAppUserInfo.put("communityId", businessOwnerAppUserInfo.get("community_id"));
        businessOwnerAppUserInfo.put("appTypeCd", businessOwnerAppUserInfo.get("app_type_cd"));
        businessOwnerAppUserInfo.put("memberId", businessOwnerAppUserInfo.get("member_id"));
        businessOwnerAppUserInfo.put("userId", businessOwnerAppUserInfo.get("user_id"));
        businessOwnerAppUserInfo.put("appType", businessOwnerAppUserInfo.get("app_type"));
        businessOwnerAppUserInfo.put("nickName", businessOwnerAppUserInfo.get("nickname"));
        businessOwnerAppUserInfo.put("headImgUrl", businessOwnerAppUserInfo.get("headimgurl"));
        businessOwnerAppUserInfo.remove("bId");
        businessOwnerAppUserInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOwnerAppUser 绑定业主信息
     */
    protected void autoSaveDelBusinessOwnerAppUser(Business business, JSONObject businessOwnerAppUser) {
//自动插入DEL
        Map info = new HashMap();
        info.put("appUserId", businessOwnerAppUser.getString("appUserId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOwnerAppUserInfos = getOwnerAppUserServiceDaoImpl().getOwnerAppUserInfo(info);
        if (currentOwnerAppUserInfos == null || currentOwnerAppUserInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOwnerAppUserInfo = currentOwnerAppUserInfos.get(0);

        currentOwnerAppUserInfo.put("bId", business.getbId());

        currentOwnerAppUserInfo.put("idCard", currentOwnerAppUserInfo.get("id_card"));
        currentOwnerAppUserInfo.put("openId", currentOwnerAppUserInfo.get("open_id"));
        currentOwnerAppUserInfo.put("link", currentOwnerAppUserInfo.get("link"));
        currentOwnerAppUserInfo.put("remark", currentOwnerAppUserInfo.get("remark"));
        currentOwnerAppUserInfo.put("operate", currentOwnerAppUserInfo.get("operate"));
        currentOwnerAppUserInfo.put("appUserName", currentOwnerAppUserInfo.get("app_user_name"));
        currentOwnerAppUserInfo.put("communityName", currentOwnerAppUserInfo.get("community_name"));
        currentOwnerAppUserInfo.put("state", currentOwnerAppUserInfo.get("state"));
        currentOwnerAppUserInfo.put("appUserId", currentOwnerAppUserInfo.get("app_user_id"));
        currentOwnerAppUserInfo.put("communityId", currentOwnerAppUserInfo.get("community_id"));
        currentOwnerAppUserInfo.put("appTypeCd", currentOwnerAppUserInfo.get("app_type_cd"));
        currentOwnerAppUserInfo.put("memberId", currentOwnerAppUserInfo.get("member_id"));
        currentOwnerAppUserInfo.put("userId", currentOwnerAppUserInfo.get("user_id"));
        currentOwnerAppUserInfo.put("appType", currentOwnerAppUserInfo.get("app_type"));
        currentOwnerAppUserInfo.put("nickName", currentOwnerAppUserInfo.get("nickname"));
        currentOwnerAppUserInfo.put("headImgUrl", currentOwnerAppUserInfo.get("headimgurl"));


        currentOwnerAppUserInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOwnerAppUserServiceDaoImpl().saveBusinessOwnerAppUserInfo(currentOwnerAppUserInfo);

        for (Object key : currentOwnerAppUserInfo.keySet()) {
            if (businessOwnerAppUser.get(key) == null) {
                businessOwnerAppUser.put(key.toString(), currentOwnerAppUserInfo.get(key));
            }
        }
    }


}
