package com.java110.community.listener.parkingArea;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IParkingAreaServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.parking.ParkingAreaPo;
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
 * 删除停车场信息 侦听
 * <p>
 * 处理节点
 * 1、businessParkingArea:{} 停车场基本信息节点
 * 2、businessParkingAreaAttr:[{}] 停车场属性信息节点
 * 3、businessParkingAreaPhoto:[{}] 停车场照片信息节点
 * 4、businessParkingAreaCerdentials:[{}] 停车场证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteParkingAreaInfoListener")
@Transactional
public class DeleteParkingAreaInfoListener extends AbstractParkingAreaBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteParkingAreaInfoListener.class);
    @Autowired
    IParkingAreaServiceDao parkingAreaServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_AREA;
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


        //处理 businessParkingArea 节点
        if (data.containsKey(ParkingAreaPo.class.getSimpleName())) {
            Object _obj = data.get(ParkingAreaPo.class.getSimpleName());
            JSONArray businessParkingAreas = null;
            if (_obj instanceof JSONObject) {
                businessParkingAreas = new JSONArray();
                businessParkingAreas.add(_obj);
            } else {
                businessParkingAreas = (JSONArray) _obj;
            }
            //JSONObject businessParkingArea = data.getJSONObject("businessParkingArea");
            for (int _parkingAreaIndex = 0; _parkingAreaIndex < businessParkingAreas.size(); _parkingAreaIndex++) {
                JSONObject businessParkingArea = businessParkingAreas.getJSONObject(_parkingAreaIndex);
                doBusinessParkingArea(business, businessParkingArea);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("paId", businessParkingArea.getString("paId"));
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

        //停车场信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //停车场信息
        List<Map> businessParkingAreaInfos = parkingAreaServiceDaoImpl.getBusinessParkingAreaInfo(info);
        if (businessParkingAreaInfos != null && businessParkingAreaInfos.size() > 0) {
            for (int _parkingAreaIndex = 0; _parkingAreaIndex < businessParkingAreaInfos.size(); _parkingAreaIndex++) {
                Map businessParkingAreaInfo = businessParkingAreaInfos.get(_parkingAreaIndex);
                flushBusinessParkingAreaInfo(businessParkingAreaInfo, StatusConstant.STATUS_CD_INVALID);
                parkingAreaServiceDaoImpl.updateParkingAreaInfoInstance(businessParkingAreaInfo);
                dataFlowContext.addParamOut("paId", businessParkingAreaInfo.get("pa_id"));
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
        //停车场信息
        List<Map> parkingAreaInfo = parkingAreaServiceDaoImpl.getParkingAreaInfo(info);
        if (parkingAreaInfo != null && parkingAreaInfo.size() > 0) {

            //停车场信息
            List<Map> businessParkingAreaInfos = parkingAreaServiceDaoImpl.getBusinessParkingAreaInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessParkingAreaInfos == null || businessParkingAreaInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（parkingArea），程序内部异常,请检查！ " + delInfo);
            }
            for (int _parkingAreaIndex = 0; _parkingAreaIndex < businessParkingAreaInfos.size(); _parkingAreaIndex++) {
                Map businessParkingAreaInfo = businessParkingAreaInfos.get(_parkingAreaIndex);
                flushBusinessParkingAreaInfo(businessParkingAreaInfo, StatusConstant.STATUS_CD_VALID);
                parkingAreaServiceDaoImpl.updateParkingAreaInfoInstance(businessParkingAreaInfo);
            }
        }
    }


    /**
     * 处理 businessParkingArea 节点
     *
     * @param business            总的数据节点
     * @param businessParkingArea 停车场节点
     */
    private void doBusinessParkingArea(Business business, JSONObject businessParkingArea) {

        Assert.jsonObjectHaveKey(businessParkingArea, "paId", "businessParkingArea 节点下没有包含 paId 节点");

        if (businessParkingArea.getString("paId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "paId 错误，不能自动生成（必须已经存在的paId）" + businessParkingArea);
        }
        //自动插入DEL
        autoSaveDelBusinessParkingArea(business, businessParkingArea);
    }

    public IParkingAreaServiceDao getParkingAreaServiceDaoImpl() {
        return parkingAreaServiceDaoImpl;
    }

    public void setParkingAreaServiceDaoImpl(IParkingAreaServiceDao parkingAreaServiceDaoImpl) {
        this.parkingAreaServiceDaoImpl = parkingAreaServiceDaoImpl;
    }
}
