package com.java110.community.listener.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.community.dao.IFloorServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区楼 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFloorBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private final static Logger logger = LoggerFactory.getLogger(AbstractFloorBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IFloorServiceDao getFloorServiceDaoImpl();

    /**
     * 刷新 businessFloorInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessFloorInfo
     */
    protected void flushBusinessFloorInfo(Map businessFloorInfo, String statusCd) {
        businessFloorInfo.put("newBId", businessFloorInfo.get("b_id"));
        businessFloorInfo.put("floorId", businessFloorInfo.get("floor_id"));
        businessFloorInfo.put("operate", businessFloorInfo.get("operate"));
        businessFloorInfo.put("name", businessFloorInfo.get("name"));
        businessFloorInfo.put("remark", businessFloorInfo.get("remark"));
        businessFloorInfo.put("userId", businessFloorInfo.get("user_id"));
        businessFloorInfo.put("floorNum", businessFloorInfo.get("floor_num"));
        businessFloorInfo.put("communityId", businessFloorInfo.get("community_id"));
        businessFloorInfo.put("floorArea", businessFloorInfo.get("floor_area"));

        businessFloorInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessFloor 小区楼信息
     */
    protected void autoSaveDelBusinessFloor(Business business, JSONObject businessFloor) {
//自动插入DEL
        Map info = new HashMap();
        info.put("floorId", businessFloor.getString("floorId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentFloorInfos = getFloorServiceDaoImpl().getFloorInfo(info);
        if (currentFloorInfos == null || currentFloorInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentFloorInfo = currentFloorInfos.get(0);

        currentFloorInfo.put("bId", business.getbId());

        currentFloorInfo.put("floorId", currentFloorInfo.get("floor_id"));
        currentFloorInfo.put("operate", currentFloorInfo.get("operate"));
        currentFloorInfo.put("name", currentFloorInfo.get("name"));
        currentFloorInfo.put("remark", currentFloorInfo.get("remark"));
        currentFloorInfo.put("userId", currentFloorInfo.get("user_id"));
        currentFloorInfo.put("floorNum", currentFloorInfo.get("floor_num"));
        currentFloorInfo.put("communityId", currentFloorInfo.get("community_id"));
        currentFloorInfo.put("floorArea", currentFloorInfo.get("floor_area"));


        currentFloorInfo.put("operate", StatusConstant.OPERATE_DEL);
        getFloorServiceDaoImpl().saveBusinessFloorInfo(currentFloorInfo);

        for(Object key : currentFloorInfo.keySet()) {
            if(businessFloor.get(key) == null) {
                businessFloor.put(key.toString(), currentFloorInfo.get(key));
            }
        }
    }


}
