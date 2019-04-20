package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IStoreServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 商户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeServiceDaoImpl")
//@Transactional
public class StoreServiceDaoImpl extends BaseServiceDao implements IStoreServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(StoreServiceDaoImpl.class);

    /**
     * 商户信息封装
     * @param businessStoreInfo 商户信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessStoreInfo(Map businessStoreInfo) throws DAOException {
        businessStoreInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商户信息 入参 businessStoreInfo : {}",businessStoreInfo);
        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveBusinessStoreInfo",businessStoreInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商户数据失败："+ JSONObject.toJSONString(businessStoreInfo));
        }
    }


    /**
     * 查询商户信息
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    @Override
    public Map getBusinessStoreInfo(Map info) throws DAOException {

        logger.debug("查询商户信息 入参 info : {}",info);

        List<Map> businessStoreInfos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getBusinessStoreInfo",info);
        if(businessStoreInfos == null){
            return null;
        }
        if(businessStoreInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessStoreInfos，"+ JSONObject.toJSONString(info));
        }

        return businessStoreInfos.get(0);
    }



    /**
     * 保存商户信息 到 instance
     * @param info   bId 信息
     * @throws DAOException
     */
    @Override
    public void saveStoreInfoInstance(Map info) throws DAOException {
        logger.debug("保存商户信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveStoreInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商户信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询商户信息（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getStoreInfo(Map info) throws DAOException {
        logger.debug("查询商户信息 入参 info : {}",info);

        List<Map> businessStoreInfos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreInfo",info);
        if(businessStoreInfos == null || businessStoreInfos.size() == 0){
            return null;
        }
        if(businessStoreInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getStoreInfo，"+ JSONObject.toJSONString(info));
        }

        return businessStoreInfos.get(0);
    }


    /**
     * 修改商户信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateStoreInfoInstance(Map info) throws DAOException {
        logger.debug("修改商户信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateStoreInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商户信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


}
