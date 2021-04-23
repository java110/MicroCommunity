package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IStoreOrderAddressServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 发货地址服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeOrderAddressServiceDaoImpl")
//@Transactional
public class StoreOrderAddressServiceDaoImpl extends BaseServiceDao implements IStoreOrderAddressServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StoreOrderAddressServiceDaoImpl.class);





    /**
     * 保存发货地址信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStoreOrderAddressInfo(Map info) throws DAOException {
        logger.debug("保存发货地址信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeOrderAddressServiceDaoImpl.saveStoreOrderAddressInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存发货地址信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询发货地址信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStoreOrderAddressInfo(Map info) throws DAOException {
        logger.debug("查询发货地址信息 入参 info : {}",info);

        List<Map> businessStoreOrderAddressInfos = sqlSessionTemplate.selectList("storeOrderAddressServiceDaoImpl.getStoreOrderAddressInfo",info);

        return businessStoreOrderAddressInfos;
    }


    /**
     * 修改发货地址信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStoreOrderAddressInfo(Map info) throws DAOException {
        logger.debug("修改发货地址信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeOrderAddressServiceDaoImpl.updateStoreOrderAddressInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改发货地址信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询发货地址数量
     * @param info 发货地址信息
     * @return 发货地址数量
     */
    @Override
    public int queryStoreOrderAddresssCount(Map info) {
        logger.debug("查询发货地址数据 入参 info : {}",info);

        List<Map> businessStoreOrderAddressInfos = sqlSessionTemplate.selectList("storeOrderAddressServiceDaoImpl.queryStoreOrderAddresssCount", info);
        if (businessStoreOrderAddressInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStoreOrderAddressInfos.get(0).get("count").toString());
    }


}
