package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IAttendanceClassesServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 考勤班次服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("attendanceClassesServiceDaoImpl")
//@Transactional
public class AttendanceClassesServiceDaoImpl extends BaseServiceDao implements IAttendanceClassesServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AttendanceClassesServiceDaoImpl.class);

    /**
     * 考勤班次信息封装
     * @param businessAttendanceClassesInfo 考勤班次信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAttendanceClassesInfo(Map businessAttendanceClassesInfo) throws DAOException {
        businessAttendanceClassesInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存考勤班次信息 入参 businessAttendanceClassesInfo : {}",businessAttendanceClassesInfo);
        int saveFlag = sqlSessionTemplate.insert("attendanceClassesServiceDaoImpl.saveBusinessAttendanceClassesInfo",businessAttendanceClassesInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存考勤班次数据失败："+ JSONObject.toJSONString(businessAttendanceClassesInfo));
        }
    }


    /**
     * 查询考勤班次信息
     * @param info bId 信息
     * @return 考勤班次信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAttendanceClassesInfo(Map info) throws DAOException {

        logger.debug("查询考勤班次信息 入参 info : {}",info);

        List<Map> businessAttendanceClassesInfos = sqlSessionTemplate.selectList("attendanceClassesServiceDaoImpl.getBusinessAttendanceClassesInfo",info);

        return businessAttendanceClassesInfos;
    }



    /**
     * 保存考勤班次信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAttendanceClassesInfoInstance(Map info) throws DAOException {
        logger.debug("保存考勤班次信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("attendanceClassesServiceDaoImpl.saveAttendanceClassesInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存考勤班次信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询考勤班次信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAttendanceClassesInfo(Map info) throws DAOException {
        logger.debug("查询考勤班次信息 入参 info : {}",info);

        List<Map> businessAttendanceClassesInfos = sqlSessionTemplate.selectList("attendanceClassesServiceDaoImpl.getAttendanceClassesInfo",info);

        return businessAttendanceClassesInfos;
    }


    /**
     * 修改考勤班次信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAttendanceClassesInfoInstance(Map info) throws DAOException {
        logger.debug("修改考勤班次信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("attendanceClassesServiceDaoImpl.updateAttendanceClassesInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改考勤班次信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询考勤班次数量
     * @param info 考勤班次信息
     * @return 考勤班次数量
     */
    @Override
    public int queryAttendanceClassessCount(Map info) {
        logger.debug("查询考勤班次数据 入参 info : {}",info);

        List<Map> businessAttendanceClassesInfos = sqlSessionTemplate.selectList("attendanceClassesServiceDaoImpl.queryAttendanceClassessCount", info);
        if (businessAttendanceClassesInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAttendanceClassesInfos.get(0).get("count").toString());
    }


}
