package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IProductSpecDetailServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 产品规格明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("productSpecDetailServiceDaoImpl")
//@Transactional
public class ProductSpecDetailServiceDaoImpl extends BaseServiceDao implements IProductSpecDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ProductSpecDetailServiceDaoImpl.class);





    /**
     * 保存产品规格明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveProductSpecDetailInfo(Map info) throws DAOException {
        logger.debug("保存产品规格明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("productSpecDetailServiceDaoImpl.saveProductSpecDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存产品规格明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询产品规格明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getProductSpecDetailInfo(Map info) throws DAOException {
        logger.debug("查询产品规格明细信息 入参 info : {}",info);

        List<Map> businessProductSpecDetailInfos = sqlSessionTemplate.selectList("productSpecDetailServiceDaoImpl.getProductSpecDetailInfo",info);

        return businessProductSpecDetailInfos;
    }


    /**
     * 修改产品规格明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateProductSpecDetailInfo(Map info) throws DAOException {
        logger.debug("修改产品规格明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("productSpecDetailServiceDaoImpl.updateProductSpecDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改产品规格明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询产品规格明细数量
     * @param info 产品规格明细信息
     * @return 产品规格明细数量
     */
    @Override
    public int queryProductSpecDetailsCount(Map info) {
        logger.debug("查询产品规格明细数据 入参 info : {}",info);

        List<Map> businessProductSpecDetailInfos = sqlSessionTemplate.selectList("productSpecDetailServiceDaoImpl.queryProductSpecDetailsCount", info);
        if (businessProductSpecDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessProductSpecDetailInfos.get(0).get("count").toString());
    }


}
