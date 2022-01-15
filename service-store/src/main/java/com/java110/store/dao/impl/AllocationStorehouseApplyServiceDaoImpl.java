package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IAllocationStorehouseApplyServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 调拨申请服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("allocationStorehouseApplyServiceDaoImpl")
//@Transactional
public class AllocationStorehouseApplyServiceDaoImpl extends BaseServiceDao implements IAllocationStorehouseApplyServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AllocationStorehouseApplyServiceDaoImpl.class);

    /**
     * 调拨申请信息封装
     *
     * @param businessAllocationStorehouseApplyInfo 调拨申请信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAllocationStorehouseApplyInfo(Map businessAllocationStorehouseApplyInfo) throws DAOException {
        businessAllocationStorehouseApplyInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存调拨申请信息 入参 businessAllocationStorehouseApplyInfo : {}", businessAllocationStorehouseApplyInfo);
        int saveFlag = sqlSessionTemplate.insert("allocationStorehouseApplyServiceDaoImpl.saveBusinessAllocationStorehouseApplyInfo", businessAllocationStorehouseApplyInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存调拨申请数据失败：" + JSONObject.toJSONString(businessAllocationStorehouseApplyInfo));
        }
    }


    /**
     * 查询调拨申请信息
     *
     * @param info bId 信息
     * @return 调拨申请信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAllocationStorehouseApplyInfo(Map info) throws DAOException {

        logger.debug("查询调拨申请信息 入参 info : {}", info);

        List<Map> businessAllocationStorehouseApplyInfos = sqlSessionTemplate.selectList("allocationStorehouseApplyServiceDaoImpl.getBusinessAllocationStorehouseApplyInfo", info);

        return businessAllocationStorehouseApplyInfos;
    }


    /**
     * 保存调拨申请信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAllocationStorehouseApplyInfoInstance(Map info) throws DAOException {
        logger.debug("保存调拨申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("allocationStorehouseApplyServiceDaoImpl.saveAllocationStorehouseApplyInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存调拨申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveAllocationStorehouseApplyInfo(Map info) throws DAOException {
        logger.debug("保存调拨申请信息入参: {}", info);

        int saveFlag = sqlSessionTemplate.insert("allocationStorehouseApplyServiceDaoImpl.saveAllocationStorehouseApplyInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存调拨申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询调拨申请信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAllocationStorehouseApplyInfo(Map info) throws DAOException {
        logger.debug("查询调拨申请信息 入参 info : {}", info);

        List<Map> businessAllocationStorehouseApplyInfos = sqlSessionTemplate.selectList("allocationStorehouseApplyServiceDaoImpl.getAllocationStorehouseApplyInfo", info);

        return businessAllocationStorehouseApplyInfos;
    }


    /**
     * 修改调拨申请信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAllocationStorehouseApplyInfoInstance(Map info) throws DAOException {
        logger.debug("修改调拨申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("allocationStorehouseApplyServiceDaoImpl.updateAllocationStorehouseApplyInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改调拨申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询调拨申请数量
     *
     * @param info 调拨申请信息
     * @return 调拨申请数量
     */
    @Override
    public int queryAllocationStorehouseApplysCount(Map info) {
        logger.debug("查询调拨申请数据 入参 info : {}", info);

        List<Map> businessAllocationStorehouseApplyInfos = sqlSessionTemplate.selectList("allocationStorehouseApplyServiceDaoImpl.queryAllocationStorehouseApplysCount", info);
        if (businessAllocationStorehouseApplyInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAllocationStorehouseApplyInfos.get(0).get("count").toString());
    }


}
