package com.java110.community.listener.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IParkingAreaServiceDao;
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
 * 停车场 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractParkingAreaBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractParkingAreaBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IParkingAreaServiceDao getParkingAreaServiceDaoImpl();

    /**
     * 刷新 businessParkingAreaInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessParkingAreaInfo
     */
    protected void flushBusinessParkingAreaInfo(Map businessParkingAreaInfo, String statusCd) {
        businessParkingAreaInfo.put("newBId", businessParkingAreaInfo.get("b_id"));
        businessParkingAreaInfo.put("operate", businessParkingAreaInfo.get("operate"));
        businessParkingAreaInfo.put("typeCd", businessParkingAreaInfo.get("type_cd"));
        businessParkingAreaInfo.put("num", businessParkingAreaInfo.get("num"));
        businessParkingAreaInfo.put("paId", businessParkingAreaInfo.get("pa_id"));
        businessParkingAreaInfo.put("remark", businessParkingAreaInfo.get("remark"));
        businessParkingAreaInfo.put("communityId", businessParkingAreaInfo.get("community_id"));
        businessParkingAreaInfo.remove("bId");
        businessParkingAreaInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessParkingArea 停车场信息
     */
    protected void autoSaveDelBusinessParkingArea(Business business, JSONObject businessParkingArea) {
//自动插入DEL
        Map info = new HashMap();
        info.put("paId", businessParkingArea.getString("paId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentParkingAreaInfos = getParkingAreaServiceDaoImpl().getParkingAreaInfo(info);
        if (currentParkingAreaInfos == null || currentParkingAreaInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentParkingAreaInfo = currentParkingAreaInfos.get(0);

        currentParkingAreaInfo.put("bId", business.getbId());

        currentParkingAreaInfo.put("operate", currentParkingAreaInfo.get("operate"));
        currentParkingAreaInfo.put("typeCd", currentParkingAreaInfo.get("type_cd"));
        currentParkingAreaInfo.put("num", currentParkingAreaInfo.get("num"));
        currentParkingAreaInfo.put("paId", currentParkingAreaInfo.get("pa_id"));
        currentParkingAreaInfo.put("remark", currentParkingAreaInfo.get("remark"));
        currentParkingAreaInfo.put("communityId", currentParkingAreaInfo.get("community_id"));


        currentParkingAreaInfo.put("operate", StatusConstant.OPERATE_DEL);
        getParkingAreaServiceDaoImpl().saveBusinessParkingAreaInfo(currentParkingAreaInfo);

        for (Object key : currentParkingAreaInfo.keySet()) {
            if (businessParkingArea.get(key) == null) {
                businessParkingArea.put(key.toString(), currentParkingAreaInfo.get(key));
            }
        }
    }


}
