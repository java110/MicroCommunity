package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IResourceStoreTypeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 物品类型服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("resourceResourceStoreTypeTypeServiceDaoImpl")
//@Transactional
public class ResourceStoreTypeServiceDaoImpl extends BaseServiceDao implements IResourceStoreTypeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ResourceStoreTypeServiceDaoImpl.class);

    /**
     * 物品类型信息封装
     * @param businessResourceStoreTypeInfo 物品类型信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessResourceStoreTypeInfo(Map businessResourceStoreTypeInfo) throws DAOException {
        businessResourceStoreTypeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存物品类型信息 入参 businessResourceStoreTypeInfo : {}",businessResourceStoreTypeInfo);
        int saveFlag = sqlSessionTemplate.insert("resourceResourceStoreTypeTypeServiceDaoImpl.saveBusinessResourceStoreTypeInfo",businessResourceStoreTypeInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物品类型数据失败："+ JSONObject.toJSONString(businessResourceStoreTypeInfo));
        }
    }


    /**
     * 查询物品类型信息
     * @param info bId 信息
     * @return 物品类型信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessResourceStoreTypeInfo(Map info) throws DAOException {

        logger.debug("查询物品类型信息 入参 info : {}",info);

        List<Map> businessResourceStoreTypeInfos = sqlSessionTemplate.selectList("resourceResourceStoreTypeTypeServiceDaoImpl.getBusinessResourceStoreTypeInfo",info);

        return businessResourceStoreTypeInfos;
    }



    /**
     * 保存物品类型信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveResourceStoreTypeInfoInstance(Map info) throws DAOException {
        logger.debug("保存物品类型信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("resourceResourceStoreTypeTypeServiceDaoImpl.saveResourceStoreTypeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物品类型信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询物品类型信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getResourceStoreTypeInfo(Map info) throws DAOException {
        logger.debug("查询物品类型信息 入参 info : {}",info);

        List<Map> businessResourceStoreTypeInfos = sqlSessionTemplate.selectList("resourceResourceStoreTypeTypeServiceDaoImpl.getResourceStoreTypeInfo",info);

        return businessResourceStoreTypeInfos;
    }


    /**
     * 修改物品类型信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateResourceStoreTypeInfoInstance(Map info) throws DAOException {
        logger.debug("修改物品类型信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("resourceResourceStoreTypeTypeServiceDaoImpl.updateResourceStoreTypeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改物品类型信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询物品类型数量
     * @param info 物品类型信息
     * @return 物品类型数量
     */
    @Override
    public int queryResourceStoreTypesCount(Map info) {
        logger.debug("查询物品类型数据 入参 info : {}",info);

        List<Map> businessResourceStoreTypeInfos = sqlSessionTemplate.selectList("resourceResourceStoreTypeTypeServiceDaoImpl.queryResourceStoreTypesCount", info);
        if (businessResourceStoreTypeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessResourceStoreTypeInfos.get(0).get("count").toString());
    }


}
