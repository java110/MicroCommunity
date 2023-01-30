package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IAttendanceClassesTaskDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 考勤任务明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("attendanceClassesTaskDetailServiceDaoImpl")
//@Transactional
public class AttendanceClassesTaskDetailServiceDaoImpl extends BaseServiceDao implements IAttendanceClassesTaskDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AttendanceClassesTaskDetailServiceDaoImpl.class);





    /**
     * 保存考勤任务明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAttendanceClassesTaskDetailInfo(Map info) throws DAOException {
        logger.debug("保存考勤任务明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("attendanceClassesTaskDetailServiceDaoImpl.saveAttendanceClassesTaskDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存考勤任务明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询考勤任务明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAttendanceClassesTaskDetailInfo(Map info) throws DAOException {
        logger.debug("查询考勤任务明细信息 入参 info : {}",info);

        List<Map> businessAttendanceClassesTaskDetailInfos = sqlSessionTemplate.selectList("attendanceClassesTaskDetailServiceDaoImpl.getAttendanceClassesTaskDetailInfo",info);

        return businessAttendanceClassesTaskDetailInfos;
    }


    /**
     * 修改考勤任务明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAttendanceClassesTaskDetailInfo(Map info) throws DAOException {
        logger.debug("修改考勤任务明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("attendanceClassesTaskDetailServiceDaoImpl.updateAttendanceClassesTaskDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改考勤任务明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询考勤任务明细数量
     * @param info 考勤任务明细信息
     * @return 考勤任务明细数量
     */
    @Override
    public int queryAttendanceClassesTaskDetailsCount(Map info) {
        logger.debug("查询考勤任务明细数据 入参 info : {}",info);

        List<Map> businessAttendanceClassesTaskDetailInfos = sqlSessionTemplate.selectList("attendanceClassesTaskDetailServiceDaoImpl.queryAttendanceClassesTaskDetailsCount", info);
        if (businessAttendanceClassesTaskDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAttendanceClassesTaskDetailInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryWaitSendMsgDetail(Map info) {
        List<Map> result = sqlSessionTemplate.selectList("attendanceClassesTaskDetailServiceDaoImpl.queryWaitSendMsgDetail", info);
        return result;
    }


}
