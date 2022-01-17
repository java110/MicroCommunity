package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeDetailServiceDao;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 费用明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeDetailServiceDaoImpl")
//@Transactional
public class FeeDetailServiceDaoImpl extends BaseServiceDao implements IFeeDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeDetailServiceDaoImpl.class);

    /**
     * 费用明细信息封装
     * @param businessFeeDetailInfo 费用明细信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFeeDetailInfo(Map businessFeeDetailInfo) throws DAOException {
        businessFeeDetailInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存费用明细信息 入参 businessFeeDetailInfo : {}",businessFeeDetailInfo);
        int saveFlag = sqlSessionTemplate.insert("feeDetailServiceDaoImpl.saveBusinessFeeDetailInfo",businessFeeDetailInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用明细数据失败："+ JSONObject.toJSONString(businessFeeDetailInfo));
        }
    }


    /**
     * 查询费用明细信息
     * @param info bId 信息
     * @return 费用明细信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFeeDetailInfo(Map info) throws DAOException {

        logger.debug("查询费用明细信息 入参 info : {}",info);

        List<Map> businessFeeDetailInfos = sqlSessionTemplate.selectList("feeDetailServiceDaoImpl.getBusinessFeeDetailInfo",info);

        return businessFeeDetailInfos;
    }



    /**
     * 保存费用明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeDetailInfoInstance(Map info) throws DAOException {
        logger.debug("保存费用明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeDetailServiceDaoImpl.saveFeeDetailInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeDetailInfo(Map info) throws DAOException {
        logger.debug("查询费用明细信息 入参 info : {}",info);

        List<Map> businessFeeDetailInfos = sqlSessionTemplate.selectList("feeDetailServiceDaoImpl.getFeeDetailInfo",info);

        return businessFeeDetailInfos;
    }


    /**
     * 修改费用明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeDetailInfoInstance(Map info) throws DAOException {
        logger.debug("修改费用明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeDetailServiceDaoImpl.updateFeeDetailInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用明细数量
     * @param info 费用明细信息
     * @return 费用明细数量
     */
    @Override
    public int queryFeeDetailsCount(Map info) {
        logger.debug("查询费用明细数据 入参 info : {}",info);

        List<Map> businessFeeDetailInfos = sqlSessionTemplate.selectList("feeDetailServiceDaoImpl.queryFeeDetailsCount", info);
        if (businessFeeDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeDetailInfos.get(0).get("count").toString());
    }

    @Override
    public void saveFeeDetail(Map feeDetail) throws DAOException {
        logger.debug("保存明细 入参 info : {}",feeDetail);

        int saveFlag = sqlSessionTemplate.update("feeDetailServiceDaoImpl.saveFeeDetail",feeDetail);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存明细 数据失败："+ JSONObject.toJSONString(feeDetail));
        }
    }


}
