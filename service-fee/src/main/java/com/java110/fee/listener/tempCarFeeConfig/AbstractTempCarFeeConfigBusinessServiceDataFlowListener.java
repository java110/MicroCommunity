package com.java110.fee.listener.tempCarFeeConfig;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.fee.dao.ITempCarFeeConfigServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 临时车收费标准 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractTempCarFeeConfigBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractTempCarFeeConfigBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ITempCarFeeConfigServiceDao getTempCarFeeConfigServiceDaoImpl();

    /**
     * 刷新 businessTempCarFeeConfigInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessTempCarFeeConfigInfo
     */
    protected void flushBusinessTempCarFeeConfigInfo(Map businessTempCarFeeConfigInfo, String statusCd) {
        businessTempCarFeeConfigInfo.put("newBId", businessTempCarFeeConfigInfo.get("b_id"));
        businessTempCarFeeConfigInfo.put("carType", businessTempCarFeeConfigInfo.get("car_type"));
        businessTempCarFeeConfigInfo.put("operate", businessTempCarFeeConfigInfo.get("operate"));
        businessTempCarFeeConfigInfo.put("configId", businessTempCarFeeConfigInfo.get("config_id"));
        businessTempCarFeeConfigInfo.put("feeName", businessTempCarFeeConfigInfo.get("fee_name"));
        businessTempCarFeeConfigInfo.put("paId", businessTempCarFeeConfigInfo.get("pa_id"));
        businessTempCarFeeConfigInfo.put("areaNum", businessTempCarFeeConfigInfo.get("area_num"));
        businessTempCarFeeConfigInfo.put("startTime", businessTempCarFeeConfigInfo.get("start_time"));
        businessTempCarFeeConfigInfo.put("endTime", businessTempCarFeeConfigInfo.get("end_time"));
        businessTempCarFeeConfigInfo.put("ruleId", businessTempCarFeeConfigInfo.get("rule_id"));
        businessTempCarFeeConfigInfo.put("communityId", businessTempCarFeeConfigInfo.get("community_id"));
        businessTempCarFeeConfigInfo.put("feeConfigId", businessTempCarFeeConfigInfo.get("fee_config_id"));
        businessTempCarFeeConfigInfo.remove("bId");
        businessTempCarFeeConfigInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessTempCarFeeConfig 临时车收费标准信息
     */
    protected void autoSaveDelBusinessTempCarFeeConfig(Business business, JSONObject businessTempCarFeeConfig) {
//自动插入DEL
        Map info = new HashMap();
        info.put("configId", businessTempCarFeeConfig.getString("configId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentTempCarFeeConfigInfos = getTempCarFeeConfigServiceDaoImpl().getTempCarFeeConfigInfo(info);
        if (currentTempCarFeeConfigInfos == null || currentTempCarFeeConfigInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentTempCarFeeConfigInfo = currentTempCarFeeConfigInfos.get(0);

        currentTempCarFeeConfigInfo.put("bId", business.getbId());

        currentTempCarFeeConfigInfo.put("carType", currentTempCarFeeConfigInfo.get("car_type"));
        currentTempCarFeeConfigInfo.put("operate", currentTempCarFeeConfigInfo.get("operate"));
        currentTempCarFeeConfigInfo.put("configId", currentTempCarFeeConfigInfo.get("config_id"));
        currentTempCarFeeConfigInfo.put("feeName", currentTempCarFeeConfigInfo.get("fee_name"));
        currentTempCarFeeConfigInfo.put("paId", currentTempCarFeeConfigInfo.get("pa_id"));
        currentTempCarFeeConfigInfo.put("areaNum", currentTempCarFeeConfigInfo.get("area_num"));
        currentTempCarFeeConfigInfo.put("startTime", currentTempCarFeeConfigInfo.get("start_time"));
        currentTempCarFeeConfigInfo.put("endTime", currentTempCarFeeConfigInfo.get("end_time"));
        currentTempCarFeeConfigInfo.put("ruleId", currentTempCarFeeConfigInfo.get("rule_id"));
        currentTempCarFeeConfigInfo.put("communityId", currentTempCarFeeConfigInfo.get("community_id"));
        currentTempCarFeeConfigInfo.put("feeConfigId", currentTempCarFeeConfigInfo.get("fee_config_id"));


        currentTempCarFeeConfigInfo.put("operate", StatusConstant.OPERATE_DEL);
        getTempCarFeeConfigServiceDaoImpl().saveBusinessTempCarFeeConfigInfo(currentTempCarFeeConfigInfo);
        for (Object key : currentTempCarFeeConfigInfo.keySet()) {
            if (businessTempCarFeeConfig.get(key) == null) {
                businessTempCarFeeConfig.put(key.toString(), currentTempCarFeeConfigInfo.get(key));
            }
        }
    }


}
