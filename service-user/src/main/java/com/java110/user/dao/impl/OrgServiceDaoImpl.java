package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOrgServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 组织服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("orgServiceDaoImpl")
//@Transactional
public class OrgServiceDaoImpl extends BaseServiceDao implements IOrgServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OrgServiceDaoImpl.class);

    /**
     * 组织信息封装
     * @param businessOrgInfo 组织信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOrgInfo(Map businessOrgInfo) throws DAOException {
        businessOrgInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存组织信息 入参 businessOrgInfo : {}",businessOrgInfo);
        int saveFlag = sqlSessionTemplate.insert("orgServiceDaoImpl.saveBusinessOrgInfo",businessOrgInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存组织数据失败："+ JSONObject.toJSONString(businessOrgInfo));
        }
    }


    /**
     * 查询组织信息
     * @param info bId 信息
     * @return 组织信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOrgInfo(Map info) throws DAOException {

        logger.debug("查询组织信息 入参 info : {}",info);

        List<Map> businessOrgInfos = sqlSessionTemplate.selectList("orgServiceDaoImpl.getBusinessOrgInfo",info);

        return businessOrgInfos;
    }



    /**
     * 保存组织信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOrgInfoInstance(Map info) throws DAOException {
        logger.debug("保存组织信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("orgServiceDaoImpl.saveOrgInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存组织信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询组织信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOrgInfo(Map info) throws DAOException {
        logger.debug("查询组织信息 入参 info : {}",info);

        List<Map> businessOrgInfos = sqlSessionTemplate.selectList("orgServiceDaoImpl.getOrgInfo",info);

        return businessOrgInfos;
    }

    @Override
    public List<Map> getParentOrgInfo(Map info) throws DAOException {
        logger.debug("查询上级组织信息 getParentOrgInfo 入参 info : {}",info);

        List<Map> businessOrgInfos = sqlSessionTemplate.selectList("orgServiceDaoImpl.getParentOrgInfo",info);

        return businessOrgInfos;    }


    /**
     * 修改组织信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOrgInfoInstance(Map info) throws DAOException {
        logger.debug("修改组织信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("orgServiceDaoImpl.updateOrgInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改组织信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询组织数量
     * @param info 组织信息
     * @return 组织数量
     */
    @Override
    public int queryOrgsCount(Map info) {
        logger.debug("查询组织数据 入参 info : {}",info);

        List<Map> businessOrgInfos = sqlSessionTemplate.selectList("orgServiceDaoImpl.queryOrgsCount", info);
        if (businessOrgInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOrgInfos.get(0).get("count").toString());
    }


}
