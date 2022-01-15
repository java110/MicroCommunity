package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IWorkflowServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("workflowServiceDaoImpl")
//@Transactional
public class WorkflowServiceDaoImpl extends BaseServiceDao implements IWorkflowServiceDao {

    private static Logger logger = LoggerFactory.getLogger(WorkflowServiceDaoImpl.class);

    /**
     * 工作流信息封装
     * @param businessWorkflowInfo 工作流信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessWorkflowInfo(Map businessWorkflowInfo) throws DAOException {
        businessWorkflowInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存工作流信息 入参 businessWorkflowInfo : {}",businessWorkflowInfo);
        int saveFlag = sqlSessionTemplate.insert("workflowServiceDaoImpl.saveBusinessWorkflowInfo",businessWorkflowInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存工作流数据失败："+ JSONObject.toJSONString(businessWorkflowInfo));
        }
    }


    /**
     * 查询工作流信息
     * @param info bId 信息
     * @return 工作流信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessWorkflowInfo(Map info) throws DAOException {

        logger.debug("查询工作流信息 入参 info : {}",info);

        List<Map> businessWorkflowInfos = sqlSessionTemplate.selectList("workflowServiceDaoImpl.getBusinessWorkflowInfo",info);

        return businessWorkflowInfos;
    }



    /**
     * 保存工作流信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveWorkflowInfoInstance(Map info) throws DAOException {
        logger.debug("保存工作流信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("workflowServiceDaoImpl.saveWorkflowInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存工作流信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询工作流信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getWorkflowInfo(Map info) throws DAOException {
        logger.debug("查询工作流信息 入参 info : {}",info);

        List<Map> businessWorkflowInfos = sqlSessionTemplate.selectList("workflowServiceDaoImpl.getWorkflowInfo",info);

        return businessWorkflowInfos;
    }


    /**
     * 修改工作流信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateWorkflowInfoInstance(Map info) throws DAOException {
        logger.debug("修改工作流信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("workflowServiceDaoImpl.updateWorkflowInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改工作流信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询工作流数量
     * @param info 工作流信息
     * @return 工作流数量
     */
    @Override
    public int queryWorkflowsCount(Map info) {
        logger.debug("查询工作流数据 入参 info : {}",info);

        List<Map> businessWorkflowInfos = sqlSessionTemplate.selectList("workflowServiceDaoImpl.queryWorkflowsCount", info);
        if (businessWorkflowInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessWorkflowInfos.get(0).get("count").toString());
    }


}
