package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairUserServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 报修派单服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("repairUserServiceDaoImpl")
//@Transactional
public class RepairUserServiceDaoImpl extends BaseServiceDao implements IRepairUserServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RepairUserServiceDaoImpl.class);

    /**
     * 报修派单信息封装
     * @param businessRepairUserInfo 报修派单信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessRepairUserInfo(Map businessRepairUserInfo) throws DAOException {
        businessRepairUserInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存报修派单信息 入参 businessRepairUserInfo : {}",businessRepairUserInfo);
        int saveFlag = sqlSessionTemplate.insert("repairUserServiceDaoImpl.saveBusinessRepairUserInfo",businessRepairUserInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存报修派单数据失败："+ JSONObject.toJSONString(businessRepairUserInfo));
        }
    }


    /**
     * 查询报修派单信息
     * @param info bId 信息
     * @return 报修派单信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessRepairUserInfo(Map info) throws DAOException {

        logger.debug("查询报修派单信息 入参 info : {}",info);

        List<Map> businessRepairUserInfos = sqlSessionTemplate.selectList("repairUserServiceDaoImpl.getBusinessRepairUserInfo",info);

        return businessRepairUserInfos;
    }



    /**
     * 保存报修派单信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRepairUserInfoInstance(Map info) throws DAOException {
        logger.debug("保存报修派单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("repairUserServiceDaoImpl.saveRepairUserInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存报修派单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询报修派单信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRepairUserInfo(Map info) throws DAOException {
        logger.debug("查询报修派单信息 入参 info : {}",info);

        List<Map> businessRepairUserInfos = sqlSessionTemplate.selectList("repairUserServiceDaoImpl.getRepairUserInfo",info);

        return businessRepairUserInfos;
    }


    /**
     * 修改报修派单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRepairUserInfoInstance(Map info) throws DAOException {
        logger.debug("修改报修派单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("repairUserServiceDaoImpl.updateRepairUserInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改报修派单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询报修派单数量
     * @param info 报修派单信息
     * @return 报修派单数量
     */
    @Override
    public int queryRepairUsersCount(Map info) {
        logger.debug("查询报修派单数据 入参 info : {}",info);

        List<Map> businessRepairUserInfos = sqlSessionTemplate.selectList("repairUserServiceDaoImpl.queryRepairUsersCount", info);
        if (businessRepairUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairUserInfos.get(0).get("count").toString());
    }


}
