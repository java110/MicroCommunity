package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.store.dao.ISmallWechatAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractSmallWechatAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractSmallWechatAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ISmallWechatAttrServiceDao getSmallWechatAttrServiceDaoImpl();

    /**
     * 刷新 businessSmallWechatAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessSmallWechatAttrInfo
     */
    protected void flushBusinessSmallWechatAttrInfo(Map businessSmallWechatAttrInfo, String statusCd) {
        businessSmallWechatAttrInfo.put("newBId", businessSmallWechatAttrInfo.get("b_id"));
        businessSmallWechatAttrInfo.put("attrId", businessSmallWechatAttrInfo.get("attr_id"));
        businessSmallWechatAttrInfo.put("operate", businessSmallWechatAttrInfo.get("operate"));
        businessSmallWechatAttrInfo.put("wechatId", businessSmallWechatAttrInfo.get("wechat_id"));
        businessSmallWechatAttrInfo.put("specCd", businessSmallWechatAttrInfo.get("spec_cd"));
        businessSmallWechatAttrInfo.put("communityId", businessSmallWechatAttrInfo.get("community_id"));
        businessSmallWechatAttrInfo.put("value", businessSmallWechatAttrInfo.get("value"));
        businessSmallWechatAttrInfo.remove("bId");
        businessSmallWechatAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessSmallWechatAttr 微信属性信息
     */
    protected void autoSaveDelBusinessSmallWechatAttr(Business business, JSONObject businessSmallWechatAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessSmallWechatAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentSmallWechatAttrInfos = getSmallWechatAttrServiceDaoImpl().getSmallWechatAttrInfo(info);
        if (currentSmallWechatAttrInfos == null || currentSmallWechatAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentSmallWechatAttrInfo = currentSmallWechatAttrInfos.get(0);

        currentSmallWechatAttrInfo.put("bId", business.getbId());

        currentSmallWechatAttrInfo.put("attrId", currentSmallWechatAttrInfo.get("attr_id"));
        currentSmallWechatAttrInfo.put("operate", currentSmallWechatAttrInfo.get("operate"));
        currentSmallWechatAttrInfo.put("wechatId", currentSmallWechatAttrInfo.get("wechat_id"));
        currentSmallWechatAttrInfo.put("specCd", currentSmallWechatAttrInfo.get("spec_cd"));
        currentSmallWechatAttrInfo.put("communityId", currentSmallWechatAttrInfo.get("community_id"));
        currentSmallWechatAttrInfo.put("value", currentSmallWechatAttrInfo.get("value"));


        currentSmallWechatAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getSmallWechatAttrServiceDaoImpl().saveBusinessSmallWechatAttrInfo(currentSmallWechatAttrInfo);
        for (Object key : currentSmallWechatAttrInfo.keySet()) {
            if (businessSmallWechatAttr.get(key) == null) {
                businessSmallWechatAttr.put(key.toString(), currentSmallWechatAttrInfo.get(key));
            }
        }
    }


}
