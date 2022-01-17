package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
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
@Service("workflowStepStaffServiceDaoImpl")
//@Transactional
public class WorkflowStepStaffServiceDaoImpl extends BaseServiceDao implements IWorkflowStepStaffServiceDao {

    private static Logger logger = LoggerFactory.getLogger(WorkflowStepStaffServiceDaoImpl.class);

    /**
     * 工作流节点信息封装
     * @param businessWorkflowStepStaffInfo 工作流节点信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessWorkflowStepStaffInfo(Map businessWorkflowStepStaffInfo) throws DAOException {
        businessWorkflowStepStaffInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存工作流节点信息 入参 businessWorkflowStepStaffInfo : {}",businessWorkflowStepStaffInfo);
        int saveFlag = sqlSessionTemplate.insert("workflowStepStaffServiceDaoImpl.saveBusinessWorkflowStepStaffInfo",businessWorkflowStepStaffInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存工作流节点数据失败："+ JSONObject.toJSONString(businessWorkflowStepStaffInfo));
        }
    }


    /**
     * 查询工作流节点信息
     * @param info bId 信息
     * @return 工作流节点信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessWorkflowStepStaffInfo(Map info) throws DAOException {

        logger.debug("查询工作流节点信息 入参 info : {}",info);

        List<Map> businessWorkflowStepStaffInfos = sqlSessionTemplate.selectList("workflowStepStaffServiceDaoImpl.getBusinessWorkflowStepStaffInfo",info);

        return businessWorkflowStepStaffInfos;
    }



    /**
     * 保存工作流节点信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveWorkflowStepStaffInfoInstance(Map info) throws DAOException {
        logger.debug("保存工作流节点信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("workflowStepStaffServiceDaoImpl.saveWorkflowStepStaffInfoInstance",info);

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
    public List<Map> getWorkflowStepStaffInfo(Map info) throws DAOException {
        logger.debug("查询工作流节点信息 入参 info : {}",info);

        List<Map> businessWorkflowStepStaffInfos = sqlSessionTemplate.selectList("workflowStepStaffServiceDaoImpl.getWorkflowStepStaffInfo",info);

        return businessWorkflowStepStaffInfos;
    }


    /**
     * 修改工作流节点信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateWorkflowStepStaffInfoInstance(Map info) throws DAOException {
        logger.debug("修改工作流节点信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("workflowStepStaffServiceDaoImpl.updateWorkflowStepStaffInfoInstance",info);

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
    public int queryWorkflowStepStaffsCount(Map info) {
        logger.debug("查询工作流节点数据 入参 info : {}",info);

        List<Map> businessWorkflowStepStaffInfos = sqlSessionTemplate.selectList("workflowStepStaffServiceDaoImpl.queryWorkflowStepStaffsCount", info);
        if (businessWorkflowStepStaffInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessWorkflowStepStaffInfos.get(0).get("count").toString());
    }


}
