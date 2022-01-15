package com.java110.store.listener.complaint;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.IComplaintServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投诉建议 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractComplaintBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractComplaintBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IComplaintServiceDao getComplaintServiceDaoImpl();

    /**
     * 刷新 businessComplaintInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessComplaintInfo
     */
    protected void flushBusinessComplaintInfo(Map businessComplaintInfo, String statusCd) {
        businessComplaintInfo.put("newBId", businessComplaintInfo.get("b_id"));
        businessComplaintInfo.put("operate", businessComplaintInfo.get("operate"));
        businessComplaintInfo.put("complaintId", businessComplaintInfo.get("complaint_id"));
        businessComplaintInfo.put("typeCd", businessComplaintInfo.get("type_cd"));
        businessComplaintInfo.put("context", businessComplaintInfo.get("context"));
        businessComplaintInfo.put("complaintName", businessComplaintInfo.get("complaint_name"));
        businessComplaintInfo.put("tel", businessComplaintInfo.get("tel"));
        businessComplaintInfo.put("state", businessComplaintInfo.get("state"));
        businessComplaintInfo.put("storeId", businessComplaintInfo.get("store_id"));
        businessComplaintInfo.put("roomId", businessComplaintInfo.get("room_id"));
        businessComplaintInfo.put("communityId", businessComplaintInfo.get("community_id"));
        businessComplaintInfo.put("startUserId", businessComplaintInfo.get("start_user_id"));
        businessComplaintInfo.remove("bId");
        businessComplaintInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessComplaint 投诉建议信息
     */
    protected void autoSaveDelBusinessComplaint(Business business, JSONObject businessComplaint) {
//自动插入DEL
        Map info = new HashMap();
        info.put("complaintId", businessComplaint.getString("complaintId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentComplaintInfos = getComplaintServiceDaoImpl().getComplaintInfo(info);
        if (currentComplaintInfos == null || currentComplaintInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentComplaintInfo = currentComplaintInfos.get(0);

        currentComplaintInfo.put("bId", business.getbId());

        currentComplaintInfo.put("operate", currentComplaintInfo.get("operate"));
        currentComplaintInfo.put("complaintId", currentComplaintInfo.get("complaint_id"));
        currentComplaintInfo.put("typeCd", currentComplaintInfo.get("type_cd"));
        currentComplaintInfo.put("context", currentComplaintInfo.get("context"));
        currentComplaintInfo.put("complaintName", currentComplaintInfo.get("complaint_name"));
        currentComplaintInfo.put("tel", currentComplaintInfo.get("tel"));
        currentComplaintInfo.put("state", currentComplaintInfo.get("state"));
        currentComplaintInfo.put("storeId", currentComplaintInfo.get("store_id"));
        currentComplaintInfo.put("roomId", currentComplaintInfo.get("room_id"));
        currentComplaintInfo.put("communityId", currentComplaintInfo.get("community_id"));
        currentComplaintInfo.put("startUserId", currentComplaintInfo.get("start_user_id"));


        currentComplaintInfo.put("operate", StatusConstant.OPERATE_DEL);
        getComplaintServiceDaoImpl().saveBusinessComplaintInfo(currentComplaintInfo);
        for(Object key : currentComplaintInfo.keySet()) {
            if(businessComplaint.get(key) == null) {
                businessComplaint.put(key.toString(), currentComplaintInfo.get(key));
            }
        }
    }


}
