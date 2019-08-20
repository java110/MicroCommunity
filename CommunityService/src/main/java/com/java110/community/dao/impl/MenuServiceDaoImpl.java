package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.community.dao.IMenuServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 路由服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("menuServiceDaoImpl")
//@Transactional
public class MenuServiceDaoImpl extends BaseServiceDao implements IMenuServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MenuServiceDaoImpl.class);


    /**
     * 保存路由信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveMenuGroupInfo(Map info) throws DAOException {
        logger.debug("保存路由信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("menuServiceDaoImpl.saveMenuGroupInfo",info);

        return saveFlag;
    }


    /**
     * 查询路由信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMenuGroupInfo(Map info) throws DAOException {
        logger.debug("查询路由信息 入参 info : {}",info);

        List<Map> businessMenuGroupInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.getMenuGroupInfo",info);

        return businessMenuGroupInfos;
    }


    /**
     * 修改路由信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateMenuGroupInfo(Map info) throws DAOException {
        logger.debug("修改路由信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateMenuGroupInfo",info);

        return saveFlag;
    }

     /**
     * 查询路由数量
     * @param info 路由信息
     * @return 路由数量
     */
    @Override
    public int queryMenuGroupsCount(Map info) {
        logger.debug("查询路由数据 入参 info : {}",info);

        List<Map> businessMenuGroupInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.queryMenuGroupsCount", info);
        if (businessMenuGroupInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMenuGroupInfos.get(0).get("count").toString());
    }


    /**
     * 保存路由信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveBasePrivilegeInfo(Map info) throws DAOException {
        logger.debug("保存路由信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("menuServiceDaoImpl.saveBasePrivilegeInfo",info);

        return saveFlag;
    }


    /**
     * 查询路由信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBasePrivilegeInfo(Map info) throws DAOException {
        logger.debug("查询路由信息 入参 info : {}",info);

        List<Map> businessBasePrivilegeInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.getBasePrivilegeInfo",info);

        return businessBasePrivilegeInfos;
    }


    /**
     * 修改路由信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateBasePrivilegeInfo(Map info) throws DAOException {
        logger.debug("修改路由信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateBasePrivilegeInfo",info);

        return saveFlag;
    }

    /**
     * 查询路由数量
     * @param info 路由信息
     * @return 路由数量
     */
    @Override
    public int queryBasePrivilegesCount(Map info) {
        logger.debug("查询路由数据 入参 info : {}",info);

        List<Map> businessBasePrivilegeInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.queryBasePrivilegesCount", info);
        if (businessBasePrivilegeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessBasePrivilegeInfos.get(0).get("count").toString());
    }

}
