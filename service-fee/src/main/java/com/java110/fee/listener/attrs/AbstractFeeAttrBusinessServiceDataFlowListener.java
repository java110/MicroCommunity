package com.java110.fee.listener.attrs;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.fee.dao.IFeeAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFeeAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractFeeAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IFeeAttrServiceDao getFeeAttrServiceDaoImpl();

    /**
     * 刷新 businessFeeAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessFeeAttrInfo
     */
    protected void flushBusinessFeeAttrInfo(Map businessFeeAttrInfo, String statusCd) {
        businessFeeAttrInfo.put("newBId", businessFeeAttrInfo.get("b_id"));
        businessFeeAttrInfo.put("attrId", businessFeeAttrInfo.get("attr_id"));
        businessFeeAttrInfo.put("operate", businessFeeAttrInfo.get("operate"));
        businessFeeAttrInfo.put("specCd", businessFeeAttrInfo.get("spec_cd"));
        businessFeeAttrInfo.put("communityId", businessFeeAttrInfo.get("community_id"));
        businessFeeAttrInfo.put("feeId", businessFeeAttrInfo.get("fee_id"));
        businessFeeAttrInfo.put("value", businessFeeAttrInfo.get("value"));
        businessFeeAttrInfo.remove("bId");
        businessFeeAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessFeeAttr 费用属性信息
     */
    protected void autoSaveDelBusinessFeeAttr(Business business, JSONObject businessFeeAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessFeeAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentFeeAttrInfos = getFeeAttrServiceDaoImpl().getFeeAttrInfo(info);
        if (currentFeeAttrInfos == null || currentFeeAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentFeeAttrInfo = currentFeeAttrInfos.get(0);

        currentFeeAttrInfo.put("bId", business.getbId());

        currentFeeAttrInfo.put("attrId", currentFeeAttrInfo.get("attr_id"));
        currentFeeAttrInfo.put("operate", currentFeeAttrInfo.get("operate"));
        currentFeeAttrInfo.put("specCd", currentFeeAttrInfo.get("spec_cd"));
        currentFeeAttrInfo.put("communityId", currentFeeAttrInfo.get("community_id"));
        currentFeeAttrInfo.put("feeId", currentFeeAttrInfo.get("fee_id"));
        currentFeeAttrInfo.put("value", currentFeeAttrInfo.get("value"));


        currentFeeAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getFeeAttrServiceDaoImpl().saveBusinessFeeAttrInfo(currentFeeAttrInfo);

        for (Object key : currentFeeAttrInfo.keySet()) {
            if (businessFeeAttr.get(key) == null) {
                businessFeeAttr.put(key.toString(), currentFeeAttrInfo.get(key));
            }
        }
    }


}
