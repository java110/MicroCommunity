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
package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.attendanceClassesTask.IDeleteAttendanceClassesTaskBMO;
import com.java110.common.bmo.attendanceClassesTask.IGetAttendanceClassesTaskBMO;
import com.java110.common.bmo.attendanceClassesTask.ISaveAttendanceClassesTaskBMO;
import com.java110.common.bmo.attendanceClassesTask.IUpdateAttendanceClassesTaskBMO;
import com.java110.dto.attendanceClassesTask.AttendanceClassesTaskDto;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AttendanceClassApi
 * @Description 考勤相关处理API类
 * @Author wuxw
 * @Date 2021/1/31 0:42
 * @Version 1.0
 * add by wuxw 2021/1/31
 **/
@RestController
@RequestMapping(value = "/attendanceClass")
public class AttendanceClassApi {

    @Autowired
    private ISaveAttendanceClassesTaskBMO saveAttendanceClassesTaskBMOImpl;
    @Autowired
    private IUpdateAttendanceClassesTaskBMO updateAttendanceClassesTaskBMOImpl;
    @Autowired
    private IDeleteAttendanceClassesTaskBMO deleteAttendanceClassesTaskBMOImpl;

    @Autowired
    private IGetAttendanceClassesTaskBMO getAttendanceClassesTaskBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attendanceClass/saveAttendanceClassesTask
     * @path /app/attendanceClass/saveAttendanceClassesTask
     */
    @RequestMapping(value = "/saveAttendanceClassesTask", method = RequestMethod.POST)
    public ResponseEntity<String> saveAttendanceClassesTask(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "classId", "请求报文中未包含classId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");


        AttendanceClassesTaskPo attendanceClassesTaskPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskPo.class);
        return saveAttendanceClassesTaskBMOImpl.save(attendanceClassesTaskPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attendanceClass/updateAttendanceClassesTask
     * @path /app/attendanceClass/updateAttendanceClassesTask
     */
    @RequestMapping(value = "/updateAttendanceClassesTask", method = RequestMethod.POST)
    public ResponseEntity<String> updateAttendanceClassesTask(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "classId", "请求报文中未包含classId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "taskId", "taskId不能为空");


        AttendanceClassesTaskPo attendanceClassesTaskPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskPo.class);
        return updateAttendanceClassesTaskBMOImpl.update(attendanceClassesTaskPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attendanceClass/deleteAttendanceClassesTask
     * @path /app/attendanceClass/deleteAttendanceClassesTask
     */
    @RequestMapping(value = "/deleteAttendanceClassesTask", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAttendanceClassesTask(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "taskId", "taskId不能为空");


        AttendanceClassesTaskPo attendanceClassesTaskPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskPo.class);
        return deleteAttendanceClassesTaskBMOImpl.delete(attendanceClassesTaskPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /attendanceClass/queryAttendanceClassesTask
     * @path /app/attendanceClass/queryAttendanceClassesTask
     */
    @RequestMapping(value = "/queryAttendanceClassesTask", method = RequestMethod.GET)
    public ResponseEntity<String> queryAttendanceClassesTask(@RequestHeader(value = "store-id") String storeId,
                                                             @RequestParam(value = "page") int page,
                                                             @RequestParam(value = "row") int row) {
        AttendanceClassesTaskDto attendanceClassesTaskDto = new AttendanceClassesTaskDto();
        attendanceClassesTaskDto.setPage(page);
        attendanceClassesTaskDto.setRow(row);
        attendanceClassesTaskDto.setStoreId(storeId);
        return getAttendanceClassesTaskBMOImpl.get(attendanceClassesTaskDto);
    }

}
