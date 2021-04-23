package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IStoreOrderCartReturnServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 购物车事件服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeOrderCartReturnServiceDaoImpl")
//@Transactional
public class StoreOrderCartReturnServiceDaoImpl extends BaseServiceDao implements IStoreOrderCartReturnServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StoreOrderCartReturnServiceDaoImpl.class);





    /**
     * 保存购物车事件信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStoreOrderCartReturnInfo(Map info) throws DAOException {
        logger.debug("保存购物车事件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeOrderCartReturnServiceDaoImpl.saveStoreOrderCartReturnInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存购物车事件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询购物车事件信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStoreOrderCartReturnInfo(Map info) throws DAOException {
        logger.debug("查询购物车事件信息 入参 info : {}",info);

        List<Map> businessStoreOrderCartReturnInfos = sqlSessionTemplate.selectList("storeOrderCartReturnServiceDaoImpl.getStoreOrderCartReturnInfo",info);

        return businessStoreOrderCartReturnInfos;
    }


    /**
     * 修改购物车事件信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStoreOrderCartReturnInfo(Map info) throws DAOException {
        logger.debug("修改购物车事件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeOrderCartReturnServiceDaoImpl.updateStoreOrderCartReturnInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改购物车事件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询购物车事件数量
     * @param info 购物车事件信息
     * @return 购物车事件数量
     */
    @Override
    public int queryStoreOrderCartReturnsCount(Map info) {
        logger.debug("查询购物车事件数据 入参 info : {}",info);

        List<Map> businessStoreOrderCartReturnInfos = sqlSessionTemplate.selectList("storeOrderCartReturnServiceDaoImpl.queryStoreOrderCartReturnsCount", info);
        if (businessStoreOrderCartReturnInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStoreOrderCartReturnInfos.get(0).get("count").toString());
    }


}
