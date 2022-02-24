
package com.java110.community.listener.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.room.RoomAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IRoomAttrServiceDao;
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
 * 保存 小区房屋属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveRoomAttrInfoListener")
@Transactional
public class SaveRoomAttrInfoListener extends AbstractRoomAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveRoomAttrInfoListener.class);

    @Autowired
    IRoomAttrServiceDao roomAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ROOM_INFO;
    }

    /**
     * 保存小区房屋属性信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessRoomAttr 节点
        if (data.containsKey(RoomAttrPo.class.getSimpleName())) {
            Object _obj = data.get(RoomAttrPo.class.getSimpleName());
            JSONArray businessRoomAttrs = null;
            if (_obj instanceof JSONObject) {
                businessRoomAttrs = new JSONArray();
                businessRoomAttrs.add(_obj);
            } else {
                businessRoomAttrs = (JSONArray) _obj;
            }
            //JSONObject businessRoomAttr = data.getJSONObject("businessRoomAttr");
            for (int _roomAttrIndex = 0; _roomAttrIndex < businessRoomAttrs.size(); _roomAttrIndex++) {
                JSONObject businessRoomAttr = businessRoomAttrs.getJSONObject(_roomAttrIndex);
                doBusinessRoomAttr(business, businessRoomAttr);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("roomAttrId", businessRoomAttr.getString("roomAttrId"));
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

        //小区房屋属性信息
        List<Map> businessRoomAttrInfo = roomAttrServiceDaoImpl.getBusinessRoomAttrInfo(info);
        if (businessRoomAttrInfo != null && businessRoomAttrInfo.size() > 0) {
            roomAttrServiceDaoImpl.saveRoomAttrInfoInstance(info);
            if (businessRoomAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("roomAttrId", businessRoomAttrInfo.get(0).get("roomAttr_id"));
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
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //小区房屋属性信息
        List<Map> roomAttrInfo = roomAttrServiceDaoImpl.getRoomAttrInfo(info);
        if (roomAttrInfo != null && roomAttrInfo.size() > 0) {
            roomAttrServiceDaoImpl.updateRoomAttrInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessRoomAttr 节点
     *
     * @param business         总的数据节点
     * @param businessRoomAttr 小区房屋属性节点
     */
    private void doBusinessRoomAttr(Business business, JSONObject businessRoomAttr) {

        Assert.jsonObjectHaveKey(businessRoomAttr, "attrId", "businessRoomAttr 节点下没有包含 attrId 节点");

        if (businessRoomAttr.getString("attrId").startsWith("-")) {
            //刷新缓存
            //flushRoomAttrId(business.getDatas());

            businessRoomAttr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomAttrId));

        }

        businessRoomAttr.put("bId", business.getbId());
        businessRoomAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区房屋属性信息
        roomAttrServiceDaoImpl.saveBusinessRoomAttrInfo(businessRoomAttr);

    }

    public IRoomAttrServiceDao getRoomAttrServiceDaoImpl() {
        return roomAttrServiceDaoImpl;
    }

    public void setRoomAttrServiceDaoImpl(IRoomAttrServiceDao roomAttrServiceDaoImpl) {
        this.roomAttrServiceDaoImpl = roomAttrServiceDaoImpl;
    }
}
