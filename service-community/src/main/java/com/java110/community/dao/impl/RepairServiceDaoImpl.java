package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 报修信息服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("repairServiceDaoImpl")
//@Transactional
public class RepairServiceDaoImpl extends BaseServiceDao implements IRepairServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RepairServiceDaoImpl.class);

    /**
     * 报修信息信息封装
     * @param businessRepairInfo 报修信息信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessRepairInfo(Map businessRepairInfo) throws DAOException {
        businessRepairInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存报修信息信息 入参 businessRepairInfo : {}",businessRepairInfo);
        int saveFlag = sqlSessionTemplate.insert("repairServiceDaoImpl.saveBusinessRepairInfo",businessRepairInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存报修信息数据失败："+ JSONObject.toJSONString(businessRepairInfo));
        }
    }


    /**
     * 查询报修信息信息
     * @param info bId 信息
     * @return 报修信息信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessRepairInfo(Map info) throws DAOException {

        logger.debug("查询报修信息信息 入参 info : {}",info);

        List<Map> businessRepairInfos = sqlSessionTemplate.selectList("repairServiceDaoImpl.getBusinessRepairInfo",info);

        return businessRepairInfos;
    }



    /**
     * 保存报修信息信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRepairInfoInstance(Map info) throws DAOException {
        logger.debug("保存报修信息信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("repairServiceDaoImpl.saveRepairInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存报修信息信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询报修信息信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRepairInfo(Map info) throws DAOException {
        logger.debug("查询报修信息信息 入参 info : {}",info);

        List<Map> businessRepairInfos = sqlSessionTemplate.selectList("repairServiceDaoImpl.getRepairInfo",info);

        return businessRepairInfos;
    }


    /**
     * 修改报修信息信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRepairInfoInstance(Map info) throws DAOException {
        logger.debug("修改报修信息信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("repairServiceDaoImpl.updateRepairInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改报修信息信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询报修信息数量
     * @param info 报修信息信息
     * @return 报修信息数量
     */
    @Override
    public int queryRepairsCount(Map info) {
        logger.debug("查询报修信息数据 入参 info : {}",info);

        List<Map> businessRepairInfos = sqlSessionTemplate.selectList("repairServiceDaoImpl.queryRepairsCount", info);
        if (businessRepairInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getStaffRepairInfo(Map info) throws DAOException {
        logger.debug("查询报修信息信息 入参 info : {}",info);

        List<Map> businessRepairInfos = sqlSessionTemplate.selectList("repairServiceDaoImpl.getStaffRepairInfo",info);

        return businessRepairInfos;
    }

    @Override
    public int queryStaffRepairsCount(Map info) {
        logger.debug("查询报修信息数据 入参 info : {}",info);

        List<Map> businessRepairInfos = sqlSessionTemplate.selectList("repairServiceDaoImpl.queryStaffRepairsCount", info);
        if (businessRepairInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairInfos.get(0).get("count").toString());    }


    @Override
    public List<Map> getStaffFinishRepairInfo(Map info) throws DAOException {
        logger.debug("查询报修信息信息 入参 info : {}",info);

        List<Map> businessRepairInfos = sqlSessionTemplate.selectList("repairServiceDaoImpl.getStaffFinishRepairInfo",info);

        return businessRepairInfos;
    }

    @Override
    public int queryStaffFinishRepairsCount(Map info) {
        logger.debug("查询报修信息数据 入参 info : {}",info);

        List<Map> businessRepairInfos = sqlSessionTemplate.selectList("repairServiceDaoImpl.queryStaffFinishRepairsCount", info);
        if (businessRepairInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairInfos.get(0).get("count").toString());    }
}
