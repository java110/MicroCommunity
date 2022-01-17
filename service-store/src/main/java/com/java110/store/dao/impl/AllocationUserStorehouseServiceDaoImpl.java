package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IAllocationUserStorehouseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 物品供应商服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("allocationUserStorehouseServiceDaoImpl")
//@Transactional
public class AllocationUserStorehouseServiceDaoImpl extends BaseServiceDao implements IAllocationUserStorehouseServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AllocationUserStorehouseServiceDaoImpl.class);

    /**
     * 物品供应商信息封装
     *
     * @param businessAllocationUserStorehouseInfo 物品供应商信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAllocationUserStorehouseInfo(Map businessAllocationUserStorehouseInfo) throws DAOException {
        businessAllocationUserStorehouseInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存物品供应商信息 入参 businessAllocationUserStorehouseInfo : {}", businessAllocationUserStorehouseInfo);
        int saveFlag = sqlSessionTemplate.insert("allocationUserStorehouseServiceDaoImpl.saveBusinessAllocationUserStorehouseInfo", businessAllocationUserStorehouseInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品供应商数据失败：" + JSONObject.toJSONString(businessAllocationUserStorehouseInfo));
        }
    }


    /**
     * 查询物品供应商信息
     *
     * @param info bId 信息
     * @return 物品供应商信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAllocationUserStorehouseInfo(Map info) throws DAOException {

        logger.debug("查询物品供应商信息 入参 info : {}", info);

        List<Map> businessAllocationUserStorehouseInfos = sqlSessionTemplate.selectList("allocationUserStorehouseServiceDaoImpl.getBusinessAllocationUserStorehouseInfo", info);

        return businessAllocationUserStorehouseInfos;
    }


    /**
     * 保存物品供应商信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAllocationUserStorehouseInfoInstance(Map info) throws DAOException {
        logger.debug("保存物品供应商信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("allocationUserStorehouseServiceDaoImpl.saveAllocationUserStorehouseInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品供应商信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询物品供应商信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAllocationUserStorehouseInfo(Map info) throws DAOException {
        logger.debug("查询物品供应商信息 入参 info : {}", info);

        List<Map> businessAllocationUserStorehouseInfos = sqlSessionTemplate.selectList("allocationUserStorehouseServiceDaoImpl.getAllocationUserStorehouseInfo", info);

        return businessAllocationUserStorehouseInfos;
    }


    /**
     * 修改物品供应商信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAllocationUserStorehouseInfoInstance(Map info) throws DAOException {
        logger.debug("修改物品供应商信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("allocationUserStorehouseServiceDaoImpl.updateAllocationUserStorehouseInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改物品供应商信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物品供应商数量
     *
     * @param info 物品供应商信息
     * @return 物品供应商数量
     */
    @Override
    public int queryAllocationUserStorehousesCount(Map info) {
        logger.debug("查询物品供应商数据 入参 info : {}", info);

        List<Map> businessAllocationUserStorehouseInfos = sqlSessionTemplate.selectList("allocationUserStorehouseServiceDaoImpl.queryAllocationUserStorehousesCount", info);
        if (businessAllocationUserStorehouseInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAllocationUserStorehouseInfos.get(0).get("count").toString());
    }


}
