package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.acct.dao.IAccountBondObjServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 保证金对象服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("accountBondObjServiceDaoImpl")
//@Transactional
public class AccountBondObjServiceDaoImpl extends BaseServiceDao implements IAccountBondObjServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AccountBondObjServiceDaoImpl.class);





    /**
     * 保存保证金对象信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAccountBondObjInfo(Map info) throws DAOException {
        logger.debug("保存保证金对象信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("accountBondObjServiceDaoImpl.saveAccountBondObjInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存保证金对象信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询保证金对象信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAccountBondObjInfo(Map info) throws DAOException {
        logger.debug("查询保证金对象信息 入参 info : {}",info);

        List<Map> businessAccountBondObjInfos = sqlSessionTemplate.selectList("accountBondObjServiceDaoImpl.getAccountBondObjInfo",info);

        return businessAccountBondObjInfos;
    }


    /**
     * 修改保证金对象信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAccountBondObjInfo(Map info) throws DAOException {
        logger.debug("修改保证金对象信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("accountBondObjServiceDaoImpl.updateAccountBondObjInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改保证金对象信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询保证金对象数量
     * @param info 保证金对象信息
     * @return 保证金对象数量
     */
    @Override
    public int queryAccountBondObjsCount(Map info) {
        logger.debug("查询保证金对象数据 入参 info : {}",info);

        List<Map> businessAccountBondObjInfos = sqlSessionTemplate.selectList("accountBondObjServiceDaoImpl.queryAccountBondObjsCount", info);
        if (businessAccountBondObjInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAccountBondObjInfos.get(0).get("count").toString());
    }


}
