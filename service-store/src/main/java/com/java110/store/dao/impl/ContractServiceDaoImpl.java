package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同管理服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractServiceDaoImpl")
//@Transactional
public class ContractServiceDaoImpl extends BaseServiceDao implements IContractServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractServiceDaoImpl.class);





    /**
     * 保存合同管理信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractInfo(Map info) throws DAOException {
        logger.debug("保存合同管理信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractServiceDaoImpl.saveContractInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同管理信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同管理信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractInfo(Map info) throws DAOException {
        logger.debug("查询合同管理信息 入参 info : {}",info);

        List<Map> businessContractInfos = sqlSessionTemplate.selectList("contractServiceDaoImpl.getContractInfo",info);

        return businessContractInfos;
    }


    /**
     * 修改合同管理信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractInfo(Map info) throws DAOException {
        logger.debug("修改合同管理信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractServiceDaoImpl.updateContractInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同管理信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同管理数量
     * @param info 合同管理信息
     * @return 合同管理数量
     */
    @Override
    public int queryContractsCount(Map info) {
        logger.debug("查询合同管理数据 入参 info : {}",info);

        List<Map> businessContractInfos = sqlSessionTemplate.selectList("contractServiceDaoImpl.queryContractsCount", info);
        if (businessContractInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryContractsByOwnerIds(Map info) {
        logger.debug("查询合同管理数据 入参 info : {}",info);

        List<Map> result = sqlSessionTemplate.selectList("contractServiceDaoImpl.queryContractsByOwnerIds", info);
        return result;
    }


}
