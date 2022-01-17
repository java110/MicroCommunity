package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.ITempCarFeeConfigServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 临时车收费标准服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("tempCarFeeConfigServiceDaoImpl")
//@Transactional
public class TempCarFeeConfigServiceDaoImpl extends BaseServiceDao implements ITempCarFeeConfigServiceDao {

    private static Logger logger = LoggerFactory.getLogger(TempCarFeeConfigServiceDaoImpl.class);

    /**
     * 临时车收费标准信息封装
     *
     * @param businessTempCarFeeConfigInfo 临时车收费标准信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessTempCarFeeConfigInfo(Map businessTempCarFeeConfigInfo) throws DAOException {
        businessTempCarFeeConfigInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存临时车收费标准信息 入参 businessTempCarFeeConfigInfo : {}", businessTempCarFeeConfigInfo);
        int saveFlag = sqlSessionTemplate.insert("tempCarFeeConfigServiceDaoImpl.saveBusinessTempCarFeeConfigInfo", businessTempCarFeeConfigInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存临时车收费标准数据失败：" + JSONObject.toJSONString(businessTempCarFeeConfigInfo));
        }
    }


    /**
     * 查询临时车收费标准信息
     *
     * @param info bId 信息
     * @return 临时车收费标准信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessTempCarFeeConfigInfo(Map info) throws DAOException {

        logger.debug("查询临时车收费标准信息 入参 info : {}", info);

        List<Map> businessTempCarFeeConfigInfos = sqlSessionTemplate.selectList("tempCarFeeConfigServiceDaoImpl.getBusinessTempCarFeeConfigInfo", info);

        return businessTempCarFeeConfigInfos;
    }


    /**
     * 保存临时车收费标准信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveTempCarFeeConfigInfoInstance(Map info) throws DAOException {
        logger.debug("保存临时车收费标准信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("tempCarFeeConfigServiceDaoImpl.saveTempCarFeeConfigInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存临时车收费标准信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询临时车收费标准信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTempCarFeeConfigInfo(Map info) throws DAOException {
        logger.debug("查询临时车收费标准信息 入参 info : {}", info);

        List<Map> businessTempCarFeeConfigInfos = sqlSessionTemplate.selectList("tempCarFeeConfigServiceDaoImpl.getTempCarFeeConfigInfo", info);

        return businessTempCarFeeConfigInfos;
    }


    /**
     * 修改临时车收费标准信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateTempCarFeeConfigInfoInstance(Map info) throws DAOException {
        logger.debug("修改临时车收费标准信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("tempCarFeeConfigServiceDaoImpl.updateTempCarFeeConfigInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改临时车收费标准信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询临时车收费标准数量
     *
     * @param info 临时车收费标准信息
     * @return 临时车收费标准数量
     */
    @Override
    public int queryTempCarFeeConfigsCount(Map info) {
        logger.debug("查询临时车收费标准数据 入参 info : {}", info);

        List<Map> businessTempCarFeeConfigInfos = sqlSessionTemplate.selectList("tempCarFeeConfigServiceDaoImpl.queryTempCarFeeConfigsCount", info);
        if (businessTempCarFeeConfigInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTempCarFeeConfigInfos.get(0).get("count").toString());
    }



    @Override
    public List<Map> queryTempCarFeeRules(Map info) {
        logger.debug("查询临时车收费规则信息 入参 info : {}", info);

        List<Map> businessTempCarFeeConfigInfos = sqlSessionTemplate.selectList("tempCarFeeConfigServiceDaoImpl.queryTempCarFeeRules", info);

        return businessTempCarFeeConfigInfos;
    }
    @Override
    public List<Map> queryTempCarFeeRuleSpecs(Map info) {
        logger.debug("查询临时车收费规则规格信息 入参 info : {}", info);

        List<Map> businessTempCarFeeConfigInfos = sqlSessionTemplate.selectList("tempCarFeeConfigServiceDaoImpl.queryTempCarFeeRuleSpecs", info);

        return businessTempCarFeeConfigInfos;
    }
}
