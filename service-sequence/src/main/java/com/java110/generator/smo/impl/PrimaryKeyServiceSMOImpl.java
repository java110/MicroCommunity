package com.java110.generator.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.generator.dao.IPrimaryKeyServiceDao;
import com.java110.generator.dao.ISnowflakeldWorker;
import com.java110.generator.smo.IPrimaryKeyServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.CodeDataFlow;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.service.init.ServiceInfoListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service("primaryKeyServiceSMOImpl")
public class PrimaryKeyServiceSMOImpl extends BaseServiceSMO implements IPrimaryKeyServiceSMO {
    protected final static Logger logger = LoggerFactory.getLogger(PrimaryKeyServiceSMOImpl.class);


    @Autowired
    IPrimaryKeyServiceDao iPrimaryKeyServiceDao;

    @Autowired
    ISnowflakeldWorker snowflakeIdWorkerImpl;

    @Autowired
    private ServiceInfoListener serviceInfoListener;

    /**
     * 根据sequence 表中name 查询ID
     *
     * @param primaryKeyInfo name信息封装
     * @return
     */
    public JSONObject queryPrimaryKey(JSONObject primaryKeyInfo) throws Exception {
        Map paramIn = JSONObject.toJavaObject(primaryKeyInfo, Map.class);
        Map primaryKey = iPrimaryKeyServiceDao.queryPrimaryKey(paramIn);
        JSONObject returnPrimaryKey = new JSONObject();
        if (primaryKey != null && primaryKey.containsKey("targetId")) {
            returnPrimaryKey.put("targetId", primaryKey.get("targetId"));
        } else {
            //如果没定义相应name的键序列，直接返回-1 表示 自己系统需要自己生成
            returnPrimaryKey.put("targetId", "-1");
        }
        return returnPrimaryKey;
    }

    public void generateCode(CodeDataFlow dataFlow){
        String code = snowflakeIdWorkerImpl.getIdByPrefix(dataFlow.getPrefix(),serviceInfoListener.getWorkId());

        JSONObject resJson = DataTransactionFactory.createCodeResponseJson(dataFlow.getTransactionId(),code, ResponseConstant.RESULT_CODE_SUCCESS,"成功");

        dataFlow.setResJson(resJson);
    }

    public ServiceInfoListener getServiceInfoListener() {
        return serviceInfoListener;
    }

    public void setServiceInfoListener(ServiceInfoListener serviceInfoListener) {
        this.serviceInfoListener = serviceInfoListener;
    }


    public ISnowflakeldWorker getSnowflakeIdWorkerImpl() {
        return snowflakeIdWorkerImpl;
    }

    public void setSnowflakeIdWorkerImpl(ISnowflakeldWorker snowflakeIdWorkerImpl) {
        this.snowflakeIdWorkerImpl = snowflakeIdWorkerImpl;
    }
}
