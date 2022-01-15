package com.java110.community.listener.parkingArea;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.parking.ParkingAreaPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IParkingAreaServiceDao;
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
 * 保存 停车场信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveParkingAreaInfoListener")
@Transactional
public class SaveParkingAreaInfoListener extends AbstractParkingAreaBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveParkingAreaInfoListener.class);

    @Autowired
    private IParkingAreaServiceDao parkingAreaServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_AREA;
    }

    /**
     * 保存停车场信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessParkingArea 节点
        if(data.containsKey(ParkingAreaPo.class.getSimpleName())){
            Object bObj = data.get(ParkingAreaPo.class.getSimpleName());
            JSONArray businessParkingAreas = null;
            if(bObj instanceof JSONObject){
                businessParkingAreas = new JSONArray();
                businessParkingAreas.add(bObj);
            }else {
                businessParkingAreas = (JSONArray)bObj;
            }
            //JSONObject businessParkingArea = data.getJSONObject("businessParkingArea");
            for (int bParkingAreaIndex = 0; bParkingAreaIndex < businessParkingAreas.size();bParkingAreaIndex++) {
                JSONObject businessParkingArea = businessParkingAreas.getJSONObject(bParkingAreaIndex);
                doBusinessParkingArea(business, businessParkingArea);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("paId", businessParkingArea.getString("paId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //停车场信息
        List<Map> businessParkingAreaInfo = parkingAreaServiceDaoImpl.getBusinessParkingAreaInfo(info);
        if( businessParkingAreaInfo != null && businessParkingAreaInfo.size() >0) {
            reFreshShareColumn(info, businessParkingAreaInfo.get(0));
            parkingAreaServiceDaoImpl.saveParkingAreaInfoInstance(info);
            if(businessParkingAreaInfo.size() == 1) {
                dataFlowContext.addParamOut("paId", businessParkingAreaInfo.get(0).get("pa_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
    }
    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //停车场信息
        List<Map> parkingAreaInfo = parkingAreaServiceDaoImpl.getParkingAreaInfo(info);
        if(parkingAreaInfo != null && parkingAreaInfo.size() > 0){
            reFreshShareColumn(paramIn, parkingAreaInfo.get(0));
            parkingAreaServiceDaoImpl.updateParkingAreaInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessParkingArea 节点
     * @param business 总的数据节点
     * @param businessParkingArea 停车场节点
     */
    private void doBusinessParkingArea(Business business,JSONObject businessParkingArea){

        Assert.jsonObjectHaveKey(businessParkingArea,"paId","businessParkingArea 节点下没有包含 paId 节点");

        if(businessParkingArea.getString("paId").startsWith("-")){
            //刷新缓存
            //flushParkingAreaId(business.getDatas());

            businessParkingArea.put("paId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_paId));

        }

        businessParkingArea.put("bId",business.getbId());
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
