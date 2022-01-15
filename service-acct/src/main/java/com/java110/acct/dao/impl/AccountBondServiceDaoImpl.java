package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.acct.dao.IAccountBondServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 保证金服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("accountBondServiceDaoImpl")
//@Transactional
public class AccountBondServiceDaoImpl extends BaseServiceDao implements IAccountBondServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AccountBondServiceDaoImpl.class);





    /**
     * 保存保证金信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAccountBondInfo(Map info) throws DAOException {
        logger.debug("保存保证金信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("accountBondServiceDaoImpl.saveAccountBondInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存保证金信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询保证金信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAccountBondInfo(Map info) throws DAOException {
        logger.debug("查询保证金信息 入参 info : {}",info);

        List<Map> businessAccountBondInfos = sqlSessionTemplate.selectList("accountBondServiceDaoImpl.getAccountBondInfo",info);

        return businessAccountBondInfos;
    }


    /**
     * 修改保证金信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAccountBondInfo(Map info) throws DAOException {
        logger.debug("修改保证金信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("accountBondServiceDaoImpl.updateAccountBondInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改保证金信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询保证金数量
     * @param info 保证金信息
     * @return 保证金数量
     */
    @Override
    public int queryAccountBondsCount(Map info) {
        logger.debug("查询保证金数据 入参 info : {}",info);

        List<Map> businessAccountBondInfos = sqlSessionTemplate.selectList("accountBondServiceDaoImpl.queryAccountBondsCount", info);
        if (businessAccountBondInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAccountBondInfos.get(0).get("count").toString());
    }


}
