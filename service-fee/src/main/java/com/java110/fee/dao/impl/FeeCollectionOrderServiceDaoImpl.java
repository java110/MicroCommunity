package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeCollectionOrderServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 催缴单服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeCollectionOrderServiceDaoImpl")
//@Transactional
public class FeeCollectionOrderServiceDaoImpl extends BaseServiceDao implements IFeeCollectionOrderServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeCollectionOrderServiceDaoImpl.class);





    /**
     * 保存催缴单信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeCollectionOrderInfo(Map info) throws DAOException {
        logger.debug("保存催缴单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeCollectionOrderServiceDaoImpl.saveFeeCollectionOrderInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存催缴单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询催缴单信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeCollectionOrderInfo(Map info) throws DAOException {
        logger.debug("查询催缴单信息 入参 info : {}",info);

        List<Map> businessFeeCollectionOrderInfos = sqlSessionTemplate.selectList("feeCollectionOrderServiceDaoImpl.getFeeCollectionOrderInfo",info);

        return businessFeeCollectionOrderInfos;
    }


    /**
     * 修改催缴单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeCollectionOrderInfo(Map info) throws DAOException {
        logger.debug("修改催缴单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeCollectionOrderServiceDaoImpl.updateFeeCollectionOrderInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改催缴单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询催缴单数量
     * @param info 催缴单信息
     * @return 催缴单数量
     */
    @Override
    public int queryFeeCollectionOrdersCount(Map info) {
        logger.debug("查询催缴单数据 入参 info : {}",info);

        List<Map> businessFeeCollectionOrderInfos = sqlSessionTemplate.selectList("feeCollectionOrderServiceDaoImpl.queryFeeCollectionOrdersCount", info);
        if (businessFeeCollectionOrderInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeCollectionOrderInfos.get(0).get("count").toString());
    }


}
