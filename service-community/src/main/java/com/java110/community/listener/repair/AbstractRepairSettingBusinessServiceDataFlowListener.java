package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairSettingServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修设置 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractRepairSettingBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractRepairSettingBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IRepairSettingServiceDao getRepairSettingServiceDaoImpl();

    /**
     * 刷新 businessRepairSettingInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessRepairSettingInfo
     */
    protected void flushBusinessRepairSettingInfo(Map businessRepairSettingInfo, String statusCd) {
        businessRepairSettingInfo.put("newBId", businessRepairSettingInfo.get("b_id"));
        businessRepairSettingInfo.put("operate", businessRepairSettingInfo.get("operate"));
        businessRepairSettingInfo.put("repairTypeName", businessRepairSettingInfo.get("repair_type_name"));
        businessRepairSettingInfo.put("repairType", businessRepairSettingInfo.get("repair_type"));
        businessRepairSettingInfo.put("remark", businessRepairSettingInfo.get("remark"));
        businessRepairSettingInfo.put("communityId", businessRepairSettingInfo.get("community_id"));
        businessRepairSettingInfo.put("repairWay", businessRepairSettingInfo.get("repair_way"));
        businessRepairSettingInfo.put("settingId", businessRepairSettingInfo.get("setting_id"));
        businessRepairSettingInfo.remove("bId");
        businessRepairSettingInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessRepairSetting 报修设置信息
     */
    protected void autoSaveDelBusinessRepairSetting(Business business, JSONObject businessRepairSetting) {
//自动插入DEL
        Map info = new HashMap();
        info.put("settingId", businessRepairSetting.getString("settingId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentRepairSettingInfos = getRepairSettingServiceDaoImpl().getRepairSettingInfo(info);
        if (currentRepairSettingInfos == null || currentRepairSettingInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentRepairSettingInfo = currentRepairSettingInfos.get(0);

        currentRepairSettingInfo.put("bId", business.getbId());

        currentRepairSettingInfo.put("operate", currentRepairSettingInfo.get("operate"));
        currentRepairSettingInfo.put("repairTypeName", currentRepairSettingInfo.get("repair_type_name"));
        currentRepairSettingInfo.put("repairType", currentRepairSettingInfo.get("repair_type"));
        currentRepairSettingInfo.put("remark", currentRepairSettingInfo.get("remark"));
        currentRepairSettingInfo.put("communityId", currentRepairSettingInfo.get("community_id"));
        currentRepairSettingInfo.put("repairWay", currentRepairSettingInfo.get("repair_way"));
        currentRepairSettingInfo.put("settingId", currentRepairSettingInfo.get("setting_id"));


        currentRepairSettingInfo.put("operate", StatusConstant.OPERATE_DEL);
        getRepairSettingServiceDaoImpl().saveBusinessRepairSettingInfo(currentRepairSettingInfo);
        for (Object key : currentRepairSettingInfo.keySet()) {
            if (businessRepairSetting.get(key) == null) {
                businessRepairSetting.put(key.toString(), currentRepairSettingInfo.get(key));
            }
        }
    }


}
