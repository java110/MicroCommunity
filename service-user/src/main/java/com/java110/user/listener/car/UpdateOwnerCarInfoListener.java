package com.java110.user.listener.car;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.car.OwnerCarPo;
import com.java110.user.dao.IOwnerCarServiceDao;
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
 * 修改车辆管理信息 侦听
 * <p>
 * 处理节点
 * 1、businessOwnerCar:{} 车辆管理基本信息节点
 * 2、businessOwnerCarAttr:[{}] 车辆管理属性信息节点
 * 3、businessOwnerCarPhoto:[{}] 车辆管理照片信息节点
 * 4、businessOwnerCarCerdentials:[{}] 车辆管理证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateOwnerCarInfoListener")
@Transactional
public class UpdateOwnerCarInfoListener extends AbstractOwnerCarBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateOwnerCarInfoListener.class);
    @Autowired
    private IOwnerCarServiceDao ownerCarServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR;
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


        //处理 businessOwnerCar 节点
        if (data.containsKey(OwnerCarPo.class.getSimpleName())) {
            Object _obj = data.get(OwnerCarPo.class.getSimpleName());
            JSONArray businessOwnerCars = null;
            if (_obj instanceof JSONObject) {
                businessOwnerCars = new JSONArray();
                businessOwnerCars.add(_obj);
            } else {
                businessOwnerCars = (JSONArray) _obj;
            }
            //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
            for (int _ownerCarIndex = 0; _ownerCarIndex < businessOwnerCars.size(); _ownerCarIndex++) {
                JSONObject businessOwnerCar = businessOwnerCars.getJSONObject(_ownerCarIndex);
                doBusinessOwnerCar(business, businessOwnerCar);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("carId", businessOwnerCar.getString("carId"));
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

        //车辆管理信息
        List<Map> businessOwnerCarInfos = ownerCarServiceDaoImpl.getBusinessOwnerCarInfo(info);
        if (businessOwnerCarInfos != null && businessOwnerCarInfos.size() > 0) {
            for (int _ownerCarIndex = 0; _ownerCarIndex < businessOwnerCarInfos.size(); _ownerCarIndex++) {
                Map businessOwnerCarInfo = businessOwnerCarInfos.get(_ownerCarIndex);
                flushBusinessOwnerCarInfo(businessOwnerCarInfo, StatusConstant.STATUS_CD_VALID);
                ownerCarServiceDaoImpl.updateOwnerCarInfoInstance(businessOwnerCarInfo);
                if (businessOwnerCarInfo.size() == 1) {
                    dataFlowContext.addParamOut("carId", businessOwnerCarInfo.get("car_id"));
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
        //车辆管理信息
        List<Map> ownerCarInfo = ownerCarServiceDaoImpl.getOwnerCarInfo(info);
        if (ownerCarInfo != null && ownerCarInfo.size() > 0) {

            //车辆管理信息
            List<Map> businessOwnerCarInfos = ownerCarServiceDaoImpl.getBusinessOwnerCarInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessOwnerCarInfos == null || businessOwnerCarInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（ownerCar），程序内部异常,请检查！ " + delInfo);
            }
            for (int _ownerCarIndex = 0; _ownerCarIndex < businessOwnerCarInfos.size(); _ownerCarIndex++) {
                Map businessOwnerCarInfo = businessOwnerCarInfos.get(_ownerCarIndex);
                flushBusinessOwnerCarInfo(businessOwnerCarInfo, StatusConstant.STATUS_CD_VALID);
                ownerCarServiceDaoImpl.updateOwnerCarInfoInstance(businessOwnerCarInfo);
            }
        }

    }


    /**
     * 处理 businessOwnerCar 节点
     *
     * @param business         总的数据节点
     * @param businessOwnerCar 车辆管理节点
     */
    private void doBusinessOwnerCar(Business business, JSONObject businessOwnerCar) {

        Assert.jsonObjectHaveKey(businessOwnerCar, "memberId", "businessOwnerCar 节点下没有包含 memberId 节点");

        if (businessOwnerCar.getString("memberId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "memberId错误，不能自动生成（必须已经存在的memberId）" + businessOwnerCar);
        }
        //自动保存DEL
        autoSaveDelBusinessOwnerCar(business, businessOwnerCar);

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
