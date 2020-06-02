package com.java110.log.dao.impl;

import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.log.dao.LogServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 日志服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */

@Service("logServiceDaoImpl")
//@Transactional
public class LogServiceDaoImpl extends BaseServiceDao implements LogServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(LogServiceDaoImpl.class);

    /**
     * 保存日志
     * @param logMessageParams 日志参数信息
     */
    @Override
    public void saveTransactionLog(Map logMessageParams) throws DAOException {
        logger.debug("save log params :{}",logMessageParams);
        try{
            int row = sqlSessionTemplate.insert("logServiceDaoImpl.saveTransactionLog",logMessageParams);
            if(row < 1){
                throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"save log error, nothing to save");
            }
        }catch (Exception e){
            logger.error("save log error:",e);
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"save log error"+e);
        }

    }

    /**
     * 保存日志（交互报文）
     * @param logMessageParams 日志参数信息
     */
    @Override
    public void saveTransactionLogMessage(Map logMessageParams) throws DAOException{
        logger.debug("save log message params :{}",logMessageParams);
        try{
            int row = sqlSessionTemplate.insert("logServiceDaoImpl.saveTransactionLogMessage",logMessageParams);
            if(row < 1){
                throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"save log error, nothing to save");
            }
        }catch (Exception e){
            logger.error("save log error:",e);
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"save log error"+e);
        }

    }
}
