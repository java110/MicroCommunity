package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAuditUserServiceDao;
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
 * 审核人员服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("auditUserServiceDaoImpl")
//@Transactional
public class AuditUserServiceDaoImpl extends BaseServiceDao implements IAuditUserServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AuditUserServiceDaoImpl.class);

    /**
     * 审核人员信息封装
     *
     * @param businessAuditUserInfo 审核人员信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAuditUserInfo(Map businessAuditUserInfo) throws DAOException {
        businessAuditUserInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存审核人员信息 入参 businessAuditUserInfo : {}", businessAuditUserInfo);
        int saveFlag = sqlSessionTemplate.insert("auditUserServiceDaoImpl.saveBusinessAuditUserInfo", businessAuditUserInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存审核人员数据失败：" + JSONObject.toJSONString(businessAuditUserInfo));
        }
    }


    /**
     * 查询审核人员信息
     *
     * @param info bId 信息
     * @return 审核人员信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAuditUserInfo(Map info) throws DAOException {

        logger.debug("查询审核人员信息 入参 info : {}", info);

        List<Map> businessAuditUserInfos = sqlSessionTemplate.selectList("auditUserServiceDaoImpl.getBusinessAuditUserInfo", info);

        return businessAuditUserInfos;
    }


    /**
     * 保存审核人员信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAuditUserInfoInstance(Map info) throws DAOException {
        logger.debug("保存审核人员信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("auditUserServiceDaoImpl.saveAuditUserInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存审核人员信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询审核人员信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAuditUserInfo(Map info) throws DAOException {
        logger.debug("查询审核人员信息 入参 info : {}", info);

        List<Map> businessAuditUserInfos = sqlSessionTemplate.selectList("auditUserServiceDaoImpl.getAuditUserInfo", info);

        return businessAuditUserInfos;
    }


    /**
     * 修改审核人员信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAuditUserInfoInstance(Map info) throws DAOException {
        logger.debug("修改审核人员信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("auditUserServiceDaoImpl.updateAuditUserInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改审核人员信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询审核人员数量
     *
     * @param info 审核人员信息
     * @return 审核人员数量
     */
    @Override
    public int queryAuditUsersCount(Map info) {
        logger.debug("查询审核人员数据 入参 info : {}", info);

        List<Map> businessAuditUserInfos = sqlSessionTemplate.selectList("auditUserServiceDaoImpl.queryAuditUsersCount", info);
        if (businessAuditUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAuditUserInfos.get(0).get("count").toString());
    }

    @Override
    public void freshActHiTaskInstAssignee(Map info) {
        logger.debug("freshActHiTaskInstAssignee 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("auditUserServiceDaoImpl.freshActHiTaskInstAssignee", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "freshActHiTaskInstAssignee数据失败：" + JSONObject.toJSONString(info));
        }
    }


}
