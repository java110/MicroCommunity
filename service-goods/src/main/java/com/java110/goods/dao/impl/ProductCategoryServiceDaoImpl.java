package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IProductCategoryServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 产品目录服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("productCategoryServiceDaoImpl")
//@Transactional
public class ProductCategoryServiceDaoImpl extends BaseServiceDao implements IProductCategoryServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ProductCategoryServiceDaoImpl.class);





    /**
     * 保存产品目录信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveProductCategoryInfo(Map info) throws DAOException {
        logger.debug("保存产品目录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("productCategoryServiceDaoImpl.saveProductCategoryInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存产品目录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询产品目录信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getProductCategoryInfo(Map info) throws DAOException {
        logger.debug("查询产品目录信息 入参 info : {}",info);

        List<Map> businessProductCategoryInfos = sqlSessionTemplate.selectList("productCategoryServiceDaoImpl.getProductCategoryInfo",info);

        return businessProductCategoryInfos;
    }


    /**
     * 修改产品目录信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateProductCategoryInfo(Map info) throws DAOException {
        logger.debug("修改产品目录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("productCategoryServiceDaoImpl.updateProductCategoryInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改产品目录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询产品目录数量
     * @param info 产品目录信息
     * @return 产品目录数量
     */
    @Override
    public int queryProductCategorysCount(Map info) {
        logger.debug("查询产品目录数据 入参 info : {}",info);

        List<Map> businessProductCategoryInfos = sqlSessionTemplate.selectList("productCategoryServiceDaoImpl.queryProductCategorysCount", info);
        if (businessProductCategoryInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessProductCategoryInfos.get(0).get("count").toString());
    }


}
