package com.java110.store.listener.resourceSupplier;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.store.dao.IResourceSupplierServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品供应商 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractResourceSupplierBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractResourceSupplierBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IResourceSupplierServiceDao getResourceSupplierServiceDaoImpl();

    /**
     * 刷新 businessResourceSupplierInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessResourceSupplierInfo
     */
    protected void flushBusinessResourceSupplierInfo(Map businessResourceSupplierInfo, String statusCd) {
        businessResourceSupplierInfo.put("newBId", businessResourceSupplierInfo.get("b_id"));
        businessResourceSupplierInfo.put("supplierName", businessResourceSupplierInfo.get("supplier_name"));
        businessResourceSupplierInfo.put("createUserId", businessResourceSupplierInfo.get("create_user_id"));
        businessResourceSupplierInfo.put("address", businessResourceSupplierInfo.get("address"));
        businessResourceSupplierInfo.put("contactName", businessResourceSupplierInfo.get("contact_name"));
        businessResourceSupplierInfo.put("rsId", businessResourceSupplierInfo.get("rs_id"));
        businessResourceSupplierInfo.put("createUserName", businessResourceSupplierInfo.get("create_user_name"));
        businessResourceSupplierInfo.put("remark", businessResourceSupplierInfo.get("remark"));
        businessResourceSupplierInfo.put("storeId", businessResourceSupplierInfo.get("store_id"));
        businessResourceSupplierInfo.put("accountBank", businessResourceSupplierInfo.get("account_bank"));
        businessResourceSupplierInfo.put("operate", businessResourceSupplierInfo.get("operate"));
        businessResourceSupplierInfo.put("tel", businessResourceSupplierInfo.get("tel"));
        businessResourceSupplierInfo.put("bankAccountNumber", businessResourceSupplierInfo.get("bank_account_number"));
        businessResourceSupplierInfo.remove("bId");
        businessResourceSupplierInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessResourceSupplier 物品供应商信息
     */
    protected void autoSaveDelBusinessResourceSupplier(Business business, JSONObject businessResourceSupplier) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("rsId", businessResourceSupplier.getString("rsId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentResourceSupplierInfos = getResourceSupplierServiceDaoImpl().getResourceSupplierInfo(info);
        if (currentResourceSupplierInfos == null || currentResourceSupplierInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentResourceSupplierInfo = currentResourceSupplierInfos.get(0);

        currentResourceSupplierInfo.put("bId", business.getbId());

        currentResourceSupplierInfo.put("supplierName", currentResourceSupplierInfo.get("supplier_name"));
        currentResourceSupplierInfo.put("createUserId", currentResourceSupplierInfo.get("create_user_id"));
        currentResourceSupplierInfo.put("address", currentResourceSupplierInfo.get("address"));
        currentResourceSupplierInfo.put("contactName", currentResourceSupplierInfo.get("contact_name"));
        currentResourceSupplierInfo.put("rsId", currentResourceSupplierInfo.get("rs_id"));
        currentResourceSupplierInfo.put("createUserName", currentResourceSupplierInfo.get("create_user_name"));
        currentResourceSupplierInfo.put("remark", currentResourceSupplierInfo.get("remark"));
        currentResourceSupplierInfo.put("storeId", currentResourceSupplierInfo.get("store_id"));
        currentResourceSupplierInfo.put("accountBank", currentResourceSupplierInfo.get("account_bank"));
        currentResourceSupplierInfo.put("operate", currentResourceSupplierInfo.get("operate"));
        currentResourceSupplierInfo.put("tel", currentResourceSupplierInfo.get("tel"));
        currentResourceSupplierInfo.put("bankAccountNumber", currentResourceSupplierInfo.get("bank_account_number"));


        currentResourceSupplierInfo.put("operate", StatusConstant.OPERATE_DEL);
        getResourceSupplierServiceDaoImpl().saveBusinessResourceSupplierInfo(currentResourceSupplierInfo);
        for (Object key : currentResourceSupplierInfo.keySet()) {
            if (businessResourceSupplier.get(key) == null) {
                businessResourceSupplier.put(key.toString(), currentResourceSupplierInfo.get(key));
            }
        }
    }


}
