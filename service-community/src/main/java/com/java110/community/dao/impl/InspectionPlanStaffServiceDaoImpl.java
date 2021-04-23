package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionPlanStaffServiceDao;
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
 * 执行计划人服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("inspectionPlanStaffServiceDaoImpl")
//@Transactional
public class InspectionPlanStaffServiceDaoImpl extends BaseServiceDao implements IInspectionPlanStaffServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionPlanStaffServiceDaoImpl.class);

    /**
     * 执行计划人信息封装
     *
     * @param businessInspectionPlanStaffInfo 执行计划人信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessInspectionPlanStaffInfo(Map businessInspectionPlanStaffInfo) throws DAOException {
        businessInspectionPlanStaffInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存执行计划人信息 入参 businessInspectionPlanStaffInfo : {}", businessInspectionPlanStaffInfo);
        int saveFlag = sqlSessionTemplate.insert("inspectionPlanStaffServiceDaoImpl.saveBusinessInspectionPlanStaffInfo", businessInspectionPlanStaffInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存执行计划人数据失败：" + JSONObject.toJSONString(businessInspectionPlanStaffInfo));
        }
    }


    /**
     * 查询执行计划人信息
     *
     * @param info bId 信息
     * @return 执行计划人信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessInspectionPlanStaffInfo(Map info) throws DAOException {

        logger.debug("查询执行计划人信息 入参 info : {}", info);

        List<Map> businessInspectionPlanStaffInfos = sqlSessionTemplate.selectList("inspectionPlanStaffServiceDaoImpl.getBusinessInspectionPlanStaffInfo", info);

        return businessInspectionPlanStaffInfos;
    }


    /**
     * 保存执行计划人信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveInspectionPlanStaffInfoInstance(Map info) throws DAOException {
        logger.debug("保存执行计划人信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("inspectionPlanStaffServiceDaoImpl.saveInspectionPlanStaffInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存执行计划人信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询执行计划人信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionPlanStaffInfo(Map info) throws DAOException {
        logger.debug("查询执行计划人信息 入参 info : {}", info);

        List<Map> businessInspectionPlanStaffInfos = sqlSessionTemplate.selectList("inspectionPlanStaffServiceDaoImpl.getInspectionPlanStaffInfo", info);

        return businessInspectionPlanStaffInfos;
    }


    /**
     * 修改执行计划人信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateInspectionPlanStaffInfoInstance(Map info) throws DAOException {
        logger.debug("修改执行计划人信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("inspectionPlanStaffServiceDaoImpl.updateInspectionPlanStaffInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改执行计划人信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询执行计划人数量
     *
     * @param info 执行计划人信息
     * @return 执行计划人数量
     */
    @Override
    public int queryInspectionPlanStaffsCount(Map info) {
        logger.debug("查询执行计划人数据 入参 info : {}", info);

        List<Map> businessInspectionPlanStaffInfos = sqlSessionTemplate.selectList("inspectionPlanStaffServiceDaoImpl.queryInspectionPlanStaffsCount", info);
        if (businessInspectionPlanStaffInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionPlanStaffInfos.get(0).get("count").toString());
    }


}
