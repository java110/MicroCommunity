package com.java110.order.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.order.dao.IPrivilegeDAO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 权限数据层操作
 * Created by Administrator on 2019/4/1.
 */
@Service("privilegeDAOImpl")
public class PrivilegeDAOImpl extends BaseServiceDao implements IPrivilegeDAO {
    protected final static Logger logger = LoggerFactory.getLogger(PrivilegeDAOImpl.class);

    @Override
    public boolean saveUserDefaultPrivilege(Map info) {
        logger.debug("用户默认权限保存入参：{}", info);
        List<Map> ps = sqlSessionTemplate.selectList("privilegeDAOImpl.queryUserDefaultPrivilege", info);
        if (ps.size() > 0) {
            //说明用户已经添加了默认权限组，这里不做处理直接返回
            return true;
        }
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.saveUserDefaultPrivilege", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存权限信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }


    /**
     * 删除用所有权限
     *
     * @param info
     * @return
     */
    public boolean deleteUserAllPrivilege(Map info) {
        logger.debug("用户默认权限保存入参：{}", info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.deleteUserAllPrivilege", info);
//        if (saveFlag < 1) {
//            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "删除权限信息失败：" + JSONObject.toJSONString(info));
//        }
        return true;
    }

    /**
     * 保存权限组
     *
     * @param info
     * @return
     */
    @Override
    public boolean savePrivilegeGroup(Map info) {
        logger.debug("保存权限组信息入参：{}", info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.savePrivilegeGroup", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存权限组信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * 保存权限组
     *
     * @param info
     * @return
     */
    @Override
    public boolean updatePrivilegeGroup(Map info) {
        logger.debug("编辑权限组信息入参：{}", info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.updatePrivilegeGroup", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "编辑权限组信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * 删除权限组
     *
     * @param info
     * @return
     */
    @Override
    public boolean deletePrivilegeGroup(Map info) {
        logger.debug("删除权限组信息入参：{}", info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.deletePrivilegeGroup", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "删除权限组信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * 删除权限组下权限
     *
     * @param info
     * @return
     */
    @Override
    public boolean deletePrivilegeRel(Map info) {
        logger.debug("删除权限组下权限信息入参：{}", info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.deletePrivilegeRel", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "删除权限组下权限信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * 查询权限组下权限
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryPrivilegeRel(Map info) {
        logger.debug("查询权限组下权限信息入参：{}", info);
        return sqlSessionTemplate.selectList("privilegeDAOImpl.queryPrivilegeRel", info);
    }

    public List<Map> queryPrivilegeGroup(Map info) {
        logger.debug("查询权限组下权限信息入参：{}", info);
        return sqlSessionTemplate.selectList("privilegeDAOImpl.queryPrivilegeGroup", info);
    }

    @Override
    public boolean addPrivilegeRel(Map info) {
        logger.debug("添加权限组下权限信息入参：{}", info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.addPrivilegeRel", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "添加权限组下权限信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * 查询权限
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryPrivilege(Map info) {
        logger.debug("查询权限信息入参：{}", info);
        return sqlSessionTemplate.selectList("privilegeDAOImpl.queryPrivilege", info);
    }

    /**
     * 查询用户默认权限
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryUserPrivilege(Map info) {
        logger.debug("查询用户权限信息入参：{}", info);
        return sqlSessionTemplate.selectList("privilegeDAOImpl.queryUserPrivilege", info);
    }

    /**
     * 添加用户权限
     *
     * @param info
     * @return
     */
    @Override
    public boolean addUserPrivilege(Map info) {
        logger.debug("添加用户权限信息入参：{}", info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.addUserPrivilege", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "添加用户权限信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * 删除用户权限
     *
     * @param info
     * @return
     */
    @Override
    public boolean deleteUserPrivilege(Map info) {
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.deleteUserPrivilege", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "删除权限信息失败：" + JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * Query employees with this permission
     *
     * @param info this permission
     * @return employees
     */
    @Override
    public List<Map> queryPrivilegeUsers(Map info) {
        logger.debug("查询特定权限员工入参：{}", info);
        return sqlSessionTemplate.selectList("privilegeDAOImpl.queryPrivilegeUsers", info);
    }


}
