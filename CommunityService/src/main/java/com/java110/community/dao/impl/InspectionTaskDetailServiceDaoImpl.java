package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionTaskDetailServiceDao;
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
 * 巡检任务明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("inspectionTaskDetailServiceDaoImpl")
//@Transactional
public class InspectionTaskDetailServiceDaoImpl extends BaseServiceDao implements IInspectionTaskDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionTaskDetailServiceDaoImpl.class);

    /**
     * 巡检任务明细信息封装
     *
     * @param businessInspectionTaskDetailInfo 巡检任务明细信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessInspectionTaskDetailInfo(Map businessInspectionTaskDetailInfo) throws DAOException {
        businessInspectionTaskDetailInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存巡检任务明细信息 入参 businessInspectionTaskDetailInfo : {}", businessInspectionTaskDetailInfo);
        int saveFlag = sqlSessionTemplate.insert("inspectionTaskDetailServiceDaoImpl.saveBusinessInspectionTaskDetailInfo", businessInspectionTaskDetailInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存巡检任务明细数据失败：" + JSONObject.toJSONString(businessInspectionTaskDetailInfo));
        }
    }


    /**
     * 查询巡检任务明细信息
     *
     * @param info bId 信息
     * @return 巡检任务明细信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessInspectionTaskDetailInfo(Map info) throws DAOException {

        logger.debug("查询巡检任务明细信息 入参 info : {}", info);

        List<Map> businessInspectionTaskDetailInfos = sqlSessionTemplate.selectList("inspectionTaskDetailServiceDaoImpl.getBusinessInspectionTaskDetailInfo", info);

        return businessInspectionTaskDetailInfos;
    }


    /**
     * 保存巡检任务明细信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveInspectionTaskDetailInfoInstance(Map info) throws DAOException {
        logger.debug("保存巡检任务明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("inspectionTaskDetailServiceDaoImpl.saveInspectionTaskDetailInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存巡检任务明细信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询巡检任务明细信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionTaskDetailInfo(Map info) throws DAOException {
        logger.debug("查询巡检任务明细信息 入参 info : {}", info);

        List<Map> businessInspectionTaskDetailInfos = sqlSessionTemplate.selectList("inspectionTaskDetailServiceDaoImpl.getInspectionTaskDetailInfo", info);

        return businessInspectionTaskDetailInfos;
    }


    /**
     * 修改巡检任务明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateInspectionTaskDetailInfoInstance(Map info) throws DAOException {
        logger.debug("修改巡检任务明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("inspectionTaskDetailServiceDaoImpl.updateInspectionTaskDetailInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改巡检任务明细信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询巡检任务明细数量
     *
     * @param info 巡检任务明细信息
     * @return 巡检任务明细数量
     */
    @Override
    public int queryInspectionTaskDetailsCount(Map info) {
        logger.debug("查询巡检任务明细数据 入参 info : {}", info);

        List<Map> businessInspectionTaskDetailInfos = sqlSessionTemplate.selectList("inspectionTaskDetailServiceDaoImpl.queryInspectionTaskDetailsCount", info);
        if (businessInspectionTaskDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionTaskDetailInfos.get(0).get("count").toString());
    }


}
