package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IAttendanceClassesTaskServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 考勤任务服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("attendanceClassesTaskServiceDaoImpl")
//@Transactional
public class AttendanceClassesTaskServiceDaoImpl extends BaseServiceDao implements IAttendanceClassesTaskServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AttendanceClassesTaskServiceDaoImpl.class);





    /**
     * 保存考勤任务信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAttendanceClassesTaskInfo(Map info) throws DAOException {
        logger.debug("保存考勤任务信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("attendanceClassesTaskServiceDaoImpl.saveAttendanceClassesTaskInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存考勤任务信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询考勤任务信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAttendanceClassesTaskInfo(Map info) throws DAOException {
        logger.debug("查询考勤任务信息 入参 info : {}",info);

        List<Map> businessAttendanceClassesTaskInfos = sqlSessionTemplate.selectList("attendanceClassesTaskServiceDaoImpl.getAttendanceClassesTaskInfo",info);

        return businessAttendanceClassesTaskInfos;
    }


    /**
     * 修改考勤任务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAttendanceClassesTaskInfo(Map info) throws DAOException {
        logger.debug("修改考勤任务信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("attendanceClassesTaskServiceDaoImpl.updateAttendanceClassesTaskInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改考勤任务信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询考勤任务数量
     * @param info 考勤任务信息
     * @return 考勤任务数量
     */
    @Override
    public int queryAttendanceClassesTasksCount(Map info) {
        logger.debug("查询考勤任务数据 入参 info : {}",info);

        List<Map> businessAttendanceClassesTaskInfos = sqlSessionTemplate.selectList("attendanceClassesTaskServiceDaoImpl.queryAttendanceClassesTasksCount", info);
        if (businessAttendanceClassesTaskInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAttendanceClassesTaskInfos.get(0).get("count").toString());
    }


}
