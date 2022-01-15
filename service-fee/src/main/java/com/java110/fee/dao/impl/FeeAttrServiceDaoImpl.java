package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeAttrServiceDao;
import com.java110.po.fee.FeeAttrPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeAttrServiceDaoImpl")
//@Transactional
public class FeeAttrServiceDaoImpl extends BaseServiceDao implements IFeeAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeAttrServiceDaoImpl.class);

    /**
     * 费用属性信息封装
     *
     * @param businessFeeAttrInfo 费用属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFeeAttrInfo(Map businessFeeAttrInfo) throws DAOException {
        businessFeeAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存费用属性信息 入参 businessFeeAttrInfo : {}", businessFeeAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("feeAttrServiceDaoImpl.saveBusinessFeeAttrInfo", businessFeeAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用属性数据失败：" + JSONObject.toJSONString(businessFeeAttrInfo));
        }
    }


    /**
     * 查询费用属性信息
     *
     * @param info bId 信息
     * @return 费用属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFeeAttrInfo(Map info) throws DAOException {

        logger.debug("查询费用属性信息 入参 info : {}", info);

        List<Map> businessFeeAttrInfos = sqlSessionTemplate.selectList("feeAttrServiceDaoImpl.getBusinessFeeAttrInfo", info);

        return businessFeeAttrInfos;
    }


    /**
     * 保存费用属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存费用属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeAttrServiceDaoImpl.saveFeeAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用属性信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeAttrInfo(Map info) throws DAOException {
        logger.debug("查询费用属性信息 入参 info : {}", info);

        List<Map> businessFeeAttrInfos = sqlSessionTemplate.selectList("feeAttrServiceDaoImpl.getFeeAttrInfo", info);

        return businessFeeAttrInfos;
    }


    /**
     * 修改费用属性信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改费用属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeAttrServiceDaoImpl.updateFeeAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询费用属性数量
     *
     * @param info 费用属性信息
     * @return 费用属性数量
     */
    @Override
    public int queryFeeAttrsCount(Map info) {
        logger.debug("查询费用属性数据 入参 info : {}", info);

        List<Map> businessFeeAttrInfos = sqlSessionTemplate.selectList("feeAttrServiceDaoImpl.queryFeeAttrsCount", info);
        if (businessFeeAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveFeeAttrs(Map info) {
        logger.debug("saveFeeAttrs 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeAttrServiceDaoImpl.saveFeeAttrs", info);

        return saveFlag;
    }


}
