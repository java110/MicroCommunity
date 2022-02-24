package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IPayFeeDetailDiscountServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 缴费优惠服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("payFeeDetailDiscountServiceDaoImpl")
//@Transactional
public class PayFeeDetailDiscountServiceDaoImpl extends BaseServiceDao implements IPayFeeDetailDiscountServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PayFeeDetailDiscountServiceDaoImpl.class);

    /**
     * 缴费优惠信息封装
     * @param businessPayFeeDetailDiscountInfo 缴费优惠信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessPayFeeDetailDiscountInfo(Map businessPayFeeDetailDiscountInfo) throws DAOException {
        businessPayFeeDetailDiscountInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存缴费优惠信息 入参 businessPayFeeDetailDiscountInfo : {}",businessPayFeeDetailDiscountInfo);
        int saveFlag = sqlSessionTemplate.insert("payFeeDetailDiscountServiceDaoImpl.saveBusinessPayFeeDetailDiscountInfo",businessPayFeeDetailDiscountInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存缴费优惠数据失败："+ JSONObject.toJSONString(businessPayFeeDetailDiscountInfo));
        }
    }


    /**
     * 查询缴费优惠信息
     * @param info bId 信息
     * @return 缴费优惠信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessPayFeeDetailDiscountInfo(Map info) throws DAOException {

        logger.debug("查询缴费优惠信息 入参 info : {}",info);

        List<Map> businessPayFeeDetailDiscountInfos = sqlSessionTemplate.selectList("payFeeDetailDiscountServiceDaoImpl.getBusinessPayFeeDetailDiscountInfo",info);

        return businessPayFeeDetailDiscountInfos;
    }



    /**
     * 保存缴费优惠信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePayFeeDetailDiscountInfoInstance(Map info) throws DAOException {
        logger.debug("保存缴费优惠信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("payFeeDetailDiscountServiceDaoImpl.savePayFeeDetailDiscountInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存缴费优惠信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询缴费优惠信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPayFeeDetailDiscountInfo(Map info) throws DAOException {
        logger.debug("查询缴费优惠信息 入参 info : {}",info);

        List<Map> businessPayFeeDetailDiscountInfos = sqlSessionTemplate.selectList("payFeeDetailDiscountServiceDaoImpl.getPayFeeDetailDiscountInfo",info);

        return businessPayFeeDetailDiscountInfos;
    }


    /**
     * 修改缴费优惠信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePayFeeDetailDiscountInfoInstance(Map info) throws DAOException {
        logger.debug("修改缴费优惠信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("payFeeDetailDiscountServiceDaoImpl.updatePayFeeDetailDiscountInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改缴费优惠信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询缴费优惠数量
     * @param info 缴费优惠信息
     * @return 缴费优惠数量
     */
    @Override
    public int queryPayFeeDetailDiscountsCount(Map info) {
        logger.debug("查询缴费优惠数据 入参 info : {}",info);

        List<Map> businessPayFeeDetailDiscountInfos = sqlSessionTemplate.selectList("payFeeDetailDiscountServiceDaoImpl.queryPayFeeDetailDiscountsCount", info);
        if (businessPayFeeDetailDiscountInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPayFeeDetailDiscountInfos.get(0).get("count").toString());
    }


}
