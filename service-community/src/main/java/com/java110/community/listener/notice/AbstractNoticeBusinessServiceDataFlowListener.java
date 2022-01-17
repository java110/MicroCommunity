package com.java110.community.listener.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.INoticeServiceDao;
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
 * 通知 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractNoticeBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractNoticeBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract INoticeServiceDao getNoticeServiceDaoImpl();

    /**
     * 刷新 businessNoticeInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessNoticeInfo
     */
    protected void flushBusinessNoticeInfo(Map businessNoticeInfo, String statusCd) {
        businessNoticeInfo.put("newBId", businessNoticeInfo.get("b_id"));
        businessNoticeInfo.put("operate", businessNoticeInfo.get("operate"));
        businessNoticeInfo.put("noticeTypeCd", businessNoticeInfo.get("notice_type_cd"));
        businessNoticeInfo.put("context", businessNoticeInfo.get("context"));
        businessNoticeInfo.put("startTime", businessNoticeInfo.get("start_time"));
        businessNoticeInfo.put("endTime", businessNoticeInfo.get("end_time"));
        businessNoticeInfo.put("communityId", businessNoticeInfo.get("community_id"));
        businessNoticeInfo.put("title", businessNoticeInfo.get("title"));
        businessNoticeInfo.put("userId", businessNoticeInfo.get("user_id"));
        businessNoticeInfo.put("noticeId", businessNoticeInfo.get("notice_id"));
        businessNoticeInfo.put("objType", businessNoticeInfo.get("obj_type"));
        businessNoticeInfo.put("objId", businessNoticeInfo.get("obj_id"));
        businessNoticeInfo.remove("bId");
        businessNoticeInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessNotice 通知信息
     */
    protected void autoSaveDelBusinessNotice(Business business, JSONObject businessNotice) {
//自动插入DEL
        Map<String,String> info = new HashMap<String,String>();
        info.put("noticeId", businessNotice.getString("noticeId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentNoticeInfos = getNoticeServiceDaoImpl().getNoticeInfo(info);
        if (currentNoticeInfos == null || currentNoticeInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentNoticeInfo = currentNoticeInfos.get(0);

        currentNoticeInfo.put("bId", business.getbId());

        currentNoticeInfo.put("operate", currentNoticeInfo.get("operate"));
        currentNoticeInfo.put("noticeTypeCd", currentNoticeInfo.get("notice_type_cd"));
        currentNoticeInfo.put("context", currentNoticeInfo.get("context"));
        currentNoticeInfo.put("startTime", currentNoticeInfo.get("start_time"));
        currentNoticeInfo.put("endTime", currentNoticeInfo.get("end_time"));
        currentNoticeInfo.put("communityId", currentNoticeInfo.get("community_id"));
        currentNoticeInfo.put("title", currentNoticeInfo.get("title"));
        currentNoticeInfo.put("userId", currentNoticeInfo.get("user_id"));
        currentNoticeInfo.put("noticeId", currentNoticeInfo.get("notice_id"));
        currentNoticeInfo.put("objType", currentNoticeInfo.get("obj_type"));
        currentNoticeInfo.put("objId", currentNoticeInfo.get("obj_id"));

        currentNoticeInfo.put("operate", StatusConstant.OPERATE_DEL);
        getNoticeServiceDaoImpl().saveBusinessNoticeInfo(currentNoticeInfo);

        for (Object key : currentNoticeInfo.keySet()) {
            if (businessNotice.get(key) == null) {
                businessNotice.put(key.toString(), currentNoticeInfo.get(key));
            }
        }
    }


}
