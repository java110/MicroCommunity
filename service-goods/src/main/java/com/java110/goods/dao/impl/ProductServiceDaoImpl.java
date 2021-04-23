package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IProductServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 产品服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("productServiceDaoImpl")
//@Transactional
public class ProductServiceDaoImpl extends BaseServiceDao implements IProductServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceDaoImpl.class);





    /**
     * 保存产品信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveProductInfo(Map info) throws DAOException {
        logger.debug("保存产品信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("productServiceDaoImpl.saveProductInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存产品信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询产品信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getProductInfo(Map info) throws DAOException {
        logger.debug("查询产品信息 入参 info : {}",info);

        List<Map> businessProductInfos = sqlSessionTemplate.selectList("productServiceDaoImpl.getProductInfo",info);

        return businessProductInfos;
    }


    /**
     * 修改产品信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateProductInfo(Map info) throws DAOException {
        logger.debug("修改产品信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("productServiceDaoImpl.updateProductInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改产品信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询产品数量
     * @param info 产品信息
     * @return 产品数量
     */
    @Override
    public int queryProductsCount(Map info) {
        logger.debug("查询产品数据 入参 info : {}",info);

        List<Map> businessProductInfos = sqlSessionTemplate.selectList("productServiceDaoImpl.queryProductsCount", info);
        if (businessProductInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessProductInfos.get(0).get("count").toString());
    }


}
