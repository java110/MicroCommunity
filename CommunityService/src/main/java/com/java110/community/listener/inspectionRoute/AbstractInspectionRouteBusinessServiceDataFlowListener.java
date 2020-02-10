package com.java110.community.listener.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionRouteServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡检路线 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractInspectionRouteBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractInspectionRouteBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IInspectionRouteServiceDao getInspectionRouteServiceDaoImpl();

    /**
     * 刷新 businessInspectionRouteInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessInspectionRouteInfo
     */
    protected void flushBusinessInspectionRouteInfo(Map businessInspectionRouteInfo, String statusCd) {
        businessInspectionRouteInfo.put("newBId", businessInspectionRouteInfo.get("b_id"));
        businessInspectionRouteInfo.put("operate", businessInspectionRouteInfo.get("operate"));
        businessInspectionRouteInfo.put("inspectionRouteId", businessInspectionRouteInfo.get("inspection_routeId"));
        businessInspectionRouteInfo.put("checkQuantity", businessInspectionRouteInfo.get("check_quantity"));
        businessInspectionRouteInfo.put("machineQuantity", businessInspectionRouteInfo.get("machine_quantity"));
        businessInspectionRouteInfo.put("remark", businessInspectionRouteInfo.get("remark"));
        businessInspectionRouteInfo.put("communityId", businessInspectionRouteInfo.get("community_id"));
        businessInspectionRouteInfo.put("routeName", businessInspectionRouteInfo.get("Route_name"));
        businessInspectionRouteInfo.remove("bId");
        businessInspectionRouteInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessInspectionRoute 巡检路线信息
     */
    protected void autoSaveDelBusinessInspectionRoute(Business business, JSONObject businessInspectionRoute) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("inspectionRouteId", businessInspectionRoute.getString("inspectionRouteId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentInspectionRouteInfos = getInspectionRouteServiceDaoImpl().getInspectionRouteInfo(info);
        if (currentInspectionRouteInfos == null || currentInspectionRouteInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentInspectionRouteInfo = currentInspectionRouteInfos.get(0);

        currentInspectionRouteInfo.put("bId", business.getbId());

        currentInspectionRouteInfo.put("operate", currentInspectionRouteInfo.get("operate"));
        currentInspectionRouteInfo.put("inspectionRouteId", currentInspectionRouteInfo.get("inspection_routeId"));
        currentInspectionRouteInfo.put("checkQuantity", currentInspectionRouteInfo.get("check_quantity"));
        currentInspectionRouteInfo.put("machineQuantity", currentInspectionRouteInfo.get("machine_quantity"));
        currentInspectionRouteInfo.put("remark", currentInspectionRouteInfo.get("remark"));
        currentInspectionRouteInfo.put("communityId", currentInspectionRouteInfo.get("community_id"));
        currentInspectionRouteInfo.put("routeName", currentInspectionRouteInfo.get("Route_name"));


        currentInspectionRouteInfo.put("operate", StatusConstant.OPERATE_DEL);
        getInspectionRouteServiceDaoImpl().saveBusinessInspectionRouteInfo(currentInspectionRouteInfo);
    }


}
