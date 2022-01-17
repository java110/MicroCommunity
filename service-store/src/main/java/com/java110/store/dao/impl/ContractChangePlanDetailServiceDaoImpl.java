package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractChangePlanDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同变更明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractChangePlanDetailServiceDaoImpl")
//@Transactional
public class ContractChangePlanDetailServiceDaoImpl extends BaseServiceDao implements IContractChangePlanDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractChangePlanDetailServiceDaoImpl.class);





    /**
     * 保存合同变更明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractChangePlanDetailInfo(Map info) throws DAOException {
        logger.debug("保存合同变更明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractChangePlanDetailServiceDaoImpl.saveContractChangePlanDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同变更明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同变更明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractChangePlanDetailInfo(Map info) throws DAOException {
        logger.debug("查询合同变更明细信息 入参 info : {}",info);

        List<Map> businessContractChangePlanDetailInfos = sqlSessionTemplate.selectList("contractChangePlanDetailServiceDaoImpl.getContractChangePlanDetailInfo",info);

        return businessContractChangePlanDetailInfos;
    }


    /**
     * 修改合同变更明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractChangePlanDetailInfo(Map info) throws DAOException {
        logger.debug("修改合同变更明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractChangePlanDetailServiceDaoImpl.updateContractChangePlanDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同变更明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同变更明细数量
     * @param info 合同变更明细信息
     * @return 合同变更明细数量
     */
    @Override
    public int queryContractChangePlanDetailsCount(Map info) {
        logger.debug("查询合同变更明细数据 入参 info : {}",info);

        List<Map> businessContractChangePlanDetailInfos = sqlSessionTemplate.selectList("contractChangePlanDetailServiceDaoImpl.queryContractChangePlanDetailsCount", info);
        if (businessContractChangePlanDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractChangePlanDetailInfos.get(0).get("count").toString());
    }


}
