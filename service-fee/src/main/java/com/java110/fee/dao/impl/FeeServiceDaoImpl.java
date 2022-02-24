package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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
     *
     * @param businessFeeInfo 费用信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFeeInfo(Map businessFeeInfo) throws DAOException {
        businessFeeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存费用信息 入参 businessFeeInfo : {}", businessFeeInfo);
        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveBusinessFeeInfo", businessFeeInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用数据失败：" + JSONObject.toJSONString(businessFeeInfo));
        }
    }


    /**
     * 查询费用信息
     *
     * @param info bId 信息
     * @return 费用信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFeeInfo(Map info) throws DAOException {

        logger.debug("查询费用信息 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getBusinessFeeInfo", info);

        return businessFeeInfos;
    }


    /**
     * 保存费用信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeInfoInstance(Map info) throws DAOException {
        logger.debug("保存费用信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveFeeInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeInfo(Map info) throws DAOException {
        logger.debug("查询费用信息 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeeInfo", info);

        return businessFeeInfos;
    }


    /**
     * 修改费用信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeInfoInstance(Map info) throws DAOException {
        logger.debug("修改费用信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeServiceDaoImpl.updateFeeInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询费用数量
     *
     * @param info 费用信息
     * @return 费用数量
     */
    @Override
    public int queryFeesCount(Map info) {
        logger.debug("查询费用数据 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryFeesCount", info);
        if (businessFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeInfos.get(0).get("count").toString());
    }

    @Override
    public int queryFeeByAttrCount(Map info) {
        logger.debug("查询费用数据 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryFeeByAttrCount", info);
        if (businessFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryFeeByAttr(Map info) throws DAOException {
        logger.debug("查询费用信息 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryFeeByAttr", info);

        return businessFeeInfos;
    }

    /**
     * 查询费用账期
     *
     * @param info 费用信息
     * @return 费用数量
     */
    @Override
    public int queryBillCount(Map info) {
        logger.debug("查询费用数据 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryBillCount", info);
        if (businessFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeInfos.get(0).get("count").toString());
    }


    /**
     * 查询账期
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> queryBills(Map info) throws DAOException {
        logger.debug("查询费用信息 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryBills", info);

        return businessFeeInfos;
    }


    /**
     * 查询账单欠费总数
     *
     * @param info 费用信息
     * @return 费用数量
     */
    @Override
    public int queryBillOweFeeCount(Map info) {
        logger.debug("查询费用数据 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryBillOweFeeCount", info);
        if (businessFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeInfos.get(0).get("count").toString());
    }


    /**
     * 查询账单欠费
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> queryBillOweFees(Map info) throws DAOException {
        logger.debug("查询费用信息 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.queryBillOweFees", info);

        return businessFeeInfos;
    }


    /**
     * 保存欠费
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public int insertBillOweFees(Map info) throws DAOException {
        logger.debug("保存欠费费用信息 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.insertBillOweFees", info);

        return saveFlag;
    }

    /**
     * 保存欠费
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public int updateBillOweFees(Map info) throws DAOException {
        logger.debug("保存欠费费用信息 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.updateBillOweFees", info);

        return saveFlag;
    }

    /**
     * 保存欠费
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public int insertBill(Map info) throws DAOException {
        logger.debug("保存欠费费用信息 入参 info : {}", info);

        sqlSessionTemplate.update("feeServiceDaoImpl.updateBill", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.insertBill", info);

        return saveFlag;
    }

    /**
     * 批量保存费用
     *
     * @param info
     * @return
     */
    @Override
    public int insertFees(Map info) {

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.insertFees", info);

        return saveFlag;
    }

    @Override
    public int computeBillOweFeeCount(Map beanCovertMap) {
        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.computeBillOweFeeCount", beanCovertMap);
        if (businessFeeInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessFeeInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> computeBillOweFee(Map beanCovertMap) {
        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.computeBillOweFee", beanCovertMap);

        return businessFeeInfos;
    }

    @Override
    public int computeEveryOweFeeCount(Map beanCovertMap) {
        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.computeEveryOweFeeCount", beanCovertMap);
        if (businessFeeInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessFeeInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> computeEveryOweFee(Map beanCovertMap) {
        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.computeEveryOweFee", beanCovertMap);

        return businessFeeInfos;
    }

    @Override
    public int deleteFeesByBatch(Map info) {
        logger.debug("修改费用信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeServiceDaoImpl.deleteFeesByBatch", info);

        return saveFlag;
    }


}
