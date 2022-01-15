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
 * 删除小区房屋属性信息 侦听
 * <p>
 * 处理节点
 * 1、businessRoomAttr:{} 小区房屋属性基本信息节点
 * 2、businessRoomAttrAttr:[{}] 小区房屋属性属性信息节点
 * 3、businessRoomAttrPhoto:[{}] 小区房屋属性照片信息节点
 * 4、businessRoomAttrCerdentials:[{}] 小区房屋属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteRoomAttrInfoListener")
@Transactional
public class DeleteRoomAttrInfoListener extends AbstractRoomAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(DeleteRoomAttrInfoListener.class);
    @Autowired
    IRoomAttrServiceDao roomAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_ROOM_INFO;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
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
     * 删除 instance数据
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //小区房屋属性信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //小区房屋属性信息
        List<Map> businessRoomAttrInfos = roomAttrServiceDaoImpl.getBusinessRoomAttrInfo(info);
        if (businessRoomAttrInfos != null && businessRoomAttrInfos.size() > 0) {
            for (int _roomAttrIndex = 0; _roomAttrIndex < businessRoomAttrInfos.size(); _roomAttrIndex++) {
                Map businessRoomAttrInfo = businessRoomAttrInfos.get(_roomAttrIndex);
                flushBusinessRoomAttrInfo(businessRoomAttrInfo, StatusConstant.STATUS_CD_INVALID);
                roomAttrServiceDaoImpl.updateRoomAttrInfoInstance(businessRoomAttrInfo);
                dataFlowContext.addParamOut("roomAttrId", businessRoomAttrInfo.get("roomAttr_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

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

        Assert.jsonObjectHaveKey(businessRoomAttr, "roomAttrId", "businessRoomAttr 节点下没有包含 roomAttrId 节点");

        if (businessRoomAttr.getString("roomAttrId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "roomAttrId 错误，不能自动生成（必须已经存在的roomAttrId）" + businessRoomAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessRoomAttr(business, businessRoomAttr);
    }

    public IRoomAttrServiceDao getRoomAttrServiceDaoImpl() {
        return roomAttrServiceDaoImpl;
    }

    public void setRoomAttrServiceDaoImpl(IRoomAttrServiceDao roomAttrServiceDaoImpl) {
        this.roomAttrServiceDaoImpl = roomAttrServiceDaoImpl;
    }
}
