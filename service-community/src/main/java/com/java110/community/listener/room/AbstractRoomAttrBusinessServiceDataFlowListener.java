package com.java110.community.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.community.dao.IRoomAttrServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区房屋属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractRoomAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractRoomAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IRoomAttrServiceDao getRoomAttrServiceDaoImpl();

    /**
     * 刷新 businessRoomAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessRoomAttrInfo 房屋属性信息
     * @param statusCd             状态信息
     */
    protected void flushBusinessRoomAttrInfo(Map businessRoomAttrInfo, String statusCd) {
        businessRoomAttrInfo.put("newBId", businessRoomAttrInfo.get("b_id"));
        businessRoomAttrInfo.put("attrId", businessRoomAttrInfo.get("attr_id"));
        businessRoomAttrInfo.put("operate", businessRoomAttrInfo.get("operate"));
        businessRoomAttrInfo.put("specCd", businessRoomAttrInfo.get("spec_cd"));
        businessRoomAttrInfo.put("value", businessRoomAttrInfo.get("value"));
        businessRoomAttrInfo.put("roomId", businessRoomAttrInfo.get("room_id"));
        businessRoomAttrInfo.remove("bId");
        businessRoomAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessRoomAttr 小区房屋属性信息
     */
    protected void autoSaveDelBusinessRoomAttr(Business business, JSONObject businessRoomAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessRoomAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentRoomAttrInfos = getRoomAttrServiceDaoImpl().getRoomAttrInfo(info);
        if (currentRoomAttrInfos == null || currentRoomAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentRoomAttrInfo = currentRoomAttrInfos.get(0);

        currentRoomAttrInfo.put("bId", business.getbId());

        currentRoomAttrInfo.put("attrId", currentRoomAttrInfo.get("attr_id"));
        currentRoomAttrInfo.put("operate", currentRoomAttrInfo.get("operate"));
        currentRoomAttrInfo.put("specCd", currentRoomAttrInfo.get("spec_cd"));
        currentRoomAttrInfo.put("value", currentRoomAttrInfo.get("value"));
        currentRoomAttrInfo.put("roomId", currentRoomAttrInfo.get("room_id"));


        currentRoomAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getRoomAttrServiceDaoImpl().saveBusinessRoomAttrInfo(currentRoomAttrInfo);

        for (Object key : currentRoomAttrInfo.keySet()) {
            if (businessRoomAttr.get(key) == null) {
                businessRoomAttr.put(key.toString(), currentRoomAttrInfo.get(key));
            }
        }
    }


}
