package com.java110.user.listener.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IOrgServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOrgBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOrgBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOrgServiceDao getOrgServiceDaoImpl();

    /**
     * 刷新 businessOrgInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOrgInfo
     */
    protected void flushBusinessOrgInfo(Map businessOrgInfo, String statusCd) {
        businessOrgInfo.put("newBId", businessOrgInfo.get("b_id"));
        businessOrgInfo.put("orgName", businessOrgInfo.get("org_name"));
        businessOrgInfo.put("operate", businessOrgInfo.get("operate"));
        businessOrgInfo.put("parentOrgId", businessOrgInfo.get("parent_org_id"));
        businessOrgInfo.put("description", businessOrgInfo.get("description"));
        businessOrgInfo.put("orgLevel", businessOrgInfo.get("org_level"));
        businessOrgInfo.put("storeId", businessOrgInfo.get("store_id"));
        businessOrgInfo.put("orgId", businessOrgInfo.get("org_id"));
        businessOrgInfo.put("belongCommunityId", businessOrgInfo.get("belong_community_id"));
        businessOrgInfo.put("allowOperation", businessOrgInfo.get("allow_operation"));
        businessOrgInfo.remove("bId");
        businessOrgInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOrg 组织信息
     */
    protected void autoSaveDelBusinessOrg(Business business, JSONObject businessOrg) {
//自动插入DEL
        Map info = new HashMap();
        info.put("orgId", businessOrg.getString("orgId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOrgInfos = getOrgServiceDaoImpl().getOrgInfo(info);
        if (currentOrgInfos == null || currentOrgInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOrgInfo = currentOrgInfos.get(0);

        currentOrgInfo.put("bId", business.getbId());

        currentOrgInfo.put("orgName", currentOrgInfo.get("org_name"));
        currentOrgInfo.put("operate", currentOrgInfo.get("operate"));
        currentOrgInfo.put("parentOrgId", currentOrgInfo.get("parent_org_id"));
        currentOrgInfo.put("description", currentOrgInfo.get("description"));
        currentOrgInfo.put("orgLevel", currentOrgInfo.get("org_level"));
        currentOrgInfo.put("storeId", currentOrgInfo.get("store_id"));
        currentOrgInfo.put("orgId", currentOrgInfo.get("org_id"));
        currentOrgInfo.put("belongCommunityId", currentOrgInfo.get("belong_community_id"));
        currentOrgInfo.put("allowOperation", currentOrgInfo.get("allow_operation"));

        currentOrgInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOrgServiceDaoImpl().saveBusinessOrgInfo(currentOrgInfo);

        for (Object key : currentOrgInfo.keySet()) {
            if (businessOrg.get(key) == null) {
                businessOrg.put(key.toString(), currentOrgInfo.get(key));
            }
        }
    }


}
