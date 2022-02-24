package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IPurchaseApplyDetailServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 订单明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("purchaseApplyDetailServiceDaoImpl")
//@Transactional
public class PurchaseApplyDetailServiceDaoImpl extends BaseServiceDao implements IPurchaseApplyDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PurchaseApplyDetailServiceDaoImpl.class);

    /**
     * 订单明细信息封装
     *
     * @param businessPurchaseApplyDetailInfo 订单明细信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessPurchaseApplyDetailInfo(Map businessPurchaseApplyDetailInfo) throws DAOException {
        businessPurchaseApplyDetailInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存订单明细信息 入参 businessPurchaseApplyDetailInfo : {}", businessPurchaseApplyDetailInfo);
        int saveFlag = sqlSessionTemplate.insert("purchaseApplyDetailServiceDaoImpl.saveBusinessPurchaseApplyDetailInfo", businessPurchaseApplyDetailInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存订单明细数据失败：" + JSONObject.toJSONString(businessPurchaseApplyDetailInfo));
        }
    }


    /**
     * 查询订单明细信息
     *
     * @param info bId 信息
     * @return 订单明细信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessPurchaseApplyDetailInfo(Map info) throws DAOException {

        logger.debug("查询订单明细信息 入参 info : {}", info);

        List<Map> businessPurchaseApplyDetailInfos = sqlSessionTemplate.selectList("purchaseApplyDetailServiceDaoImpl.getBusinessPurchaseApplyDetailInfo", info);

        return businessPurchaseApplyDetailInfos;
    }


    /**
     * 保存订单明细信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePurchaseApplyDetailInfoInstance(Map info) throws DAOException {
        logger.debug("保存订单明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("purchaseApplyDetailServiceDaoImpl.savePurchaseApplyDetailInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存订单明细信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询订单明细信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPurchaseApplyDetailInfo(Map info) throws DAOException {
        logger.debug("查询订单明细信息 入参 info : {}", info);

        List<Map> businessPurchaseApplyDetailInfos = sqlSessionTemplate.selectList("purchaseApplyDetailServiceDaoImpl.getPurchaseApplyDetailInfo", info);

        return businessPurchaseApplyDetailInfos;
    }


    /**
     * 修改订单明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updatePurchaseApplyDetailInfoInstance(Map info) throws DAOException {
        logger.debug("修改订单明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("purchaseApplyDetailServiceDaoImpl.updatePurchaseApplyDetailInfoInstance", info);

        return saveFlag;
    }

    /**
     * 查询订单明细数量
     *
     * @param info 订单明细信息
     * @return 订单明细数量
     */
    @Override
    public int queryPurchaseApplyDetailsCount(Map info) {
        logger.debug("查询订单明细数据 入参 info : {}", info);

        List<Map> businessPurchaseApplyDetailInfos = sqlSessionTemplate.selectList("purchaseApplyDetailServiceDaoImpl.queryPurchaseApplyDetailsCount", info);
        if (businessPurchaseApplyDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPurchaseApplyDetailInfos.get(0).get("count").toString());
    }


}
