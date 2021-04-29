package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairSettingServiceDao;
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
 * 报修设置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("repairSettingServiceDaoImpl")
//@Transactional
public class RepairSettingServiceDaoImpl extends BaseServiceDao implements IRepairSettingServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RepairSettingServiceDaoImpl.class);

    /**
     * 报修设置信息封装
     *
     * @param businessRepairSettingInfo 报修设置信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessRepairSettingInfo(Map businessRepairSettingInfo) throws DAOException {
        businessRepairSettingInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存报修设置信息 入参 businessRepairSettingInfo : {}", businessRepairSettingInfo);
        int saveFlag = sqlSessionTemplate.insert("repairSettingServiceDaoImpl.saveBusinessRepairSettingInfo", businessRepairSettingInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存报修设置数据失败：" + JSONObject.toJSONString(businessRepairSettingInfo));
        }
    }


    /**
     * 查询报修设置信息
     *
     * @param info bId 信息
     * @return 报修设置信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessRepairSettingInfo(Map info) throws DAOException {

        logger.debug("查询报修设置信息 入参 info : {}", info);

        List<Map> businessRepairSettingInfos = sqlSessionTemplate.selectList("repairSettingServiceDaoImpl.getBusinessRepairSettingInfo", info);

        return businessRepairSettingInfos;
    }


    /**
     * 保存报修设置信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRepairSettingInfoInstance(Map info) throws DAOException {
        logger.debug("保存报修设置信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("repairSettingServiceDaoImpl.saveRepairSettingInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存报修设置信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询报修设置信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRepairSettingInfo(Map info) throws DAOException {
        logger.debug("查询报修设置信息 入参 info : {}", info);

        List<Map> businessRepairSettingInfos = sqlSessionTemplate.selectList("repairSettingServiceDaoImpl.getRepairSettingInfo", info);

        return businessRepairSettingInfos;
    }


    /**
     * 修改报修设置信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRepairSettingInfoInstance(Map info) throws DAOException {
        logger.debug("修改报修设置信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("repairSettingServiceDaoImpl.updateRepairSettingInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改报修设置信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询报修设置数量
     *
     * @param info 报修设置信息
     * @return 报修设置数量
     */
    @Override
    public int queryRepairSettingsCount(Map info) {
        logger.debug("查询报修设置数据 入参 info : {}", info);

        List<Map> businessRepairSettingInfos = sqlSessionTemplate.selectList("repairSettingServiceDaoImpl.queryRepairSettingsCount", info);
        if (businessRepairSettingInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairSettingInfos.get(0).get("count").toString());
    }


}
