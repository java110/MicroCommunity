package com.java110.community.dao.impl;

import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.DomainContant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.DAOException;
import com.java110.community.dao.IMenuServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("menuServiceDaoImpl")
@Transactional
public class MenuServiceDaoImpl extends BaseServiceDao implements IMenuServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MenuServiceDaoImpl.class);


    /**
     * 保存路由信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveMenuGroupInfo(Map info) throws DAOException {
        logger.debug("保存路由信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("menuServiceDaoImpl.saveMenuGroupInfo", info);

        return saveFlag;
    }


    /**
     * 查询路由信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMenuGroupInfo(Map info) throws DAOException {
        logger.debug("查询路由信息 入参 info : {}", info);

        List<Map> businessMenuGroupInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.getMenuGroupInfo", info);

        return businessMenuGroupInfos;
    }


    /**
     * 修改路由信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateMenuGroupInfo(Map info) throws DAOException {
        logger.debug("修改路由信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateMenuGroupInfo", info);

        return saveFlag;
    }

    /**
     * 查询路由数量
     *
     * @param info 路由信息
     * @return 路由数量
     */
    @Override
    public int queryMenuGroupsCount(Map info) {
        logger.debug("查询路由数据 入参 info : {}", info);

        List<Map> businessMenuGroupInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.queryMenuGroupsCount", info);
        if (businessMenuGroupInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMenuGroupInfos.get(0).get("count").toString());
    }


    /**
     * 保存路由信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveBasePrivilegeInfo(Map info) throws DAOException {
        logger.debug("保存路由信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("menuServiceDaoImpl.saveBasePrivilegeInfo", info);

        if (saveFlag < 1) {
            return saveFlag;
        }


        // 将权限组分配给对应商户类型管理员
        info.put("pgId", MappingCache.getValue(DomainContant.DEFAULT_PRIVILEGE_ADMIN, info.get("domain").toString()));

        saveFlag = sqlSessionTemplate.insert("menuServiceDaoImpl.saveBasePrivilegeRelInfo", info);


        return saveFlag;
    }


    /**
     * 查询路由信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBasePrivilegeInfo(Map info) throws DAOException {
        logger.debug("查询路由信息 入参 info : {}", info);

        List<Map> businessBasePrivilegeInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.getBasePrivilegeInfo", info);

        return businessBasePrivilegeInfos;
    }


    /**
     * 修改路由信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateBasePrivilegeInfo(Map info) throws DAOException {
        logger.debug("修改路由信息Instance 入参 info : {}", info);
        int saveFlag = 0;
        //判断是否为删除
        if (info.containsKey("statusCd") && StatusConstant.STATUS_CD_INVALID.equals(info.get("statusCd"))) {
            //做查询
            List<Map> basePrivileges = getBasePrivilegeInfo(info);

            if (basePrivileges != null && basePrivileges.size() > 0) {
                saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateBasePrivilegeRelInfo", info);

                if (saveFlag < 1) {
                    return saveFlag;
                }
            }

        }

        saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateBasePrivilegeInfo", info);
        return saveFlag;
    }

    /**
     * 查询路由数量
     *
     * @param info 路由信息
     * @return 路由数量
     */
    @Override
    public int queryBasePrivilegesCount(Map info) {
        logger.debug("查询路由数据 入参 info : {}", info);

        List<Map> businessBasePrivilegeInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.queryBasePrivilegesCount", info);
        if (businessBasePrivilegeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessBasePrivilegeInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> checkUserHasResource(Map info) {
        logger.debug("查询路由信息 入参 info : {}", info);

        List<Map> businessBasePrivilegeInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.checkUserHasResource", info);

        return businessBasePrivilegeInfos;
    }


    /**
     * 保存路由信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveMenuInfo(Map info) throws DAOException {
        logger.debug("保存路由信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("menuServiceDaoImpl.saveMenuInfo", info);

        return saveFlag;
    }


    /**
     * 查询路由信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMenuInfo(Map info) throws DAOException {
        logger.debug("查询路由信息 入参 info : {}", info);

        List<Map> businessMenuInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.getMenuInfo", info);

        return businessMenuInfos;
    }


    /**
     * 修改路由信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateMenuInfo(Map info) throws DAOException {
        logger.debug("修改路由信息Instance 入参 info : {}", info);

        int saveFlag = 0;

        //判断是否为删除
        if (info.containsKey("statusCd") && StatusConstant.STATUS_CD_INVALID.equals(info.get("statusCd"))) {
            //做查询
            List<Map> baseMenus = getMenuInfo(info);

            if (baseMenus != null && baseMenus.size() > 0) {
                Map privilegeInfo = new HashMap();
                privilegeInfo.put("pId", baseMenus.get(0).get("pId"));
                privilegeInfo.put("statusCd", StatusConstant.STATUS_CD_INVALID);
                //删除权限 权限组关系
                saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateBasePrivilegeRelInfo", info);

                if (saveFlag < 1) {
                    return saveFlag;
                }
                //删除权限
                saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateBasePrivilegeInfo", info);

                if (saveFlag < 1) {
                    return saveFlag;
                }
            }

        }

        saveFlag = sqlSessionTemplate.update("menuServiceDaoImpl.updateMenuInfo", info);

        return saveFlag;
    }

    /**
     * 查询路由数量
     *
     * @param info 路由信息
     * @return 路由数量
     */
    @Override
    public int queryMenusCount(Map info) {
        logger.debug("查询路由数据 入参 info : {}", info);

        List<Map> businessMenuInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.queryMenusCount", info);
        if (businessMenuInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMenuInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> hasPrivilege(Map info) {
        logger.debug("查询路由信息 入参 info : {}", info);

        List<Map> businessMenuInfos = sqlSessionTemplate.selectList("menuServiceDaoImpl.hasPrivilege", info);

        return businessMenuInfos;
    }


}
