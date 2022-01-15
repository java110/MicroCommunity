package com.java110.job.listener.taskAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.job.dao.ITaskAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractTaskAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractTaskAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ITaskAttrServiceDao getTaskAttrServiceDaoImpl();

    /**
     * 刷新 businessTaskAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessTaskAttrInfo
     */
    protected void flushBusinessTaskAttrInfo(Map businessTaskAttrInfo, String statusCd) {
        businessTaskAttrInfo.put("newBId", businessTaskAttrInfo.get("b_id"));
        businessTaskAttrInfo.put("attrId", businessTaskAttrInfo.get("attr_id"));
        businessTaskAttrInfo.put("operate", businessTaskAttrInfo.get("operate"));
        businessTaskAttrInfo.put("createTime", businessTaskAttrInfo.get("create_time"));
        businessTaskAttrInfo.put("specCd", businessTaskAttrInfo.get("spec_cd"));
        businessTaskAttrInfo.put("value", businessTaskAttrInfo.get("value"));
        businessTaskAttrInfo.put("taskId", businessTaskAttrInfo.get("task_id"));
        businessTaskAttrInfo.remove("bId");
        businessTaskAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessTaskAttr 定时任务属性信息
     */
    protected void autoSaveDelBusinessTaskAttr(Business business, JSONObject businessTaskAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessTaskAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentTaskAttrInfos = getTaskAttrServiceDaoImpl().getTaskAttrInfo(info);
        if (currentTaskAttrInfos == null || currentTaskAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentTaskAttrInfo = currentTaskAttrInfos.get(0);

        currentTaskAttrInfo.put("bId", business.getbId());

        currentTaskAttrInfo.put("attrId", currentTaskAttrInfo.get("attr_id"));
        currentTaskAttrInfo.put("operate", currentTaskAttrInfo.get("operate"));
        currentTaskAttrInfo.put("createTime", currentTaskAttrInfo.get("create_time"));
        currentTaskAttrInfo.put("specCd", currentTaskAttrInfo.get("spec_cd"));
        currentTaskAttrInfo.put("value", currentTaskAttrInfo.get("value"));
        currentTaskAttrInfo.put("taskId", currentTaskAttrInfo.get("task_id"));


        currentTaskAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getTaskAttrServiceDaoImpl().saveBusinessTaskAttrInfo(currentTaskAttrInfo);
        for (Object key : currentTaskAttrInfo.keySet()) {
            if (businessTaskAttr.get(key) == null) {
                businessTaskAttr.put(key.toString(), currentTaskAttrInfo.get(key));
            }
        }
    }


}
