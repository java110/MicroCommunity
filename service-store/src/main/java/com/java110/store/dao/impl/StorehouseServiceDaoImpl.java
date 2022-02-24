package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IStorehouseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 仓库服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storehouseServiceDaoImpl")
//@Transactional
public class StorehouseServiceDaoImpl extends BaseServiceDao implements IStorehouseServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StorehouseServiceDaoImpl.class);

    /**
     * 仓库信息封装
     * @param businessStorehouseInfo 仓库信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessStorehouseInfo(Map businessStorehouseInfo) throws DAOException {
        businessStorehouseInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存仓库信息 入参 businessStorehouseInfo : {}",businessStorehouseInfo);
        int saveFlag = sqlSessionTemplate.insert("storehouseServiceDaoImpl.saveBusinessStorehouseInfo",businessStorehouseInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存仓库数据失败："+ JSONObject.toJSONString(businessStorehouseInfo));
        }
    }


    /**
     * 查询仓库信息
     * @param info bId 信息
     * @return 仓库信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessStorehouseInfo(Map info) throws DAOException {

        logger.debug("查询仓库信息 入参 info : {}",info);

        List<Map> businessStorehouseInfos = sqlSessionTemplate.selectList("storehouseServiceDaoImpl.getBusinessStorehouseInfo",info);

        return businessStorehouseInfos;
    }



    /**
     * 保存仓库信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStorehouseInfoInstance(Map info) throws DAOException {
        logger.debug("保存仓库信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storehouseServiceDaoImpl.saveStorehouseInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存仓库信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询仓库信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStorehouseInfo(Map info) throws DAOException {
        logger.debug("查询仓库信息 入参 info : {}",info);

        List<Map> businessStorehouseInfos = sqlSessionTemplate.selectList("storehouseServiceDaoImpl.getStorehouseInfo",info);

        return businessStorehouseInfos;
    }


    /**
     * 修改仓库信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStorehouseInfoInstance(Map info) throws DAOException {
        logger.debug("修改仓库信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storehouseServiceDaoImpl.updateStorehouseInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改仓库信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询仓库数量
     * @param info 仓库信息
     * @return 仓库数量
     */
    @Override
    public int queryStorehousesCount(Map info) {
        logger.debug("查询仓库数据 入参 info : {}",info);

        List<Map> businessStorehouseInfos = sqlSessionTemplate.selectList("storehouseServiceDaoImpl.queryStorehousesCount", info);
        if (businessStorehouseInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStorehouseInfos.get(0).get("count").toString());
    }


}
