package com.java110.community.listener.parkingSpaceAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IParkingSpaceAttrServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车位属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractParkingSpaceAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractParkingSpaceAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IParkingSpaceAttrServiceDao getParkingSpaceAttrServiceDaoImpl();

    /**
     * 刷新 businessParkingSpaceAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessParkingSpaceAttrInfo
     */
    protected void flushBusinessParkingSpaceAttrInfo(Map businessParkingSpaceAttrInfo, String statusCd) {
        businessParkingSpaceAttrInfo.put("newBId", businessParkingSpaceAttrInfo.get("b_id"));
        businessParkingSpaceAttrInfo.put("attrId", businessParkingSpaceAttrInfo.get("attr_id"));
        businessParkingSpaceAttrInfo.put("operate", businessParkingSpaceAttrInfo.get("operate"));
        businessParkingSpaceAttrInfo.put("createTime", businessParkingSpaceAttrInfo.get("create_time"));
        businessParkingSpaceAttrInfo.put("psId", businessParkingSpaceAttrInfo.get("ps_id"));
        businessParkingSpaceAttrInfo.put("specCd", businessParkingSpaceAttrInfo.get("spec_cd"));
        businessParkingSpaceAttrInfo.put("communityId", businessParkingSpaceAttrInfo.get("community_id"));
        businessParkingSpaceAttrInfo.put("value", businessParkingSpaceAttrInfo.get("value"));
        businessParkingSpaceAttrInfo.remove("bId");
        businessParkingSpaceAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessParkingSpaceAttr 车位属性信息
     */
    protected void autoSaveDelBusinessParkingSpaceAttr(Business business, JSONObject businessParkingSpaceAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessParkingSpaceAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentParkingSpaceAttrInfos = getParkingSpaceAttrServiceDaoImpl().getParkingSpaceAttrInfo(info);
        if (currentParkingSpaceAttrInfos == null || currentParkingSpaceAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentParkingSpaceAttrInfo = currentParkingSpaceAttrInfos.get(0);

        currentParkingSpaceAttrInfo.put("bId", business.getbId());

        currentParkingSpaceAttrInfo.put("attrId", currentParkingSpaceAttrInfo.get("attr_id"));
        currentParkingSpaceAttrInfo.put("operate", currentParkingSpaceAttrInfo.get("operate"));
        currentParkingSpaceAttrInfo.put("createTime", currentParkingSpaceAttrInfo.get("create_time"));
        currentParkingSpaceAttrInfo.put("psId", currentParkingSpaceAttrInfo.get("ps_id"));
        currentParkingSpaceAttrInfo.put("specCd", currentParkingSpaceAttrInfo.get("spec_cd"));
        currentParkingSpaceAttrInfo.put("communityId", currentParkingSpaceAttrInfo.get("community_id"));
        currentParkingSpaceAttrInfo.put("value", currentParkingSpaceAttrInfo.get("value"));


        currentParkingSpaceAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getParkingSpaceAttrServiceDaoImpl().saveBusinessParkingSpaceAttrInfo(currentParkingSpaceAttrInfo);
        for (Object key : currentParkingSpaceAttrInfo.keySet()) {
            if (businessParkingSpaceAttr.get(key) == null) {
                businessParkingSpaceAttr.put(key.toString(), currentParkingSpaceAttrInfo.get(key));
            }
        }
    }


}
