package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IApplyRoomDiscountTypeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 优惠申请类型服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("applyRoomDiscountTypeServiceDaoImpl")
//@Transactional
public class ApplyRoomDiscountTypeServiceDaoImpl extends BaseServiceDao implements IApplyRoomDiscountTypeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ApplyRoomDiscountTypeServiceDaoImpl.class);





    /**
     * 保存优惠申请类型信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveApplyRoomDiscountTypeInfo(Map info) throws DAOException {
        logger.debug("保存优惠申请类型信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("applyRoomDiscountTypeServiceDaoImpl.saveApplyRoomDiscountTypeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存优惠申请类型信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询优惠申请类型信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getApplyRoomDiscountTypeInfo(Map info) throws DAOException {
        logger.debug("查询优惠申请类型信息 入参 info : {}",info);

        List<Map> businessApplyRoomDiscountTypeInfos = sqlSessionTemplate.selectList("applyRoomDiscountTypeServiceDaoImpl.getApplyRoomDiscountTypeInfo",info);

        return businessApplyRoomDiscountTypeInfos;
    }


    /**
     * 修改优惠申请类型信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateApplyRoomDiscountTypeInfo(Map info) throws DAOException {
        logger.debug("修改优惠申请类型信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("applyRoomDiscountTypeServiceDaoImpl.updateApplyRoomDiscountTypeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改优惠申请类型信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询优惠申请类型数量
     * @param info 优惠申请类型信息
     * @return 优惠申请类型数量
     */
    @Override
    public int queryApplyRoomDiscountTypesCount(Map info) {
        logger.debug("查询优惠申请类型数据 入参 info : {}",info);

        List<Map> businessApplyRoomDiscountTypeInfos = sqlSessionTemplate.selectList("applyRoomDiscountTypeServiceDaoImpl.queryApplyRoomDiscountTypesCount", info);
        if (businessApplyRoomDiscountTypeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessApplyRoomDiscountTypeInfos.get(0).get("count").toString());
    }


}
