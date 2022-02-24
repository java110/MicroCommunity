package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeDiscountServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 费用折扣服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeDiscountServiceDaoImpl")
//@Transactional
public class FeeDiscountServiceDaoImpl extends BaseServiceDao implements IFeeDiscountServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeDiscountServiceDaoImpl.class);





    /**
     * 保存费用折扣信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeDiscountInfo(Map info) throws DAOException {
        logger.debug("保存费用折扣信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeDiscountServiceDaoImpl.saveFeeDiscountInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用折扣信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用折扣信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeDiscountInfo(Map info) throws DAOException {
        logger.debug("查询费用折扣信息 入参 info : {}",info);

        List<Map> businessFeeDiscountInfos = sqlSessionTemplate.selectList("feeDiscountServiceDaoImpl.getFeeDiscountInfo",info);

        return businessFeeDiscountInfos;
    }


    /**
     * 修改费用折扣信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeDiscountInfo(Map info) throws DAOException {
        logger.debug("修改费用折扣信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeDiscountServiceDaoImpl.updateFeeDiscountInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用折扣信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用折扣数量
     * @param info 费用折扣信息
     * @return 费用折扣数量
     */
    @Override
    public int queryFeeDiscountsCount(Map info) {
        logger.debug("查询费用折扣数据 入参 info : {}",info);

        List<Map> businessFeeDiscountInfos = sqlSessionTemplate.selectList("feeDiscountServiceDaoImpl.queryFeeDiscountsCount", info);
        if (businessFeeDiscountInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeDiscountInfos.get(0).get("count").toString());
    }


}
