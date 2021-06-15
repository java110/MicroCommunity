package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionPlanServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 巡检计划服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("inspectionPlanServiceDaoImpl")
//@Transactional
public class InspectionPlanServiceDaoImpl extends BaseServiceDao implements IInspectionPlanServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionPlanServiceDaoImpl.class);

    /**
     * 巡检计划信息封装
     * @param businessInspectionPlanInfo 巡检计划信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessInspectionPlanInfo(Map businessInspectionPlanInfo) throws DAOException {
        businessInspectionPlanInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存巡检计划信息 入参 businessInspectionPlanInfo : {}",businessInspectionPlanInfo);
        int saveFlag = sqlSessionTemplate.insert("inspectionPlanServiceDaoImpl.saveBusinessInspectionPlanInfo",businessInspectionPlanInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存巡检计划数据失败："+ JSONObject.toJSONString(businessInspectionPlanInfo));
        }
    }


    /**
     * 查询巡检计划信息
     * @param info bId 信息
     * @return 巡检计划信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessInspectionPlanInfo(Map info) throws DAOException {

        logger.debug("查询巡检计划信息 入参 info : {}",info);

        List<Map> businessInspectionPlanInfos = sqlSessionTemplate.selectList("inspectionPlanServiceDaoImpl.getBusinessInspectionPlanInfo",info);

        return businessInspectionPlanInfos;
    }



    /**
     * 保存巡检计划信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveInspectionPlanInfoInstance(Map info) throws DAOException {
        logger.debug("保存巡检计划信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("inspectionPlanServiceDaoImpl.saveInspectionPlanInfoInstance",info);
        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存巡检计划信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询巡检计划信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionPlanInfo(Map info) throws DAOException {
        logger.debug("查询巡检计划信息 入参 info : {}",info);

        List<Map> businessInspectionPlanInfos = sqlSessionTemplate.selectList("inspectionPlanServiceDaoImpl.getInspectionPlanInfo",info);

        return businessInspectionPlanInfos;
    }


    /**
     * 修改巡检计划信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateInspectionPlanInfoInstance(Map info) throws DAOException {
        logger.debug("修改巡检计划信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("inspectionPlanServiceDaoImpl.updateInspectionPlanInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改巡检计划信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询巡检计划数量
     * @param info 巡检计划信息
     * @return 巡检计划数量
     */
    @Override
    public int queryInspectionPlansCount(Map info) {
        logger.debug("查询巡检计划数据 入参 info : {}",info);

        List<Map> businessInspectionPlanInfos = sqlSessionTemplate.selectList("inspectionPlanServiceDaoImpl.queryInspectionPlansCount", info);
        if (businessInspectionPlanInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionPlanInfos.get(0).get("count").toString());
    }


}
