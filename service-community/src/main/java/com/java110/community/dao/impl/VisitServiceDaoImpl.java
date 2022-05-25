package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IVisitServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访客信息服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("visitServiceDaoImpl")
//@Transactional
public class VisitServiceDaoImpl extends BaseServiceDao implements IVisitServiceDao {

    private static Logger logger = LoggerFactory.getLogger(VisitServiceDaoImpl.class);

    /**
     * 访客信息信息封装
     * @param businessVisitInfo 访客信息信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessVisitInfo(Map businessVisitInfo) throws DAOException {
        businessVisitInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存访客信息信息 入参 businessVisitInfo : {}",businessVisitInfo);
        int saveFlag = sqlSessionTemplate.insert("visitServiceDaoImpl.saveBusinessVisitInfo",businessVisitInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存访客信息数据失败："+ JSONObject.toJSONString(businessVisitInfo));
        }
    }


    /**
     * 查询访客信息信息
     * @param info bId 信息
     * @return 访客信息信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessVisitInfo(Map info) throws DAOException {

        logger.debug("查询访客信息信息 入参 info : {}",info);

        List<Map> businessVisitInfos = sqlSessionTemplate.selectList("visitServiceDaoImpl.getBusinessVisitInfo",info);

        return businessVisitInfos;
    }



    /**
     * 保存访客信息信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveVisitInfoInstance(Map info) throws DAOException {
        logger.debug("保存访客信息信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("visitServiceDaoImpl.saveVisitInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存访客信息信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询访客信息信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getVisitInfo(Map info) throws DAOException {
        logger.debug("查询访客信息信息 入参 info : {}",info);

        List<Map> businessVisitInfos = sqlSessionTemplate.selectList("visitServiceDaoImpl.getVisitInfo",info);

        return businessVisitInfos;
    }


    /**
     * 修改访客信息信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateVisitInfoInstance(Map info) throws DAOException {
        logger.debug("修改访客信息信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("visitServiceDaoImpl.updateVisitInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改访客信息信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询访客信息数量
     * @param info 访客信息信息
     * @return 访客信息数量
     */
    @Override
    public int queryVisitsCount(Map info) {
        logger.debug("查询访客信息数据 入参 info : {}",info);

        List<Map> businessVisitInfos = sqlSessionTemplate.selectList("visitServiceDaoImpl.queryVisitsCount", info);
        if (businessVisitInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessVisitInfos.get(0).get("count").toString());
    }


}
