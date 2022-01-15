package com.java110.community.listener.floorAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IFloorAttrServiceDao;
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
 * 考勤班组属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFloorAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractFloorAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IFloorAttrServiceDao getFloorAttrServiceDaoImpl();

    /**
     * 刷新 businessFloorAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessFloorAttrInfo
     */
    protected void flushBusinessFloorAttrInfo(Map businessFloorAttrInfo, String statusCd) {
        businessFloorAttrInfo.put("newBId", businessFloorAttrInfo.get("b_id"));
        businessFloorAttrInfo.put("floorId", businessFloorAttrInfo.get("floor_id"));
        businessFloorAttrInfo.put("attrId", businessFloorAttrInfo.get("attr_id"));
        businessFloorAttrInfo.put("operate", businessFloorAttrInfo.get("operate"));
        businessFloorAttrInfo.put("specCd", businessFloorAttrInfo.get("spec_cd"));
        businessFloorAttrInfo.put("communityId", businessFloorAttrInfo.get("community_id"));
        businessFloorAttrInfo.put("value", businessFloorAttrInfo.get("value"));
        businessFloorAttrInfo.remove("bId");
        businessFloorAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessFloorAttr 考勤班组属性信息
     */
    protected void autoSaveDelBusinessFloorAttr(Business business, JSONObject businessFloorAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessFloorAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentFloorAttrInfos = getFloorAttrServiceDaoImpl().getFloorAttrInfo(info);
        if (currentFloorAttrInfos == null || currentFloorAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentFloorAttrInfo = currentFloorAttrInfos.get(0);

        currentFloorAttrInfo.put("bId", business.getbId());

        currentFloorAttrInfo.put("floorId", currentFloorAttrInfo.get("floor_id"));
        currentFloorAttrInfo.put("attrId", currentFloorAttrInfo.get("attr_id"));
        currentFloorAttrInfo.put("operate", currentFloorAttrInfo.get("operate"));
        currentFloorAttrInfo.put("specCd", currentFloorAttrInfo.get("spec_cd"));
        currentFloorAttrInfo.put("communityId", currentFloorAttrInfo.get("community_id"));
        currentFloorAttrInfo.put("value", currentFloorAttrInfo.get("value"));


        currentFloorAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getFloorAttrServiceDaoImpl().saveBusinessFloorAttrInfo(currentFloorAttrInfo);
        for (Object key : currentFloorAttrInfo.keySet()) {
            if (businessFloorAttr.get(key) == null) {
                businessFloorAttr.put(key.toString(), currentFloorAttrInfo.get(key));
            }
        }
    }


}
