package com.java110.fee.listener.tempCarFeeConfigAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.fee.dao.ITempCarFeeConfigAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 临时车收费标准属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractTempCarFeeConfigAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractTempCarFeeConfigAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ITempCarFeeConfigAttrServiceDao getTempCarFeeConfigAttrServiceDaoImpl();

    /**
     * 刷新 businessTempCarFeeConfigAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessTempCarFeeConfigAttrInfo
     */
    protected void flushBusinessTempCarFeeConfigAttrInfo(Map businessTempCarFeeConfigAttrInfo, String statusCd) {
        businessTempCarFeeConfigAttrInfo.put("newBId", businessTempCarFeeConfigAttrInfo.get("b_id"));
        businessTempCarFeeConfigAttrInfo.put("attrId", businessTempCarFeeConfigAttrInfo.get("attr_id"));
        businessTempCarFeeConfigAttrInfo.put("operate", businessTempCarFeeConfigAttrInfo.get("operate"));
        businessTempCarFeeConfigAttrInfo.put("createTime", businessTempCarFeeConfigAttrInfo.get("create_time"));
        businessTempCarFeeConfigAttrInfo.put("configId", businessTempCarFeeConfigAttrInfo.get("config_id"));
        businessTempCarFeeConfigAttrInfo.put("specCd", businessTempCarFeeConfigAttrInfo.get("spec_cd"));
        businessTempCarFeeConfigAttrInfo.put("value", businessTempCarFeeConfigAttrInfo.get("value"));
        businessTempCarFeeConfigAttrInfo.put("communityId", businessTempCarFeeConfigAttrInfo.get("community_id"));
        businessTempCarFeeConfigAttrInfo.remove("bId");
        businessTempCarFeeConfigAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessTempCarFeeConfigAttr 临时车收费标准属性信息
     */
    protected void autoSaveDelBusinessTempCarFeeConfigAttr(Business business, JSONObject businessTempCarFeeConfigAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessTempCarFeeConfigAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentTempCarFeeConfigAttrInfos = getTempCarFeeConfigAttrServiceDaoImpl().getTempCarFeeConfigAttrInfo(info);
        if (currentTempCarFeeConfigAttrInfos == null || currentTempCarFeeConfigAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentTempCarFeeConfigAttrInfo = currentTempCarFeeConfigAttrInfos.get(0);

        currentTempCarFeeConfigAttrInfo.put("bId", business.getbId());

        currentTempCarFeeConfigAttrInfo.put("attrId", currentTempCarFeeConfigAttrInfo.get("attr_id"));
        currentTempCarFeeConfigAttrInfo.put("operate", currentTempCarFeeConfigAttrInfo.get("operate"));
        currentTempCarFeeConfigAttrInfo.put("createTime", currentTempCarFeeConfigAttrInfo.get("create_time"));
        currentTempCarFeeConfigAttrInfo.put("configId", currentTempCarFeeConfigAttrInfo.get("config_id"));
        currentTempCarFeeConfigAttrInfo.put("specCd", currentTempCarFeeConfigAttrInfo.get("spec_cd"));
        currentTempCarFeeConfigAttrInfo.put("value", currentTempCarFeeConfigAttrInfo.get("value"));
        currentTempCarFeeConfigAttrInfo.put("communityId", currentTempCarFeeConfigAttrInfo.get("community_id"));


        currentTempCarFeeConfigAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getTempCarFeeConfigAttrServiceDaoImpl().saveBusinessTempCarFeeConfigAttrInfo(currentTempCarFeeConfigAttrInfo);
        for (Object key : currentTempCarFeeConfigAttrInfo.keySet()) {
            if (businessTempCarFeeConfigAttr.get(key) == null) {
                businessTempCarFeeConfigAttr.put(key.toString(), currentTempCarFeeConfigAttrInfo.get(key));
            }
        }
    }


}
