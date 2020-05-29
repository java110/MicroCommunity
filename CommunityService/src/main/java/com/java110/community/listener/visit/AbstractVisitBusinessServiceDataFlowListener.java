package com.java110.community.listener.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.community.dao.IVisitServiceDao;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访客信息 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractVisitBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractVisitBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IVisitServiceDao getVisitServiceDaoImpl();

    /**
     * 刷新 businessVisitInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessVisitInfo
     */
    protected void flushBusinessVisitInfo(Map businessVisitInfo, String statusCd) {
        businessVisitInfo.put("newBId", businessVisitInfo.get("b_id"));
        businessVisitInfo.put("departureTime", businessVisitInfo.get("departure_time"));
        businessVisitInfo.put("vName", businessVisitInfo.get("v_name"));
        businessVisitInfo.put("visitGender", businessVisitInfo.get("visit_gender"));
        businessVisitInfo.put("ownerId", businessVisitInfo.get("owner_id"));
        businessVisitInfo.put("userId", businessVisitInfo.get("user_id"));
        businessVisitInfo.put("vId", businessVisitInfo.get("v_id"));
        businessVisitInfo.put("visitTime", businessVisitInfo.get("visit_time"));
        businessVisitInfo.put("phoneNumber", businessVisitInfo.get("phone_number"));
        businessVisitInfo.put("operate", businessVisitInfo.get("operate"));
        businessVisitInfo.put("visitCase", businessVisitInfo.get("visit_case"));
        businessVisitInfo.put("communityId", businessVisitInfo.get("community_id"));
        businessVisitInfo.remove("bId");
        businessVisitInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessVisit 访客信息信息
     */
    protected void autoSaveDelBusinessVisit(Business business, JSONObject businessVisit) {
//自动插入DEL
        Map info = new HashMap();
        info.put("vId", businessVisit.getString("vId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentVisitInfos = getVisitServiceDaoImpl().getVisitInfo(info);
        if (currentVisitInfos == null || currentVisitInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentVisitInfo = currentVisitInfos.get(0);

        currentVisitInfo.put("bId", business.getbId());

        currentVisitInfo.put("departureTime", currentVisitInfo.get("departure_time"));
        currentVisitInfo.put("vName", currentVisitInfo.get("v_name"));
        currentVisitInfo.put("visitGender", currentVisitInfo.get("visit_gender"));
        currentVisitInfo.put("ownerId", currentVisitInfo.get("owner_id"));
        currentVisitInfo.put("userId", currentVisitInfo.get("user_id"));
        currentVisitInfo.put("vId", currentVisitInfo.get("v_id"));
        currentVisitInfo.put("visitTime", currentVisitInfo.get("visit_time"));
        currentVisitInfo.put("phoneNumber", currentVisitInfo.get("phone_number"));
        currentVisitInfo.put("operate", currentVisitInfo.get("operate"));
        currentVisitInfo.put("visitCase", currentVisitInfo.get("visit_case"));
        currentVisitInfo.put("communityId", currentVisitInfo.get("community_id"));


        currentVisitInfo.put("operate", StatusConstant.OPERATE_DEL);
        getVisitServiceDaoImpl().saveBusinessVisitInfo(currentVisitInfo);

        for (Object key : currentVisitInfo.keySet()) {
            if (businessVisit.get(key) == null) {
                businessVisit.put(key.toString(), currentVisitInfo.get(key));
            }
        }
    }


}
