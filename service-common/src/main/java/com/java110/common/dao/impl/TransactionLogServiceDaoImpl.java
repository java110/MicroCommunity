package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ITransactionLogServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 交互日志服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("transactionLogServiceDaoImpl")
//@Transactional
public class TransactionLogServiceDaoImpl extends BaseServiceDao implements ITransactionLogServiceDao {

    private static Logger logger = LoggerFactory.getLogger(TransactionLogServiceDaoImpl.class);


    /**
     * 保存交互日志信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveTransactionLogInfo(Map info) throws DAOException {
        logger.debug("保存交互日志信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("transactionLogServiceDaoImpl.saveTransactionLogInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存交互日志信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询交互日志信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTransactionLogInfo(Map info) throws DAOException {
        logger.debug("查询交互日志信息 入参 info : {}", info);

        List<Map> businessTransactionLogInfos = sqlSessionTemplate.selectList("transactionLogServiceDaoImpl.getTransactionLogInfo", info);

        return businessTransactionLogInfos;
    }


    /**
     * 修改交互日志信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateTransactionLogInfo(Map info) throws DAOException {
        logger.debug("修改交互日志信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("transactionLogServiceDaoImpl.updateTransactionLogInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改交互日志信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询交互日志数量
     *
     * @param info 交互日志信息
     * @return 交互日志数量
     */
    @Override
    public int queryTransactionLogsCount(Map info) {
        logger.debug("查询交互日志数据 入参 info : {}", info);

        List<Map> businessTransactionLogInfos = sqlSessionTemplate.selectList("transactionLogServiceDaoImpl.queryTransactionLogsCount", info);
        if (businessTransactionLogInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTransactionLogInfos.get(0).get("count").toString());
    }


}
