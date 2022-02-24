package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.ISmallWeChatServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序管理 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractSmallWeChatBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractSmallWeChatBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ISmallWeChatServiceDao getSmallWeChatServiceDaoImpl();

    /**
     * 刷新 businessSmallWeChatInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessSmallWeChatInfo
     */
    protected void flushBusinessSmallWeChatInfo(Map businessSmallWeChatInfo, String statusCd) {
        businessSmallWeChatInfo.put("newBId", businessSmallWeChatInfo.get("b_id"));
        businessSmallWeChatInfo.put("operate", businessSmallWeChatInfo.get("operate"));
        businessSmallWeChatInfo.put("createTime", businessSmallWeChatInfo.get("create_time"));
        businessSmallWeChatInfo.put("appId", businessSmallWeChatInfo.get("appId"));
        businessSmallWeChatInfo.put("name", businessSmallWeChatInfo.get("name"));
        businessSmallWeChatInfo.put("appSecret", businessSmallWeChatInfo.get("appSecret"));
        businessSmallWeChatInfo.put("weChatId", businessSmallWeChatInfo.get("wechat_id"));
        businessSmallWeChatInfo.put("storeId", businessSmallWeChatInfo.get("store_Id"));
        businessSmallWeChatInfo.put("payPassword", businessSmallWeChatInfo.get("pay_password"));
        businessSmallWeChatInfo.put("remarks", businessSmallWeChatInfo.get("remarks"));
        businessSmallWeChatInfo.put("objId", businessSmallWeChatInfo.get("obj_id"));
        businessSmallWeChatInfo.put("objType", businessSmallWeChatInfo.get("obj_type"));
        businessSmallWeChatInfo.put("mchId", businessSmallWeChatInfo.get("mch_id"));
        businessSmallWeChatInfo.remove("bId");
        businessSmallWeChatInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessSmallWeChat 小程序管理信息
     */
    protected void autoSaveDelBusinessSmallWeChat(Business business, JSONObject businessSmallWeChat) {
        Map info = new HashMap();
        info.put("weChatId", businessSmallWeChat.getString("weChatId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentSmallWeChatInfos = getSmallWeChatServiceDaoImpl().getSmallWeChatInfo(info);
        if (currentSmallWeChatInfos == null || currentSmallWeChatInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentSmallWeChatInfo = currentSmallWeChatInfos.get(0);
        currentSmallWeChatInfo.put("bId", business.getbId());
        currentSmallWeChatInfo.put("operate", currentSmallWeChatInfo.get("operate"));
        currentSmallWeChatInfo.put("createTime", currentSmallWeChatInfo.get("create_time"));
        currentSmallWeChatInfo.put("appId", currentSmallWeChatInfo.get("appId"));
        currentSmallWeChatInfo.put("name", currentSmallWeChatInfo.get("name"));
        currentSmallWeChatInfo.put("appSecret", currentSmallWeChatInfo.get("appSecret"));
        currentSmallWeChatInfo.put("weChatId", currentSmallWeChatInfo.get("wechat_id"));
        currentSmallWeChatInfo.put("storeId", currentSmallWeChatInfo.get("store_Id"));
        currentSmallWeChatInfo.put("payPassword", currentSmallWeChatInfo.get("pay_password"));
        currentSmallWeChatInfo.put("remarks", currentSmallWeChatInfo.get("remarks"));
        currentSmallWeChatInfo.put("objType", currentSmallWeChatInfo.get("obj_type"));
        currentSmallWeChatInfo.put("objId", currentSmallWeChatInfo.get("obj_id"));
        currentSmallWeChatInfo.put("mchId", currentSmallWeChatInfo.get("mch_id"));
        currentSmallWeChatInfo.put("operate", StatusConstant.OPERATE_DEL);
        getSmallWeChatServiceDaoImpl().saveBusinessSmallWeChatInfo(currentSmallWeChatInfo);
        for (Object key : currentSmallWeChatInfo.keySet()) {
            if (businessSmallWeChat.get(key) == null) {
                businessSmallWeChat.put(key.toString(), currentSmallWeChatInfo.get(key));
            }
        }
    }


}
