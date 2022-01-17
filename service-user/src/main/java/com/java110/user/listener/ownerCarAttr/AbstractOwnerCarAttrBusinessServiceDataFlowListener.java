package com.java110.user.listener.ownerCarAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.user.dao.IOwnerCarAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业主车辆属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOwnerCarAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOwnerCarAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOwnerCarAttrServiceDao getOwnerCarAttrServiceDaoImpl();

    /**
     * 刷新 businessOwnerCarAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOwnerCarAttrInfo
     */
    protected void flushBusinessOwnerCarAttrInfo(Map businessOwnerCarAttrInfo, String statusCd) {
        businessOwnerCarAttrInfo.put("newBId", businessOwnerCarAttrInfo.get("b_id"));
        businessOwnerCarAttrInfo.put("attrId", businessOwnerCarAttrInfo.get("attr_id"));
        businessOwnerCarAttrInfo.put("operate", businessOwnerCarAttrInfo.get("operate"));
        businessOwnerCarAttrInfo.put("createTime", businessOwnerCarAttrInfo.get("create_time"));
        businessOwnerCarAttrInfo.put("specCd", businessOwnerCarAttrInfo.get("spec_cd"));
        businessOwnerCarAttrInfo.put("communityId", businessOwnerCarAttrInfo.get("community_id"));
        businessOwnerCarAttrInfo.put("value", businessOwnerCarAttrInfo.get("value"));
        businessOwnerCarAttrInfo.put("carId", businessOwnerCarAttrInfo.get("car_id"));
        businessOwnerCarAttrInfo.remove("bId");
        businessOwnerCarAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOwnerCarAttr 业主车辆属性信息
     */
    protected void autoSaveDelBusinessOwnerCarAttr(Business business, JSONObject businessOwnerCarAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessOwnerCarAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOwnerCarAttrInfos = getOwnerCarAttrServiceDaoImpl().getOwnerCarAttrInfo(info);
        if (currentOwnerCarAttrInfos == null || currentOwnerCarAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOwnerCarAttrInfo = currentOwnerCarAttrInfos.get(0);

        currentOwnerCarAttrInfo.put("bId", business.getbId());

        currentOwnerCarAttrInfo.put("attrId", currentOwnerCarAttrInfo.get("attr_id"));
        currentOwnerCarAttrInfo.put("operate", currentOwnerCarAttrInfo.get("operate"));
        currentOwnerCarAttrInfo.put("createTime", currentOwnerCarAttrInfo.get("create_time"));
        currentOwnerCarAttrInfo.put("specCd", currentOwnerCarAttrInfo.get("spec_cd"));
        currentOwnerCarAttrInfo.put("communityId", currentOwnerCarAttrInfo.get("community_id"));
        currentOwnerCarAttrInfo.put("value", currentOwnerCarAttrInfo.get("value"));
        currentOwnerCarAttrInfo.put("carId", currentOwnerCarAttrInfo.get("car_id"));


        currentOwnerCarAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOwnerCarAttrServiceDaoImpl().saveBusinessOwnerCarAttrInfo(currentOwnerCarAttrInfo);
        for (Object key : currentOwnerCarAttrInfo.keySet()) {
            if (businessOwnerCarAttr.get(key) == null) {
                businessOwnerCarAttr.put(key.toString(), currentOwnerCarAttrInfo.get(key));
            }
        }
    }


}
