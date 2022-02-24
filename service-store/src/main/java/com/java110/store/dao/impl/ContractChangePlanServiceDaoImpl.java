package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractChangePlanServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同变更计划服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractChangePlanServiceDaoImpl")
//@Transactional
public class ContractChangePlanServiceDaoImpl extends BaseServiceDao implements IContractChangePlanServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractChangePlanServiceDaoImpl.class);





    /**
     * 保存合同变更计划信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractChangePlanInfo(Map info) throws DAOException {
        logger.debug("保存合同变更计划信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractChangePlanServiceDaoImpl.saveContractChangePlanInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同变更计划信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同变更计划信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractChangePlanInfo(Map info) throws DAOException {
        logger.debug("查询合同变更计划信息 入参 info : {}",info);

        List<Map> businessContractChangePlanInfos = sqlSessionTemplate.selectList("contractChangePlanServiceDaoImpl.getContractChangePlanInfo",info);

        return businessContractChangePlanInfos;
    }


    /**
     * 修改合同变更计划信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractChangePlanInfo(Map info) throws DAOException {
        logger.debug("修改合同变更计划信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractChangePlanServiceDaoImpl.updateContractChangePlanInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同变更计划信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同变更计划数量
     * @param info 合同变更计划信息
     * @return 合同变更计划数量
     */
    @Override
    public int queryContractChangePlansCount(Map info) {
        logger.debug("查询合同变更计划数据 入参 info : {}",info);

        List<Map> businessContractChangePlanInfos = sqlSessionTemplate.selectList("contractChangePlanServiceDaoImpl.queryContractChangePlansCount", info);
        if (businessContractChangePlanInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractChangePlanInfos.get(0).get("count").toString());
    }


}
