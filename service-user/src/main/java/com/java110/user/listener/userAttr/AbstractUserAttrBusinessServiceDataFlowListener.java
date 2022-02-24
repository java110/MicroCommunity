package com.java110.user.listener.userAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.user.dao.IUserAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractUserAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractUserAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IUserAttrServiceDao getUserAttrServiceDaoImpl();

    /**
     * 刷新 businessUserAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessUserAttrInfo
     */
    protected void flushBusinessUserAttrInfo(Map businessUserAttrInfo, String statusCd) {
        businessUserAttrInfo.put("newBId", businessUserAttrInfo.get("b_id"));
        businessUserAttrInfo.put("attrId", businessUserAttrInfo.get("attr_id"));
        businessUserAttrInfo.put("operate", businessUserAttrInfo.get("operate"));
        businessUserAttrInfo.put("specCd", businessUserAttrInfo.get("spec_cd"));
        businessUserAttrInfo.put("userId", businessUserAttrInfo.get("user_id"));
        businessUserAttrInfo.put("value", businessUserAttrInfo.get("value"));
        businessUserAttrInfo.remove("bId");
        businessUserAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessUserAttr 用户属性信息
     */
    protected void autoSaveDelBusinessUserAttr(Business business, JSONObject businessUserAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessUserAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentUserAttrInfos = getUserAttrServiceDaoImpl().getUserAttrInfo(info);
        if (currentUserAttrInfos == null || currentUserAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentUserAttrInfo = currentUserAttrInfos.get(0);

        currentUserAttrInfo.put("bId", business.getbId());

        currentUserAttrInfo.put("attrId", currentUserAttrInfo.get("attr_id"));
        currentUserAttrInfo.put("operate", currentUserAttrInfo.get("operate"));
        currentUserAttrInfo.put("specCd", currentUserAttrInfo.get("spec_cd"));
        currentUserAttrInfo.put("userId", currentUserAttrInfo.get("user_id"));
        currentUserAttrInfo.put("value", currentUserAttrInfo.get("value"));


        currentUserAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getUserAttrServiceDaoImpl().saveBusinessUserAttrInfo(currentUserAttrInfo);
        for (Object key : currentUserAttrInfo.keySet()) {
            if (businessUserAttr.get(key) == null) {
                businessUserAttr.put(key.toString(), currentUserAttrInfo.get(key));
            }
        }
    }


}
