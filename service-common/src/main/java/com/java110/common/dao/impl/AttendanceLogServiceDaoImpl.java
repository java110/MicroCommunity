package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IAttendanceLogServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 考勤日志服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("attendanceLogServiceDaoImpl")
//@Transactional
public class AttendanceLogServiceDaoImpl extends BaseServiceDao implements IAttendanceLogServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AttendanceLogServiceDaoImpl.class);





    /**
     * 保存考勤日志信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAttendanceLogInfo(Map info) throws DAOException {
        logger.debug("保存考勤日志信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("attendanceLogServiceDaoImpl.saveAttendanceLogInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存考勤日志信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询考勤日志信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAttendanceLogInfo(Map info) throws DAOException {
        logger.debug("查询考勤日志信息 入参 info : {}",info);

        List<Map> businessAttendanceLogInfos = sqlSessionTemplate.selectList("attendanceLogServiceDaoImpl.getAttendanceLogInfo",info);

        return businessAttendanceLogInfos;
    }


    /**
     * 修改考勤日志信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAttendanceLogInfo(Map info) throws DAOException {
        logger.debug("修改考勤日志信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("attendanceLogServiceDaoImpl.updateAttendanceLogInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改考勤日志信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询考勤日志数量
     * @param info 考勤日志信息
     * @return 考勤日志数量
     */
    @Override
    public int queryAttendanceLogsCount(Map info) {
        logger.debug("查询考勤日志数据 入参 info : {}",info);

        List<Map> businessAttendanceLogInfos = sqlSessionTemplate.selectList("attendanceLogServiceDaoImpl.queryAttendanceLogsCount", info);
        if (businessAttendanceLogInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAttendanceLogInfos.get(0).get("count").toString());
    }


}
