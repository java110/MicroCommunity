package com.java110.community.listener.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IParkingSpaceServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 停车位 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractParkingSpaceBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractParkingSpaceBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IParkingSpaceServiceDao getParkingSpaceServiceDaoImpl();

    /**
     * 刷新 businessParkingSpaceInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessParkingSpaceInfo
     */
    protected void flushBusinessParkingSpaceInfo(Map businessParkingSpaceInfo, String statusCd) {
        businessParkingSpaceInfo.put("newBId", businessParkingSpaceInfo.get("b_id"));
        businessParkingSpaceInfo.put("area", businessParkingSpaceInfo.get("area"));
        businessParkingSpaceInfo.put("operate", businessParkingSpaceInfo.get("operate"));
        businessParkingSpaceInfo.put("typeCd", businessParkingSpaceInfo.get("type_cd"));
        businessParkingSpaceInfo.put("num", businessParkingSpaceInfo.get("num"));
        businessParkingSpaceInfo.put("psId", businessParkingSpaceInfo.get("ps_id"));
        businessParkingSpaceInfo.put("paId", businessParkingSpaceInfo.get("pa_id"));
        businessParkingSpaceInfo.put("remark", businessParkingSpaceInfo.get("remark"));
        businessParkingSpaceInfo.put("state", businessParkingSpaceInfo.get("state"));
        businessParkingSpaceInfo.put("communityId", businessParkingSpaceInfo.get("community_id"));
        businessParkingSpaceInfo.put("userId", businessParkingSpaceInfo.get("user_id"));
        businessParkingSpaceInfo.put("parkingType", businessParkingSpaceInfo.get("parking_type"));
        businessParkingSpaceInfo.remove("bId");
        businessParkingSpaceInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessParkingSpace 停车位信息
     */
    protected void autoSaveDelBusinessParkingSpace(Business business, JSONObject businessParkingSpace) {
//自动插入DEL
        Map info = new HashMap();
        info.put("psId", businessParkingSpace.getString("psId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentParkingSpaceInfos = getParkingSpaceServiceDaoImpl().getParkingSpaceInfo(info);
        if (currentParkingSpaceInfos == null || currentParkingSpaceInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentParkingSpaceInfo = currentParkingSpaceInfos.get(0);

        currentParkingSpaceInfo.put("bId", business.getbId());

        currentParkingSpaceInfo.put("area", currentParkingSpaceInfo.get("area"));
        currentParkingSpaceInfo.put("operate", currentParkingSpaceInfo.get("operate"));
        currentParkingSpaceInfo.put("typeCd", currentParkingSpaceInfo.get("type_cd"));
        currentParkingSpaceInfo.put("num", currentParkingSpaceInfo.get("num"));
        currentParkingSpaceInfo.put("psId", currentParkingSpaceInfo.get("ps_id"));
        currentParkingSpaceInfo.put("paId", currentParkingSpaceInfo.get("pa_id"));
        currentParkingSpaceInfo.put("remark", currentParkingSpaceInfo.get("remark"));
        currentParkingSpaceInfo.put("state", currentParkingSpaceInfo.get("state"));
        currentParkingSpaceInfo.put("communityId", currentParkingSpaceInfo.get("community_id"));
        currentParkingSpaceInfo.put("userId", currentParkingSpaceInfo.get("user_id"));
        currentParkingSpaceInfo.put("parkingType", currentParkingSpaceInfo.get("parking_type"));


        currentParkingSpaceInfo.put("operate", StatusConstant.OPERATE_DEL);
        getParkingSpaceServiceDaoImpl().saveBusinessParkingSpaceInfo(currentParkingSpaceInfo);

        for (Object key : currentParkingSpaceInfo.keySet()) {
            if (businessParkingSpace.get(key) == null) {
                businessParkingSpace.put(key.toString(), currentParkingSpaceInfo.get(key));
            }
        }
    }


}
