package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairTypeUserServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修设置 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractRepairTypeUserBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractRepairTypeUserBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IRepairTypeUserServiceDao getRepairTypeUserServiceDaoImpl();

    /**
     * 刷新 businessRepairTypeUserInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessRepairTypeUserInfo
     */
    protected void flushBusinessRepairTypeUserInfo(Map businessRepairTypeUserInfo, String statusCd) {
        businessRepairTypeUserInfo.put("newBId", businessRepairTypeUserInfo.get("b_id"));
        businessRepairTypeUserInfo.put("operate", businessRepairTypeUserInfo.get("operate"));
        businessRepairTypeUserInfo.put("typeUserId", businessRepairTypeUserInfo.get("type_user_id"));
        businessRepairTypeUserInfo.put("repairType", businessRepairTypeUserInfo.get("repair_type"));
        businessRepairTypeUserInfo.put("remark", businessRepairTypeUserInfo.get("remark"));
        businessRepairTypeUserInfo.put("state", businessRepairTypeUserInfo.get("state"));
        businessRepairTypeUserInfo.put("staffName", businessRepairTypeUserInfo.get("staff_name"));
        businessRepairTypeUserInfo.put("communityId", businessRepairTypeUserInfo.get("community_id"));
        businessRepairTypeUserInfo.put("staffId", businessRepairTypeUserInfo.get("staff_id"));
        businessRepairTypeUserInfo.remove("bId");
        businessRepairTypeUserInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessRepairTypeUser 报修设置信息
     */
    protected void autoSaveDelBusinessRepairTypeUser(Business business, JSONObject businessRepairTypeUser) {
//自动插入DEL
        Map info = new HashMap();
        info.put("typeUserId", businessRepairTypeUser.getString("typeUserId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentRepairTypeUserInfos = getRepairTypeUserServiceDaoImpl().getRepairTypeUserInfo(info);
        if (currentRepairTypeUserInfos == null || currentRepairTypeUserInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentRepairTypeUserInfo = currentRepairTypeUserInfos.get(0);

        currentRepairTypeUserInfo.put("bId", business.getbId());

        currentRepairTypeUserInfo.put("operate", currentRepairTypeUserInfo.get("operate"));
        currentRepairTypeUserInfo.put("typeUserId", currentRepairTypeUserInfo.get("type_user_id"));
        currentRepairTypeUserInfo.put("repairType", currentRepairTypeUserInfo.get("repair_type"));
        currentRepairTypeUserInfo.put("remark", currentRepairTypeUserInfo.get("remark"));
        currentRepairTypeUserInfo.put("state", currentRepairTypeUserInfo.get("state"));
        currentRepairTypeUserInfo.put("staffName", currentRepairTypeUserInfo.get("staff_name"));
        currentRepairTypeUserInfo.put("communityId", currentRepairTypeUserInfo.get("community_id"));
        currentRepairTypeUserInfo.put("staffId", currentRepairTypeUserInfo.get("staff_id"));


        currentRepairTypeUserInfo.put("operate", StatusConstant.OPERATE_DEL);
        getRepairTypeUserServiceDaoImpl().saveBusinessRepairTypeUserInfo(currentRepairTypeUserInfo);
        for (Object key : currentRepairTypeUserInfo.keySet()) {
            if (businessRepairTypeUser.get(key) == null) {
                businessRepairTypeUser.put(key.toString(), currentRepairTypeUserInfo.get(key));
            }
        }
    }


}
