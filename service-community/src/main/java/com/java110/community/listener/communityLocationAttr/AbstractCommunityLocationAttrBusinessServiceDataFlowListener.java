package com.java110.community.listener.communityLocationAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityLocationAttrServiceDao;
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
 * 位置属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommunityLocationAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractCommunityLocationAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ICommunityLocationAttrServiceDao getCommunityLocationAttrServiceDaoImpl();

    /**
     * 刷新 businessCommunityLocationAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCommunityLocationAttrInfo
     */
    protected void flushBusinessCommunityLocationAttrInfo(Map businessCommunityLocationAttrInfo, String statusCd) {
        businessCommunityLocationAttrInfo.put("newBId", businessCommunityLocationAttrInfo.get("b_id"));
        businessCommunityLocationAttrInfo.put("attrId", businessCommunityLocationAttrInfo.get("attr_id"));
        businessCommunityLocationAttrInfo.put("operate", businessCommunityLocationAttrInfo.get("operate"));
        businessCommunityLocationAttrInfo.put("createTime", businessCommunityLocationAttrInfo.get("create_time"));
        businessCommunityLocationAttrInfo.put("locationId", businessCommunityLocationAttrInfo.get("location_id"));
        businessCommunityLocationAttrInfo.put("specCd", businessCommunityLocationAttrInfo.get("spec_cd"));
        businessCommunityLocationAttrInfo.put("communityId", businessCommunityLocationAttrInfo.get("community_id"));
        businessCommunityLocationAttrInfo.put("value", businessCommunityLocationAttrInfo.get("value"));
        businessCommunityLocationAttrInfo.remove("bId");
        businessCommunityLocationAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCommunityLocationAttr 位置属性信息
     */
    protected void autoSaveDelBusinessCommunityLocationAttr(Business business, JSONObject businessCommunityLocationAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessCommunityLocationAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommunityLocationAttrInfos = getCommunityLocationAttrServiceDaoImpl().getCommunityLocationAttrInfo(info);
        if (currentCommunityLocationAttrInfos == null || currentCommunityLocationAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentCommunityLocationAttrInfo = currentCommunityLocationAttrInfos.get(0);

        currentCommunityLocationAttrInfo.put("bId", business.getbId());

        currentCommunityLocationAttrInfo.put("attrId", currentCommunityLocationAttrInfo.get("attr_id"));
        currentCommunityLocationAttrInfo.put("operate", currentCommunityLocationAttrInfo.get("operate"));
        currentCommunityLocationAttrInfo.put("createTime", currentCommunityLocationAttrInfo.get("create_time"));
        currentCommunityLocationAttrInfo.put("locationId", currentCommunityLocationAttrInfo.get("location_id"));
        currentCommunityLocationAttrInfo.put("specCd", currentCommunityLocationAttrInfo.get("spec_cd"));
        currentCommunityLocationAttrInfo.put("communityId", currentCommunityLocationAttrInfo.get("community_id"));
        currentCommunityLocationAttrInfo.put("value", currentCommunityLocationAttrInfo.get("value"));


        currentCommunityLocationAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getCommunityLocationAttrServiceDaoImpl().saveBusinessCommunityLocationAttrInfo(currentCommunityLocationAttrInfo);
        for (Object key : currentCommunityLocationAttrInfo.keySet()) {
            if (businessCommunityLocationAttr.get(key) == null) {
                businessCommunityLocationAttr.put(key.toString(), currentCommunityLocationAttrInfo.get(key));
            }
        }
    }


}
