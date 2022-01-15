package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.ITransactionLogMessageServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 交互日志服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("transactionLogMessageServiceDaoImpl")
//@Transactional
public class TransactionLogMessageServiceDaoImpl extends BaseServiceDao implements ITransactionLogMessageServiceDao {

    private static Logger logger = LoggerFactory.getLogger(TransactionLogMessageServiceDaoImpl.class);





    /**
     * 保存交互日志信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveTransactionLogMessageInfo(Map info) throws DAOException {
        logger.debug("保存交互日志信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("transactionLogMessageServiceDaoImpl.saveTransactionLogMessageInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存交互日志信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询交互日志信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTransactionLogMessageInfo(Map info) throws DAOException {
        logger.debug("查询交互日志信息 入参 info : {}",info);

        List<Map> businessTransactionLogMessageInfos = sqlSessionTemplate.selectList("transactionLogMessageServiceDaoImpl.getTransactionLogMessageInfo",info);

        return businessTransactionLogMessageInfos;
    }


    /**
     * 修改交互日志信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateTransactionLogMessageInfo(Map info) throws DAOException {
        logger.debug("修改交互日志信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("transactionLogMessageServiceDaoImpl.updateTransactionLogMessageInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改交互日志信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询交互日志数量
     * @param info 交互日志信息
     * @return 交互日志数量
     */
    @Override
    public int queryTransactionLogMessagesCount(Map info) {
        logger.debug("查询交互日志数据 入参 info : {}",info);

        List<Map> businessTransactionLogMessageInfos = sqlSessionTemplate.selectList("transactionLogMessageServiceDaoImpl.queryTransactionLogMessagesCount", info);
        if (businessTransactionLogMessageInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTransactionLogMessageInfos.get(0).get("count").toString());
    }


}
