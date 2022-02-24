package com.java110.user.listener.orgCommunity;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IOrgCommunityServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 隶属小区 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOrgCommunityBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOrgCommunityBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOrgCommunityServiceDao getOrgCommunityServiceDaoImpl();

    /**
     * 刷新 businessOrgCommunityInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOrgCommunityInfo
     */
    protected void flushBusinessOrgCommunityInfo(Map businessOrgCommunityInfo, String statusCd) {
        businessOrgCommunityInfo.put("newBId", businessOrgCommunityInfo.get("b_id"));
        businessOrgCommunityInfo.put("orgName", businessOrgCommunityInfo.get("org_name"));
        businessOrgCommunityInfo.put("operate", businessOrgCommunityInfo.get("operate"));
        businessOrgCommunityInfo.put("communityName", businessOrgCommunityInfo.get("community_name"));
        businessOrgCommunityInfo.put("communityId", businessOrgCommunityInfo.get("community_id"));
        businessOrgCommunityInfo.put("storeId", businessOrgCommunityInfo.get("store_id"));
        businessOrgCommunityInfo.put("orgId", businessOrgCommunityInfo.get("org_id"));
        businessOrgCommunityInfo.put("orgCommunityId", businessOrgCommunityInfo.get("org_community_id"));
        businessOrgCommunityInfo.remove("bId");
        businessOrgCommunityInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOrgCommunity 隶属小区信息
     */
    protected void autoSaveDelBusinessOrgCommunity(Business business, JSONObject businessOrgCommunity) {
//自动插入DEL
        Map info = new HashMap();
        info.put("orgCommunityId", businessOrgCommunity.getString("orgCommunityId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOrgCommunityInfos = getOrgCommunityServiceDaoImpl().getOrgCommunityInfo(info);
        if (currentOrgCommunityInfos == null || currentOrgCommunityInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOrgCommunityInfo = currentOrgCommunityInfos.get(0);

        currentOrgCommunityInfo.put("bId", business.getbId());

        currentOrgCommunityInfo.put("orgName", currentOrgCommunityInfo.get("org_name"));
        currentOrgCommunityInfo.put("operate", currentOrgCommunityInfo.get("operate"));
        currentOrgCommunityInfo.put("communityName", currentOrgCommunityInfo.get("community_name"));
        currentOrgCommunityInfo.put("communityId", currentOrgCommunityInfo.get("community_id"));
        currentOrgCommunityInfo.put("storeId", currentOrgCommunityInfo.get("store_id"));
        currentOrgCommunityInfo.put("orgId", currentOrgCommunityInfo.get("org_id"));
        currentOrgCommunityInfo.put("orgCommunityId", currentOrgCommunityInfo.get("org_community_id"));


        currentOrgCommunityInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOrgCommunityServiceDaoImpl().saveBusinessOrgCommunityInfo(currentOrgCommunityInfo);

        for (Object key : currentOrgCommunityInfo.keySet()) {
            if (businessOrgCommunity.get(key) == null) {
                businessOrgCommunity.put(key.toString(), currentOrgCommunityInfo.get(key));
            }
        }
    }


}
