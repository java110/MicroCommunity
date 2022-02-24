package com.java110.common.listener.msgRead;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMsgReadServiceDao;
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
 * 消息阅读 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractMsgReadBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractMsgReadBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IMsgReadServiceDao getMsgReadServiceDaoImpl();

    /**
     * 刷新 businessMsgReadInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessMsgReadInfo
     */
    protected void flushBusinessMsgReadInfo(Map businessMsgReadInfo, String statusCd) {
        businessMsgReadInfo.put("newBId", businessMsgReadInfo.get("b_id"));
        businessMsgReadInfo.put("operate", businessMsgReadInfo.get("operate"));
        businessMsgReadInfo.put("msgReadId", businessMsgReadInfo.get("msg_read_id"));
        businessMsgReadInfo.put("msgId", businessMsgReadInfo.get("msg_id"));
        businessMsgReadInfo.put("userName", businessMsgReadInfo.get("user_name"));
        businessMsgReadInfo.put("userId", businessMsgReadInfo.get("user_id"));
        businessMsgReadInfo.remove("bId");
        businessMsgReadInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessMsgRead 消息阅读信息
     */
    protected void autoSaveDelBusinessMsgRead(Business business, JSONObject businessMsgRead) {
//自动插入DEL
        Map info = new HashMap();
        info.put("msgReadId", businessMsgRead.getString("msgReadId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentMsgReadInfos = getMsgReadServiceDaoImpl().getMsgReadInfo(info);
        if (currentMsgReadInfos == null || currentMsgReadInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentMsgReadInfo = currentMsgReadInfos.get(0);

        currentMsgReadInfo.put("bId", business.getbId());

        currentMsgReadInfo.put("operate", currentMsgReadInfo.get("operate"));
        currentMsgReadInfo.put("msgReadId", currentMsgReadInfo.get("msg_read_id"));
        currentMsgReadInfo.put("msgId", currentMsgReadInfo.get("msg_id"));
        currentMsgReadInfo.put("userName", currentMsgReadInfo.get("user_name"));
        currentMsgReadInfo.put("userId", currentMsgReadInfo.get("user_id"));


        currentMsgReadInfo.put("operate", StatusConstant.OPERATE_DEL);
        getMsgReadServiceDaoImpl().saveBusinessMsgReadInfo(currentMsgReadInfo);
        for (Object key : currentMsgReadInfo.keySet()) {
            if (businessMsgRead.get(key) == null) {
                businessMsgRead.put(key.toString(), currentMsgReadInfo.get(key));
            }
        }
    }


}
