package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.ICommunityLocationAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 位置属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("communityLocationAttrServiceDaoImpl")
//@Transactional
public class CommunityLocationAttrServiceDaoImpl extends BaseServiceDao implements ICommunityLocationAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CommunityLocationAttrServiceDaoImpl.class);

    /**
     * 位置属性信息封装
     * @param businessCommunityLocationAttrInfo 位置属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCommunityLocationAttrInfo(Map businessCommunityLocationAttrInfo) throws DAOException {
        businessCommunityLocationAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存位置属性信息 入参 businessCommunityLocationAttrInfo : {}",businessCommunityLocationAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("communityLocationAttrServiceDaoImpl.saveBusinessCommunityLocationAttrInfo",businessCommunityLocationAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存位置属性数据失败："+ JSONObject.toJSONString(businessCommunityLocationAttrInfo));
        }
    }


    /**
     * 查询位置属性信息
     * @param info bId 信息
     * @return 位置属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCommunityLocationAttrInfo(Map info) throws DAOException {

        logger.debug("查询位置属性信息 入参 info : {}",info);

        List<Map> businessCommunityLocationAttrInfos = sqlSessionTemplate.selectList("communityLocationAttrServiceDaoImpl.getBusinessCommunityLocationAttrInfo",info);

        return businessCommunityLocationAttrInfos;
    }



    /**
     * 保存位置属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCommunityLocationAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存位置属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("communityLocationAttrServiceDaoImpl.saveCommunityLocationAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存位置属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询位置属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCommunityLocationAttrInfo(Map info) throws DAOException {
        logger.debug("查询位置属性信息 入参 info : {}",info);

        List<Map> businessCommunityLocationAttrInfos = sqlSessionTemplate.selectList("communityLocationAttrServiceDaoImpl.getCommunityLocationAttrInfo",info);

        return businessCommunityLocationAttrInfos;
    }


    /**
     * 修改位置属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCommunityLocationAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改位置属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("communityLocationAttrServiceDaoImpl.updateCommunityLocationAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改位置属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询位置属性数量
     * @param info 位置属性信息
     * @return 位置属性数量
     */
    @Override
    public int queryCommunityLocationAttrsCount(Map info) {
        logger.debug("查询位置属性数据 入参 info : {}",info);

        List<Map> businessCommunityLocationAttrInfos = sqlSessionTemplate.selectList("communityLocationAttrServiceDaoImpl.queryCommunityLocationAttrsCount", info);
        if (businessCommunityLocationAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCommunityLocationAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveCommunityLocationAttr(Map info) {
        logger.debug("修改位置属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("communityLocationAttrServiceDaoImpl.saveCommunityLocationAttr",info);

        return saveFlag;
    }


}
