package com.java110.user.listener.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IOrgStaffRelServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织员工关系 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOrgStaffRelBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOrgStaffRelBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOrgStaffRelServiceDao getOrgStaffRelServiceDaoImpl();

    /**
     * 刷新 businessOrgStaffRelInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOrgStaffRelInfo
     */
    protected void flushBusinessOrgStaffRelInfo(Map businessOrgStaffRelInfo, String statusCd) {
        businessOrgStaffRelInfo.put("newBId", businessOrgStaffRelInfo.get("b_id"));
        businessOrgStaffRelInfo.put("relId", businessOrgStaffRelInfo.get("rel_id"));
        businessOrgStaffRelInfo.put("operate", businessOrgStaffRelInfo.get("operate"));
        businessOrgStaffRelInfo.put("storeId", businessOrgStaffRelInfo.get("store_id"));
        businessOrgStaffRelInfo.put("orgId", businessOrgStaffRelInfo.get("org_id"));
        businessOrgStaffRelInfo.put("staffId", businessOrgStaffRelInfo.get("staff_id"));
        businessOrgStaffRelInfo.put("relCd", businessOrgStaffRelInfo.get("rel_cd"));
        businessOrgStaffRelInfo.remove("bId");
        businessOrgStaffRelInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOrgStaffRel 组织员工关系信息
     */
    protected void autoSaveDelBusinessOrgStaffRel(Business business, JSONObject businessOrgStaffRel) {
//自动插入DEL
        Map info = new HashMap();
        info.put("relId", businessOrgStaffRel.getString("relId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOrgStaffRelInfos = getOrgStaffRelServiceDaoImpl().getOrgStaffRelInfo(info);
        if (currentOrgStaffRelInfos == null || currentOrgStaffRelInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOrgStaffRelInfo = currentOrgStaffRelInfos.get(0);

        currentOrgStaffRelInfo.put("bId", business.getbId());

        currentOrgStaffRelInfo.put("relId", currentOrgStaffRelInfo.get("rel_id"));
        currentOrgStaffRelInfo.put("operate", currentOrgStaffRelInfo.get("operate"));
        currentOrgStaffRelInfo.put("storeId", currentOrgStaffRelInfo.get("store_id"));
        currentOrgStaffRelInfo.put("orgId", currentOrgStaffRelInfo.get("org_id"));
        currentOrgStaffRelInfo.put("staffId", currentOrgStaffRelInfo.get("staff_id"));
        currentOrgStaffRelInfo.put("relCd", currentOrgStaffRelInfo.get("rel_cd"));


        currentOrgStaffRelInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOrgStaffRelServiceDaoImpl().saveBusinessOrgStaffRelInfo(currentOrgStaffRelInfo);

        for (Object key : currentOrgStaffRelInfo.keySet()) {
            if (businessOrgStaffRel.get(key) == null) {
                businessOrgStaffRel.put(key.toString(), currentOrgStaffRelInfo.get(key));
            }
        }
    }
    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessUser 商户信息
     */
    protected void autoSaveAddBusinessOrgStaffRel(Business business, JSONObject businessOrgStaffRel) {
        Map info = new HashMap();
        info.put("relId", businessOrgStaffRel.getString("relId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOrgStaffRelInfos = getOrgStaffRelServiceDaoImpl().getOrgStaffRelInfo(info);
        if (currentOrgStaffRelInfos == null || currentOrgStaffRelInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOrgStaffRelInfo = currentOrgStaffRelInfos.get(0);

        currentOrgStaffRelInfo.put("bId", business.getbId());

        currentOrgStaffRelInfo.put("relId", currentOrgStaffRelInfo.get("rel_id"));
        currentOrgStaffRelInfo.put("operate", currentOrgStaffRelInfo.get("operate"));
        currentOrgStaffRelInfo.put("storeId", currentOrgStaffRelInfo.get("store_id"));
        currentOrgStaffRelInfo.put("orgId", currentOrgStaffRelInfo.get("org_id"));
        currentOrgStaffRelInfo.put("staffId", currentOrgStaffRelInfo.get("staff_id"));
        currentOrgStaffRelInfo.put("relCd", currentOrgStaffRelInfo.get("rel_cd"));
        currentOrgStaffRelInfo.put("operate",StatusConstant.OPERATE_ADD);
        getOrgStaffRelServiceDaoImpl().saveBusinessOrgStaffRelInfo(currentOrgStaffRelInfo);
    }


}
