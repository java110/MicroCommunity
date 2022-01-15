package com.java110.community.listener.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRoomAttrServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.room.RoomAttrPo;
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
import java.util.List;
import java.util.Map;

/**
 * 修改小区房屋属性信息 侦听
 * <p>
 * 处理节点
 * 1、businessRoomAttr:{} 小区房屋属性基本信息节点
 * 2、businessRoomAttrAttr:[{}] 小区房屋属性属性信息节点
 * 3、businessRoomAttrPhoto:[{}] 小区房屋属性照片信息节点
 * 4、businessRoomAttrCerdentials:[{}] 小区房屋属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateRoomAttrInfoListener")
@Transactional
public class UpdateRoomAttrInfoListener extends AbstractRoomAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateRoomAttrInfoListener.class);
    @Autowired
    IRoomAttrServiceDao roomAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
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

        //小区房屋属性信息
        List<Map> businessRoomAttrInfos = roomAttrServiceDaoImpl.getBusinessRoomAttrInfo(info);
        if (businessRoomAttrInfos != null && businessRoomAttrInfos.size() > 0) {
            for (int _roomAttrIndex = 0; _roomAttrIndex < businessRoomAttrInfos.size(); _roomAttrIndex++) {
                Map businessRoomAttrInfo = businessRoomAttrInfos.get(_roomAttrIndex);
                flushBusinessRoomAttrInfo(businessRoomAttrInfo, StatusConstant.STATUS_CD_VALID);
                roomAttrServiceDaoImpl.updateRoomAttrInfoInstance(businessRoomAttrInfo);
                if (businessRoomAttrInfo.size() == 1) {
                    dataFlowContext.addParamOut("roomAttrId", businessRoomAttrInfo.get("roomAttr_id"));
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
        //小区房屋属性信息
        List<Map> roomAttrInfo = roomAttrServiceDaoImpl.getRoomAttrInfo(info);
        if (roomAttrInfo != null && roomAttrInfo.size() > 0) {

            //小区房屋属性信息
            List<Map> businessRoomAttrInfos = roomAttrServiceDaoImpl.getBusinessRoomAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessRoomAttrInfos == null || businessRoomAttrInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（roomAttr），程序内部异常,请检查！ " + delInfo);
            }
            for (int _roomAttrIndex = 0; _roomAttrIndex < businessRoomAttrInfos.size(); _roomAttrIndex++) {
                Map businessRoomAttrInfo = businessRoomAttrInfos.get(_roomAttrIndex);
                flushBusinessRoomAttrInfo(businessRoomAttrInfo, StatusConstant.STATUS_CD_VALID);
                roomAttrServiceDaoImpl.updateRoomAttrInfoInstance(businessRoomAttrInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "roomAttrId 错误，不能自动生成（必须已经存在的attrId）" + businessRoomAttr);
        }
        //自动保存DEL
        autoSaveDelBusinessRoomAttr(business, businessRoomAttr);

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
