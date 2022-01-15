package com.java110.store.listener.storeAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.IStoreAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractStoreAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractStoreAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IStoreAttrServiceDao getStoreAttrServiceDaoImpl();

    /**
     * 刷新 businessStoreAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessStoreAttrInfo
     */
    protected void flushBusinessStoreAttrInfo(Map businessStoreAttrInfo, String statusCd) {
        businessStoreAttrInfo.put("newBId", businessStoreAttrInfo.get("b_id"));
        businessStoreAttrInfo.put("attrId", businessStoreAttrInfo.get("attr_id"));
        businessStoreAttrInfo.put("operate", businessStoreAttrInfo.get("operate"));
        businessStoreAttrInfo.put("createTime", businessStoreAttrInfo.get("create_time"));
        businessStoreAttrInfo.put("specCd", businessStoreAttrInfo.get("spec_cd"));
        businessStoreAttrInfo.put("storeId", businessStoreAttrInfo.get("store_id"));
        businessStoreAttrInfo.put("value", businessStoreAttrInfo.get("value"));
        businessStoreAttrInfo.remove("bId");
        businessStoreAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessStoreAttr 商户属性信息
     */
    protected void autoSaveDelBusinessStoreAttr(Business business, JSONObject businessStoreAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessStoreAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentStoreAttrInfos = getStoreAttrServiceDaoImpl().getStoreAttrInfo(info);
        if (currentStoreAttrInfos == null || currentStoreAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentStoreAttrInfo = currentStoreAttrInfos.get(0);

        currentStoreAttrInfo.put("bId", business.getbId());

        currentStoreAttrInfo.put("attrId", currentStoreAttrInfo.get("attr_id"));
        currentStoreAttrInfo.put("operate", currentStoreAttrInfo.get("operate"));
//        currentStoreAttrInfo.put("createTime", currentStoreAttrInfo.get("create_time"));
        currentStoreAttrInfo.put("specCd", currentStoreAttrInfo.get("spec_cd"));
        currentStoreAttrInfo.put("storeId", currentStoreAttrInfo.get("store_id"));
        currentStoreAttrInfo.put("value", currentStoreAttrInfo.get("value"));
        currentStoreAttrInfo.put("month", DateUtil.getCurrentMonth());
        currentStoreAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getStoreAttrServiceDaoImpl().saveBusinessStoreAttrInfo(currentStoreAttrInfo);

        for (Object key : currentStoreAttrInfo.keySet()) {
            if (businessStoreAttr.get(key) == null) {
                businessStoreAttr.put(key.toString(), currentStoreAttrInfo.get(key));
            }
        }
    }


}
