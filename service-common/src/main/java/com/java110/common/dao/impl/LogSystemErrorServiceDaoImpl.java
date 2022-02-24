package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.ILogSystemErrorServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统异常服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("logSystemErrorServiceDaoImpl")
//@Transactional
public class LogSystemErrorServiceDaoImpl extends BaseServiceDao implements ILogSystemErrorServiceDao {

    private static Logger logger = LoggerFactory.getLogger(LogSystemErrorServiceDaoImpl.class);





    /**
     * 保存系统异常信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveLogSystemErrorInfo(Map info) throws DAOException {
        logger.debug("保存系统异常信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("logSystemErrorServiceDaoImpl.saveLogSystemErrorInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存系统异常信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询系统异常信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getLogSystemErrorInfo(Map info) throws DAOException {
        logger.debug("查询系统异常信息 入参 info : {}",info);

        List<Map> businessLogSystemErrorInfos = sqlSessionTemplate.selectList("logSystemErrorServiceDaoImpl.getLogSystemErrorInfo",info);

        return businessLogSystemErrorInfos;
    }


    /**
     * 修改系统异常信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateLogSystemErrorInfo(Map info) throws DAOException {
        logger.debug("修改系统异常信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("logSystemErrorServiceDaoImpl.updateLogSystemErrorInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改系统异常信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询系统异常数量
     * @param info 系统异常信息
     * @return 系统异常数量
     */
    @Override
    public int queryLogSystemErrorsCount(Map info) {
        logger.debug("查询系统异常数据 入参 info : {}",info);

        List<Map> businessLogSystemErrorInfos = sqlSessionTemplate.selectList("logSystemErrorServiceDaoImpl.queryLogSystemErrorsCount", info);
        if (businessLogSystemErrorInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessLogSystemErrorInfos.get(0).get("count").toString());
    }


}
