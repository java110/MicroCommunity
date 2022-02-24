package com.java110.acct.listener.accountDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IAccountDetailServiceDao;
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
 * 账户交易 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAccountDetailBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAccountDetailBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAccountDetailServiceDao getAccountDetailServiceDaoImpl();

    /**
     * 刷新 businessAccountDetailInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAccountDetailInfo
     */
    protected void flushBusinessAccountDetailInfo(Map businessAccountDetailInfo, String statusCd) {
        businessAccountDetailInfo.put("newBId", businessAccountDetailInfo.get("b_id"));
        businessAccountDetailInfo.put("detailType", businessAccountDetailInfo.get("detail_type"));
        businessAccountDetailInfo.put("amount", businessAccountDetailInfo.get("amount"));
        businessAccountDetailInfo.put("operate", businessAccountDetailInfo.get("operate"));
        businessAccountDetailInfo.put("orderId", businessAccountDetailInfo.get("order_id"));
        businessAccountDetailInfo.put("objId", businessAccountDetailInfo.get("obj_id"));
        businessAccountDetailInfo.put("detailId", businessAccountDetailInfo.get("detail_id"));
        businessAccountDetailInfo.put("acctId", businessAccountDetailInfo.get("acct_id"));
        businessAccountDetailInfo.put("relAcctId", businessAccountDetailInfo.get("rel_acct_id"));
        businessAccountDetailInfo.put("remark", businessAccountDetailInfo.get("remark"));
        businessAccountDetailInfo.put("objType", businessAccountDetailInfo.get("obj_type"));
        businessAccountDetailInfo.remove("bId");
        businessAccountDetailInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAccountDetail 账户交易信息
     */
    protected void autoSaveDelBusinessAccountDetail(Business business, JSONObject businessAccountDetail) {
//自动插入DEL
        Map info = new HashMap();
        info.put("detailId", businessAccountDetail.getString("detailId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAccountDetailInfos = getAccountDetailServiceDaoImpl().getAccountDetailInfo(info);
        if (currentAccountDetailInfos == null || currentAccountDetailInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAccountDetailInfo = currentAccountDetailInfos.get(0);

        currentAccountDetailInfo.put("bId", business.getbId());

        currentAccountDetailInfo.put("detailType", currentAccountDetailInfo.get("detail_type"));
        currentAccountDetailInfo.put("amount", currentAccountDetailInfo.get("amount"));
        currentAccountDetailInfo.put("operate", currentAccountDetailInfo.get("operate"));
        currentAccountDetailInfo.put("orderId", currentAccountDetailInfo.get("order_id"));
        currentAccountDetailInfo.put("objId", currentAccountDetailInfo.get("obj_id"));
        currentAccountDetailInfo.put("detailId", currentAccountDetailInfo.get("detail_id"));
        currentAccountDetailInfo.put("acctId", currentAccountDetailInfo.get("acct_id"));
        currentAccountDetailInfo.put("relAcctId", currentAccountDetailInfo.get("rel_acct_id"));
        currentAccountDetailInfo.put("remark", currentAccountDetailInfo.get("remark"));
        currentAccountDetailInfo.put("objType", currentAccountDetailInfo.get("obj_type"));


        currentAccountDetailInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAccountDetailServiceDaoImpl().saveBusinessAccountDetailInfo(currentAccountDetailInfo);
        for (Object key : currentAccountDetailInfo.keySet()) {
            if (businessAccountDetail.get(key) == null) {
                businessAccountDetail.put(key.toString(), currentAccountDetailInfo.get(key));
            }
        }
    }


}
