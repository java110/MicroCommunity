package com.java110.community.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRoomServiceDao;
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
 * 小区房屋 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractRoomBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractRoomBusinessServiceDataFlowListener.class);

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
        businessRoomInfo.put("feeCoefficient", businessRoomInfo.get("fee_coefficient"));
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
        businessRoomInfo.put("communityId", businessRoomInfo.get("community_id"));
        businessRoomInfo.put("roomType", businessRoomInfo.get("room_type"));
        businessRoomInfo.put("roomSubType", businessRoomInfo.get("room_sub_type"));
        businessRoomInfo.put("roomRent", businessRoomInfo.get("room_rent"));
        businessRoomInfo.put("roomArea", businessRoomInfo.get("room_area"));
        businessRoomInfo.remove("bId");
        businessRoomInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessRoom 小区房屋信息
     */
    protected Map<String, String> autoSaveDelBusinessRoom(Business business, JSONObject businessRoom) {
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

        currentRoomInfo.put("feeCoefficient", currentRoomInfo.get("fee_coefficient"));
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
        currentRoomInfo.put("communityId", currentRoomInfo.get("community_id"));
        currentRoomInfo.put("roomType", currentRoomInfo.get("room_type"));
        currentRoomInfo.put("roomSubType", currentRoomInfo.get("room_sub_type"));
        currentRoomInfo.put("roomRent", currentRoomInfo.get("room_rent"));
        currentRoomInfo.put("roomArea", currentRoomInfo.get("room_area"));

        currentRoomInfo.put("operate", StatusConstant.OPERATE_DEL);
        getRoomServiceDaoImpl().saveBusinessRoomInfo(currentRoomInfo);

        for (Object key : currentRoomInfo.keySet()) {
            if (businessRoom.get(key) == null) {
                businessRoom.put(key.toString(), currentRoomInfo.get(key));
            }
        }

        //便于更新数据
        return currentRoomInfo;
    }


}
