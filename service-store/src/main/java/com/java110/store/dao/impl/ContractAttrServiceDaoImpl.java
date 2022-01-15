package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractAttrServiceDaoImpl")
//@Transactional
public class ContractAttrServiceDaoImpl extends BaseServiceDao implements IContractAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractAttrServiceDaoImpl.class);





    /**
     * 保存合同属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractAttrInfo(Map info) throws DAOException {
        logger.debug("保存合同属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractAttrServiceDaoImpl.saveContractAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractAttrInfo(Map info) throws DAOException {
        logger.debug("查询合同属性信息 入参 info : {}",info);

        List<Map> businessContractAttrInfos = sqlSessionTemplate.selectList("contractAttrServiceDaoImpl.getContractAttrInfo",info);

        return businessContractAttrInfos;
    }


    /**
     * 修改合同属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractAttrInfo(Map info) throws DAOException {
        logger.debug("修改合同属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractAttrServiceDaoImpl.updateContractAttrInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同属性数量
     * @param info 合同属性信息
     * @return 合同属性数量
     */
    @Override
    public int queryContractAttrsCount(Map info) {
        logger.debug("查询合同属性数据 入参 info : {}",info);

        List<Map> businessContractAttrInfos = sqlSessionTemplate.selectList("contractAttrServiceDaoImpl.queryContractAttrsCount", info);
        if (businessContractAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractAttrInfos.get(0).get("count").toString());
    }


}
