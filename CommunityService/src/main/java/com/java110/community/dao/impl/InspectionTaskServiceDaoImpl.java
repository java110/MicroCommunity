package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionTaskServiceDao;
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
 * 活动服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("inspectionTaskServiceDaoImpl")
//@Transactional
public class InspectionTaskServiceDaoImpl extends BaseServiceDao implements IInspectionTaskServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionTaskServiceDaoImpl.class);

    /**
     * 活动信息封装
     *
     * @param businessInspectionTaskInfo 活动信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessInspectionTaskInfo(Map businessInspectionTaskInfo) throws DAOException {
        businessInspectionTaskInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存活动信息 入参 businessInspectionTaskInfo : {}", businessInspectionTaskInfo);
        int saveFlag = sqlSessionTemplate.insert("inspectionTaskServiceDaoImpl.saveBusinessInspectionTaskInfo", businessInspectionTaskInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存活动数据失败：" + JSONObject.toJSONString(businessInspectionTaskInfo));
        }
    }


    /**
     * 查询活动信息
     *
     * @param info bId 信息
     * @return 活动信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessInspectionTaskInfo(Map info) throws DAOException {

        logger.debug("查询活动信息 入参 info : {}", info);

        List<Map> businessInspectionTaskInfos = sqlSessionTemplate.selectList("inspectionTaskServiceDaoImpl.getBusinessInspectionTaskInfo", info);

        return businessInspectionTaskInfos;
    }


    /**
     * 保存活动信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveInspectionTaskInfoInstance(Map info) throws DAOException {
        logger.debug("保存活动信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("inspectionTaskServiceDaoImpl.saveInspectionTaskInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存活动信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询活动信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionTaskInfo(Map info) throws DAOException {
        logger.debug("查询活动信息 入参 info : {}", info);

        List<Map> businessInspectionTaskInfos = sqlSessionTemplate.selectList("inspectionTaskServiceDaoImpl.getInspectionTaskInfo", info);

        return businessInspectionTaskInfos;
    }


    /**
     * 修改活动信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateInspectionTaskInfoInstance(Map info) throws DAOException {
        logger.debug("修改活动信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("inspectionTaskServiceDaoImpl.updateInspectionTaskInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改活动信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询活动数量
     *
     * @param info 活动信息
     * @return 活动数量
     */
    @Override
    public int queryInspectionTasksCount(Map info) {
        logger.debug("查询活动数据 入参 info : {}", info);

        List<Map> businessInspectionTaskInfos = sqlSessionTemplate.selectList("inspectionTaskServiceDaoImpl.queryInspectionTasksCount", info);
        if (businessInspectionTaskInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionTaskInfos.get(0).get("count").toString());
    }

    /**
     * 查询今天巡检计划信息
     * @return
     */
    public List<Map> queryTodayInspectionPlan(Map info){
        logger.debug("查询活动数据 入参 info : {}", info);

        List<Map> InspectionPlans = sqlSessionTemplate.selectList("inspectionTaskServiceDaoImpl.queryTodayInspectionPlan", info);

        return InspectionPlans;
    }

    /**
     * 生成巡检任务信息
     * @return
     */
    public int insertInspectionTask(Map info){
        logger.debug("插入 巡检任务 入参 info : {}", info);

        return sqlSessionTemplate.update("inspectionTaskServiceDaoImpl.insertInspectionTask", info);
    }

    /**
     * 生成巡检任务明细信息
     * @return
     */
    public int insertInspectionTaskDetail(Map info){
        logger.debug("插入 巡检任务明细 入参 info : {}", info);

        return sqlSessionTemplate.update("inspectionTaskServiceDaoImpl.insertInspectionTaskDetail", info);
    }

}
