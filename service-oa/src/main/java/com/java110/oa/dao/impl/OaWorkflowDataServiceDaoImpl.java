package com.java110.oa.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.oa.dao.IOaWorkflowDataServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * OA表单审批数据服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("oaWorkflowDataServiceDaoImpl")
//@Transactional
public class OaWorkflowDataServiceDaoImpl extends BaseServiceDao implements IOaWorkflowDataServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OaWorkflowDataServiceDaoImpl.class);





    /**
     * 保存OA表单审批数据信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOaWorkflowDataInfo(Map info) throws DAOException {
        logger.debug("保存OA表单审批数据信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("oaWorkflowDataServiceDaoImpl.saveOaWorkflowDataInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存OA表单审批数据信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询OA表单审批数据信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOaWorkflowDataInfo(Map info) throws DAOException {
        logger.debug("查询OA表单审批数据信息 入参 info : {}",info);

        List<Map> businessOaWorkflowDataInfos = sqlSessionTemplate.selectList("oaWorkflowDataServiceDaoImpl.getOaWorkflowDataInfo",info);

        return businessOaWorkflowDataInfos;
    }


    /**
     * 修改OA表单审批数据信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOaWorkflowDataInfo(Map info) throws DAOException {
        logger.debug("修改OA表单审批数据信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("oaWorkflowDataServiceDaoImpl.updateOaWorkflowDataInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改OA表单审批数据信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询OA表单审批数据数量
     * @param info OA表单审批数据信息
     * @return OA表单审批数据数量
     */
    @Override
    public int queryOaWorkflowDatasCount(Map info) {
        logger.debug("查询OA表单审批数据数据 入参 info : {}",info);

        List<Map> businessOaWorkflowDataInfos = sqlSessionTemplate.selectList("oaWorkflowDataServiceDaoImpl.queryOaWorkflowDatasCount", info);
        if (businessOaWorkflowDataInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOaWorkflowDataInfos.get(0).get("count").toString());
    }


}
