package com.java110.order.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.order.dao.IPrivilegeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        logger.debug("用户默认权限保存入参：{}",info);
        List<Map> ps = sqlSessionTemplate.selectList("privilegeDAOImpl.queryUserDefaultPrivilege",info);
        if(ps.size()>0){
            //说明用户已经添加了默认权限组，这里不做处理直接返回
            return true;
        }
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.saveUserDefaultPrivilege",info);
        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"保存权限信息失败："+ JSONObject.toJSONString(info));
        }
        return true;
    }


    /**
     * 删除用所有权限
     * @param info
     * @return
     */
    public boolean deleteUserAllPrivilege(Map info){
        logger.debug("用户默认权限保存入参：{}",info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.deleteUserAllPrivilege",info);
        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"删除权限信息失败："+ JSONObject.toJSONString(info));
        }
        return true;
    }

    /**
     * 保存权限组
     * @param info
     * @return
     */
    @Override
    public boolean savePrivilegeGroup(Map info) {
        logger.debug("保存权限组信息入参：{}",info);
        int saveFlag = sqlSessionTemplate.insert("privilegeDAOImpl.savePrivilegeGroup",info);
        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"保存权限组信息失败："+ JSONObject.toJSONString(info));
        }
        return true;
    }
}
