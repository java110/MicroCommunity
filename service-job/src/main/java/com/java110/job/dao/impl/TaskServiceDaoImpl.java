package com.java110.job.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.job.dao.ITaskServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 定时任务服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("taskServiceDaoImpl")
//@Transactional
public class TaskServiceDaoImpl extends BaseServiceDao implements ITaskServiceDao {

    private static Logger logger = LoggerFactory.getLogger(TaskServiceDaoImpl.class);

    /**
     * 定时任务信息封装
     * @param businessTaskInfo 定时任务信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessTaskInfo(Map businessTaskInfo) throws DAOException {
        businessTaskInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存定时任务信息 入参 businessTaskInfo : {}",businessTaskInfo);
        int saveFlag = sqlSessionTemplate.insert("taskServiceDaoImpl.saveBusinessTaskInfo",businessTaskInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存定时任务数据失败："+ JSONObject.toJSONString(businessTaskInfo));
        }
    }


    /**
     * 查询定时任务信息
     * @param info bId 信息
     * @return 定时任务信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessTaskInfo(Map info) throws DAOException {

        logger.debug("查询定时任务信息 入参 info : {}",info);

        List<Map> businessTaskInfos = sqlSessionTemplate.selectList("taskServiceDaoImpl.getBusinessTaskInfo",info);

        return businessTaskInfos;
    }



    /**
     * 保存定时任务信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveTaskInfoInstance(Map info) throws DAOException {
        logger.debug("保存定时任务信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("taskServiceDaoImpl.saveTaskInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存定时任务信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询定时任务信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTaskInfo(Map info) throws DAOException {
        logger.debug("查询定时任务信息 入参 info : {}",info);

        List<Map> businessTaskInfos = sqlSessionTemplate.selectList("taskServiceDaoImpl.getTaskInfo",info);

        return businessTaskInfos;
    }


    /**
     * 修改定时任务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateTaskInfoInstance(Map info) throws DAOException {
        logger.debug("修改定时任务信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("taskServiceDaoImpl.updateTaskInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改定时任务信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询定时任务数量
     * @param info 定时任务信息
     * @return 定时任务数量
     */
    @Override
    public int queryTasksCount(Map info) {
        logger.debug("查询定时任务数据 入参 info : {}",info);

        List<Map> businessTaskInfos = sqlSessionTemplate.selectList("taskServiceDaoImpl.queryTasksCount", info);
        if (businessTaskInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTaskInfos.get(0).get("count").toString());
    }

    /**
     * 查询定时任务数量
     * @param info 定时任务信息
     * @return 定时任务数量
     */
    @Override
    public int queryTaskTemplateCount(Map info) {
        logger.debug("查询定时任务模板数据 入参 info : {}",info);

        List<Map> businessTaskInfos = sqlSessionTemplate.selectList("taskServiceDaoImpl.queryTaskTemplateCount", info);
        if (businessTaskInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTaskInfos.get(0).get("count").toString());
    }

    /**
     * 查询定时任务信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTaskTemplateInfo(Map info) throws DAOException {
        logger.debug("查询定时任务信息 入参 info : {}",info);

        List<Map> businessTaskInfos = sqlSessionTemplate.selectList("taskServiceDaoImpl.getTaskTemplateInfo",info);

        return businessTaskInfos;
    }

    /**
     * 查询定时任务数量
     * @param info 定时任务信息
     * @return 定时任务数量
     */
    @Override
    public int queryTaskTemplateSpecCount(Map info) {
        logger.debug("查询定时任务模板数据 入参 info : {}",info);

        List<Map> businessTaskInfos = sqlSessionTemplate.selectList("taskServiceDaoImpl.queryTaskTemplateSpecCount", info);
        if (businessTaskInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTaskInfos.get(0).get("count").toString());
    }

    /**
     * 查询定时任务信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTaskTemplateSpecInfo(Map info) throws DAOException {
        logger.debug("查询定时任务信息 入参 info : {}",info);

        List<Map> businessTaskInfos = sqlSessionTemplate.selectList("taskServiceDaoImpl.getTaskTemplateSpecInfo",info);

        return businessTaskInfos;
    }



}
