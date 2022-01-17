package com.java110.oa.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.oa.dao.IOaWorkflowServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * OA工作流服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("oaWorkflowServiceDaoImpl")
//@Transactional
public class OaWorkflowServiceDaoImpl extends BaseServiceDao implements IOaWorkflowServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OaWorkflowServiceDaoImpl.class);





    /**
     * 保存OA工作流信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOaWorkflowInfo(Map info) throws DAOException {
        logger.debug("保存OA工作流信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("oaWorkflowServiceDaoImpl.saveOaWorkflowInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存OA工作流信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询OA工作流信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOaWorkflowInfo(Map info) throws DAOException {
        logger.debug("查询OA工作流信息 入参 info : {}",info);

        List<Map> businessOaWorkflowInfos = sqlSessionTemplate.selectList("oaWorkflowServiceDaoImpl.getOaWorkflowInfo",info);

        return businessOaWorkflowInfos;
    }


    /**
     * 修改OA工作流信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOaWorkflowInfo(Map info) throws DAOException {
        logger.debug("修改OA工作流信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("oaWorkflowServiceDaoImpl.updateOaWorkflowInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改OA工作流信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询OA工作流数量
     * @param info OA工作流信息
     * @return OA工作流数量
     */
    @Override
    public int queryOaWorkflowsCount(Map info) {
        logger.debug("查询OA工作流数据 入参 info : {}",info);

        List<Map> businessOaWorkflowInfos = sqlSessionTemplate.selectList("oaWorkflowServiceDaoImpl.queryOaWorkflowsCount", info);
        if (businessOaWorkflowInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOaWorkflowInfos.get(0).get("count").toString());
    }


}
