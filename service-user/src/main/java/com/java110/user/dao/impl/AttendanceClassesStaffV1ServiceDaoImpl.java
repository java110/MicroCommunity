/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.user.dao.impl;

import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IAttendanceClassesStaffV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2023-02-21 23:48:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("attendanceClassesStaffV1ServiceDaoImpl")
public class AttendanceClassesStaffV1ServiceDaoImpl extends BaseServiceDao implements IAttendanceClassesStaffV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AttendanceClassesStaffV1ServiceDaoImpl.class);





    /**
     * 保存考勤员工信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveAttendanceClassesStaffInfo(Map info) throws DAOException {
        logger.debug("保存 saveAttendanceClassesStaffInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("attendanceClassesStaffV1ServiceDaoImpl.saveAttendanceClassesStaffInfo",info);

        return saveFlag;
    }


    /**
     * 查询考勤员工信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAttendanceClassesStaffInfo(Map info) throws DAOException {
        logger.debug("查询 getAttendanceClassesStaffInfo 入参 info : {}",info);

        List<Map> businessAttendanceClassesStaffInfos = sqlSessionTemplate.selectList("attendanceClassesStaffV1ServiceDaoImpl.getAttendanceClassesStaffInfo",info);

        return businessAttendanceClassesStaffInfos;
    }


    /**
     * 修改考勤员工信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateAttendanceClassesStaffInfo(Map info) throws DAOException {
        logger.debug("修改 updateAttendanceClassesStaffInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("attendanceClassesStaffV1ServiceDaoImpl.updateAttendanceClassesStaffInfo",info);

        return saveFlag;
    }

     /**
     * 查询考勤员工数量
     * @param info 考勤员工信息
     * @return 考勤员工数量
     */
    @Override
    public int queryAttendanceClassesStaffsCount(Map info) {
        logger.debug("查询 queryAttendanceClassesStaffsCount 入参 info : {}",info);

        List<Map> businessAttendanceClassesStaffInfos = sqlSessionTemplate.selectList("attendanceClassesStaffV1ServiceDaoImpl.queryAttendanceClassesStaffsCount", info);
        if (businessAttendanceClassesStaffInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAttendanceClassesStaffInfos.get(0).get("count").toString());
    }


}
