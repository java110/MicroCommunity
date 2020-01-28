package com.java110.hardwareAdapation.listener.carBlackWhite;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.hardwareAdapation.dao.ICarBlackWhiteServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 黑白名单 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCarBlackWhiteBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractCarBlackWhiteBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ICarBlackWhiteServiceDao getCarBlackWhiteServiceDaoImpl();

    /**
     * 刷新 businessCarBlackWhiteInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCarBlackWhiteInfo
     */
    protected void flushBusinessCarBlackWhiteInfo(Map businessCarBlackWhiteInfo, String statusCd) {
        businessCarBlackWhiteInfo.put("newBId", businessCarBlackWhiteInfo.get("b_id"));
        businessCarBlackWhiteInfo.put("blackWhite", businessCarBlackWhiteInfo.get("black_white"));
        businessCarBlackWhiteInfo.put("operate", businessCarBlackWhiteInfo.get("operate"));
        businessCarBlackWhiteInfo.put("carNum", businessCarBlackWhiteInfo.get("car_num"));
        businessCarBlackWhiteInfo.put("startTime", businessCarBlackWhiteInfo.get("start_time"));
        businessCarBlackWhiteInfo.put("endTime", businessCarBlackWhiteInfo.get("end_time"));
        businessCarBlackWhiteInfo.put("communityId", businessCarBlackWhiteInfo.get("community_id"));
        businessCarBlackWhiteInfo.put("bwId", businessCarBlackWhiteInfo.get("bw_id"));
        businessCarBlackWhiteInfo.remove("bId");
        businessCarBlackWhiteInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCarBlackWhite 黑白名单信息
     */
    protected void autoSaveDelBusinessCarBlackWhite(Business business, JSONObject businessCarBlackWhite) {
//自动插入DEL
        Map info = new HashMap();
        info.put("bwId", businessCarBlackWhite.getString("bwId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCarBlackWhiteInfos = getCarBlackWhiteServiceDaoImpl().getCarBlackWhiteInfo(info);
        if (currentCarBlackWhiteInfos == null || currentCarBlackWhiteInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentCarBlackWhiteInfo = currentCarBlackWhiteInfos.get(0);

        currentCarBlackWhiteInfo.put("bId", business.getbId());

        currentCarBlackWhiteInfo.put("blackWhite", currentCarBlackWhiteInfo.get("black_white"));
        currentCarBlackWhiteInfo.put("operate", currentCarBlackWhiteInfo.get("operate"));
        currentCarBlackWhiteInfo.put("carNum", currentCarBlackWhiteInfo.get("car_num"));
        currentCarBlackWhiteInfo.put("startTime", currentCarBlackWhiteInfo.get("start_time"));
        currentCarBlackWhiteInfo.put("endTime", currentCarBlackWhiteInfo.get("end_time"));
        currentCarBlackWhiteInfo.put("communityId", currentCarBlackWhiteInfo.get("community_id"));
        currentCarBlackWhiteInfo.put("bwId", currentCarBlackWhiteInfo.get("bw_id"));


        currentCarBlackWhiteInfo.put("operate", StatusConstant.OPERATE_DEL);
        getCarBlackWhiteServiceDaoImpl().saveBusinessCarBlackWhiteInfo(currentCarBlackWhiteInfo);
    }


}
