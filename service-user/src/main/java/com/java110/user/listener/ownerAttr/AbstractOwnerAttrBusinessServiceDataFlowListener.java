package com.java110.user.listener.ownerAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.user.dao.IOwnerAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业主属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOwnerAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractOwnerAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOwnerAttrServiceDao getOwnerAttrServiceDaoImpl();

    /**
     * 刷新 businessOwnerAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOwnerAttrInfo
     */
    protected void flushBusinessOwnerAttrInfo(Map businessOwnerAttrInfo, String statusCd) {
        businessOwnerAttrInfo.put("newBId", businessOwnerAttrInfo.get("b_id"));
        businessOwnerAttrInfo.put("attrId", businessOwnerAttrInfo.get("attr_id"));
        businessOwnerAttrInfo.put("operate", businessOwnerAttrInfo.get("operate"));
        businessOwnerAttrInfo.put("createTime", businessOwnerAttrInfo.get("create_time"));
        businessOwnerAttrInfo.put("specCd", businessOwnerAttrInfo.get("spec_cd"));
        businessOwnerAttrInfo.put("communityId", businessOwnerAttrInfo.get("community_id"));
        businessOwnerAttrInfo.put("value", businessOwnerAttrInfo.get("value"));
        businessOwnerAttrInfo.put("memberId", businessOwnerAttrInfo.get("member_id"));
        businessOwnerAttrInfo.remove("bId");
        businessOwnerAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOwnerAttr 业主属性信息
     */
    protected void autoSaveDelBusinessOwnerAttr(Business business, JSONObject businessOwnerAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessOwnerAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOwnerAttrInfos = getOwnerAttrServiceDaoImpl().getOwnerAttrInfo(info);
        if (currentOwnerAttrInfos == null || currentOwnerAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentOwnerAttrInfo = currentOwnerAttrInfos.get(0);

        currentOwnerAttrInfo.put("bId", business.getbId());

        currentOwnerAttrInfo.put("attrId", currentOwnerAttrInfo.get("attr_id"));
        currentOwnerAttrInfo.put("operate", currentOwnerAttrInfo.get("operate"));
        currentOwnerAttrInfo.put("createTime", currentOwnerAttrInfo.get("create_time"));
        currentOwnerAttrInfo.put("specCd", currentOwnerAttrInfo.get("spec_cd"));
        currentOwnerAttrInfo.put("communityId", currentOwnerAttrInfo.get("community_id"));
        currentOwnerAttrInfo.put("value", currentOwnerAttrInfo.get("value"));
        currentOwnerAttrInfo.put("memberId", currentOwnerAttrInfo.get("member_id"));


        currentOwnerAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOwnerAttrServiceDaoImpl().saveBusinessOwnerAttrInfo(currentOwnerAttrInfo);
        for (Object key : currentOwnerAttrInfo.keySet()) {
            if (businessOwnerAttr.get(key) == null) {
                businessOwnerAttr.put(key.toString(), currentOwnerAttrInfo.get(key));
            }
        }
    }


}
