package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IAllocationStorehouseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 仓库调拨服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("allocationStorehouseServiceDaoImpl")
//@Transactional
public class AllocationStorehouseServiceDaoImpl extends BaseServiceDao implements IAllocationStorehouseServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AllocationStorehouseServiceDaoImpl.class);

    /**
     * 仓库调拨信息封装
     *
     * @param businessAllocationStorehouseInfo 仓库调拨信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAllocationStorehouseInfo(Map businessAllocationStorehouseInfo) throws DAOException {
        businessAllocationStorehouseInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存仓库调拨信息 入参 businessAllocationStorehouseInfo : {}", businessAllocationStorehouseInfo);
        int saveFlag = sqlSessionTemplate.insert("allocationStorehouseServiceDaoImpl.saveBusinessAllocationStorehouseInfo", businessAllocationStorehouseInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存仓库调拨数据失败：" + JSONObject.toJSONString(businessAllocationStorehouseInfo));
        }
    }


    /**
     * 查询仓库调拨信息
     *
     * @param info bId 信息
     * @return 仓库调拨信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAllocationStorehouseInfo(Map info) throws DAOException {

        logger.debug("查询仓库调拨信息 入参 info : {}", info);

        List<Map> businessAllocationStorehouseInfos = sqlSessionTemplate.selectList("allocationStorehouseServiceDaoImpl.getBusinessAllocationStorehouseInfo", info);

        return businessAllocationStorehouseInfos;
    }


    /**
     * 保存仓库调拨信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAllocationStorehouseInfoInstance(Map info) throws DAOException {
        logger.debug("保存仓库调拨信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("allocationStorehouseServiceDaoImpl.saveAllocationStorehouseInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存仓库调拨信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存调拨记录
     *
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveAllocationStorehouseInfo(Map info) throws DAOException {
        logger.debug("保存调拨记录入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("allocationStorehouseServiceDaoImpl.saveAllocationStorehouseInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存调拨记录数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询仓库调拨信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAllocationStorehouseInfo(Map info) throws DAOException {
        logger.debug("查询仓库调拨信息 入参 info : {}", info);

        List<Map> businessAllocationStorehouseInfos = sqlSessionTemplate.selectList("allocationStorehouseServiceDaoImpl.getAllocationStorehouseInfo", info);

        return businessAllocationStorehouseInfos;
    }


    /**
     * 修改仓库调拨信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAllocationStorehouseInfoInstance(Map info) throws DAOException {
        logger.debug("修改仓库调拨信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("allocationStorehouseServiceDaoImpl.updateAllocationStorehouseInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改仓库调拨信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询仓库调拨数量
     *
     * @param info 仓库调拨信息
     * @return 仓库调拨数量
     */
    @Override
    public int queryAllocationStorehousesCount(Map info) {
        logger.debug("查询仓库调拨数据 入参 info : {}", info);

        List<Map> businessAllocationStorehouseInfos = sqlSessionTemplate.selectList("allocationStorehouseServiceDaoImpl.queryAllocationStorehousesCount", info);
        if (businessAllocationStorehouseInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAllocationStorehouseInfos.get(0).get("count").toString());
    }


}
