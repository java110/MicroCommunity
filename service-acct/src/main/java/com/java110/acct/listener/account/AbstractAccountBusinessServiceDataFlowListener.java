package com.java110.acct.listener.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IAccountServiceDao;
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
 * 账户 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAccountBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAccountBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAccountServiceDao getAccountServiceDaoImpl();

    /**
     * 刷新 businessAccountInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAccountInfo
     */
    protected void flushBusinessAccountInfo(Map businessAccountInfo, String statusCd) {
        businessAccountInfo.put("newBId", businessAccountInfo.get("b_id"));
        businessAccountInfo.put("amount", businessAccountInfo.get("amount"));
        businessAccountInfo.put("operate", businessAccountInfo.get("operate"));
        businessAccountInfo.put("acctType", businessAccountInfo.get("acct_type"));
        businessAccountInfo.put("objId", businessAccountInfo.get("obj_id"));
        businessAccountInfo.put("acctId", businessAccountInfo.get("acct_id"));
        businessAccountInfo.put("acctName", businessAccountInfo.get("acct_name"));
        businessAccountInfo.put("objType", businessAccountInfo.get("obj_type"));
        businessAccountInfo.remove("bId");
        businessAccountInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAccount 账户信息
     */
    protected void autoSaveDelBusinessAccount(Business business, JSONObject businessAccount) {
//自动插入DEL
        Map info = new HashMap();
        info.put("acctId", businessAccount.getString("acctId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAccountInfos = getAccountServiceDaoImpl().getAccountInfo(info);
        if (currentAccountInfos == null || currentAccountInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAccountInfo = currentAccountInfos.get(0);

        currentAccountInfo.put("bId", business.getbId());

        currentAccountInfo.put("amount", currentAccountInfo.get("amount"));
        currentAccountInfo.put("operate", currentAccountInfo.get("operate"));
        currentAccountInfo.put("acctType", currentAccountInfo.get("acct_type"));
        currentAccountInfo.put("objId", currentAccountInfo.get("obj_id"));
        currentAccountInfo.put("acctId", currentAccountInfo.get("acct_id"));
        currentAccountInfo.put("acctName", currentAccountInfo.get("acct_name"));
        currentAccountInfo.put("objType", currentAccountInfo.get("obj_type"));


        currentAccountInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAccountServiceDaoImpl().saveBusinessAccountInfo(currentAccountInfo);
        for (Object key : currentAccountInfo.keySet()) {
            if (businessAccount.get(key) == null) {
                businessAccount.put(key.toString(), currentAccountInfo.get(key));
            }
        }
    }


}
