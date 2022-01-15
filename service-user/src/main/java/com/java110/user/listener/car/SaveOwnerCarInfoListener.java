package com.java110.user.listener.car;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.user.dao.IOwnerCarServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 车辆管理信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOwnerCarInfoListener")
@Transactional
public class SaveOwnerCarInfoListener extends AbstractOwnerCarBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerCarInfoListener.class);

    @Autowired
    private IOwnerCarServiceDao ownerCarServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR;
    }

    /**
     * 保存车辆管理信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOwnerCar 节点
        if (data.containsKey(OwnerCarPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerCarPo.class.getSimpleName());
            JSONArray businessOwnerCars = null;
            if (bObj instanceof JSONObject) {
                businessOwnerCars = new JSONArray();
                businessOwnerCars.add(bObj);
            } else {
                businessOwnerCars = (JSONArray) bObj;
            }
            //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
            for (int bOwnerCarIndex = 0; bOwnerCarIndex < businessOwnerCars.size(); bOwnerCarIndex++) {
                JSONObject businessOwnerCar = businessOwnerCars.getJSONObject(bOwnerCarIndex);
                doBusinessOwnerCar(business, businessOwnerCar);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("carId", businessOwnerCar.getString("carId"));
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

        //车辆管理信息
        List<Map> businessOwnerCarInfo = ownerCarServiceDaoImpl.getBusinessOwnerCarInfo(info);
        if (businessOwnerCarInfo != null && businessOwnerCarInfo.size() > 0) {
            reFreshShareColumn(info, businessOwnerCarInfo.get(0));
            ownerCarServiceDaoImpl.saveOwnerCarInfoInstance(info);
            if (businessOwnerCarInfo.size() == 1) {
                dataFlowContext.addParamOut("carId", businessOwnerCarInfo.get(0).get("car_id"));
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

        if (info.containsKey("ownerId")) {
            return;
        }

        if (!businessInfo.containsKey("owner_id")) {
            return;
        }

        info.put("ownerId", businessInfo.get("owner_id"));
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
        //车辆管理信息
        List<Map> ownerCarInfo = ownerCarServiceDaoImpl.getOwnerCarInfo(info);
        if (ownerCarInfo != null && ownerCarInfo.size() > 0) {
            reFreshShareColumn(paramIn, ownerCarInfo.get(0));
            ownerCarServiceDaoImpl.updateOwnerCarInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOwnerCar 节点
     *
     * @param business         总的数据节点
     * @param businessOwnerCar 车辆管理节点
     */
    private void doBusinessOwnerCar(Business business, JSONObject businessOwnerCar) {

        Assert.jsonObjectHaveKey(businessOwnerCar, "carId", "businessOwnerCar 节点下没有包含 carId 节点");

        if (businessOwnerCar.getString("carId").startsWith("-")) {
            //刷新缓存
            //flushOwnerCarId(business.getDatas());

            businessOwnerCar.put("carId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));

        }

        businessOwnerCar.put("bId", business.getbId());
        businessOwnerCar.put("operate", StatusConstant.OPERATE_ADD);
        //保存车辆管理信息
        ownerCarServiceDaoImpl.saveBusinessOwnerCarInfo(businessOwnerCar);

    }

    public IOwnerCarServiceDao getOwnerCarServiceDaoImpl() {
        return ownerCarServiceDaoImpl;
    }

    public void setOwnerCarServiceDaoImpl(IOwnerCarServiceDao ownerCarServiceDaoImpl) {
        this.ownerCarServiceDaoImpl = ownerCarServiceDaoImpl;
    }
}
