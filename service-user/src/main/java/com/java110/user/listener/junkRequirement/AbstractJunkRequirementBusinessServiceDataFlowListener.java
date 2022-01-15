package com.java110.user.listener.junkRequirement;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IJunkRequirementServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 旧货市场 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractJunkRequirementBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractJunkRequirementBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IJunkRequirementServiceDao getJunkRequirementServiceDaoImpl();

    /**
     * 刷新 businessJunkRequirementInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessJunkRequirementInfo
     */
    protected void flushBusinessJunkRequirementInfo(Map businessJunkRequirementInfo, String statusCd) {
        businessJunkRequirementInfo.put("newBId", businessJunkRequirementInfo.get("b_id"));
        businessJunkRequirementInfo.put("publishUserName", businessJunkRequirementInfo.get("publish_user_name"));
        businessJunkRequirementInfo.put("publishUserId", businessJunkRequirementInfo.get("publish_user_id"));
        businessJunkRequirementInfo.put("junkRequirementId", businessJunkRequirementInfo.get("junk_requirement_id"));
        businessJunkRequirementInfo.put("classification", businessJunkRequirementInfo.get("classification"));
        businessJunkRequirementInfo.put("referencePrice", businessJunkRequirementInfo.get("reference_price"));
        businessJunkRequirementInfo.put("operate", businessJunkRequirementInfo.get("operate"));
        businessJunkRequirementInfo.put("typeCd", businessJunkRequirementInfo.get("type_cd"));
        businessJunkRequirementInfo.put("publishUserLink", businessJunkRequirementInfo.get("publish_user_link"));
        businessJunkRequirementInfo.put("context", businessJunkRequirementInfo.get("context"));
        businessJunkRequirementInfo.put("state", businessJunkRequirementInfo.get("state"));
        businessJunkRequirementInfo.put("communityId", businessJunkRequirementInfo.get("community_id"));
        businessJunkRequirementInfo.remove("bId");
        businessJunkRequirementInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessJunkRequirement 旧货市场信息
     */
    protected void autoSaveDelBusinessJunkRequirement(Business business, JSONObject businessJunkRequirement) {
//自动插入DEL
        Map info = new HashMap();
        info.put("junkRequirementId", businessJunkRequirement.getString("junkRequirementId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentJunkRequirementInfos = getJunkRequirementServiceDaoImpl().getJunkRequirementInfo(info);
        if (currentJunkRequirementInfos == null || currentJunkRequirementInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentJunkRequirementInfo = currentJunkRequirementInfos.get(0);

        currentJunkRequirementInfo.put("bId", business.getbId());

        currentJunkRequirementInfo.put("publishUserName", currentJunkRequirementInfo.get("publish_user_name"));
        currentJunkRequirementInfo.put("publishUserId", currentJunkRequirementInfo.get("publish_user_id"));
        currentJunkRequirementInfo.put("junkRequirementId", currentJunkRequirementInfo.get("junk_requirement_id"));
        currentJunkRequirementInfo.put("classification", currentJunkRequirementInfo.get("classification"));
        currentJunkRequirementInfo.put("referencePrice", currentJunkRequirementInfo.get("reference_price"));
        currentJunkRequirementInfo.put("operate", currentJunkRequirementInfo.get("operate"));
        currentJunkRequirementInfo.put("typeCd", currentJunkRequirementInfo.get("type_cd"));
        currentJunkRequirementInfo.put("publishUserLink", currentJunkRequirementInfo.get("publish_user_link"));
        currentJunkRequirementInfo.put("context", currentJunkRequirementInfo.get("context"));
        currentJunkRequirementInfo.put("state", currentJunkRequirementInfo.get("state"));
        currentJunkRequirementInfo.put("communityId", currentJunkRequirementInfo.get("community_id"));


        currentJunkRequirementInfo.put("operate", StatusConstant.OPERATE_DEL);
        getJunkRequirementServiceDaoImpl().saveBusinessJunkRequirementInfo(currentJunkRequirementInfo);
        for (Object key : currentJunkRequirementInfo.keySet()) {
            if (businessJunkRequirement.get(key) == null) {
                businessJunkRequirement.put(key.toString(), currentJunkRequirementInfo.get(key));
            }
        }
    }


}
