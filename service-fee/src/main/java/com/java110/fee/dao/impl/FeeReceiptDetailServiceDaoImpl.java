package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeReceiptDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 收据明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeReceiptDetailServiceDaoImpl")
//@Transactional
public class FeeReceiptDetailServiceDaoImpl extends BaseServiceDao implements IFeeReceiptDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeReceiptDetailServiceDaoImpl.class);





    /**
     * 保存收据明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeReceiptDetailInfo(Map info) throws DAOException {
        logger.debug("保存收据明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeReceiptDetailServiceDaoImpl.saveFeeReceiptDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存收据明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveFeeReceiptDetails(Map info) throws DAOException {
        logger.debug("保存收据明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeReceiptDetailServiceDaoImpl.saveFeeReceiptDetails",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存收据明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询收据明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeReceiptDetailInfo(Map info) throws DAOException {
        logger.debug("查询收据明细信息 入参 info : {}",info);

        List<Map> businessFeeReceiptDetailInfos = sqlSessionTemplate.selectList("feeReceiptDetailServiceDaoImpl.getFeeReceiptDetailInfo",info);

        return businessFeeReceiptDetailInfos;
    }


    /**
     * 修改收据明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeReceiptDetailInfo(Map info) throws DAOException {
        logger.debug("修改收据明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeReceiptDetailServiceDaoImpl.updateFeeReceiptDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改收据明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询收据明细数量
     * @param info 收据明细信息
     * @return 收据明细数量
     */
    @Override
    public int queryFeeReceiptDetailsCount(Map info) {
        logger.debug("查询收据明细数据 入参 info : {}",info);

        List<Map> businessFeeReceiptDetailInfos = sqlSessionTemplate.selectList("feeReceiptDetailServiceDaoImpl.queryFeeReceiptDetailsCount", info);
        if (businessFeeReceiptDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeReceiptDetailInfos.get(0).get("count").toString());
    }


}
