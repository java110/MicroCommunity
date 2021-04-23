package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IStoreOrderCartServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 订单购物车服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeOrderCartServiceDaoImpl")
//@Transactional
public class StoreOrderCartServiceDaoImpl extends BaseServiceDao implements IStoreOrderCartServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StoreOrderCartServiceDaoImpl.class);





    /**
     * 保存订单购物车信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStoreOrderCartInfo(Map info) throws DAOException {
        logger.debug("保存订单购物车信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeOrderCartServiceDaoImpl.saveStoreOrderCartInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存订单购物车信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询订单购物车信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStoreOrderCartInfo(Map info) throws DAOException {
        logger.debug("查询订单购物车信息 入参 info : {}",info);

        List<Map> businessStoreOrderCartInfos = sqlSessionTemplate.selectList("storeOrderCartServiceDaoImpl.getStoreOrderCartInfo",info);

        return businessStoreOrderCartInfos;
    }


    /**
     * 修改订单购物车信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStoreOrderCartInfo(Map info) throws DAOException {
        logger.debug("修改订单购物车信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeOrderCartServiceDaoImpl.updateStoreOrderCartInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改订单购物车信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询订单购物车数量
     * @param info 订单购物车信息
     * @return 订单购物车数量
     */
    @Override
    public int queryStoreOrderCartsCount(Map info) {
        logger.debug("查询订单购物车数据 入参 info : {}",info);

        List<Map> businessStoreOrderCartInfos = sqlSessionTemplate.selectList("storeOrderCartServiceDaoImpl.queryStoreOrderCartsCount", info);
        if (businessStoreOrderCartInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStoreOrderCartInfos.get(0).get("count").toString());
    }


}
