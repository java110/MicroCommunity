package com.java110.oa.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.oa.dao.IOaWorkflowFormServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * OA表单服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("oaWorkflowFormServiceDaoImpl")
//@Transactional
public class OaWorkflowFormServiceDaoImpl extends BaseServiceDao implements IOaWorkflowFormServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OaWorkflowFormServiceDaoImpl.class);





    /**
     * 保存OA表单信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOaWorkflowFormInfo(Map info) throws DAOException {
        logger.debug("保存OA表单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("oaWorkflowFormServiceDaoImpl.saveOaWorkflowFormInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存OA表单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询OA表单信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOaWorkflowFormInfo(Map info) throws DAOException {
        logger.debug("查询OA表单信息 入参 info : {}",info);

        List<Map> businessOaWorkflowFormInfos = sqlSessionTemplate.selectList("oaWorkflowFormServiceDaoImpl.getOaWorkflowFormInfo",info);

        return businessOaWorkflowFormInfos;
    }


    /**
     * 修改OA表单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOaWorkflowFormInfo(Map info) throws DAOException {
        logger.debug("修改OA表单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("oaWorkflowFormServiceDaoImpl.updateOaWorkflowFormInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改OA表单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询OA表单数量
     * @param info OA表单信息
     * @return OA表单数量
     */
    @Override
    public int queryOaWorkflowFormsCount(Map info) {
        logger.debug("查询OA表单数据 入参 info : {}",info);

        List<Map> businessOaWorkflowFormInfos = sqlSessionTemplate.selectList("oaWorkflowFormServiceDaoImpl.queryOaWorkflowFormsCount", info);
        if (businessOaWorkflowFormInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOaWorkflowFormInfos.get(0).get("count").toString());
    }

    /**
     * 查询是否有表
     * @param info
     * @return
     */
    @Override
    public List<Map> hasTable(Map info) {
        List<Map> rows = sqlSessionTemplate.selectList("oaWorkflowFormServiceDaoImpl.hasTable", info);
        return  rows;
    }
    /**
     * 查询是否有表
     * @param info
     * @return
     */
    @Override
    public int createTable(Map info) {
        int flag = sqlSessionTemplate.update("oaWorkflowFormServiceDaoImpl.createTable", info);
        return  flag;
    }

    @Override
    public int queryOaWorkflowFormDataCount(Map paramIn) {
        logger.debug("查询queryOaWorkflowFormDataCount数据 入参 info : {}",paramIn);

        List<Map> businessOaWorkflowFormInfos = sqlSessionTemplate.selectList("oaWorkflowFormServiceDaoImpl.queryOaWorkflowFormDataCount", paramIn);
        if (businessOaWorkflowFormInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOaWorkflowFormInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryOaWorkflowFormDatas(Map paramIn) {
        logger.debug("查询queryOaWorkflowFormDatas信息 入参 info : {}",paramIn);

        List<Map> businessOaWorkflowFormInfos = sqlSessionTemplate.selectList("oaWorkflowFormServiceDaoImpl.queryOaWorkflowFormDatas",paramIn);

        return businessOaWorkflowFormInfos;
    }

    @Override
    public int saveOaWorkflowFormDataInfo(Map paramIn) {
        logger.debug("保存saveOaWorkflowFormDataInfo 入参 info : {}",paramIn);

        int saveFlag = sqlSessionTemplate.insert("oaWorkflowFormServiceDaoImpl.saveOaWorkflowFormDataInfo",paramIn);
        return saveFlag;
    }



    @Override
    public int updateOaWorkflowFormData(Map paramIn) {
        logger.debug("保存updateOaWorkflowFormData 入参 info : {}",paramIn);

        int saveFlag = sqlSessionTemplate.insert("oaWorkflowFormServiceDaoImpl.updateOaWorkflowFormData",paramIn);
        return saveFlag;
    }
    @Override
    public int updateOaWorkflowFormDataAll(Map paramIn) {
        logger.debug("保存updateOaWorkflowFormData 入参 info : {}",paramIn);

        int saveFlag = sqlSessionTemplate.insert("oaWorkflowFormServiceDaoImpl.updateOaWorkflowFormDataAll",paramIn);
        return saveFlag;
    }
}
