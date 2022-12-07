package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeConfigServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用配置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeConfigServiceDaoImpl")
//@Transactional
public class FeeConfigServiceDaoImpl extends BaseServiceDao implements IFeeConfigServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeConfigServiceDaoImpl.class);

    /**
     * 费用配置信息封装
     *
     * @param businessFeeConfigInfo 费用配置信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFeeConfigInfo(Map businessFeeConfigInfo) throws DAOException {
        businessFeeConfigInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存费用配置信息 入参 businessFeeConfigInfo : {}", businessFeeConfigInfo);
        int saveFlag = sqlSessionTemplate.insert("feeConfigServiceDaoImpl.saveBusinessFeeConfigInfo", businessFeeConfigInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用配置数据失败：" + JSONObject.toJSONString(businessFeeConfigInfo));
        }
    }

    /**
     * 查询费用配置信息
     *
     * @param info bId 信息
     * @return 费用配置信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFeeConfigInfo(Map info) throws DAOException {
        logger.debug("查询费用配置信息 入参 info : {}", info);
        List<Map> businessFeeConfigInfos = sqlSessionTemplate.selectList("feeConfigServiceDaoImpl.getBusinessFeeConfigInfo", info);
        return businessFeeConfigInfos;
    }


    /**
     * 保存费用配置信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeConfigInfoInstance(Map info) throws DAOException {
        logger.debug("保存费用配置信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("feeConfigServiceDaoImpl.saveFeeConfigInfoInstance", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用配置信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用配置信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeConfigInfo(Map info) throws DAOException {
        logger.debug("查询费用配置信息 入参 info : {}", info);
        List<Map> businessFeeConfigInfos = sqlSessionTemplate.selectList("feeConfigServiceDaoImpl.getFeeConfigInfo", info);
        return businessFeeConfigInfos;
    }


    /**
     * 修改费用配置信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeConfigInfoInstance(Map info) throws DAOException {
        logger.debug("修改费用配置信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("feeConfigServiceDaoImpl.updateFeeConfigInfoInstance", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用配置信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询费用配置数量
     *
     * @param info 费用配置信息
     * @return 费用配置数量
     */
    @Override
    public int queryFeeConfigsCount(Map info) {
        logger.debug("查询费用配置数据 入参 info : {}", info);
        List<Map> businessFeeConfigInfos = sqlSessionTemplate.selectList("feeConfigServiceDaoImpl.queryFeeConfigsCount", info);
        if (businessFeeConfigInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessFeeConfigInfos.get(0).get("count").toString());
    }

    @Override
    public int saveFeeConfig(Map info) {
        logger.debug("保存费用配置saveFeeConfig : {}", info);
        int saveFlag = sqlSessionTemplate.update("feeConfigServiceDaoImpl.saveFeeConfig", info);
        return saveFlag;
    }

    @Override
    public int deleteFeeConfig(Map info) {
        logger.debug("删除费用配置deleteFeeConfig : {}", info);
        int deleteFlag = sqlSessionTemplate.update("feeConfigServiceDaoImpl.updateFeeConfigInfoInstance", info);
        if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "删除费用配置信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return deleteFlag;
    }
}
