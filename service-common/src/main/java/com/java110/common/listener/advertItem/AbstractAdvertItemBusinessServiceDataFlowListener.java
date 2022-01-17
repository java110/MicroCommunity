package com.java110.common.listener.advertItem;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertItemServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告项信息 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAdvertItemBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAdvertItemBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAdvertItemServiceDao getAdvertItemServiceDaoImpl();

    /**
     * 刷新 businessAdvertItemInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAdvertItemInfo
     */
    protected void flushBusinessAdvertItemInfo(Map businessAdvertItemInfo, String statusCd) {
        businessAdvertItemInfo.put("newBId", businessAdvertItemInfo.get("b_id"));
        businessAdvertItemInfo.put("operate", businessAdvertItemInfo.get("operate"));
        businessAdvertItemInfo.put("itemTypeCd", businessAdvertItemInfo.get("item_type_cd"));
        businessAdvertItemInfo.put("communityId", businessAdvertItemInfo.get("community_id"));
        businessAdvertItemInfo.put("advertItemId", businessAdvertItemInfo.get("advert_item_id"));
        businessAdvertItemInfo.put("advertId", businessAdvertItemInfo.get("advert_id"));
        businessAdvertItemInfo.put("url", businessAdvertItemInfo.get("url"));
        businessAdvertItemInfo.put("seq", businessAdvertItemInfo.get("seq"));
        businessAdvertItemInfo.remove("bId");
        businessAdvertItemInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAdvertItem 广告项信息信息
     */
    protected void autoSaveDelBusinessAdvertItem(Business business, JSONObject businessAdvertItem) {
//自动插入DEL
        Map info = new HashMap();
        info.put("advertItemId", businessAdvertItem.getString("advertItemId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAdvertItemInfos = getAdvertItemServiceDaoImpl().getAdvertItemInfo(info);
        if (currentAdvertItemInfos == null || currentAdvertItemInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAdvertItemInfo = currentAdvertItemInfos.get(0);

        currentAdvertItemInfo.put("bId", business.getbId());

        currentAdvertItemInfo.put("operate", currentAdvertItemInfo.get("operate"));
        currentAdvertItemInfo.put("itemTypeCd", currentAdvertItemInfo.get("item_type_cd"));
        currentAdvertItemInfo.put("communityId", currentAdvertItemInfo.get("community_id"));
        currentAdvertItemInfo.put("advertItemId", currentAdvertItemInfo.get("advert_item_id"));
        currentAdvertItemInfo.put("advertId", currentAdvertItemInfo.get("advert_id"));
        currentAdvertItemInfo.put("url", currentAdvertItemInfo.get("url"));
        currentAdvertItemInfo.put("seq", currentAdvertItemInfo.get("seq"));


        currentAdvertItemInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAdvertItemServiceDaoImpl().saveBusinessAdvertItemInfo(currentAdvertItemInfo);

        for(Object key : currentAdvertItemInfo.keySet()) {
            if(businessAdvertItem.get(key) == null) {
                businessAdvertItem.put(key.toString(), currentAdvertItemInfo.get(key));
            }
        }
    }


}
