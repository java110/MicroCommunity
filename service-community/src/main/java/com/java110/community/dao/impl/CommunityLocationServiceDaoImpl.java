package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.ICommunityLocationServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 小区位置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("communityLocationServiceDaoImpl")
//@Transactional
public class CommunityLocationServiceDaoImpl extends BaseServiceDao implements ICommunityLocationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CommunityLocationServiceDaoImpl.class);

    /**
     * 小区位置信息封装
     * @param businessCommunityLocationInfo 小区位置信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCommunityLocationInfo(Map businessCommunityLocationInfo) throws DAOException {
        businessCommunityLocationInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区位置信息 入参 businessCommunityLocationInfo : {}",businessCommunityLocationInfo);
        int saveFlag = sqlSessionTemplate.insert("communityLocationServiceDaoImpl.saveBusinessCommunityLocationInfo",businessCommunityLocationInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存小区位置数据失败："+ JSONObject.toJSONString(businessCommunityLocationInfo));
        }
    }


    /**
     * 查询小区位置信息
     * @param info bId 信息
     * @return 小区位置信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCommunityLocationInfo(Map info) throws DAOException {

        logger.debug("查询小区位置信息 入参 info : {}",info);

        List<Map> businessCommunityLocationInfos = sqlSessionTemplate.selectList("communityLocationServiceDaoImpl.getBusinessCommunityLocationInfo",info);

        return businessCommunityLocationInfos;
    }



    /**
     * 保存小区位置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCommunityLocationInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区位置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("communityLocationServiceDaoImpl.saveCommunityLocationInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存小区位置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区位置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCommunityLocationInfo(Map info) throws DAOException {
        logger.debug("查询小区位置信息 入参 info : {}",info);

        List<Map> businessCommunityLocationInfos = sqlSessionTemplate.selectList("communityLocationServiceDaoImpl.getCommunityLocationInfo",info);

        return businessCommunityLocationInfos;
    }


    /**
     * 修改小区位置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCommunityLocationInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区位置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("communityLocationServiceDaoImpl.updateCommunityLocationInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改小区位置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询小区位置数量
     * @param info 小区位置信息
     * @return 小区位置数量
     */
    @Override
    public int queryCommunityLocationsCount(Map info) {
        logger.debug("查询小区位置数据 入参 info : {}",info);

        List<Map> businessCommunityLocationInfos = sqlSessionTemplate.selectList("communityLocationServiceDaoImpl.queryCommunityLocationsCount", info);
        if (businessCommunityLocationInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCommunityLocationInfos.get(0).get("count").toString());
    }


}
