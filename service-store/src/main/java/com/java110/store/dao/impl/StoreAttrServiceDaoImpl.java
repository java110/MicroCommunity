package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IStoreAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商户属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeAttrServiceDaoImpl")
//@Transactional
public class StoreAttrServiceDaoImpl extends BaseServiceDao implements IStoreAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StoreAttrServiceDaoImpl.class);

    /**
     * 商户属性信息封装
     * @param businessStoreAttrInfo 商户属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessStoreAttrInfo(Map businessStoreAttrInfo) throws DAOException {
        businessStoreAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商户属性信息 入参 businessStoreAttrInfo : {}",businessStoreAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("storeAttrServiceDaoImpl.saveBusinessStoreAttrInfo",businessStoreAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商户属性数据失败："+ JSONObject.toJSONString(businessStoreAttrInfo));
        }
    }


    /**
     * 查询商户属性信息
     * @param info bId 信息
     * @return 商户属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessStoreAttrInfo(Map info) throws DAOException {

        logger.debug("查询商户属性信息 入参 info : {}",info);

        List<Map> businessStoreAttrInfos = sqlSessionTemplate.selectList("storeAttrServiceDaoImpl.getBusinessStoreAttrInfo",info);

        return businessStoreAttrInfos;
    }



    /**
     * 保存商户属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveStoreAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存商户属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeAttrServiceDaoImpl.saveStoreAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商户属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    /**
     * 保存商户属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveStoreAttrInfo(Map info) throws DAOException {
        logger.debug("保存商户属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeAttrServiceDaoImpl.saveStoreAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商户属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
        return saveFlag;
    }


    /**
     * 查询商户属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStoreAttrInfo(Map info) throws DAOException {
        logger.debug("查询商户属性信息 入参 info : {}",info);

        List<Map> businessStoreAttrInfos = sqlSessionTemplate.selectList("storeAttrServiceDaoImpl.getStoreAttrInfo",info);

        return businessStoreAttrInfos;
    }


    /**
     * 修改商户属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateStoreAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改商户属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeAttrServiceDaoImpl.updateStoreAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商户属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

     /**
     * 查询商户属性数量
     * @param info 商户属性信息
     * @return 商户属性数量
     */
    @Override
    public int queryStoreAttrsCount(Map info) {
        logger.debug("查询商户属性数据 入参 info : {}",info);

        List<Map> businessStoreAttrInfos = sqlSessionTemplate.selectList("storeAttrServiceDaoImpl.queryStoreAttrsCount", info);
        if (businessStoreAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStoreAttrInfos.get(0).get("count").toString());
    }


}
