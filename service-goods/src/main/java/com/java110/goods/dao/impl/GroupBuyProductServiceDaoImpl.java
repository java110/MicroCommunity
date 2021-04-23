package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IGroupBuyProductServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 拼团产品服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("groupBuyProductServiceDaoImpl")
//@Transactional
public class GroupBuyProductServiceDaoImpl extends BaseServiceDao implements IGroupBuyProductServiceDao {

    private static Logger logger = LoggerFactory.getLogger(GroupBuyProductServiceDaoImpl.class);





    /**
     * 保存拼团产品信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveGroupBuyProductInfo(Map info) throws DAOException {
        logger.debug("保存拼团产品信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("groupBuyProductServiceDaoImpl.saveGroupBuyProductInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存拼团产品信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询拼团产品信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getGroupBuyProductInfo(Map info) throws DAOException {
        logger.debug("查询拼团产品信息 入参 info : {}",info);

        List<Map> businessGroupBuyProductInfos = sqlSessionTemplate.selectList("groupBuyProductServiceDaoImpl.getGroupBuyProductInfo",info);

        return businessGroupBuyProductInfos;
    }


    /**
     * 修改拼团产品信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateGroupBuyProductInfo(Map info) throws DAOException {
        logger.debug("修改拼团产品信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("groupBuyProductServiceDaoImpl.updateGroupBuyProductInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改拼团产品信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询拼团产品数量
     * @param info 拼团产品信息
     * @return 拼团产品数量
     */
    @Override
    public int queryGroupBuyProductsCount(Map info) {
        logger.debug("查询拼团产品数据 入参 info : {}",info);

        List<Map> businessGroupBuyProductInfos = sqlSessionTemplate.selectList("groupBuyProductServiceDaoImpl.queryGroupBuyProductsCount", info);
        if (businessGroupBuyProductInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessGroupBuyProductInfos.get(0).get("count").toString());
    }


}
