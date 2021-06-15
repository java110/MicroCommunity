package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionRoutePointRelServiceDao;
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
 * 巡检路线巡检点关系服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("inspectionRoutePointRelServiceDaoImpl")
//@Transactional
public class InspectionRoutePointRelServiceDaoImpl extends BaseServiceDao implements IInspectionRoutePointRelServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionRoutePointRelServiceDaoImpl.class);

    /**
     * 巡检路线巡检点关系信息封装
     *
     * @param businessInspectionRoutePointRelInfo 巡检路线巡检点关系信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessInspectionRoutePointRelInfo(Map businessInspectionRoutePointRelInfo) throws DAOException {
        businessInspectionRoutePointRelInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存巡检路线巡检点关系信息 入参 businessInspectionRoutePointRelInfo : {}", businessInspectionRoutePointRelInfo);
        int saveFlag = sqlSessionTemplate.insert("inspectionRoutePointRelServiceDaoImpl.saveBusinessInspectionRoutePointRelInfo", businessInspectionRoutePointRelInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存巡检路线巡检点关系数据失败：" + JSONObject.toJSONString(businessInspectionRoutePointRelInfo));
        }
    }


    /**
     * 查询巡检路线巡检点关系信息
     *
     * @param info bId 信息
     * @return 巡检路线巡检点关系信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessInspectionRoutePointRelInfo(Map info) throws DAOException {

        logger.debug("查询巡检路线巡检点关系信息 入参 info : {}", info);

        List<Map> businessInspectionRoutePointRelInfos = sqlSessionTemplate.selectList("inspectionRoutePointRelServiceDaoImpl.getBusinessInspectionRoutePointRelInfo", info);

        return businessInspectionRoutePointRelInfos;
    }


    /**
     * 保存巡检路线巡检点关系信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveInspectionRoutePointRelInfoInstance(Map info) throws DAOException {
        logger.debug("保存巡检路线巡检点关系信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("inspectionRoutePointRelServiceDaoImpl.saveInspectionRoutePointRelInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存巡检路线巡检点关系信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询巡检路线巡检点关系信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionRoutePointRelInfo(Map info) throws DAOException {
        logger.debug("查询巡检路线巡检点关系信息 入参 info : {}", info);

        List<Map> businessInspectionRoutePointRelInfos = sqlSessionTemplate.selectList("inspectionRoutePointRelServiceDaoImpl.getInspectionRoutePointRelInfo", info);

        return businessInspectionRoutePointRelInfos;
    }


    /**
     * 修改巡检路线巡检点关系信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateInspectionRoutePointRelInfoInstance(Map info) throws DAOException {
        logger.debug("修改巡检路线巡检点关系信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("inspectionRoutePointRelServiceDaoImpl.updateInspectionRoutePointRelInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改巡检路线巡检点关系信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询巡检路线巡检点关系数量
     *
     * @param info 巡检路线巡检点关系信息
     * @return 巡检路线巡检点关系数量
     */
    @Override
    public int queryInspectionRoutePointRelsCount(Map info) {
        logger.debug("查询巡检路线巡检点关系数据 入参 info : {}", info);

        List<Map> businessInspectionRoutePointRelInfos = sqlSessionTemplate.selectList("inspectionRoutePointRelServiceDaoImpl.queryInspectionRoutePointRelsCount", info);
        if (businessInspectionRoutePointRelInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionRoutePointRelInfos.get(0).get("count").toString());
    }


}
