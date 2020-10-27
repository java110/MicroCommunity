package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IProductLabelServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 产品标签服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("productLabelServiceDaoImpl")
//@Transactional
public class ProductLabelServiceDaoImpl extends BaseServiceDao implements IProductLabelServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ProductLabelServiceDaoImpl.class);





    /**
     * 保存产品标签信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveProductLabelInfo(Map info) throws DAOException {
        logger.debug("保存产品标签信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("productLabelServiceDaoImpl.saveProductLabelInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存产品标签信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询产品标签信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getProductLabelInfo(Map info) throws DAOException {
        logger.debug("查询产品标签信息 入参 info : {}",info);

        List<Map> businessProductLabelInfos = sqlSessionTemplate.selectList("productLabelServiceDaoImpl.getProductLabelInfo",info);

        return businessProductLabelInfos;
    }


    /**
     * 修改产品标签信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateProductLabelInfo(Map info) throws DAOException {
        logger.debug("修改产品标签信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("productLabelServiceDaoImpl.updateProductLabelInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改产品标签信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询产品标签数量
     * @param info 产品标签信息
     * @return 产品标签数量
     */
    @Override
    public int queryProductLabelsCount(Map info) {
        logger.debug("查询产品标签数据 入参 info : {}",info);

        List<Map> businessProductLabelInfos = sqlSessionTemplate.selectList("productLabelServiceDaoImpl.queryProductLabelsCount", info);
        if (businessProductLabelInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessProductLabelInfos.get(0).get("count").toString());
    }


}
