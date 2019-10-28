package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAuditMessageServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 审核原因服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("auditMessageServiceDaoImpl")
//@Transactional
public class AuditMessageServiceDaoImpl extends BaseServiceDao implements IAuditMessageServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AuditMessageServiceDaoImpl.class);

    /**
     * 审核原因信息封装
     *
     * @param businessAuditMessageInfo 审核原因信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAuditMessageInfo(Map businessAuditMessageInfo) throws DAOException {
        businessAuditMessageInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存审核原因信息 入参 businessAuditMessageInfo : {}", businessAuditMessageInfo);
        int saveFlag = sqlSessionTemplate.insert("auditMessageServiceDaoImpl.saveBusinessAuditMessageInfo", businessAuditMessageInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存审核原因数据失败：" + JSONObject.toJSONString(businessAuditMessageInfo));
        }
    }


    /**
     * 查询审核原因信息
     *
     * @param info bId 信息
     * @return 审核原因信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAuditMessageInfo(Map info) throws DAOException {

        logger.debug("查询审核原因信息 入参 info : {}", info);

        List<Map> businessAuditMessageInfos = sqlSessionTemplate.selectList("auditMessageServiceDaoImpl.getBusinessAuditMessageInfo", info);

        return businessAuditMessageInfos;
    }


    /**
     * 保存审核原因信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAuditMessageInfoInstance(Map info) throws DAOException {
        logger.debug("保存审核原因信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("auditMessageServiceDaoImpl.saveAuditMessageInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存审核原因信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询审核原因信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAuditMessageInfo(Map info) throws DAOException {
        logger.debug("查询审核原因信息 入参 info : {}", info);

        List<Map> businessAuditMessageInfos = sqlSessionTemplate.selectList("auditMessageServiceDaoImpl.getAuditMessageInfo", info);

        return businessAuditMessageInfos;
    }


    /**
     * 修改审核原因信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAuditMessageInfoInstance(Map info) throws DAOException {
        logger.debug("修改审核原因信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("auditMessageServiceDaoImpl.updateAuditMessageInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改审核原因信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询审核原因数量
     *
     * @param info 审核原因信息
     * @return 审核原因数量
     */
    @Override
    public int queryAuditMessagesCount(Map info) {
        logger.debug("查询审核原因数据 入参 info : {}", info);

        List<Map> businessAuditMessageInfos = sqlSessionTemplate.selectList("auditMessageServiceDaoImpl.queryAuditMessagesCount", info);
        if (businessAuditMessageInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAuditMessageInfos.get(0).get("count").toString());
    }


}
