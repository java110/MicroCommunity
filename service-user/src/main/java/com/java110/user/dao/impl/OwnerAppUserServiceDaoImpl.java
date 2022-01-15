package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOwnerAppUserServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 绑定业主服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("ownerAppUserServiceDaoImpl")
//@Transactional
public class OwnerAppUserServiceDaoImpl extends BaseServiceDao implements IOwnerAppUserServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerAppUserServiceDaoImpl.class);

    /**
     * 绑定业主信息封装
     *
     * @param businessOwnerAppUserInfo 绑定业主信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOwnerAppUserInfo(Map businessOwnerAppUserInfo) throws DAOException {
        businessOwnerAppUserInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存绑定业主信息 入参 businessOwnerAppUserInfo : {}", businessOwnerAppUserInfo);
        int saveFlag = sqlSessionTemplate.insert("ownerAppUserServiceDaoImpl.saveBusinessOwnerAppUserInfo", businessOwnerAppUserInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存绑定业主数据失败：" + JSONObject.toJSONString(businessOwnerAppUserInfo));
        }
    }


    /**
     * 查询绑定业主信息
     *
     * @param info bId 信息
     * @return 绑定业主信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOwnerAppUserInfo(Map info) throws DAOException {

        logger.debug("查询绑定业主信息 入参 info : {}", info);

        List<Map> businessOwnerAppUserInfos = sqlSessionTemplate.selectList("ownerAppUserServiceDaoImpl.getBusinessOwnerAppUserInfo", info);

        return businessOwnerAppUserInfos;
    }


    /**
     * 保存绑定业主信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOwnerAppUserInfoInstance(Map info) throws DAOException {
        logger.debug("保存绑定业主信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("ownerAppUserServiceDaoImpl.saveOwnerAppUserInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存绑定业主信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询绑定业主信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerAppUserInfo(Map info) throws DAOException {
        logger.debug("查询绑定业主信息 入参 info : {}", info);

        List<Map> businessOwnerAppUserInfos = sqlSessionTemplate.selectList("ownerAppUserServiceDaoImpl.getOwnerAppUserInfo", info);

        return businessOwnerAppUserInfos;
    }


    /**
     * 修改绑定业主信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOwnerAppUserInfoInstance(Map info) throws DAOException {
        logger.debug("修改绑定业主信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("ownerAppUserServiceDaoImpl.updateOwnerAppUserInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改绑定业主信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询绑定业主数量
     *
     * @param info 绑定业主信息
     * @return 绑定业主数量
     */
    @Override
    public int queryOwnerAppUsersCount(Map info) {
        logger.debug("查询绑定业主数据 入参 info : {}", info);

        List<Map> businessOwnerAppUserInfos = sqlSessionTemplate.selectList("ownerAppUserServiceDaoImpl.queryOwnerAppUsersCount", info);
        if (businessOwnerAppUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerAppUserInfos.get(0).get("count").toString());
    }


}
