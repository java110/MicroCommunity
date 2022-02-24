package com.java110.user.listener.ownerRoomRel;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IOwnerRoomRelServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业主房屋 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOwnerRoomRelBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOwnerRoomRelBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOwnerRoomRelServiceDao getOwnerRoomRelServiceDaoImpl();

    /**
     * 刷新 businessOwnerRoomRelInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOwnerRoomRelInfo
     */
    protected void flushBusinessOwnerRoomRelInfo(Map<Object,Object> businessOwnerRoomRelInfo, String statusCd) {
        businessOwnerRoomRelInfo.put("newBId", businessOwnerRoomRelInfo.get("b_id"));
        businessOwnerRoomRelInfo.put("relId", businessOwnerRoomRelInfo.get("rel_id"));
        businessOwnerRoomRelInfo.put("operate", businessOwnerRoomRelInfo.get("operate"));
        businessOwnerRoomRelInfo.put("remark", businessOwnerRoomRelInfo.get("remark"));
        businessOwnerRoomRelInfo.put("state", businessOwnerRoomRelInfo.get("state"));
        businessOwnerRoomRelInfo.put("ownerId", businessOwnerRoomRelInfo.get("owner_id"));
        businessOwnerRoomRelInfo.put("userId", businessOwnerRoomRelInfo.get("user_id"));
        businessOwnerRoomRelInfo.put("roomId", businessOwnerRoomRelInfo.get("room_id"));
        businessOwnerRoomRelInfo.put("startTime", businessOwnerRoomRelInfo.get("start_time"));
        businessOwnerRoomRelInfo.put("endTime", businessOwnerRoomRelInfo.get("end_time"));
        businessOwnerRoomRelInfo.remove("bId");
        businessOwnerRoomRelInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOwnerRoomRel 业主房屋信息
     */
    protected void autoSaveDelBusinessOwnerRoomRel(Business business, JSONObject businessOwnerRoomRel) {
//自动插入DEL
        Map<Object,Object> info = new HashMap<Object,Object>();
        info.put("relId", businessOwnerRoomRel.getString("relId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map<Object,Object>> currentOwnerRoomRelInfos = getOwnerRoomRelServiceDaoImpl().getOwnerRoomRelInfo(info);
        if (currentOwnerRoomRelInfos == null || currentOwnerRoomRelInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map<Object,Object> currentOwnerRoomRelInfo = currentOwnerRoomRelInfos.get(0);
        currentOwnerRoomRelInfo.put("bId", business.getbId());
        currentOwnerRoomRelInfo.put("relId", currentOwnerRoomRelInfo.get("rel_id"));
        currentOwnerRoomRelInfo.put("operate", currentOwnerRoomRelInfo.get("operate"));
        currentOwnerRoomRelInfo.put("remark", currentOwnerRoomRelInfo.get("remark"));
        currentOwnerRoomRelInfo.put("state", currentOwnerRoomRelInfo.get("state"));
        currentOwnerRoomRelInfo.put("ownerId", currentOwnerRoomRelInfo.get("owner_id"));
        currentOwnerRoomRelInfo.put("userId", currentOwnerRoomRelInfo.get("user_id"));
        currentOwnerRoomRelInfo.put("roomId", currentOwnerRoomRelInfo.get("room_id"));
        currentOwnerRoomRelInfo.put("startTime", currentOwnerRoomRelInfo.get("start_time"));
        currentOwnerRoomRelInfo.put("endTime", currentOwnerRoomRelInfo.get("end_time"));
        currentOwnerRoomRelInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOwnerRoomRelServiceDaoImpl().saveBusinessOwnerRoomRelInfo(currentOwnerRoomRelInfo);

        for (Object key : currentOwnerRoomRelInfo.keySet()) {
            if (businessOwnerRoomRel.get(key) == null) {
                businessOwnerRoomRel.put(key.toString(), currentOwnerRoomRelInfo.get(key));
            }
        }

    }


}
