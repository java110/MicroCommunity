package com.java110.community.listener.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.room.RoomAttrPo;
import com.java110.po.room.RoomPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IRoomServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 小区房屋信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveRoomInfoListener")
@Transactional
public class SaveRoomInfoListener extends AbstractRoomBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveRoomInfoListener.class);

    @Autowired
    IRoomServiceDao roomServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ROOM_INFO;
    }

    /**
     * 保存小区房屋信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessRoom 节点
        if (data.containsKey(RoomPo.class.getSimpleName())) {
            Object _obj = data.get(RoomPo.class.getSimpleName());
            JSONArray businessRooms = null;
            if (_obj instanceof JSONObject) {
                businessRooms = new JSONArray();
                businessRooms.add(_obj);
            } else {
                businessRooms = (JSONArray) _obj;
            }
            //JSONObject businessRoom = data.getJSONObject("businessRoom");
            for (int _roomIndex = 0; _roomIndex < businessRooms.size(); _roomIndex++) {
                JSONObject businessRoom = businessRooms.getJSONObject(_roomIndex);
                doBusinessRoom(business, businessRoom);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("roomId", businessRoom.getString("roomId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //小区房屋信息
        List<Map> businessRoomInfo = roomServiceDaoImpl.getBusinessRoomInfo(info);
        if (businessRoomInfo != null && businessRoomInfo.size() > 0) {
            reFresh(info, businessRoomInfo.get(0));
            roomServiceDaoImpl.saveRoomInfoInstance(info);
            if (businessRoomInfo.size() == 1) {
                dataFlowContext.addParamOut("roomId", businessRoomInfo.get(0).get("room_id"));
            }
        }
    }


    /**
     * 刷 roomId
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFresh(Map info, Map businessInfo) {

        if (info.containsKey("roomId")) {
            return;
        }

        if (!businessInfo.containsKey("room_id")) {
            return;
        }

        info.put("roomId", businessInfo.get("room_id"));
    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //小区房屋信息
        List<Map> roomInfo = roomServiceDaoImpl.getRoomInfo(info);
        if (roomInfo != null && roomInfo.size() > 0) {
            reFresh(paramIn, roomInfo.get(0));
            roomServiceDaoImpl.updateRoomInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessRoom 节点
     *
     * @param business     总的数据节点
     * @param businessRoom 小区房屋节点
     */
    private void doBusinessRoom(Business business, JSONObject businessRoom) {

        Assert.jsonObjectHaveKey(businessRoom, "roomId", "businessRoom 节点下没有包含 roomId 节点");

        if (businessRoom.getString("roomId").startsWith("-")) {
            //刷新缓存
            flushRoomId(business.getDatas(),businessRoom);

            //businessRoom.put("roomId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomId));

        }

        businessRoom.put("bId", business.getbId());
        businessRoom.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区房屋信息
        roomServiceDaoImpl.saveBusinessRoomInfo(businessRoom);

    }

    /**
     * 刷新 小区ID
     *
     * @param data 数据
     */
    private void flushRoomId(JSONObject data,JSONObject businessRoom) {

        String roomId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomId);
        //JSONObject businessRoom = data.getJSONObject(RoomPo.class.getSimpleName());
        businessRoom.put("roomId", roomId);
        //刷小区属性
        if (data.containsKey(RoomAttrPo.class.getSimpleName())) {
            JSONArray businessRoomAttrs = data.getJSONArray(RoomAttrPo.class.getSimpleName());
            for (int businessRoomAttrIndex = 0; businessRoomAttrIndex < businessRoomAttrs.size(); businessRoomAttrIndex++) {
                JSONObject businessRoomAttr = businessRoomAttrs.getJSONObject(businessRoomAttrIndex);
                businessRoomAttr.put("roomId", roomId);
            }
        }
    }

    public IRoomServiceDao getRoomServiceDaoImpl() {
        return roomServiceDaoImpl;
    }

    public void setRoomServiceDaoImpl(IRoomServiceDao roomServiceDaoImpl) {
        this.roomServiceDaoImpl = roomServiceDaoImpl;
    }
}
