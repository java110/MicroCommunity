package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IStoreCartServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 购物车服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeCartServiceDaoImpl")
//@Transactional
public class StoreCartServiceDaoImpl extends BaseServiceDao implements IStoreCartServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StoreCartServiceDaoImpl.class);





    /**
     * 保存购物车信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStoreCartInfo(Map info) throws DAOException {
        logger.debug("保存购物车信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeCartServiceDaoImpl.saveStoreCartInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存购物车信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询购物车信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStoreCartInfo(Map info) throws DAOException {
        logger.debug("查询购物车信息 入参 info : {}",info);

        List<Map> businessStoreCartInfos = sqlSessionTemplate.selectList("storeCartServiceDaoImpl.getStoreCartInfo",info);

        return businessStoreCartInfos;
    }


    /**
     * 修改购物车信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStoreCartInfo(Map info) throws DAOException {
        logger.debug("修改购物车信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeCartServiceDaoImpl.updateStoreCartInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改购物车信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询购物车数量
     * @param info 购物车信息
     * @return 购物车数量
     */
    @Override
    public int queryStoreCartsCount(Map info) {
        logger.debug("查询购物车数据 入参 info : {}",info);

        List<Map> businessStoreCartInfos = sqlSessionTemplate.selectList("storeCartServiceDaoImpl.queryStoreCartsCount", info);
        if (businessStoreCartInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStoreCartInfos.get(0).get("count").toString());
    }


}
