package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IRepairTypeUserServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 报修设置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("repairTypeUserServiceDaoImpl")
//@Transactional
public class RepairTypeUserServiceDaoImpl extends BaseServiceDao implements IRepairTypeUserServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RepairTypeUserServiceDaoImpl.class);

    /**
     * 报修设置信息封装
     * @param businessRepairTypeUserInfo 报修设置信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessRepairTypeUserInfo(Map businessRepairTypeUserInfo) throws DAOException {
        businessRepairTypeUserInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存报修设置信息 入参 businessRepairTypeUserInfo : {}",businessRepairTypeUserInfo);
        int saveFlag = sqlSessionTemplate.insert("repairTypeUserServiceDaoImpl.saveBusinessRepairTypeUserInfo",businessRepairTypeUserInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存报修设置数据失败："+ JSONObject.toJSONString(businessRepairTypeUserInfo));
        }
    }


    /**
     * 查询报修设置信息
     * @param info bId 信息
     * @return 报修设置信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessRepairTypeUserInfo(Map info) throws DAOException {

        logger.debug("查询报修设置信息 入参 info : {}",info);

        List<Map> businessRepairTypeUserInfos = sqlSessionTemplate.selectList("repairTypeUserServiceDaoImpl.getBusinessRepairTypeUserInfo",info);

        return businessRepairTypeUserInfos;
    }



    /**
     * 保存报修设置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRepairTypeUserInfoInstance(Map info) throws DAOException {
        logger.debug("保存报修设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("repairTypeUserServiceDaoImpl.saveRepairTypeUserInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存报修设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询报修设置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRepairTypeUserInfo(Map info) throws DAOException {
        logger.debug("查询报修设置信息 入参 info : {}",info);

        List<Map> businessRepairTypeUserInfos = sqlSessionTemplate.selectList("repairTypeUserServiceDaoImpl.getRepairTypeUserInfo",info);

        return businessRepairTypeUserInfos;
    }


    /**
     * 修改报修设置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRepairTypeUserInfoInstance(Map info) throws DAOException {
        logger.debug("修改报修设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("repairTypeUserServiceDaoImpl.updateRepairTypeUserInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改报修设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询报修设置数量
     * @param info 报修设置信息
     * @return 报修设置数量
     */
    @Override
    public int queryRepairTypeUsersCount(Map info) {
        logger.debug("查询报修设置数据 入参 info : {}",info);

        List<Map> businessRepairTypeUserInfos = sqlSessionTemplate.selectList("repairTypeUserServiceDaoImpl.queryRepairTypeUsersCount", info);
        if (businessRepairTypeUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairTypeUserInfos.get(0).get("count").toString());
    }


}
