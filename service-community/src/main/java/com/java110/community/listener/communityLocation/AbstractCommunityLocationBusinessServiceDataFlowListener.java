package com.java110.community.listener.communityLocation;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityLocationServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区位置 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommunityLocationBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractCommunityLocationBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ICommunityLocationServiceDao getCommunityLocationServiceDaoImpl();

    /**
     * 刷新 businessCommunityLocationInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCommunityLocationInfo
     */
    protected void flushBusinessCommunityLocationInfo(Map businessCommunityLocationInfo, String statusCd) {
        businessCommunityLocationInfo.put("newBId", businessCommunityLocationInfo.get("b_id"));
        businessCommunityLocationInfo.put("locationName", businessCommunityLocationInfo.get("location_name"));
        businessCommunityLocationInfo.put("operate", businessCommunityLocationInfo.get("operate"));
        businessCommunityLocationInfo.put("locationId", businessCommunityLocationInfo.get("location_id"));
        businessCommunityLocationInfo.put("locationType", businessCommunityLocationInfo.get("location_type"));
        businessCommunityLocationInfo.put("communityId", businessCommunityLocationInfo.get("community_id"));
        businessCommunityLocationInfo.remove("bId");
        businessCommunityLocationInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCommunityLocation 小区位置信息
     */
    protected void autoSaveDelBusinessCommunityLocation(Business business, JSONObject businessCommunityLocation) {
//自动插入DEL
        Map info = new HashMap();
        info.put("locationId", businessCommunityLocation.getString("locationId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommunityLocationInfos = getCommunityLocationServiceDaoImpl().getCommunityLocationInfo(info);
        if (currentCommunityLocationInfos == null || currentCommunityLocationInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentCommunityLocationInfo = currentCommunityLocationInfos.get(0);

        currentCommunityLocationInfo.put("bId", business.getbId());

        currentCommunityLocationInfo.put("locationName", currentCommunityLocationInfo.get("location_name"));
        currentCommunityLocationInfo.put("operate", currentCommunityLocationInfo.get("operate"));
        currentCommunityLocationInfo.put("locationId", currentCommunityLocationInfo.get("location_id"));
        currentCommunityLocationInfo.put("locationType", currentCommunityLocationInfo.get("location_type"));
        currentCommunityLocationInfo.put("communityId", currentCommunityLocationInfo.get("community_id"));


        currentCommunityLocationInfo.put("operate", StatusConstant.OPERATE_DEL);
        getCommunityLocationServiceDaoImpl().saveBusinessCommunityLocationInfo(currentCommunityLocationInfo);
        for (Object key : currentCommunityLocationInfo.keySet()) {
            if (businessCommunityLocation.get(key) == null) {
                businessCommunityLocation.put(key.toString(), currentCommunityLocationInfo.get(key));
            }
        }
    }


}
