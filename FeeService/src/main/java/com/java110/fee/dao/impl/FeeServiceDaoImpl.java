package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeServiceDaoImpl")
//@Transactional
public class FeeServiceDaoImpl extends BaseServiceDao implements IFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeServiceDaoImpl.class);

    /**
     * 费用信息封装
     * @param businessFeeInfo 费用信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFeeInfo(Map businessFeeInfo) throws DAOException {
        businessFeeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存费用信息 入参 businessFeeInfo : {}",businessFeeInfo);
        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveBusinessFeeInfo",businessFeeInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用数据失败："+ JSONObject.toJSONString(businessFeeInfo));
        }
    }


    /**
     * 查询费用信息
     * @param info bId 信息
     * @return 费用信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFeeInfo(Map info) throws DAOException {

        logger.debug("查询费用信息 入参 info : {}",info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getBusinessFeeInfo",info);

        return businessFeeInfos;
    }



    /**
     * 保存费用信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeInfoInstance(Map info) throws DAOException {
        logger.debug("保存费用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveFeeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeInfo(Map info) throws DAOException {
        logger.debug("查询费用信息 入参 info : {}",info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeeInfo",info);

        return businessFeeInfos;
    }


    /**
     * 修改费用信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeInfoInstance(Map info) throws DAOException {
        logger.debug("修改费用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeServiceDaoImpl.updateFeeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用数量
     * @param info 费用信息
     * @return 费用数量
     */
    @Override
    public int queryFeesCount(Map info) {
        logger.debug("查询费用数据 入参 info : {}",info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryFeesCount", info);
        if (businessFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeInfos.get(0).get("count").toString());
    }


}
