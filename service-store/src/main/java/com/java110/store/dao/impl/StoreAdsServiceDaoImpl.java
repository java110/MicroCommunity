package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IStoreAdsServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 商户广告服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeAdsServiceDaoImpl")
//@Transactional
public class StoreAdsServiceDaoImpl extends BaseServiceDao implements IStoreAdsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StoreAdsServiceDaoImpl.class);





    /**
     * 保存商户广告信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStoreAdsInfo(Map info) throws DAOException {
        logger.debug("保存商户广告信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeAdsServiceDaoImpl.saveStoreAdsInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商户广告信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询商户广告信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStoreAdsInfo(Map info) throws DAOException {
        logger.debug("查询商户广告信息 入参 info : {}",info);

        List<Map> businessStoreAdsInfos = sqlSessionTemplate.selectList("storeAdsServiceDaoImpl.getStoreAdsInfo",info);

        return businessStoreAdsInfos;
    }


    /**
     * 修改商户广告信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStoreAdsInfo(Map info) throws DAOException {
        logger.debug("修改商户广告信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeAdsServiceDaoImpl.updateStoreAdsInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商户广告信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询商户广告数量
     * @param info 商户广告信息
     * @return 商户广告数量
     */
    @Override
    public int queryStoreAdssCount(Map info) {
        logger.debug("查询商户广告数据 入参 info : {}",info);

        List<Map> businessStoreAdsInfos = sqlSessionTemplate.selectList("storeAdsServiceDaoImpl.queryStoreAdssCount", info);
        if (businessStoreAdsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStoreAdsInfos.get(0).get("count").toString());
    }


}
