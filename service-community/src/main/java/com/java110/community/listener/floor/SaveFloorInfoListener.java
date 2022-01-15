package com.java110.community.listener.floor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.floor.FloorPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IFloorServiceDao;
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
 * 保存 小区楼信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveFloorInfoListener")
@Transactional
public class SaveFloorInfoListener extends AbstractFloorBusinessServiceDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(SaveFloorInfoListener.class);

    @Autowired
    IFloorServiceDao floorServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_FLOOR_INFO;
    }

    /**
     * 保存小区楼信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessFloor 节点
        if(data.containsKey(FloorPo.class.getSimpleName())){
            Object _obj = data.get(FloorPo.class.getSimpleName());
            JSONArray businessFloors = null;
            if(_obj instanceof JSONObject){
                businessFloors = new JSONArray();
                businessFloors.add(_obj);
            }else {
                businessFloors = (JSONArray)_obj;
            }
            //JSONObject businessFloor = data.getJSONObject("businessFloor");
            for (int _floorIndex = 0; _floorIndex < businessFloors.size();_floorIndex++) {
                JSONObject businessFloor = businessFloors.getJSONObject(_floorIndex);
                doBusinessFloor(business, businessFloor);
                if(_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("floorId", businessFloor.getString("floorId"));
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

        //小区楼信息
        List<Map> businessFloorInfo = floorServiceDaoImpl.getBusinessFloorInfo(info);
        if( businessFloorInfo != null && businessFloorInfo.size() >0) {
            floorServiceDaoImpl.saveFloorInfoInstance(info);
            if(businessFloorInfo.size() == 1) {
                dataFlowContext.addParamOut("floorId", businessFloorInfo.get(0).get("floor_id"));
            }
        }
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
        //小区楼信息
        List<Map> floorInfo = floorServiceDaoImpl.getFloorInfo(info);
        if(floorInfo != null && floorInfo.size() > 0){
            floorServiceDaoImpl.updateFloorInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessFloor 节点
     * @param business 总的数据节点
     * @param businessFloor 小区楼节点
     */
    private void doBusinessFloor(Business business,JSONObject businessFloor){

        Assert.jsonObjectHaveKey(businessFloor,"floorId","businessFloor 节点下没有包含 floorId 节点");

        if(businessFloor.getString("floorId").startsWith("-")){
            //刷新缓存
            //flushFloorId(business.getDatas());

            businessFloor.put("floorId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_floorId));

        }

        businessFloor.put("bId",business.getbId());
        businessFloor.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区楼信息
        floorServiceDaoImpl.saveBusinessFloorInfo(businessFloor);

    }

    public IFloorServiceDao getFloorServiceDaoImpl() {
        return floorServiceDaoImpl;
    }

    public void setFloorServiceDaoImpl(IFloorServiceDao floorServiceDaoImpl) {
        this.floorServiceDaoImpl = floorServiceDaoImpl;
    }
}
