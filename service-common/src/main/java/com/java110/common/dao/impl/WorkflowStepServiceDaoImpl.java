package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IWorkflowStepServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 工作流节点服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("workflowStepServiceDaoImpl")
//@Transactional
public class WorkflowStepServiceDaoImpl extends BaseServiceDao implements IWorkflowStepServiceDao {

    private static Logger logger = LoggerFactory.getLogger(WorkflowStepServiceDaoImpl.class);

    /**
     * 工作流节点信息封装
     * @param businessWorkflowStepInfo 工作流节点信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessWorkflowStepInfo(Map businessWorkflowStepInfo) throws DAOException {
        businessWorkflowStepInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存工作流节点信息 入参 businessWorkflowStepInfo : {}",businessWorkflowStepInfo);
        int saveFlag = sqlSessionTemplate.insert("workflowStepServiceDaoImpl.saveBusinessWorkflowStepInfo",businessWorkflowStepInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存工作流节点数据失败："+ JSONObject.toJSONString(businessWorkflowStepInfo));
        }
    }


    /**
     * 查询工作流节点信息
     * @param info bId 信息
     * @return 工作流节点信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessWorkflowStepInfo(Map info) throws DAOException {

        logger.debug("查询工作流节点信息 入参 info : {}",info);

        List<Map> businessWorkflowStepInfos = sqlSessionTemplate.selectList("workflowStepServiceDaoImpl.getBusinessWorkflowStepInfo",info);

        return businessWorkflowStepInfos;
    }



    /**
     * 保存工作流节点信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveWorkflowStepInfoInstance(Map info) throws DAOException {
        logger.debug("保存工作流节点信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("workflowStepServiceDaoImpl.saveWorkflowStepInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存工作流节点信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询工作流节点信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getWorkflowStepInfo(Map info) throws DAOException {
        logger.debug("查询工作流节点信息 入参 info : {}",info);

        List<Map> businessWorkflowStepInfos = sqlSessionTemplate.selectList("workflowStepServiceDaoImpl.getWorkflowStepInfo",info);

        return businessWorkflowStepInfos;
    }


    /**
     * 修改工作流节点信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateWorkflowStepInfoInstance(Map info) throws DAOException {
        logger.debug("修改工作流节点信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("workflowStepServiceDaoImpl.updateWorkflowStepInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改工作流节点信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询工作流节点数量
     * @param info 工作流节点信息
     * @return 工作流节点数量
     */
    @Override
    public int queryWorkflowStepsCount(Map info) {
        logger.debug("查询工作流节点数据 入参 info : {}",info);

        List<Map> businessWorkflowStepInfos = sqlSessionTemplate.selectList("workflowStepServiceDaoImpl.queryWorkflowStepsCount", info);
        if (businessWorkflowStepInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessWorkflowStepInfos.get(0).get("count").toString());
    }


}
