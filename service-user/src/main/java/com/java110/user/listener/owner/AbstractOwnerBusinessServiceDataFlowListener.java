package com.java110.user.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IOwnerServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业主 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOwnerBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractOwnerBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOwnerServiceDao getOwnerServiceDaoImpl();

    /**
     * 刷新 businessOwnerInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * ,t.community_id,t.community_id communityId
     *
     * @param businessOwnerInfo
     */
    protected void flushBusinessOwnerInfo(Map businessOwnerInfo, String statusCd) {
        businessOwnerInfo.put("newBId", businessOwnerInfo.get("b_id"));
        businessOwnerInfo.put("operate", businessOwnerInfo.get("operate"));
        businessOwnerInfo.put("sex", businessOwnerInfo.get("sex"));
        businessOwnerInfo.put("name", businessOwnerInfo.get("name"));
        businessOwnerInfo.put("link", businessOwnerInfo.get("link"));
        businessOwnerInfo.put("address", businessOwnerInfo.get("address"));
        businessOwnerInfo.put("remark", businessOwnerInfo.get("remark"));
        businessOwnerInfo.put("ownerId", businessOwnerInfo.get("owner_id"));
        businessOwnerInfo.put("userId", businessOwnerInfo.get("user_id"));
        businessOwnerInfo.put("age", businessOwnerInfo.get("age"));
        businessOwnerInfo.put("memberId", businessOwnerInfo.get("member_id"));
        businessOwnerInfo.put("ownerTypeCd", businessOwnerInfo.get("owner_type_cd"));
        businessOwnerInfo.put("communityId", businessOwnerInfo.get("community_id"));
        businessOwnerInfo.put("idCard", businessOwnerInfo.get("id_card"));
        businessOwnerInfo.put("state", businessOwnerInfo.get("state"));
        businessOwnerInfo.put("ownerFlag", businessOwnerInfo.get("owner_flag"));
        businessOwnerInfo.remove("bId");
        businessOwnerInfo.put("statusCd", statusCd);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOwner 业主信息
     */
    protected void autoSaveDelBusinessOwner(Business business, JSONObject businessOwner) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("memberId", businessOwner.getString("memberId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentOwnerInfos = getOwnerServiceDaoImpl().getOwnerInfo(info);
        if (currentOwnerInfos == null || currentOwnerInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentOwnerInfo = currentOwnerInfos.get(0);
        currentOwnerInfo.put("bId", business.getbId());
        currentOwnerInfo.put("operate", currentOwnerInfo.get("operate"));
        currentOwnerInfo.put("sex", currentOwnerInfo.get("sex"));
        currentOwnerInfo.put("name", currentOwnerInfo.get("name"));
        currentOwnerInfo.put("link", currentOwnerInfo.get("link"));
        currentOwnerInfo.put("address", currentOwnerInfo.get("address"));
        currentOwnerInfo.put("remark", currentOwnerInfo.get("remark"));
        currentOwnerInfo.put("ownerId", currentOwnerInfo.get("owner_id"));
        currentOwnerInfo.put("userId", currentOwnerInfo.get("user_id"));
        currentOwnerInfo.put("age", currentOwnerInfo.get("age"));
        currentOwnerInfo.put("memberId", currentOwnerInfo.get("member_id"));
        currentOwnerInfo.put("ownerTypeCd", currentOwnerInfo.get("owner_type_cd"));
        currentOwnerInfo.put("communityId", currentOwnerInfo.get("community_id"));
        currentOwnerInfo.put("idCard", currentOwnerInfo.get("id_card"));
        currentOwnerInfo.put("state", currentOwnerInfo.get("state"));
        currentOwnerInfo.put("ownerFlag", currentOwnerInfo.get("owner_flag"));
        currentOwnerInfo.put("operate", StatusConstant.OPERATE_DEL);
        getOwnerServiceDaoImpl().saveBusinessOwnerInfo(currentOwnerInfo);
        for (Object key : currentOwnerInfo.keySet()) {
            if (businessOwner.get(key) == null) {
                businessOwner.put(key.toString(), currentOwnerInfo.get(key));
            }
        }
    }

}
