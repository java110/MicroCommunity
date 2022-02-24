package com.java110.community.listener.unitAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IUnitAttrServiceDao;
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
 * 单元属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractUnitAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractUnitAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IUnitAttrServiceDao getUnitAttrServiceDaoImpl();

    /**
     * 刷新 businessUnitAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessUnitAttrInfo
     */
    protected void flushBusinessUnitAttrInfo(Map businessUnitAttrInfo, String statusCd) {
        businessUnitAttrInfo.put("newBId", businessUnitAttrInfo.get("b_id"));
        businessUnitAttrInfo.put("attrId", businessUnitAttrInfo.get("attr_id"));
        businessUnitAttrInfo.put("operate", businessUnitAttrInfo.get("operate"));
        businessUnitAttrInfo.put("unitId", businessUnitAttrInfo.get("unit_id"));
        businessUnitAttrInfo.put("specCd", businessUnitAttrInfo.get("spec_cd"));
        businessUnitAttrInfo.put("value", businessUnitAttrInfo.get("value"));
        businessUnitAttrInfo.remove("bId");
        businessUnitAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessUnitAttr 单元属性信息
     */
    protected void autoSaveDelBusinessUnitAttr(Business business, JSONObject businessUnitAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessUnitAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentUnitAttrInfos = getUnitAttrServiceDaoImpl().getUnitAttrInfo(info);
        if (currentUnitAttrInfos == null || currentUnitAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentUnitAttrInfo = currentUnitAttrInfos.get(0);

        currentUnitAttrInfo.put("bId", business.getbId());

        currentUnitAttrInfo.put("attrId", currentUnitAttrInfo.get("attr_id"));
        currentUnitAttrInfo.put("operate", currentUnitAttrInfo.get("operate"));
        currentUnitAttrInfo.put("unitId", currentUnitAttrInfo.get("unit_id"));
        currentUnitAttrInfo.put("specCd", currentUnitAttrInfo.get("spec_cd"));
        currentUnitAttrInfo.put("value", currentUnitAttrInfo.get("value"));


        currentUnitAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getUnitAttrServiceDaoImpl().saveBusinessUnitAttrInfo(currentUnitAttrInfo);
        for (Object key : currentUnitAttrInfo.keySet()) {
            if (businessUnitAttr.get(key) == null) {
                businessUnitAttr.put(key.toString(), currentUnitAttrInfo.get(key));
            }
        }
    }


}
