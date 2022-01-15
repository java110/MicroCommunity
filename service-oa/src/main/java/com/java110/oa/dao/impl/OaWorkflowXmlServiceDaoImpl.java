package com.java110.oa.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.oa.dao.IOaWorkflowXmlServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * OA流程图服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("oaWorkflowXmlServiceDaoImpl")
//@Transactional
public class OaWorkflowXmlServiceDaoImpl extends BaseServiceDao implements IOaWorkflowXmlServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OaWorkflowXmlServiceDaoImpl.class);





    /**
     * 保存OA流程图信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOaWorkflowXmlInfo(Map info) throws DAOException {
        logger.debug("保存OA流程图信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("oaWorkflowXmlServiceDaoImpl.saveOaWorkflowXmlInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存OA流程图信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询OA流程图信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOaWorkflowXmlInfo(Map info) throws DAOException {
        logger.debug("查询OA流程图信息 入参 info : {}",info);

        List<Map> businessOaWorkflowXmlInfos = sqlSessionTemplate.selectList("oaWorkflowXmlServiceDaoImpl.getOaWorkflowXmlInfo",info);

        return businessOaWorkflowXmlInfos;
    }


    /**
     * 修改OA流程图信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOaWorkflowXmlInfo(Map info) throws DAOException {
        logger.debug("修改OA流程图信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("oaWorkflowXmlServiceDaoImpl.updateOaWorkflowXmlInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改OA流程图信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询OA流程图数量
     * @param info OA流程图信息
     * @return OA流程图数量
     */
    @Override
    public int queryOaWorkflowXmlsCount(Map info) {
        logger.debug("查询OA流程图数据 入参 info : {}",info);

        List<Map> businessOaWorkflowXmlInfos = sqlSessionTemplate.selectList("oaWorkflowXmlServiceDaoImpl.queryOaWorkflowXmlsCount", info);
        if (businessOaWorkflowXmlInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOaWorkflowXmlInfos.get(0).get("count").toString());
    }


}
