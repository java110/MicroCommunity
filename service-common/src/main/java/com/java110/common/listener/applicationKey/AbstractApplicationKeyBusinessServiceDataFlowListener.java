package com.java110.common.listener.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IApplicationKeyServiceDao;
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
 * 钥匙申请 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractApplicationKeyBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractApplicationKeyBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IApplicationKeyServiceDao getApplicationKeyServiceDaoImpl();

    /**
     * 刷新 businessApplicationKeyInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessApplicationKeyInfo
     */
    protected void flushBusinessApplicationKeyInfo(Map businessApplicationKeyInfo, String statusCd) {
        businessApplicationKeyInfo.put("newBId", businessApplicationKeyInfo.get("b_id"));
        businessApplicationKeyInfo.put("applicationKeyId", businessApplicationKeyInfo.get("application_key_id"));
        businessApplicationKeyInfo.put("idCard", businessApplicationKeyInfo.get("id_card"));
        businessApplicationKeyInfo.put("sex", businessApplicationKeyInfo.get("sex"));
        businessApplicationKeyInfo.put("endTime", businessApplicationKeyInfo.get("end_time"));
        businessApplicationKeyInfo.put("machineId", businessApplicationKeyInfo.get("machine_id"));
        businessApplicationKeyInfo.put("operate", businessApplicationKeyInfo.get("operate"));
        businessApplicationKeyInfo.put("typeCd", businessApplicationKeyInfo.get("type_cd"));
        businessApplicationKeyInfo.put("name", businessApplicationKeyInfo.get("name"));
        businessApplicationKeyInfo.put("tel", businessApplicationKeyInfo.get("tel"));
        businessApplicationKeyInfo.put("startTime", businessApplicationKeyInfo.get("start_time"));
        businessApplicationKeyInfo.put("state", businessApplicationKeyInfo.get("state"));
        businessApplicationKeyInfo.put("age", businessApplicationKeyInfo.get("age"));
        businessApplicationKeyInfo.put("communityId", businessApplicationKeyInfo.get("community_id"));
        businessApplicationKeyInfo.put("typeFlag", businessApplicationKeyInfo.get("type_flag"));
        businessApplicationKeyInfo.put("pwd", businessApplicationKeyInfo.get("pwd"));

        businessApplicationKeyInfo.remove("bId");
        businessApplicationKeyInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessApplicationKey 钥匙申请信息
     */
    protected void autoSaveDelBusinessApplicationKey(Business business, JSONObject businessApplicationKey) {
//自动插入DEL
        Map info = new HashMap();
        info.put("applicationKeyId", businessApplicationKey.getString("applicationKeyId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentApplicationKeyInfos = getApplicationKeyServiceDaoImpl().getApplicationKeyInfo(info);
        if (currentApplicationKeyInfos == null || currentApplicationKeyInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentApplicationKeyInfo = currentApplicationKeyInfos.get(0);

        currentApplicationKeyInfo.put("bId", business.getbId());

        currentApplicationKeyInfo.put("applicationKeyId", currentApplicationKeyInfo.get("application_key_id"));
        currentApplicationKeyInfo.put("idCard", currentApplicationKeyInfo.get("id_card"));
        currentApplicationKeyInfo.put("sex", currentApplicationKeyInfo.get("sex"));
        currentApplicationKeyInfo.put("endTime", currentApplicationKeyInfo.get("end_time"));
        currentApplicationKeyInfo.put("machineId", currentApplicationKeyInfo.get("machine_id"));
        currentApplicationKeyInfo.put("operate", currentApplicationKeyInfo.get("operate"));
        currentApplicationKeyInfo.put("typeCd", currentApplicationKeyInfo.get("type_cd"));
        currentApplicationKeyInfo.put("name", currentApplicationKeyInfo.get("name"));
        currentApplicationKeyInfo.put("tel", currentApplicationKeyInfo.get("tel"));
        currentApplicationKeyInfo.put("startTime", currentApplicationKeyInfo.get("start_time"));
        currentApplicationKeyInfo.put("state", currentApplicationKeyInfo.get("state"));
        currentApplicationKeyInfo.put("age", currentApplicationKeyInfo.get("age"));
        currentApplicationKeyInfo.put("communityId", currentApplicationKeyInfo.get("community_id"));
        currentApplicationKeyInfo.put("typeFlag", currentApplicationKeyInfo.get("type_flag"));
        currentApplicationKeyInfo.put("pwd", currentApplicationKeyInfo.get("pwd"));


        currentApplicationKeyInfo.put("operate", StatusConstant.OPERATE_DEL);
        getApplicationKeyServiceDaoImpl().saveBusinessApplicationKeyInfo(currentApplicationKeyInfo);

        for (Object key : currentApplicationKeyInfo.keySet()) {
            if (businessApplicationKey.get(key) == null) {
                businessApplicationKey.put(key.toString(), currentApplicationKeyInfo.get(key));
            }
        }
    }


}
