package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractPartyaServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同房屋服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractPartyaServiceDaoImpl")
//@Transactional
public class ContractPartyaServiceDaoImpl extends BaseServiceDao implements IContractPartyaServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractPartyaServiceDaoImpl.class);





    /**
     * 保存合同房屋信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractPartyaInfo(Map info) throws DAOException {
        logger.debug("保存合同房屋信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractPartyaServiceDaoImpl.saveContractPartyaInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同房屋信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同房屋信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractPartyaInfo(Map info) throws DAOException {
        logger.debug("查询合同房屋信息 入参 info : {}",info);

        List<Map> businessContractPartyaInfos = sqlSessionTemplate.selectList("contractPartyaServiceDaoImpl.getContractPartyaInfo",info);

        return businessContractPartyaInfos;
    }


    /**
     * 修改合同房屋信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractPartyaInfo(Map info) throws DAOException {
        logger.debug("修改合同房屋信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractPartyaServiceDaoImpl.updateContractPartyaInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同房屋信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同房屋数量
     * @param info 合同房屋信息
     * @return 合同房屋数量
     */
    @Override
    public int queryContractPartyasCount(Map info) {
        logger.debug("查询合同房屋数据 入参 info : {}",info);

        List<Map> businessContractPartyaInfos = sqlSessionTemplate.selectList("contractPartyaServiceDaoImpl.queryContractPartyasCount", info);
        if (businessContractPartyaInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractPartyaInfos.get(0).get("count").toString());
    }


}
