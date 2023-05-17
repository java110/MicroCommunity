package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IPayFeeDetailMonthServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 月缴费表服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("payFeeDetailMonthServiceDaoImpl")
//@Transactional
public class PayFeeDetailMonthServiceDaoImpl extends BaseServiceDao implements IPayFeeDetailMonthServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PayFeeDetailMonthServiceDaoImpl.class);


    /**
     * 保存月缴费表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePayFeeDetailMonthInfo(Map info) throws DAOException {
        logger.debug("保存月缴费表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("payFeeDetailMonthServiceDaoImpl.savePayFeeDetailMonthInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存月缴费表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public int savePayFeeDetailMonthInfos(Map info) {
        logger.debug("保存月缴费表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("payFeeDetailMonthServiceDaoImpl.savePayFeeDetailMonthInfos", info);

        return saveFlag;
    }


    /**
     * 查询月缴费表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPayFeeDetailMonthInfo(Map info) throws DAOException {
        logger.debug("查询月缴费表信息 入参 info : {}", info);

        List<Map> businessPayFeeDetailMonthInfos = sqlSessionTemplate.selectList("payFeeDetailMonthServiceDaoImpl.getPayFeeDetailMonthInfo", info);

        return businessPayFeeDetailMonthInfos;
    }


    /**
     * 修改月缴费表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePayFeeDetailMonthInfo(Map info) throws DAOException {
        logger.debug("修改月缴费表信息Instance 入参 info : {}", info);

         sqlSessionTemplate.update("payFeeDetailMonthServiceDaoImpl.updatePayFeeDetailMonthInfo", info);

//        if (saveFlag < 1) {
//            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改月缴费表信息Instance数据失败：" + JSONObject.toJSONString(info));
//        }
    }

    /**
     * 查询月缴费表数量
     *
     * @param info 月缴费表信息
     * @return 月缴费表数量
     */
    @Override
    public int queryPayFeeDetailMonthsCount(Map info) {
        logger.debug("查询月缴费表数据 入参 info : {}", info);

        List<Map> businessPayFeeDetailMonthInfos = sqlSessionTemplate.selectList("payFeeDetailMonthServiceDaoImpl.queryPayFeeDetailMonthsCount", info);
        if (businessPayFeeDetailMonthInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPayFeeDetailMonthInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryPayFeeDetailMaxMonths(Map info) {
        logger.debug("查询queryPayFeeDetailMaxMonths信息 入参 info : {}", info);

        List<Map> businessPayFeeDetailMonthInfos = sqlSessionTemplate.selectList("payFeeDetailMonthServiceDaoImpl.queryPayFeeDetailMaxMonths", info);

        return businessPayFeeDetailMonthInfos;
    }

    @Override
    public List<Map> getWaitDispersedFeeDetail(Map info) {
        logger.debug("查询getWaitDispersedFeeDetail信息 入参 info : {}", info);

        List<Map> businessPayFeeDetailMonthInfos = sqlSessionTemplate.selectList("payFeeDetailMonthServiceDaoImpl.getWaitDispersedFeeDetail", info);

        return businessPayFeeDetailMonthInfos;
    }

    @Override
    public void deletePayFeeDetailMonthInfo(Map info) {
        sqlSessionTemplate.update("payFeeDetailMonthServiceDaoImpl.deletePayFeeDetailMonthInfo", info);
    }


}
