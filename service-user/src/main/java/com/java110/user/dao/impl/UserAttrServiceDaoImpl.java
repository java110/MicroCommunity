package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IUserAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("userAttrServiceDaoImpl")
//@Transactional
public class UserAttrServiceDaoImpl extends BaseServiceDao implements IUserAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UserAttrServiceDaoImpl.class);

    /**
     * 用户属性信息封装
     * @param businessUserAttrInfo 用户属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessUserAttrInfo(Map businessUserAttrInfo) throws DAOException {
        businessUserAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存用户属性信息 入参 businessUserAttrInfo : {}",businessUserAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("userAttrServiceDaoImpl.saveBusinessUserAttrInfo",businessUserAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户属性数据失败："+ JSONObject.toJSONString(businessUserAttrInfo));
        }
    }


    /**
     * 查询用户属性信息
     * @param info bId 信息
     * @return 用户属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessUserAttrInfo(Map info) throws DAOException {

        logger.debug("查询用户属性信息 入参 info : {}",info);

        List<Map> businessUserAttrInfos = sqlSessionTemplate.selectList("userAttrServiceDaoImpl.getBusinessUserAttrInfo",info);

        return businessUserAttrInfos;
    }



    /**
     * 保存用户属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveUserAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存用户属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("userAttrServiceDaoImpl.saveUserAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询用户属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUserAttrInfo(Map info) throws DAOException {
        logger.debug("查询用户属性信息 入参 info : {}",info);

        List<Map> businessUserAttrInfos = sqlSessionTemplate.selectList("userAttrServiceDaoImpl.getUserAttrInfo",info);

        return businessUserAttrInfos;
    }


    /**
     * 修改用户属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateUserAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改用户属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("userAttrServiceDaoImpl.updateUserAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改用户属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

     /**
     * 查询用户属性数量
     * @param info 用户属性信息
     * @return 用户属性数量
     */
    @Override
    public int queryUserAttrsCount(Map info) {
        logger.debug("查询用户属性数据 入参 info : {}",info);

        List<Map> businessUserAttrInfos = sqlSessionTemplate.selectList("userAttrServiceDaoImpl.queryUserAttrsCount", info);
        if (businessUserAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUserAttrInfos.get(0).get("count").toString());
    }

    /**
     * 保存用户属性
     * @param info
     * @return
     */
    @Override
    public int saveUserAttr(Map info) {
        int saveFlag = sqlSessionTemplate.insert("userAttrServiceDaoImpl.saveUserAttr",info);
        return saveFlag;
    }


}
