package com.java110.store.listener.demo;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.store.dao.IDemoServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * demo 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractDemoBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractDemoBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IDemoServiceDao getDemoServiceDaoImpl();

    /**
     * 刷新 businessDemoInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessDemoInfo
     */
    protected void flushBusinessDemoInfo(Map businessDemoInfo, String statusCd) {
        businessDemoInfo.put("newBId", businessDemoInfo.get("b_id"));
        businessDemoInfo.put("demoRemark", businessDemoInfo.get("demo_remark"));
        businessDemoInfo.put("demoName", businessDemoInfo.get("demo_name"));
        businessDemoInfo.put("demoValue", businessDemoInfo.get("demo_value"));
        businessDemoInfo.put("operate", businessDemoInfo.get("operate"));
        businessDemoInfo.put("demoId", businessDemoInfo.get("demo_id"));
        businessDemoInfo.put("userId", businessDemoInfo.get("user_id"));
        businessDemoInfo.remove("bId");
        businessDemoInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessDemo demo信息
     */
    protected void autoSaveDelBusinessDemo(Business business, JSONObject businessDemo) {
//自动插入DEL
        Map info = new HashMap();
        info.put("demoId", businessDemo.getString("demoId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentDemoInfos = getDemoServiceDaoImpl().getDemoInfo(info);
        if (currentDemoInfos == null || currentDemoInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentDemoInfo = currentDemoInfos.get(0);

        currentDemoInfo.put("bId", business.getbId());

        currentDemoInfo.put("demoRemark", currentDemoInfo.get("demo_remark"));
        currentDemoInfo.put("demoName", currentDemoInfo.get("demo_name"));
        currentDemoInfo.put("demoValue", currentDemoInfo.get("demo_value"));
        currentDemoInfo.put("operate", currentDemoInfo.get("operate"));
        currentDemoInfo.put("demoId", currentDemoInfo.get("demo_id"));
        currentDemoInfo.put("userId", currentDemoInfo.get("user_id"));


        currentDemoInfo.put("operate", StatusConstant.OPERATE_DEL);
        getDemoServiceDaoImpl().saveBusinessDemoInfo(currentDemoInfo);

        for(Object key : currentDemoInfo.keySet()) {
            if(businessDemo.get(key) == null) {
                businessDemo.put(key.toString(), currentDemoInfo.get(key));
            }
        }
    }


}
