package com.java110.store.dao.impl.businesstype;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IC_business_typeServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * BusinessType服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("c_business_typeServiceDaoImpl")
//@Transactional
public class C_business_typeServiceDaoImpl extends BaseServiceDao implements IC_business_typeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(C_business_typeServiceDaoImpl.class);

    /**
     * BusinessType信息封装
     * @param businessC_business_typeInfo BusinessType信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessC_business_typeInfo(Map businessC_business_typeInfo) throws DAOException {
        businessC_business_typeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存BusinessType信息 入参 businessC_business_typeInfo : {}",businessC_business_typeInfo);
        int saveFlag = sqlSessionTemplate.insert("c_business_typeServiceDaoImpl.saveBusinessC_business_typeInfo",businessC_business_typeInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存BusinessType数据失败："+ JSONObject.toJSONString(businessC_business_typeInfo));
        }
    }


    /**
     * 查询BusinessType信息
     * @param info bId 信息
     * @return BusinessType信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessC_business_typeInfo(Map info) throws DAOException {

        logger.debug("查询BusinessType信息 入参 info : {}",info);

        List<Map> businessC_business_typeInfos = sqlSessionTemplate.selectList("c_business_typeServiceDaoImpl.getBusinessC_business_typeInfo",info);

        return businessC_business_typeInfos;
    }



    /**
     * 保存BusinessType信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveC_business_typeInfoInstance(Map info) throws DAOException {
        logger.debug("保存BusinessType信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("c_business_typeServiceDaoImpl.saveC_business_typeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存BusinessType信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询BusinessType信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getC_business_typeInfo(Map info) throws DAOException {
        logger.debug("查询BusinessType信息 入参 info : {}",info);

        List<Map> businessC_business_typeInfos = sqlSessionTemplate.selectList("c_business_typeServiceDaoImpl.getC_business_typeInfo",info);

        return businessC_business_typeInfos;
    }


    /**
     * 修改BusinessType信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateC_business_typeInfoInstance(Map info) throws DAOException {
        logger.debug("修改BusinessType信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("c_business_typeServiceDaoImpl.updateC_business_typeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改BusinessType信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询BusinessType数量
     * @param info BusinessType信息
     * @return BusinessType数量
     */
    @Override
    public int queryC_business_typesCount(Map info) {
        logger.debug("查询BusinessType数据 入参 info : {}",info);

        List<Map> businessC_business_typeInfos = sqlSessionTemplate.selectList("c_business_typeServiceDaoImpl.queryC_business_typesCount", info);
        if (businessC_business_typeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessC_business_typeInfos.get(0).get("count").toString());
    }


}
