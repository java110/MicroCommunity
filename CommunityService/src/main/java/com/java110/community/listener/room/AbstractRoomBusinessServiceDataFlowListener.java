package com.java110.community.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.community.dao.IRoomServiceDao;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区房屋 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractRoomBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private  static Logger logger = LoggerFactory.getLogger(AbstractRoomBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IRoomServiceDao getRoomServiceDaoImpl();

    /**
     * 刷新 businessRoomInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessRoomInfo
     */
    protected void flushBusinessRoomInfo(Map businessRoomInfo, String statusCd) {
        businessRoomInfo.put("newBId", businessRoomInfo.get("b_id"));
        businessRoomInfo.put("unitPrice", businessRoomInfo.get("unit_price"));
        businessRoomInfo.put("section", businessRoomInfo.get("section"));
        businessRoomInfo.put("remark", businessRoomInfo.get("remark"));
        businessRoomInfo.put("userId", businessRoomInfo.get("user_id"));
        businessRoomInfo.put("roomId", businessRoomInfo.get("room_id"));
        businessRoomInfo.put("layer", businessRoomInfo.get("layer"));
        businessRoomInfo.put("builtUpArea", businessRoomInfo.get("built_up_area"));
        businessRoomInfo.put("operate", businessRoomInfo.get("operate"));
        businessRoomInfo.put("state", businessRoomInfo.get("state"));
        businessRoomInfo.put("roomNum", businessRoomInfo.get("room_num"));
        businessRoomInfo.put("unitId", businessRoomInfo.get("unit_id"));
        businessRoomInfo.put("apartment", businessRoomInfo.get("apartment"));
        businessRoomInfo.remove("bId");
        businessRoomInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessRoom 小区房屋信息
     */
    protected void autoSaveDelBusinessRoom(Business business, JSONObject businessRoom) {
//自动插入DEL
        Map info = new HashMap();
        info.put("roomId", businessRoom.getString("roomId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentRoomInfos = getRoomServiceDaoImpl().getRoomInfo(info);
        if (currentRoomInfos == null || currentRoomInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentRoomInfo = currentRoomInfos.get(0);

        currentRoomInfo.put("bId", business.getbId());

        currentRoomInfo.put("unitPrice", currentRoomInfo.get("unit_price"));
        currentRoomInfo.put("section", currentRoomInfo.get("section"));
        currentRoomInfo.put("remark", currentRoomInfo.get("remark"));
        currentRoomInfo.put("userId", currentRoomInfo.get("user_id"));
        currentRoomInfo.put("roomId", currentRoomInfo.get("room_id"));
        currentRoomInfo.put("layer", currentRoomInfo.get("layer"));
        currentRoomInfo.put("builtUpArea", currentRoomInfo.get("built_up_area"));
        currentRoomInfo.put("operate", currentRoomInfo.get("operate"));
        currentRoomInfo.put("state", currentRoomInfo.get("state"));
        currentRoomInfo.put("roomNum", currentRoomInfo.get("room_num"));
        currentRoomInfo.put("unitId", currentRoomInfo.get("unit_id"));
        currentRoomInfo.put("apartment", currentRoomInfo.get("apartment"));


        currentRoomInfo.put("operate", StatusConstant.OPERATE_DEL);
        getRoomServiceDaoImpl().saveBusinessRoomInfo(currentRoomInfo);
    }


}
