package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeCollectionDetailServiceDao;
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
@Service("feeCollectionDetailServiceDaoImpl")
//@Transactional
public class FeeCollectionDetailServiceDaoImpl extends BaseServiceDao implements IFeeCollectionDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeCollectionDetailServiceDaoImpl.class);





    /**
     * 保存催缴单信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("保存催缴单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeCollectionDetailServiceDaoImpl.saveFeeCollectionDetailInfo",info);

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
    public List<Map> getFeeCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("查询催缴单信息 入参 info : {}",info);

        List<Map> businessFeeCollectionDetailInfos = sqlSessionTemplate.selectList("feeCollectionDetailServiceDaoImpl.getFeeCollectionDetailInfo",info);

        return businessFeeCollectionDetailInfos;
    }


    /**
     * 修改催缴单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("修改催缴单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeCollectionDetailServiceDaoImpl.updateFeeCollectionDetailInfo",info);

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
    public int queryFeeCollectionDetailsCount(Map info) {
        logger.debug("查询催缴单数据 入参 info : {}",info);

        List<Map> businessFeeCollectionDetailInfos = sqlSessionTemplate.selectList("feeCollectionDetailServiceDaoImpl.queryFeeCollectionDetailsCount", info);
        if (businessFeeCollectionDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeCollectionDetailInfos.get(0).get("count").toString());
    }


}
