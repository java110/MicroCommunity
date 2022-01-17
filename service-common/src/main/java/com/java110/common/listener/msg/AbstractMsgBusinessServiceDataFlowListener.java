package com.java110.common.listener.msg;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMsgServiceDao;
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
 * 消息 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMsgBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMsgBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMsgServiceDao getMsgServiceDaoImpl();

    /**
     * 刷新 businessMsgInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMsgInfo
     */
    protected void flushBusinessMsgInfo(Map businessMsgInfo, String statusCd) {
        businessMsgInfo.put("newBId", businessMsgInfo.get("b_id"));
        businessMsgInfo.put("msgType", businessMsgInfo.get("msg_type"));
        businessMsgInfo.put("operate", businessMsgInfo.get("operate"));
        businessMsgInfo.put("msgId", businessMsgInfo.get("msg_id"));
        businessMsgInfo.put("viewObjId", businessMsgInfo.get("view_obj_id"));
        businessMsgInfo.put("title", businessMsgInfo.get("title"));
        businessMsgInfo.put("viewTypeCd", businessMsgInfo.get("view_type_cd"));
        businessMsgInfo.put("url", businessMsgInfo.get("url"));
        businessMsgInfo.remove("bId");
        businessMsgInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMsg 消息信息
     */
    protected void autoSaveDelBusinessMsg(Business business, JSONObject businessMsg) {
//自动插入DEL
        Map info = new HashMap();
        info.put("msgId", businessMsg.getString("msgId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMsgInfos = getMsgServiceDaoImpl().getMsgInfo(info);
        if (currentMsgInfos == null || currentMsgInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMsgInfo = currentMsgInfos.get(0);

        currentMsgInfo.put("bId", business.getbId());

        currentMsgInfo.put("msgType", currentMsgInfo.get("msg_type"));
        currentMsgInfo.put("operate", currentMsgInfo.get("operate"));
        currentMsgInfo.put("msgId", currentMsgInfo.get("msg_id"));
        currentMsgInfo.put("viewObjId", currentMsgInfo.get("view_obj_id"));
        currentMsgInfo.put("title", currentMsgInfo.get("title"));
        currentMsgInfo.put("viewTypeCd", currentMsgInfo.get("view_type_cd"));
        currentMsgInfo.put("url", currentMsgInfo.get("url"));


        currentMsgInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMsgServiceDaoImpl().saveBusinessMsgInfo(currentMsgInfo);
        for (Object key : currentMsgInfo.keySet()) {
            if (businessMsg.get(key) == null) {
                businessMsg.put(key.toString(), currentMsgInfo.get(key));
            }
        }
    }


}
