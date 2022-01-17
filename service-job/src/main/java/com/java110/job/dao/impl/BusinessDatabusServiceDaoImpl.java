package com.java110.job.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.job.dao.IBusinessDatabusServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 业务数据同步服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("businessDatabusServiceDaoImpl")
//@Transactional
public class BusinessDatabusServiceDaoImpl extends BaseServiceDao implements IBusinessDatabusServiceDao {

    private static Logger logger = LoggerFactory.getLogger(BusinessDatabusServiceDaoImpl.class);





    /**
     * 保存业务数据同步信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessDatabusInfo(Map info) throws DAOException {
        logger.debug("保存业务数据同步信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("businessDatabusServiceDaoImpl.saveBusinessDatabusInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存业务数据同步信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询业务数据同步信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessDatabusInfo(Map info) throws DAOException {
        logger.debug("查询业务数据同步信息 入参 info : {}",info);

        List<Map> businessBusinessDatabusInfos = sqlSessionTemplate.selectList("businessDatabusServiceDaoImpl.getBusinessDatabusInfo",info);

        return businessBusinessDatabusInfos;
    }


    /**
     * 修改业务数据同步信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateBusinessDatabusInfo(Map info) throws DAOException {
        logger.debug("修改业务数据同步信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("businessDatabusServiceDaoImpl.updateBusinessDatabusInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改业务数据同步信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询业务数据同步数量
     * @param info 业务数据同步信息
     * @return 业务数据同步数量
     */
    @Override
    public int queryBusinessDatabussCount(Map info) {
        logger.debug("查询业务数据同步数据 入参 info : {}",info);

        List<Map> businessBusinessDatabusInfos = sqlSessionTemplate.selectList("businessDatabusServiceDaoImpl.queryBusinessDatabussCount", info);
        if (businessBusinessDatabusInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessBusinessDatabusInfos.get(0).get("count").toString());
    }


}
