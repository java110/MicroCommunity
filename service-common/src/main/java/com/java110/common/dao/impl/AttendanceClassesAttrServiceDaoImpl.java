package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IAttendanceClassesAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 考勤班组属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("attendanceClassesAttrServiceDaoImpl")
//@Transactional
public class AttendanceClassesAttrServiceDaoImpl extends BaseServiceDao implements IAttendanceClassesAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AttendanceClassesAttrServiceDaoImpl.class);

    /**
     * 考勤班组属性信息封装
     * @param businessAttendanceClassesAttrInfo 考勤班组属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAttendanceClassesAttrInfo(Map businessAttendanceClassesAttrInfo) throws DAOException {
        businessAttendanceClassesAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存考勤班组属性信息 入参 businessAttendanceClassesAttrInfo : {}",businessAttendanceClassesAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("attendanceClassesAttrServiceDaoImpl.saveBusinessAttendanceClassesAttrInfo",businessAttendanceClassesAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存考勤班组属性数据失败："+ JSONObject.toJSONString(businessAttendanceClassesAttrInfo));
        }
    }


    /**
     * 查询考勤班组属性信息
     * @param info bId 信息
     * @return 考勤班组属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAttendanceClassesAttrInfo(Map info) throws DAOException {

        logger.debug("查询考勤班组属性信息 入参 info : {}",info);

        List<Map> businessAttendanceClassesAttrInfos = sqlSessionTemplate.selectList("attendanceClassesAttrServiceDaoImpl.getBusinessAttendanceClassesAttrInfo",info);

        return businessAttendanceClassesAttrInfos;
    }



    /**
     * 保存考勤班组属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAttendanceClassesAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存考勤班组属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("attendanceClassesAttrServiceDaoImpl.saveAttendanceClassesAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存考勤班组属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询考勤班组属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAttendanceClassesAttrInfo(Map info) throws DAOException {
        logger.debug("查询考勤班组属性信息 入参 info : {}",info);

        List<Map> businessAttendanceClassesAttrInfos = sqlSessionTemplate.selectList("attendanceClassesAttrServiceDaoImpl.getAttendanceClassesAttrInfo",info);

        return businessAttendanceClassesAttrInfos;
    }


    /**
     * 修改考勤班组属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAttendanceClassesAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改考勤班组属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("attendanceClassesAttrServiceDaoImpl.updateAttendanceClassesAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改考勤班组属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询考勤班组属性数量
     * @param info 考勤班组属性信息
     * @return 考勤班组属性数量
     */
    @Override
    public int queryAttendanceClassesAttrsCount(Map info) {
        logger.debug("查询考勤班组属性数据 入参 info : {}",info);

        List<Map> businessAttendanceClassesAttrInfos = sqlSessionTemplate.selectList("attendanceClassesAttrServiceDaoImpl.queryAttendanceClassesAttrsCount", info);
        if (businessAttendanceClassesAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAttendanceClassesAttrInfos.get(0).get("count").toString());
    }


}
