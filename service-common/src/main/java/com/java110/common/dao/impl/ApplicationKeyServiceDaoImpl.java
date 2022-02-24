package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IApplicationKeyServiceDao;
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
 * 钥匙申请服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("applicationKeyServiceDaoImpl")
//@Transactional
public class ApplicationKeyServiceDaoImpl extends BaseServiceDao implements IApplicationKeyServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ApplicationKeyServiceDaoImpl.class);

    /**
     * 钥匙申请信息封装
     *
     * @param businessApplicationKeyInfo 钥匙申请信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessApplicationKeyInfo(Map businessApplicationKeyInfo) throws DAOException {
        businessApplicationKeyInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存钥匙申请信息 入参 businessApplicationKeyInfo : {}", businessApplicationKeyInfo);
        int saveFlag = sqlSessionTemplate.insert("applicationKeyServiceDaoImpl.saveBusinessApplicationKeyInfo", businessApplicationKeyInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存钥匙申请数据失败：" + JSONObject.toJSONString(businessApplicationKeyInfo));
        }
    }


    /**
     * 查询钥匙申请信息
     *
     * @param info bId 信息
     * @return 钥匙申请信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessApplicationKeyInfo(Map info) throws DAOException {

        logger.debug("查询钥匙申请信息 入参 info : {}", info);

        List<Map> businessApplicationKeyInfos = sqlSessionTemplate.selectList("applicationKeyServiceDaoImpl.getBusinessApplicationKeyInfo", info);

        return businessApplicationKeyInfos;
    }


    /**
     * 保存钥匙申请信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveApplicationKeyInfoInstance(Map info) throws DAOException {
        logger.debug("保存钥匙申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("applicationKeyServiceDaoImpl.saveApplicationKeyInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存钥匙申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询钥匙申请信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getApplicationKeyInfo(Map info) throws DAOException {
        logger.debug("查询钥匙申请信息 入参 info : {}", info);

        List<Map> businessApplicationKeyInfos = sqlSessionTemplate.selectList("applicationKeyServiceDaoImpl.getApplicationKeyInfo", info);

        return businessApplicationKeyInfos;
    }


    /**
     * 修改钥匙申请信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateApplicationKeyInfoInstance(Map info) throws DAOException {
        logger.debug("修改钥匙申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("applicationKeyServiceDaoImpl.updateApplicationKeyInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改钥匙申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询钥匙申请数量
     *
     * @param info 钥匙申请信息
     * @return 钥匙申请数量
     */
    @Override
    public int queryApplicationKeysCount(Map info) {
        logger.debug("查询钥匙申请数据 入参 info : {}", info);

        List<Map> businessApplicationKeyInfos = sqlSessionTemplate.selectList("applicationKeyServiceDaoImpl.queryApplicationKeysCount", info);
        if (businessApplicationKeyInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessApplicationKeyInfos.get(0).get("count").toString());
    }


}
