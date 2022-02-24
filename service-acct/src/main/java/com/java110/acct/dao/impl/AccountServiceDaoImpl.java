package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IAccountServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 账户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("accountServiceDaoImpl")
//@Transactional
public class AccountServiceDaoImpl extends BaseServiceDao implements IAccountServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AccountServiceDaoImpl.class);

    /**
     * 账户信息封装
     *
     * @param businessAccountInfo 账户信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAccountInfo(Map businessAccountInfo) throws DAOException {
        businessAccountInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存账户信息 入参 businessAccountInfo : {}", businessAccountInfo);
        int saveFlag = sqlSessionTemplate.insert("accountServiceDaoImpl.saveBusinessAccountInfo", businessAccountInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存账户数据失败：" + JSONObject.toJSONString(businessAccountInfo));
        }
    }


    /**
     * 查询账户信息
     *
     * @param info bId 信息
     * @return 账户信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAccountInfo(Map info) throws DAOException {

        logger.debug("查询账户信息 入参 info : {}", info);

        List<Map> businessAccountInfos = sqlSessionTemplate.selectList("accountServiceDaoImpl.getBusinessAccountInfo", info);

        return businessAccountInfos;
    }


    /**
     * 保存账户信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAccountInfoInstance(Map info) throws DAOException {
        logger.debug("保存账户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("accountServiceDaoImpl.saveAccountInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存账户信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询账户信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAccountInfo(Map info) throws DAOException {
        logger.debug("查询账户信息 入参 info : {}", info);

        List<Map> businessAccountInfos = sqlSessionTemplate.selectList("accountServiceDaoImpl.getAccountInfo", info);

        return businessAccountInfos;
    }


    /**
     * 修改账户信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAccountInfoInstance(Map info) throws DAOException {
        logger.debug("修改账户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("accountServiceDaoImpl.updateAccountInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改账户信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询账户数量
     *
     * @param info 账户信息
     * @return 账户数量
     */
    @Override
    public int queryAccountsCount(Map info) {
        logger.debug("查询账户数据 入参 info : {}", info);

        List<Map> businessAccountInfos = sqlSessionTemplate.selectList("accountServiceDaoImpl.queryAccountsCount", info);
        if (businessAccountInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAccountInfos.get(0).get("count").toString());
    }

    /**
     * 查询账户数量
     *
     * @param info 账户信息
     * @return 账户数量
     */
    @Override
    public int updateAccount(Map info) {
        logger.debug("查询账户数据 入参 info : {}", info);

        int flag = sqlSessionTemplate.update("accountServiceDaoImpl.updateAccount", info);
        return flag;
    }

    @Override
    public void saveAccount(Map info) {
        logger.debug("查询账户数据 入参 info : {}", info);

        sqlSessionTemplate.update("accountServiceDaoImpl.saveAccount", info);

    }


}
