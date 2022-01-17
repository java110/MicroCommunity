package com.java110.community.listener.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRoomServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.room.RoomPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 修改小区房屋信息 侦听
 * <p>
 * 处理节点
 * 1、businessRoom:{} 小区房屋基本信息节点
 * 2、businessRoomAttr:[{}] 小区房屋属性信息节点
 * 3、businessRoomPhoto:[{}] 小区房屋照片信息节点
 * 4、businessRoomCerdentials:[{}] 小区房屋证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateRoomInfoListener")
@Transactional
public class UpdateRoomInfoListener extends AbstractRoomBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateRoomInfoListener.class);
    @Autowired
    IRoomServiceDao roomServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ROOM_INFO;
    }

    /**
     * business过程
     *
     * @param dataFlowContext
     * @param business
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
     * business to instance 过程
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
        List<Map> businessRoomInfos = roomServiceDaoImpl.getBusinessRoomInfo(info);
        if (businessRoomInfos != null && businessRoomInfos.size() > 0) {
            for (int _roomIndex = 0; _roomIndex < businessRoomInfos.size(); _roomIndex++) {
                Map businessRoomInfo = businessRoomInfos.get(_roomIndex);
                flushBusinessRoomInfo(businessRoomInfo, StatusConstant.STATUS_CD_VALID);
                roomServiceDaoImpl.updateRoomInfoInstance(businessRoomInfo);
                if (businessRoomInfo.size() == 1) {
                    dataFlowContext.addParamOut("roomId", businessRoomInfo.get("room_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //小区房屋信息
        List<Map> roomInfo = roomServiceDaoImpl.getRoomInfo(info);
        if (roomInfo != null && roomInfo.size() > 0) {

            //小区房屋信息
            List<Map> businessRoomInfos = roomServiceDaoImpl.getBusinessRoomInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessRoomInfos == null || businessRoomInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（room），程序内部异常,请检查！ " + delInfo);
            }
            for (int _roomIndex = 0; _roomIndex < businessRoomInfos.size(); _roomIndex++) {
                Map businessRoomInfo = businessRoomInfos.get(_roomIndex);
                flushBusinessRoomInfo(businessRoomInfo, StatusConstant.STATUS_CD_VALID);
                roomServiceDaoImpl.updateRoomInfoInstance(businessRoomInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "roomId 错误，不能自动生成（必须已经存在的roomId）" + businessRoom);
        }
        //自动保存DEL
        Map<String, String> currentRoomInfo = autoSaveDelBusinessRoom(business, businessRoom);

        businessRoom.put("bId", business.getbId());
        businessRoom.put("operate", StatusConstant.OPERATE_ADD);
        Map<String, String> needInsert = new HashMap<>();
        //将不需要改变的数据写到businessRoom中
        for (Map.Entry<String, String> currentRoomInfoEntry : currentRoomInfo.entrySet()) {
            Iterator iter = businessRoom.entrySet().iterator();
            boolean writeFlag = true;
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                //key值比对
                if (currentRoomInfoEntry.getKey().equals(entry.getKey().toString())) {
                    writeFlag = false;
                    break;
                }
            }
            if (writeFlag) {
                needInsert.put(currentRoomInfoEntry.getKey(), currentRoomInfoEntry.getValue());
            }
        }
        //写入businessRoom
        for (Map.Entry<String, String> needInsertMap : needInsert.entrySet()) {
            businessRoom.put(needInsertMap.getKey(), needInsertMap.getValue());
        }

        //保存小区房屋信息
        roomServiceDaoImpl.saveBusinessRoomInfo(businessRoom);

    }


    public IRoomServiceDao getRoomServiceDaoImpl() {
        return roomServiceDaoImpl;
    }

    public void setRoomServiceDaoImpl(IRoomServiceDao roomServiceDaoImpl) {
        this.roomServiceDaoImpl = roomServiceDaoImpl;
    }


}
