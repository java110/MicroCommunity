package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IPurchaseApplyServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 采购申请服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("purchaseApplyServiceDaoImpl")
//@Transactional
public class PurchaseApplyServiceDaoImpl extends BaseServiceDao implements IPurchaseApplyServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PurchaseApplyServiceDaoImpl.class);

    /**
     * 采购申请信息封装
     * @param businessPurchaseApplyInfo 采购申请信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessPurchaseApplyInfo(Map businessPurchaseApplyInfo) throws DAOException {
        businessPurchaseApplyInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存采购申请信息 入参 businessPurchaseApplyInfo : {}",businessPurchaseApplyInfo);
        int saveFlag = sqlSessionTemplate.insert("purchaseApplyServiceDaoImpl.saveBusinessPurchaseApplyInfo",businessPurchaseApplyInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存采购申请数据失败："+ JSONObject.toJSONString(businessPurchaseApplyInfo));
        }
    }


    /**
     * 查询采购申请信息
     * @param info bId 信息
     * @return 采购申请信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessPurchaseApplyInfo(Map info) throws DAOException {

        logger.debug("查询采购申请信息 入参 info : {}",info);

        List<Map> businessPurchaseApplyInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getBusinessPurchaseApplyInfo",info);

        return businessPurchaseApplyInfos;
    }



    /**
     * 保存采购申请信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePurchaseApplyInfoInstance(Map info) throws DAOException {
        logger.debug("保存采购申请信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("purchaseApplyServiceDaoImpl.savePurchaseApplyInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存采购申请信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询采购申请信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPurchaseApplyInfo(Map info) throws DAOException {
        logger.debug("查询采购申请信息 入参 info : {}",info);

        List<Map> businessPurchaseApplyInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getPurchaseApplyInfo",info);

        return businessPurchaseApplyInfos;
    }


    /**
     * 修改采购申请信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePurchaseApplyInfoInstance(Map info) throws DAOException {
        logger.debug("修改采购申请信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("purchaseApplyServiceDaoImpl.updatePurchaseApplyInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改采购申请信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询采购申请数量
     * @param info 采购申请信息
     * @return 采购申请数量
     */
    @Override
    public int queryPurchaseApplysCount(Map info) {
        logger.debug("查询采购申请数据 入参 info : {}",info);

        List<Map> businessPurchaseApplyInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.queryPurchaseApplysCount", info);
        if (businessPurchaseApplyInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPurchaseApplyInfos.get(0).get("count").toString());
    }


}
