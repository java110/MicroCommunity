package com.java110.fee.listener.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.fee.dao.IMeterWaterServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水电费 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMeterWaterBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMeterWaterBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMeterWaterServiceDao getMeterWaterServiceDaoImpl();

    /**
     * 刷新 businessMeterWaterInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMeterWaterInfo
     */
    protected void flushBusinessMeterWaterInfo(Map businessMeterWaterInfo, String statusCd) {
        businessMeterWaterInfo.put("newBId", businessMeterWaterInfo.get("b_id"));
        businessMeterWaterInfo.put("remark", businessMeterWaterInfo.get("remark"));
        businessMeterWaterInfo.put("curReadingTime", businessMeterWaterInfo.get("cur_reading_time"));
        businessMeterWaterInfo.put("waterId", businessMeterWaterInfo.get("water_id"));
        businessMeterWaterInfo.put("curDegrees", businessMeterWaterInfo.get("cur_degrees"));
        businessMeterWaterInfo.put("operate", businessMeterWaterInfo.get("operate"));
        businessMeterWaterInfo.put("createTime", businessMeterWaterInfo.get("create_time"));
        businessMeterWaterInfo.put("meterType", businessMeterWaterInfo.get("meter_type"));
        businessMeterWaterInfo.put("preDegrees", businessMeterWaterInfo.get("pre_degrees"));
        businessMeterWaterInfo.put("objId", businessMeterWaterInfo.get("obj_id"));
        businessMeterWaterInfo.put("preReadingTime", businessMeterWaterInfo.get("pre_reading_time"));
        businessMeterWaterInfo.put("communityId", businessMeterWaterInfo.get("community_id"));
        businessMeterWaterInfo.put("objType", businessMeterWaterInfo.get("obj_type"));
        businessMeterWaterInfo.put("feeId", businessMeterWaterInfo.get("fee_id"));
        businessMeterWaterInfo.put("objName", businessMeterWaterInfo.get("obj_name"));
        businessMeterWaterInfo.remove("bId");
        businessMeterWaterInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMeterWater 水电费信息
     */
    protected void autoSaveDelBusinessMeterWater(Business business, JSONObject businessMeterWater) {
//自动插入DEL
        Map info = new HashMap();
        info.put("waterId", businessMeterWater.getString("waterId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMeterWaterInfos = getMeterWaterServiceDaoImpl().getMeterWaterInfo(info);
        if (currentMeterWaterInfos == null || currentMeterWaterInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMeterWaterInfo = currentMeterWaterInfos.get(0);

        currentMeterWaterInfo.put("bId", business.getbId());

        currentMeterWaterInfo.put("remark", currentMeterWaterInfo.get("remark"));
        currentMeterWaterInfo.put("curReadingTime", currentMeterWaterInfo.get("cur_reading_time"));
        currentMeterWaterInfo.put("waterId", currentMeterWaterInfo.get("water_id"));
        currentMeterWaterInfo.put("curDegrees", currentMeterWaterInfo.get("cur_degrees"));
        currentMeterWaterInfo.put("operate", currentMeterWaterInfo.get("operate"));
        currentMeterWaterInfo.put("createTime", currentMeterWaterInfo.get("create_time"));
        currentMeterWaterInfo.put("meterType", currentMeterWaterInfo.get("meter_type"));
        currentMeterWaterInfo.put("preDegrees", currentMeterWaterInfo.get("pre_degrees"));
        currentMeterWaterInfo.put("objId", currentMeterWaterInfo.get("obj_id"));
        currentMeterWaterInfo.put("preReadingTime", currentMeterWaterInfo.get("pre_reading_time"));
        currentMeterWaterInfo.put("communityId", currentMeterWaterInfo.get("community_id"));
        currentMeterWaterInfo.put("objType", currentMeterWaterInfo.get("obj_type"));
        currentMeterWaterInfo.put("feeId", currentMeterWaterInfo.get("fee_id"));
        currentMeterWaterInfo.put("objName", currentMeterWaterInfo.get("obj_name"));


        currentMeterWaterInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMeterWaterServiceDaoImpl().saveBusinessMeterWaterInfo(currentMeterWaterInfo);
        for (Object key : currentMeterWaterInfo.keySet()) {
            if (businessMeterWater.get(key) == null) {
                businessMeterWater.put(key.toString(), currentMeterWaterInfo.get(key));
            }
        }
    }


}
