package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeDiscountRuleServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 费用折扣规则服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeDiscountRuleServiceDaoImpl")
//@Transactional
public class FeeDiscountRuleServiceDaoImpl extends BaseServiceDao implements IFeeDiscountRuleServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeDiscountRuleServiceDaoImpl.class);





    /**
     * 保存费用折扣规则信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeDiscountRuleInfo(Map info) throws DAOException {
        logger.debug("保存费用折扣规则信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeDiscountRuleServiceDaoImpl.saveFeeDiscountRuleInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用折扣规则信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用折扣规则信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeDiscountRuleInfo(Map info) throws DAOException {
        logger.debug("查询费用折扣规则信息 入参 info : {}",info);

        List<Map> businessFeeDiscountRuleInfos = sqlSessionTemplate.selectList("feeDiscountRuleServiceDaoImpl.getFeeDiscountRuleInfo",info);

        return businessFeeDiscountRuleInfos;
    }


    /**
     * 修改费用折扣规则信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeDiscountRuleInfo(Map info) throws DAOException {
        logger.debug("修改费用折扣规则信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeDiscountRuleServiceDaoImpl.updateFeeDiscountRuleInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用折扣规则信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用折扣规则数量
     * @param info 费用折扣规则信息
     * @return 费用折扣规则数量
     */
    @Override
    public int queryFeeDiscountRulesCount(Map info) {
        logger.debug("查询费用折扣规则数据 入参 info : {}",info);

        List<Map> businessFeeDiscountRuleInfos = sqlSessionTemplate.selectList("feeDiscountRuleServiceDaoImpl.queryFeeDiscountRulesCount", info);
        if (businessFeeDiscountRuleInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeDiscountRuleInfos.get(0).get("count").toString());
    }


}
