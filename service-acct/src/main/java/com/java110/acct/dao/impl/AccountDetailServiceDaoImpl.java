package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IAccountDetailServiceDao;
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
 * 账户交易服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("accountDetailServiceDaoImpl")
//@Transactional
public class AccountDetailServiceDaoImpl extends BaseServiceDao implements IAccountDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AccountDetailServiceDaoImpl.class);

    /**
     * 账户交易信息封装
     *
     * @param businessAccountDetailInfo 账户交易信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAccountDetailInfo(Map businessAccountDetailInfo) throws DAOException {
        businessAccountDetailInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存账户交易信息 入参 businessAccountDetailInfo : {}", businessAccountDetailInfo);
        int saveFlag = sqlSessionTemplate.insert("accountDetailServiceDaoImpl.saveBusinessAccountDetailInfo", businessAccountDetailInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存账户交易数据失败：" + JSONObject.toJSONString(businessAccountDetailInfo));
        }
    }


    /**
     * 查询账户交易信息
     *
     * @param info bId 信息
     * @return 账户交易信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAccountDetailInfo(Map info) throws DAOException {

        logger.debug("查询账户交易信息 入参 info : {}", info);

        List<Map> businessAccountDetailInfos = sqlSessionTemplate.selectList("accountDetailServiceDaoImpl.getBusinessAccountDetailInfo", info);

        return businessAccountDetailInfos;
    }


    /**
     * 保存账户交易信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAccountDetailInfoInstance(Map info) throws DAOException {
        logger.debug("保存账户交易信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("accountDetailServiceDaoImpl.saveAccountDetailInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存账户交易信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询账户交易信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAccountDetailInfo(Map info) throws DAOException {
        logger.debug("查询账户交易信息 入参 info : {}", info);

        List<Map> businessAccountDetailInfos = sqlSessionTemplate.selectList("accountDetailServiceDaoImpl.getAccountDetailInfo", info);

        return businessAccountDetailInfos;
    }


    /**
     * 修改账户交易信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAccountDetailInfoInstance(Map info) throws DAOException {
        logger.debug("修改账户交易信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("accountDetailServiceDaoImpl.updateAccountDetailInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改账户交易信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询账户交易数量
     *
     * @param info 账户交易信息
     * @return 账户交易数量
     */
    @Override
    public int queryAccountDetailsCount(Map info) {
        logger.debug("查询账户交易数据 入参 info : {}", info);

        List<Map> businessAccountDetailInfos = sqlSessionTemplate.selectList("accountDetailServiceDaoImpl.queryAccountDetailsCount", info);
        if (businessAccountDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAccountDetailInfos.get(0).get("count").toString());
    }

    @Override
    public int saveAccountDetails(Map info) {
        int saveFlag = sqlSessionTemplate.update("accountDetailServiceDaoImpl.saveAccountDetails", info);

        return saveFlag;
    }

    @Override
    public int updateAccountDetails(Map info) {
        int saveFlag = sqlSessionTemplate.update("accountDetailServiceDaoImpl.updateAccountDetails", info);

        return saveFlag;
    }


}
