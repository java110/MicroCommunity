package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IProductSpecValueServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 产品规格值服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("productSpecValueServiceDaoImpl")
//@Transactional
public class ProductSpecValueServiceDaoImpl extends BaseServiceDao implements IProductSpecValueServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ProductSpecValueServiceDaoImpl.class);





    /**
     * 保存产品规格值信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveProductSpecValueInfo(Map info) throws DAOException {
        logger.debug("保存产品规格值信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("productSpecValueServiceDaoImpl.saveProductSpecValueInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存产品规格值信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询产品规格值信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getProductSpecValueInfo(Map info) throws DAOException {
        logger.debug("查询产品规格值信息 入参 info : {}",info);

        List<Map> businessProductSpecValueInfos = sqlSessionTemplate.selectList("productSpecValueServiceDaoImpl.getProductSpecValueInfo",info);

        return businessProductSpecValueInfos;
    }



    /**
     * 查询库存和销量（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> queryProductStockAndSales(Map info) throws DAOException {
        logger.debug("查询产品规格值信息 入参 info : {}",info);

        List<Map> businessProductSpecValueInfos = sqlSessionTemplate.selectList("productSpecValueServiceDaoImpl.queryProductStockAndSales",info);

        return businessProductSpecValueInfos;
    }

    /**
     * 修改产品规格值信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateProductSpecValueInfo(Map info) throws DAOException {
        logger.debug("修改产品规格值信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("productSpecValueServiceDaoImpl.updateProductSpecValueInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改产品规格值信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询产品规格值数量
     * @param info 产品规格值信息
     * @return 产品规格值数量
     */
    @Override
    public int queryProductSpecValuesCount(Map info) {
        logger.debug("查询产品规格值数据 入参 info : {}",info);

        List<Map> businessProductSpecValueInfos = sqlSessionTemplate.selectList("productSpecValueServiceDaoImpl.queryProductSpecValuesCount", info);
        if (businessProductSpecValueInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessProductSpecValueInfos.get(0).get("count").toString());
    }


}
