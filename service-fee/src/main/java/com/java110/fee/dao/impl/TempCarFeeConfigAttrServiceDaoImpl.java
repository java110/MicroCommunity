package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.ITempCarFeeConfigAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 临时车收费标准属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("tempCarFeeConfigAttrServiceDaoImpl")
//@Transactional
public class TempCarFeeConfigAttrServiceDaoImpl extends BaseServiceDao implements ITempCarFeeConfigAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(TempCarFeeConfigAttrServiceDaoImpl.class);

    /**
     * 临时车收费标准属性信息封装
     * @param businessTempCarFeeConfigAttrInfo 临时车收费标准属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessTempCarFeeConfigAttrInfo(Map businessTempCarFeeConfigAttrInfo) throws DAOException {
        businessTempCarFeeConfigAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存临时车收费标准属性信息 入参 businessTempCarFeeConfigAttrInfo : {}",businessTempCarFeeConfigAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("tempCarFeeConfigAttrServiceDaoImpl.saveBusinessTempCarFeeConfigAttrInfo",businessTempCarFeeConfigAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存临时车收费标准属性数据失败："+ JSONObject.toJSONString(businessTempCarFeeConfigAttrInfo));
        }
    }


    /**
     * 查询临时车收费标准属性信息
     * @param info bId 信息
     * @return 临时车收费标准属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessTempCarFeeConfigAttrInfo(Map info) throws DAOException {

        logger.debug("查询临时车收费标准属性信息 入参 info : {}",info);

        List<Map> businessTempCarFeeConfigAttrInfos = sqlSessionTemplate.selectList("tempCarFeeConfigAttrServiceDaoImpl.getBusinessTempCarFeeConfigAttrInfo",info);

        return businessTempCarFeeConfigAttrInfos;
    }



    /**
     * 保存临时车收费标准属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveTempCarFeeConfigAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存临时车收费标准属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("tempCarFeeConfigAttrServiceDaoImpl.saveTempCarFeeConfigAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存临时车收费标准属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询临时车收费标准属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTempCarFeeConfigAttrInfo(Map info) throws DAOException {
        logger.debug("查询临时车收费标准属性信息 入参 info : {}",info);

        List<Map> businessTempCarFeeConfigAttrInfos = sqlSessionTemplate.selectList("tempCarFeeConfigAttrServiceDaoImpl.getTempCarFeeConfigAttrInfo",info);

        return businessTempCarFeeConfigAttrInfos;
    }


    /**
     * 修改临时车收费标准属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateTempCarFeeConfigAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改临时车收费标准属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("tempCarFeeConfigAttrServiceDaoImpl.updateTempCarFeeConfigAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改临时车收费标准属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询临时车收费标准属性数量
     * @param info 临时车收费标准属性信息
     * @return 临时车收费标准属性数量
     */
    @Override
    public int queryTempCarFeeConfigAttrsCount(Map info) {
        logger.debug("查询临时车收费标准属性数据 入参 info : {}",info);

        List<Map> businessTempCarFeeConfigAttrInfos = sqlSessionTemplate.selectList("tempCarFeeConfigAttrServiceDaoImpl.queryTempCarFeeConfigAttrsCount", info);
        if (businessTempCarFeeConfigAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTempCarFeeConfigAttrInfos.get(0).get("count").toString());
    }


}
