package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairUserServiceDao;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修派单 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractRepairUserBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractRepairUserBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IRepairUserServiceDao getRepairUserServiceDaoImpl();

    /**
     * 刷新 businessRepairUserInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessRepairUserInfo
     */
    protected void flushBusinessRepairUserInfo(Map businessRepairUserInfo, String statusCd) {
        businessRepairUserInfo.put("newBId", businessRepairUserInfo.get("b_id"));
        businessRepairUserInfo.put("operate", businessRepairUserInfo.get("operate"));
        businessRepairUserInfo.put("context", businessRepairUserInfo.get("context"));
        businessRepairUserInfo.put("repairId", businessRepairUserInfo.get("repair_id"));
        businessRepairUserInfo.put("ruId", businessRepairUserInfo.get("ru_id"));
        businessRepairUserInfo.put("state", businessRepairUserInfo.get("state"));
        businessRepairUserInfo.put("communityId", businessRepairUserInfo.get("community_id"));
        businessRepairUserInfo.put("userId", businessRepairUserInfo.get("user_id"));
        businessRepairUserInfo.remove("bId");
        businessRepairUserInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessRepairUser 报修派单信息
     */
    protected void autoSaveDelBusinessRepairUser(Business business, JSONObject businessRepairUser) {
//自动插入DEL
        Map info = new HashMap();
        info.put("ruId", businessRepairUser.getString("ruId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentRepairUserInfos = getRepairUserServiceDaoImpl().getRepairUserInfo(info);
        if (currentRepairUserInfos == null || currentRepairUserInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentRepairUserInfo = currentRepairUserInfos.get(0);

        currentRepairUserInfo.put("bId", business.getbId());

        currentRepairUserInfo.put("operate", currentRepairUserInfo.get("operate"));
        currentRepairUserInfo.put("context", currentRepairUserInfo.get("context"));
        currentRepairUserInfo.put("repairId", currentRepairUserInfo.get("repair_id"));
        currentRepairUserInfo.put("ruId", currentRepairUserInfo.get("ru_id"));
        currentRepairUserInfo.put("state", currentRepairUserInfo.get("state"));
        currentRepairUserInfo.put("communityId", currentRepairUserInfo.get("community_id"));
        currentRepairUserInfo.put("userId", currentRepairUserInfo.get("user_id"));


        currentRepairUserInfo.put("operate", StatusConstant.OPERATE_DEL);
        getRepairUserServiceDaoImpl().saveBusinessRepairUserInfo(currentRepairUserInfo);
    }


}
