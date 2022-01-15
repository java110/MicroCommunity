package com.java110.common.listener.carInout;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutServiceDao;
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
 * 进出场 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCarInoutBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractCarInoutBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ICarInoutServiceDao getCarInoutServiceDaoImpl();

    /**
     * 刷新 businessCarInoutInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCarInoutInfo
     */
    protected void flushBusinessCarInoutInfo(Map businessCarInoutInfo, String statusCd) {
        businessCarInoutInfo.put("newBId", businessCarInoutInfo.get("b_id"));
        businessCarInoutInfo.put("inTime", businessCarInoutInfo.get("in_time"));
        businessCarInoutInfo.put("inoutId", businessCarInoutInfo.get("inout_id"));
        businessCarInoutInfo.put("operate", businessCarInoutInfo.get("operate"));
        businessCarInoutInfo.put("carNum", businessCarInoutInfo.get("car_num"));
        businessCarInoutInfo.put("state", businessCarInoutInfo.get("state"));
        businessCarInoutInfo.put("communityId", businessCarInoutInfo.get("community_id"));
        businessCarInoutInfo.put("outTime", businessCarInoutInfo.get("out_time"));
        businessCarInoutInfo.remove("bId");
        businessCarInoutInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCarInout 进出场信息
     */
    protected void autoSaveDelBusinessCarInout(Business business, JSONObject businessCarInout) {
//自动插入DEL
        Map info = new HashMap();
        info.put("inoutId", businessCarInout.getString("inoutId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCarInoutInfos = getCarInoutServiceDaoImpl().getCarInoutInfo(info);
        if (currentCarInoutInfos == null || currentCarInoutInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentCarInoutInfo = currentCarInoutInfos.get(0);

        currentCarInoutInfo.put("bId", business.getbId());

        currentCarInoutInfo.put("inTime", currentCarInoutInfo.get("in_time"));
        currentCarInoutInfo.put("inoutId", currentCarInoutInfo.get("inout_id"));
        currentCarInoutInfo.put("operate", currentCarInoutInfo.get("operate"));
        currentCarInoutInfo.put("carNum", currentCarInoutInfo.get("car_num"));
        currentCarInoutInfo.put("state", currentCarInoutInfo.get("state"));
        currentCarInoutInfo.put("communityId", currentCarInoutInfo.get("community_id"));
        currentCarInoutInfo.put("outTime", currentCarInoutInfo.get("out_time"));


        currentCarInoutInfo.put("operate", StatusConstant.OPERATE_DEL);
        getCarInoutServiceDaoImpl().saveBusinessCarInoutInfo(currentCarInoutInfo);

        for (Object key : currentCarInoutInfo.keySet()) {
            if (businessCarInout.get(key) == null) {
                businessCarInout.put(key.toString(), currentCarInoutInfo.get(key));
            }
        }
    }


}
