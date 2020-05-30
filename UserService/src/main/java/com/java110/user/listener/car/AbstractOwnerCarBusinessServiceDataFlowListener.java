package com.java110.user.listener.car;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IOwnerCarServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆管理 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOwnerCarBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOwnerCarBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOwnerCarServiceDao getOwnerCarServiceDaoImpl();

    /**
     * 刷新 businessOwnerCarInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOwnerCarInfo
     */
    protected void flushBusinessOwnerCarInfo(Map businessOwnerCarInfo, String statusCd) {
        businessOwnerCarInfo.put("newBId", businessOwnerCarInfo.get("b_id"));
        businessOwnerCarInfo.put("carColor", businessOwnerCarInfo.get("car_color"));
        businessOwnerCarInfo.put("carBrand", businessOwnerCarInfo.get("car_brand"));
        businessOwnerCarInfo.put("carType", businessOwnerCarInfo.get("car_type"));
        businessOwnerCarInfo.put("operate", businessOwnerCarInfo.get("operate"));
        businessOwnerCarInfo.put("carNum", businessOwnerCarInfo.get("car_num"));
        businessOwnerCarInfo.put("psId", businessOwnerCarInfo.get("ps_id"));
        businessOwnerCarInfo.put("remark", businessOwnerCarInfo.get("remark"));
        businessOwnerCarInfo.put("ownerId", businessOwnerCarInfo.get("owner_id"));
        businessOwnerCarInfo.put("userId", businessOwnerCarInfo.get("user_id"));
        businessOwnerCarInfo.put("carId", businessOwnerCarInfo.get("car_id"));
        businessOwnerCarInfo.remove("bId");
        businessOwnerCarInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOwnerCar 车辆管理信息
     */
    protected void autoSaveDelBusinessOwnerCar(Business business, JSONObject businessOwnerCar) {
//自动插入DEL
        Map info = new HashMap();
        info.put("carId", businessOwnerCar.getString("carId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOwnerCarInfos = getOwnerCarServiceDaoImpl().getOwnerCarInfo(info);
        if (currentOwnerCarInfos == null || currentOwnerCarInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOwnerCarInfo = currentOwnerCarInfos.get(0);

        currentOwnerCarInfo.put("bId", business.getbId());

        currentOwnerCarInfo.put("carColor", currentOwnerCarInfo.get("car_color"));
        currentOwnerCarInfo.put("carBrand", currentOwnerCarInfo.get("car_brand"));
        currentOwnerCarInfo.put("carType", currentOwnerCarInfo.get("car_type"));
        currentOwnerCarInfo.put("operate", currentOwnerCarInfo.get("operate"));
        currentOwnerCarInfo.put("carNum", currentOwnerCarInfo.get("car_num"));
        currentOwnerCarInfo.put("psId", currentOwnerCarInfo.get("ps_id"));
        currentOwnerCarInfo.put("remark", currentOwnerCarInfo.get("remark"));
        currentOwnerCarInfo.put("ownerId", currentOwnerCarInfo.get("owner_id"));
        currentOwnerCarInfo.put("userId", currentOwnerCarInfo.get("user_id"));
        currentOwnerCarInfo.put("carId", currentOwnerCarInfo.get("car_id"));


        currentOwnerCarInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOwnerCarServiceDaoImpl().saveBusinessOwnerCarInfo(currentOwnerCarInfo);
        for (Object key : currentOwnerCarInfo.keySet()) {
            if (businessOwnerCar.get(key) == null) {
                businessOwnerCar.put(key.toString(), currentOwnerCarInfo.get(key));
            }
        }
    }


}
