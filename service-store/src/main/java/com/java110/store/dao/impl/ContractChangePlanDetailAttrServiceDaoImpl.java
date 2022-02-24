package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractChangePlanDetailAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同变更属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractChangePlanDetailAttrServiceDaoImpl")
//@Transactional
public class ContractChangePlanDetailAttrServiceDaoImpl extends BaseServiceDao implements IContractChangePlanDetailAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractChangePlanDetailAttrServiceDaoImpl.class);





    /**
     * 保存合同变更属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractChangePlanDetailAttrInfo(Map info) throws DAOException {
        logger.debug("保存合同变更属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractChangePlanDetailAttrServiceDaoImpl.saveContractChangePlanDetailAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同变更属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同变更属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractChangePlanDetailAttrInfo(Map info) throws DAOException {
        logger.debug("查询合同变更属性信息 入参 info : {}",info);

        List<Map> businessContractChangePlanDetailAttrInfos = sqlSessionTemplate.selectList("contractChangePlanDetailAttrServiceDaoImpl.getContractChangePlanDetailAttrInfo",info);

        return businessContractChangePlanDetailAttrInfos;
    }


    /**
     * 修改合同变更属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractChangePlanDetailAttrInfo(Map info) throws DAOException {
        logger.debug("修改合同变更属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractChangePlanDetailAttrServiceDaoImpl.updateContractChangePlanDetailAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同变更属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同变更属性数量
     * @param info 合同变更属性信息
     * @return 合同变更属性数量
     */
    @Override
    public int queryContractChangePlanDetailAttrsCount(Map info) {
        logger.debug("查询合同变更属性数据 入参 info : {}",info);

        List<Map> businessContractChangePlanDetailAttrInfos = sqlSessionTemplate.selectList("contractChangePlanDetailAttrServiceDaoImpl.queryContractChangePlanDetailAttrsCount", info);
        if (businessContractChangePlanDetailAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractChangePlanDetailAttrInfos.get(0).get("count").toString());
    }


}
