package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IProductAttrServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 产品属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("productAttrServiceDaoImpl")
//@Transactional
public class ProductAttrServiceDaoImpl extends BaseServiceDao implements IProductAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ProductAttrServiceDaoImpl.class);





    /**
     * 保存产品属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveProductAttrInfo(Map info) throws DAOException {
        logger.debug("保存产品属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("productAttrServiceDaoImpl.saveProductAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存产品属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询产品属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getProductAttrInfo(Map info) throws DAOException {
        logger.debug("查询产品属性信息 入参 info : {}",info);

        List<Map> businessProductAttrInfos = sqlSessionTemplate.selectList("productAttrServiceDaoImpl.getProductAttrInfo",info);

        return businessProductAttrInfos;
    }


    /**
     * 修改产品属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateProductAttrInfo(Map info) throws DAOException {
        logger.debug("修改产品属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("productAttrServiceDaoImpl.updateProductAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改产品属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询产品属性数量
     * @param info 产品属性信息
     * @return 产品属性数量
     */
    @Override
    public int queryProductAttrsCount(Map info) {
        logger.debug("查询产品属性数据 入参 info : {}",info);

        List<Map> businessProductAttrInfos = sqlSessionTemplate.selectList("productAttrServiceDaoImpl.queryProductAttrsCount", info);
        if (businessProductAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessProductAttrInfos.get(0).get("count").toString());
    }


}
