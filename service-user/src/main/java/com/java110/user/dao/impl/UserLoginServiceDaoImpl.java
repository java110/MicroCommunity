package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IUserLoginServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户登录服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("userLoginServiceDaoImpl")
//@Transactional
public class UserLoginServiceDaoImpl extends BaseServiceDao implements IUserLoginServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UserLoginServiceDaoImpl.class);





    /**
     * 保存用户登录信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveUserLoginInfo(Map info) throws DAOException {
        logger.debug("保存用户登录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("userLoginServiceDaoImpl.saveUserLoginInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户登录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询用户登录信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUserLoginInfo(Map info) throws DAOException {
        logger.debug("查询用户登录信息 入参 info : {}",info);

        List<Map> businessUserLoginInfos = sqlSessionTemplate.selectList("userLoginServiceDaoImpl.getUserLoginInfo",info);

        return businessUserLoginInfos;
    }


    /**
     * 修改用户登录信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateUserLoginInfo(Map info) throws DAOException {
        logger.debug("修改用户登录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("userLoginServiceDaoImpl.updateUserLoginInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改用户登录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询用户登录数量
     * @param info 用户登录信息
     * @return 用户登录数量
     */
    @Override
    public int queryUserLoginsCount(Map info) {
        logger.debug("查询用户登录数据 入参 info : {}",info);

        List<Map> businessUserLoginInfos = sqlSessionTemplate.selectList("userLoginServiceDaoImpl.queryUserLoginsCount", info);
        if (businessUserLoginInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUserLoginInfos.get(0).get("count").toString());
    }


}
