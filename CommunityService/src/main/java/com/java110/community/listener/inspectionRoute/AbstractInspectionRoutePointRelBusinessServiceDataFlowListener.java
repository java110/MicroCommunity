package com.java110.community.listener.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionRoutePointRelServiceDao;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡检路线巡检点关系 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractInspectionRoutePointRelBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractInspectionRoutePointRelBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IInspectionRoutePointRelServiceDao getInspectionRoutePointRelServiceDaoImpl();

    /**
     * 刷新 businessInspectionRoutePointRelInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessInspectionRoutePointRelInfo
     */
    protected void flushBusinessInspectionRoutePointRelInfo(Map businessInspectionRoutePointRelInfo, String statusCd) {
        businessInspectionRoutePointRelInfo.put("newBId", businessInspectionRoutePointRelInfo.get("b_id"));
        businessInspectionRoutePointRelInfo.put("inspectionId", businessInspectionRoutePointRelInfo.get("inspection_id"));
        businessInspectionRoutePointRelInfo.put("operate", businessInspectionRoutePointRelInfo.get("operate"));
        businessInspectionRoutePointRelInfo.put("inspectionRouteId", businessInspectionRoutePointRelInfo.get("inspection_route_id"));
        businessInspectionRoutePointRelInfo.put("irpRelId", businessInspectionRoutePointRelInfo.get("irp_rel_id"));
        businessInspectionRoutePointRelInfo.put("remark", businessInspectionRoutePointRelInfo.get("remark"));
        businessInspectionRoutePointRelInfo.put("communityId", businessInspectionRoutePointRelInfo.get("community_id"));
        businessInspectionRoutePointRelInfo.remove("bId");
        businessInspectionRoutePointRelInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessInspectionRoutePointRel 巡检路线巡检点关系信息
     */
    protected void autoSaveDelBusinessInspectionRoutePointRel(Business business, JSONObject businessInspectionRoutePointRel) {
//自动插入DEL
        Map info = new HashMap();
        info.put("irpRelId", businessInspectionRoutePointRel.getString("irpRelId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentInspectionRoutePointRelInfos = getInspectionRoutePointRelServiceDaoImpl().getInspectionRoutePointRelInfo(info);
        if (currentInspectionRoutePointRelInfos == null || currentInspectionRoutePointRelInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentInspectionRoutePointRelInfo = currentInspectionRoutePointRelInfos.get(0);

        currentInspectionRoutePointRelInfo.put("bId", business.getbId());

        currentInspectionRoutePointRelInfo.put("inspectionId", currentInspectionRoutePointRelInfo.get("inspection_id"));
        currentInspectionRoutePointRelInfo.put("operate", currentInspectionRoutePointRelInfo.get("operate"));
        currentInspectionRoutePointRelInfo.put("inspectionRouteId", currentInspectionRoutePointRelInfo.get("inspection_route_id"));
        currentInspectionRoutePointRelInfo.put("irpRelId", currentInspectionRoutePointRelInfo.get("irp_rel_id"));
        currentInspectionRoutePointRelInfo.put("remark", currentInspectionRoutePointRelInfo.get("remark"));
        currentInspectionRoutePointRelInfo.put("communityId", currentInspectionRoutePointRelInfo.get("community_id"));


        currentInspectionRoutePointRelInfo.put("operate", StatusConstant.OPERATE_DEL);
        getInspectionRoutePointRelServiceDaoImpl().saveBusinessInspectionRoutePointRelInfo(currentInspectionRoutePointRelInfo);
        for (Object key : currentInspectionRoutePointRelInfo.keySet()) {
            if (businessInspectionRoutePointRel.get(key) == null) {
                businessInspectionRoutePointRel.put(key.toString(), currentInspectionRoutePointRelInfo.get(key));
            }
        }
    }


}
