package com.java110.community.listener.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.community.dao.IUnitServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区单元 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractUnitBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractUnitBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IUnitServiceDao getUnitServiceDaoImpl();

    /**
     * 刷新 businessUnitInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessUnitInfo
     */
    protected void flushBusinessUnitInfo(Map businessUnitInfo, String statusCd) {
        businessUnitInfo.put("newBId", businessUnitInfo.get("b_id"));
        businessUnitInfo.put("floorId", businessUnitInfo.get("floor_id"));
        businessUnitInfo.put("operate", businessUnitInfo.get("operate"));
        businessUnitInfo.put("layerCount", businessUnitInfo.get("layer_count"));
        businessUnitInfo.put("unitId", businessUnitInfo.get("unit_id"));
        businessUnitInfo.put("unitNum", businessUnitInfo.get("unit_num"));
        businessUnitInfo.put("lift", businessUnitInfo.get("lift"));
        businessUnitInfo.put("remark", businessUnitInfo.get("remark"));
        businessUnitInfo.put("userId", businessUnitInfo.get("user_id"));
        businessUnitInfo.put("unitArea", businessUnitInfo.get("unit_area"));
        //删除bId
        businessUnitInfo.remove("bId");

        businessUnitInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessUnit 小区单元信息
     */
    protected void autoSaveDelBusinessUnit(Business business, JSONObject businessUnit) {
//自动插入DEL
        Map info = new HashMap();
        info.put("unitId", businessUnit.getString("unitId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentUnitInfos = getUnitServiceDaoImpl().getUnitInfo(info);
        if (currentUnitInfos == null || currentUnitInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentUnitInfo = currentUnitInfos.get(0);

        currentUnitInfo.put("bId", business.getbId());

        currentUnitInfo.put("floorId", currentUnitInfo.get("floor_id"));
        currentUnitInfo.put("operate", currentUnitInfo.get("operate"));
        currentUnitInfo.put("layerCount", currentUnitInfo.get("layer_count"));
        currentUnitInfo.put("unitId", currentUnitInfo.get("unit_id"));
        currentUnitInfo.put("unitNum", currentUnitInfo.get("unit_num"));
        currentUnitInfo.put("lift", currentUnitInfo.get("lift"));
        currentUnitInfo.put("remark", currentUnitInfo.get("remark"));
        currentUnitInfo.put("userId", currentUnitInfo.get("user_id"));
        currentUnitInfo.put("unitArea", currentUnitInfo.get("unit_area"));


        currentUnitInfo.put("operate", StatusConstant.OPERATE_DEL);
        getUnitServiceDaoImpl().saveBusinessUnitInfo(currentUnitInfo);

        for (Object key : currentUnitInfo.keySet()) {
            if (businessUnit.get(key) == null) {
                businessUnit.put(key.toString(), currentUnitInfo.get(key));
            }
        }
    }


}
