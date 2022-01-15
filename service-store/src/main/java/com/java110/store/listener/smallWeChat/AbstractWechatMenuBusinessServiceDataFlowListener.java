package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.store.dao.IWechatMenuServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公众号菜单 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractWechatMenuBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractWechatMenuBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IWechatMenuServiceDao getWechatMenuServiceDaoImpl();

    /**
     * 刷新 businessWechatMenuInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessWechatMenuInfo
     */
    protected void flushBusinessWechatMenuInfo(Map businessWechatMenuInfo, String statusCd) {
        businessWechatMenuInfo.put("newBId", businessWechatMenuInfo.get("b_id"));
        businessWechatMenuInfo.put("pagepath", businessWechatMenuInfo.get("pagepath"));
        businessWechatMenuInfo.put("operate", businessWechatMenuInfo.get("operate"));
        businessWechatMenuInfo.put("appId", businessWechatMenuInfo.get("app_id"));
        businessWechatMenuInfo.put("menuLevel", businessWechatMenuInfo.get("menu_level"));
        businessWechatMenuInfo.put("menuName", businessWechatMenuInfo.get("menu_name"));
        businessWechatMenuInfo.put("menuType", businessWechatMenuInfo.get("menu_type"));
        businessWechatMenuInfo.put("menuValue", businessWechatMenuInfo.get("menu_value"));
        businessWechatMenuInfo.put("communityId", businessWechatMenuInfo.get("community_id"));
        businessWechatMenuInfo.put("wechatMenuId", businessWechatMenuInfo.get("wechat_menu_id"));
        businessWechatMenuInfo.put("parentMenuId", businessWechatMenuInfo.get("parent_menu_id"));
        businessWechatMenuInfo.put("seq", businessWechatMenuInfo.get("seq"));

        businessWechatMenuInfo.remove("bId");
        businessWechatMenuInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessWechatMenu 公众号菜单信息
     */
    protected void autoSaveDelBusinessWechatMenu(Business business, JSONObject businessWechatMenu) {
//自动插入DEL
        Map info = new HashMap();
        info.put("wechatMenuId", businessWechatMenu.getString("wechatMenuId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentWechatMenuInfos = getWechatMenuServiceDaoImpl().getWechatMenuInfo(info);
        if (currentWechatMenuInfos == null || currentWechatMenuInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentWechatMenuInfo = currentWechatMenuInfos.get(0);

        currentWechatMenuInfo.put("bId", business.getbId());

        currentWechatMenuInfo.put("pagepath", currentWechatMenuInfo.get("pagepath"));
        currentWechatMenuInfo.put("operate", currentWechatMenuInfo.get("operate"));
        currentWechatMenuInfo.put("appId", currentWechatMenuInfo.get("app_id"));
        currentWechatMenuInfo.put("menuLevel", currentWechatMenuInfo.get("menu_level"));
        currentWechatMenuInfo.put("menuName", currentWechatMenuInfo.get("menu_name"));
        currentWechatMenuInfo.put("menuType", currentWechatMenuInfo.get("menu_type"));
        currentWechatMenuInfo.put("menuValue", currentWechatMenuInfo.get("menu_value"));
        currentWechatMenuInfo.put("communityId", currentWechatMenuInfo.get("community_id"));
        currentWechatMenuInfo.put("wechatMenuId", currentWechatMenuInfo.get("wechat_menu_id"));

        currentWechatMenuInfo.put("parentMenuId", currentWechatMenuInfo.get("parent_menu_id"));
        currentWechatMenuInfo.put("seq", currentWechatMenuInfo.get("seq"));

        currentWechatMenuInfo.put("operate", StatusConstant.OPERATE_DEL);
        getWechatMenuServiceDaoImpl().saveBusinessWechatMenuInfo(currentWechatMenuInfo);
        for (Object key : currentWechatMenuInfo.keySet()) {
            if (businessWechatMenu.get(key) == null) {
                businessWechatMenu.put(key.toString(), currentWechatMenuInfo.get(key));
            }
        }
    }


}
