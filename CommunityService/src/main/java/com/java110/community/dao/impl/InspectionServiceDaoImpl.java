package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionServiceDao;
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
 * 巡检点服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("inspectionServiceDaoImpl")
//@Transactional
public class InspectionServiceDaoImpl extends BaseServiceDao implements IInspectionServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionServiceDaoImpl.class);

    /**
     * 巡检点信息封装
     * @param businessInspectionInfo 巡检点信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessInspectionInfo(Map businessInspectionInfo) throws DAOException {
        businessInspectionInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存巡检点信息 入参 businessInspectionInfo : {}",businessInspectionInfo);
        int saveFlag = sqlSessionTemplate.insert("inspectionServiceDaoImpl.saveBusinessInspectionInfo",businessInspectionInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存巡检点数据失败："+ JSONObject.toJSONString(businessInspectionInfo));
        }
    }


    /**
     * 查询巡检点信息
     * @param info bId 信息
     * @return 巡检点信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessInspectionInfo(Map info) throws DAOException {

        logger.debug("查询巡检点信息 入参 info : {}",info);

        List<Map> businessInspectionInfos = sqlSessionTemplate.selectList("inspectionServiceDaoImpl.getBusinessInspectionInfo",info);

        return businessInspectionInfos;
    }



    /**
     * 保存巡检点信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveInspectionInfoInstance(Map info) throws DAOException {
        logger.debug("保存巡检点信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("inspectionServiceDaoImpl.saveInspectionInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存巡检点信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询巡检点信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionInfo(Map info) throws DAOException {
        logger.debug("查询巡检点信息 入参 info : {}",info);

        List<Map> businessInspectionInfos = sqlSessionTemplate.selectList("inspectionServiceDaoImpl.getInspectionInfo",info);

        return businessInspectionInfos;
    }


    /**
     * 修改巡检点信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateInspectionInfoInstance(Map info) throws DAOException {
        logger.debug("修改巡检点信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("inspectionServiceDaoImpl.updateInspectionInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改巡检点信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询巡检点数量
     * @param info 巡检点信息
     * @return 巡检点数量
     */
    @Override
    public int queryInspectionsCount(Map info) {
        logger.debug("查询巡检点数据 入参 info : {}",info);

        List<Map> businessInspectionInfos = sqlSessionTemplate.selectList("inspectionServiceDaoImpl.queryInspectionsCount", info);
        if (businessInspectionInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionInfos.get(0).get("count").toString());
    }


}
