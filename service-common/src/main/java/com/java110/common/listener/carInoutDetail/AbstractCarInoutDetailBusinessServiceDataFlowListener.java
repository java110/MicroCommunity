package com.java110.common.listener.carInoutDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutDetailServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进出场详情 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCarInoutDetailBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractCarInoutDetailBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ICarInoutDetailServiceDao getCarInoutDetailServiceDaoImpl();

    /**
     * 刷新 businessCarInoutDetailInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCarInoutDetailInfo
     */
    protected void flushBusinessCarInoutDetailInfo(Map businessCarInoutDetailInfo, String statusCd) {
        businessCarInoutDetailInfo.put("newBId", businessCarInoutDetailInfo.get("b_id"));
        businessCarInoutDetailInfo.put("inoutId", businessCarInoutDetailInfo.get("inout_id"));
        businessCarInoutDetailInfo.put("machineId", businessCarInoutDetailInfo.get("machine_id"));
        businessCarInoutDetailInfo.put("machineCode", businessCarInoutDetailInfo.get("machine_code"));
        businessCarInoutDetailInfo.put("operate", businessCarInoutDetailInfo.get("operate"));
        businessCarInoutDetailInfo.put("carInout", businessCarInoutDetailInfo.get("car_inout"));
        businessCarInoutDetailInfo.put("detailId", businessCarInoutDetailInfo.get("detail_id"));
        businessCarInoutDetailInfo.put("carNum", businessCarInoutDetailInfo.get("car_num"));
        businessCarInoutDetailInfo.put("communityId", businessCarInoutDetailInfo.get("community_id"));
        businessCarInoutDetailInfo.remove("bId");
        businessCarInoutDetailInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCarInoutDetail 进出场详情信息
     */
    protected void autoSaveDelBusinessCarInoutDetail(Business business, JSONObject businessCarInoutDetail) {
//自动插入DEL
        Map info = new HashMap();
        info.put("detailId", businessCarInoutDetail.getString("detailId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCarInoutDetailInfos = getCarInoutDetailServiceDaoImpl().getCarInoutDetailInfo(info);
        if (currentCarInoutDetailInfos == null || currentCarInoutDetailInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentCarInoutDetailInfo = currentCarInoutDetailInfos.get(0);

        currentCarInoutDetailInfo.put("bId", business.getbId());

        currentCarInoutDetailInfo.put("inoutId", currentCarInoutDetailInfo.get("inout_id"));
        currentCarInoutDetailInfo.put("machineId", currentCarInoutDetailInfo.get("machine_id"));
        currentCarInoutDetailInfo.put("machineCode", currentCarInoutDetailInfo.get("machine_code"));
        currentCarInoutDetailInfo.put("operate", currentCarInoutDetailInfo.get("operate"));
        currentCarInoutDetailInfo.put("carInout", currentCarInoutDetailInfo.get("car_inout"));
        currentCarInoutDetailInfo.put("detailId", currentCarInoutDetailInfo.get("detail_id"));
        currentCarInoutDetailInfo.put("carNum", currentCarInoutDetailInfo.get("car_num"));
        currentCarInoutDetailInfo.put("communityId", currentCarInoutDetailInfo.get("community_id"));


        currentCarInoutDetailInfo.put("operate", StatusConstant.OPERATE_DEL);
        getCarInoutDetailServiceDaoImpl().saveBusinessCarInoutDetailInfo(currentCarInoutDetailInfo);

        for (Object key : currentCarInoutDetailInfo.keySet()) {
            if (businessCarInoutDetail.get(key) == null) {
                businessCarInoutDetail.put(key.toString(), currentCarInoutDetailInfo.get(key));
            }
        }
    }


}
