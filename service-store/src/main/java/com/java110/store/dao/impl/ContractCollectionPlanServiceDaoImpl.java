package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractCollectionPlanServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同收款计划服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractCollectionPlanServiceDaoImpl")
//@Transactional
public class ContractCollectionPlanServiceDaoImpl extends BaseServiceDao implements IContractCollectionPlanServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractCollectionPlanServiceDaoImpl.class);





    /**
     * 保存合同收款计划信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractCollectionPlanInfo(Map info) throws DAOException {
        logger.debug("保存合同收款计划信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractCollectionPlanServiceDaoImpl.saveContractCollectionPlanInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同收款计划信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同收款计划信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractCollectionPlanInfo(Map info) throws DAOException {
        logger.debug("查询合同收款计划信息 入参 info : {}",info);

        List<Map> businessContractCollectionPlanInfos = sqlSessionTemplate.selectList("contractCollectionPlanServiceDaoImpl.getContractCollectionPlanInfo",info);

        return businessContractCollectionPlanInfos;
    }


    /**
     * 修改合同收款计划信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractCollectionPlanInfo(Map info) throws DAOException {
        logger.debug("修改合同收款计划信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractCollectionPlanServiceDaoImpl.updateContractCollectionPlanInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同收款计划信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同收款计划数量
     * @param info 合同收款计划信息
     * @return 合同收款计划数量
     */
    @Override
    public int queryContractCollectionPlansCount(Map info) {
        logger.debug("查询合同收款计划数据 入参 info : {}",info);

        List<Map> businessContractCollectionPlanInfos = sqlSessionTemplate.selectList("contractCollectionPlanServiceDaoImpl.queryContractCollectionPlansCount", info);
        if (businessContractCollectionPlanInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractCollectionPlanInfos.get(0).get("count").toString());
    }


}
