package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.community.dao.IFloorServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 小区楼服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("floorServiceDaoImpl")
//@Transactional
public class FloorServiceDaoImpl extends BaseServiceDao implements IFloorServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(FloorServiceDaoImpl.class);

    /**
     * 小区楼信息封装
     * @param businessFloorInfo 小区楼信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessFloorInfo(Map businessFloorInfo) throws DAOException {
        businessFloorInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区楼信息 入参 businessFloorInfo : {}",businessFloorInfo);
        int saveFlag = sqlSessionTemplate.insert("floorServiceDaoImpl.saveBusinessFloorInfo",businessFloorInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存小区楼数据失败："+ JSONObject.toJSONString(businessFloorInfo));
        }
    }


    /**
     * 查询小区楼信息
     * @param info bId 信息
     * @return 小区楼信息
     * @throws DAOException
     */
    @Override
    public Map getBusinessFloorInfo(Map info) throws DAOException {

        logger.debug("查询小区楼信息 入参 info : {}",info);

        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.getBusinessFloorInfo",info);
        if(businessFloorInfos == null){
            return null;
        }
        if(businessFloorInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessFloorInfos，"+ JSONObject.toJSONString(info));
        }

        return businessFloorInfos.get(0);
    }



    /**
     * 保存小区楼信息 到 instance
     * @param info   bId 信息
     * @throws DAOException
     */
    @Override
    public void saveFloorInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区楼信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("floorServiceDaoImpl.saveFloorInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存小区楼信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区楼信息（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getFloorInfo(Map info) throws DAOException {
        logger.debug("查询小区楼信息 入参 info : {}",info);

        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.getFloorInfo",info);
        if(businessFloorInfos == null || businessFloorInfos.size() == 0){
            return null;
        }
        if(businessFloorInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getFloorInfo，"+ JSONObject.toJSONString(info));
        }

        return businessFloorInfos.get(0);
    }


    /**
     * 修改小区楼信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateFloorInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区楼信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("floorServiceDaoImpl.updateFloorInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改小区楼信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


}
