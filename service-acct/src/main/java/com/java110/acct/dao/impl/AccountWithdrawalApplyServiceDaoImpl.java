package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.acct.dao.IAccountWithdrawalApplyServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 账户提现服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("accountWithdrawalApplyServiceDaoImpl")
//@Transactional
public class AccountWithdrawalApplyServiceDaoImpl extends BaseServiceDao implements IAccountWithdrawalApplyServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AccountWithdrawalApplyServiceDaoImpl.class);





    /**
     * 保存账户提现信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAccountWithdrawalApplyInfo(Map info) throws DAOException {
        logger.debug("保存账户提现信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("accountWithdrawalApplyServiceDaoImpl.saveAccountWithdrawalApplyInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存账户提现信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询账户提现信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAccountWithdrawalApplyInfo(Map info) throws DAOException {
        logger.debug("查询账户提现信息 入参 info : {}",info);

        List<Map> businessAccountWithdrawalApplyInfos = sqlSessionTemplate.selectList("accountWithdrawalApplyServiceDaoImpl.getAccountWithdrawalApplyInfo",info);

        return businessAccountWithdrawalApplyInfos;
    }


    /**
     * 修改账户提现信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAccountWithdrawalApplyInfo(Map info) throws DAOException {
        logger.debug("修改账户提现信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("accountWithdrawalApplyServiceDaoImpl.updateAccountWithdrawalApplyInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改账户提现信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询账户提现数量
     * @param info 账户提现信息
     * @return 账户提现数量
     */
    @Override
    public int queryAccountWithdrawalApplysCount(Map info) {
        logger.debug("查询账户提现数据 入参 info : {}",info);

        List<Map> businessAccountWithdrawalApplyInfos = sqlSessionTemplate.selectList("accountWithdrawalApplyServiceDaoImpl.queryAccountWithdrawalApplysCount", info);
        if (businessAccountWithdrawalApplyInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAccountWithdrawalApplyInfos.get(0).get("count").toString());
    }

    /**
     * 查询账户提现信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> listStateWithdrawalApplys(Map info) throws DAOException {
        logger.debug("查询账户提现信息 入参 info : {}",info);

        List<Map> businessAccountWithdrawalApplyInfos = sqlSessionTemplate.selectList("accountWithdrawalApplyServiceDaoImpl.listStateWithdrawalApplys",info);

        return businessAccountWithdrawalApplyInfos;
    }

    /**
     * 查询账户提现数量
     * @param info 账户提现信息
     * @return 账户提现数量
     */
    @Override
    public int listStateWithdrawalApplysCount(Map info) {
        logger.debug("查询账户提现数据 入参 info : {}",info);

        List<Map> businessAccountWithdrawalApplyInfos = sqlSessionTemplate.selectList("accountWithdrawalApplyServiceDaoImpl.listStateWithdrawalApplysCount", info);
        if (businessAccountWithdrawalApplyInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAccountWithdrawalApplyInfos.get(0).get("count").toString());
    }
}
