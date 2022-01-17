package com.java110.job.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.job.dao.ITaskAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 定时任务属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("taskAttrServiceDaoImpl")
//@Transactional
public class TaskAttrServiceDaoImpl extends BaseServiceDao implements ITaskAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(TaskAttrServiceDaoImpl.class);

    /**
     * 定时任务属性信息封装
     * @param businessTaskAttrInfo 定时任务属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessTaskAttrInfo(Map businessTaskAttrInfo) throws DAOException {
        businessTaskAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存定时任务属性信息 入参 businessTaskAttrInfo : {}",businessTaskAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("taskAttrServiceDaoImpl.saveBusinessTaskAttrInfo",businessTaskAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存定时任务属性数据失败："+ JSONObject.toJSONString(businessTaskAttrInfo));
        }
    }


    /**
     * 查询定时任务属性信息
     * @param info bId 信息
     * @return 定时任务属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessTaskAttrInfo(Map info) throws DAOException {

        logger.debug("查询定时任务属性信息 入参 info : {}",info);

        List<Map> businessTaskAttrInfos = sqlSessionTemplate.selectList("taskAttrServiceDaoImpl.getBusinessTaskAttrInfo",info);

        return businessTaskAttrInfos;
    }



    /**
     * 保存定时任务属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveTaskAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存定时任务属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("taskAttrServiceDaoImpl.saveTaskAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存定时任务属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询定时任务属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getTaskAttrInfo(Map info) throws DAOException {
        logger.debug("查询定时任务属性信息 入参 info : {}",info);

        List<Map> businessTaskAttrInfos = sqlSessionTemplate.selectList("taskAttrServiceDaoImpl.getTaskAttrInfo",info);

        return businessTaskAttrInfos;
    }


    /**
     * 修改定时任务属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateTaskAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改定时任务属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("taskAttrServiceDaoImpl.updateTaskAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改定时任务属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询定时任务属性数量
     * @param info 定时任务属性信息
     * @return 定时任务属性数量
     */
    @Override
    public int queryTaskAttrsCount(Map info) {
        logger.debug("查询定时任务属性数据 入参 info : {}",info);

        List<Map> businessTaskAttrInfos = sqlSessionTemplate.selectList("taskAttrServiceDaoImpl.queryTaskAttrsCount", info);
        if (businessTaskAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessTaskAttrInfos.get(0).get("count").toString());
    }


}
