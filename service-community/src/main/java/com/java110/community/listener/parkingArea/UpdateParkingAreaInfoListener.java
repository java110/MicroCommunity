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
 * 修改停车场信息 侦听
 * <p>
 * 处理节点
 * 1、businessParkingArea:{} 停车场基本信息节点
 * 2、businessParkingAreaAttr:[{}] 停车场属性信息节点
 * 3、businessParkingAreaPhoto:[{}] 停车场照片信息节点
 * 4、businessParkingAreaCerdentials:[{}] 停车场证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateParkingAreaInfoListener")
@Transactional
public class UpdateParkingAreaInfoListener extends AbstractParkingAreaBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateParkingAreaInfoListener.class);
    @Autowired
    private IParkingAreaServiceDao parkingAreaServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_AREA;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
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

        //停车场信息
        List<Map> businessParkingAreaInfos = parkingAreaServiceDaoImpl.getBusinessParkingAreaInfo(info);
        if (businessParkingAreaInfos != null && businessParkingAreaInfos.size() > 0) {
            for (int _parkingAreaIndex = 0; _parkingAreaIndex < businessParkingAreaInfos.size(); _parkingAreaIndex++) {
                Map businessParkingAreaInfo = businessParkingAreaInfos.get(_parkingAreaIndex);
                flushBusinessParkingAreaInfo(businessParkingAreaInfo, StatusConstant.STATUS_CD_VALID);
                parkingAreaServiceDaoImpl.updateParkingAreaInfoInstance(businessParkingAreaInfo);
                if (businessParkingAreaInfo.size() == 1) {
                    dataFlowContext.addParamOut("paId", businessParkingAreaInfo.get("pa_id"));
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
        //自动保存DEL
        autoSaveDelBusinessParkingArea(business, businessParkingArea);

        businessParkingArea.put("bId", business.getbId());
        businessParkingArea.put("operate", StatusConstant.OPERATE_ADD);
        //保存停车场信息
        parkingAreaServiceDaoImpl.saveBusinessParkingAreaInfo(businessParkingArea);

    }


    public IParkingAreaServiceDao getParkingAreaServiceDaoImpl() {
        return parkingAreaServiceDaoImpl;
    }

    public void setParkingAreaServiceDaoImpl(IParkingAreaServiceDao parkingAreaServiceDaoImpl) {
        this.parkingAreaServiceDaoImpl = parkingAreaServiceDaoImpl;
    }


}
