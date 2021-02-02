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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.attendanceClassesTask.IDeleteAttendanceClassesTaskBMO;
import com.java110.common.bmo.attendanceClassesTask.IGetAttendanceClassesTaskBMO;
import com.java110.common.bmo.attendanceClassesTask.ISaveAttendanceClassesTaskBMO;
import com.java110.common.bmo.attendanceClassesTask.IUpdateAttendanceClassesTaskBMO;
import com.java110.common.bmo.attendanceClassesTaskDetail.IDeleteAttendanceClassesTaskDetailBMO;
import com.java110.common.bmo.attendanceClassesTaskDetail.IGetAttendanceClassesTaskDetailBMO;
import com.java110.common.bmo.attendanceClassesTaskDetail.ISaveAttendanceClassesTaskDetailBMO;
import com.java110.common.bmo.attendanceClassesTaskDetail.IUpdateAttendanceClassesTaskDetailBMO;
import com.java110.dto.attendanceClassesTask.AttendanceClassesTaskDto;
import com.java110.dto.attendanceClassesTaskDetail.AttendanceClassesTaskDetailDto;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    @Autowired
    private ISaveAttendanceClassesTaskDetailBMO saveAttendanceClassesTaskDetailBMOImpl;
    @Autowired
    private IUpdateAttendanceClassesTaskDetailBMO updateAttendanceClassesTaskDetailBMOImpl;
    @Autowired
    private IDeleteAttendanceClassesTaskDetailBMO deleteAttendanceClassesTaskDetailBMOImpl;

    @Autowired
    private IGetAttendanceClassesTaskDetailBMO getAttendanceClassesTaskDetailBMOImpl;

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

        JSONArray attendanceClassesTaskDetails = reqJson.getJSONArray("attendanceClassesTaskDetails");

        List<AttendanceClassesTaskDetailPo> attendanceClassesTaskDetailPos
                = JSONArray.parseArray(attendanceClassesTaskDetails.toJSONString(), AttendanceClassesTaskDetailPo.class);


        AttendanceClassesTaskPo attendanceClassesTaskPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskPo.class);
        return saveAttendanceClassesTaskBMOImpl.save(attendanceClassesTaskPo, attendanceClassesTaskDetailPos);
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
                                                             @RequestParam(value = "row") int row,
                                                             @RequestParam(value = "classId", required = false) String classId,
                                                             @RequestParam(value = "staffId", required = false) String staffId,
                                                             @RequestParam(name = "date", required = false) String date) throws Exception {
        AttendanceClassesTaskDto attendanceClassesTaskDto = new AttendanceClassesTaskDto();
        attendanceClassesTaskDto.setPage(page);
        attendanceClassesTaskDto.setRow(row);
        attendanceClassesTaskDto.setStoreId(storeId);
        attendanceClassesTaskDto.setClassId(classId);
        if (!StringUtil.isEmpty(date)) {
            Date reqDate = DateUtil.getDateFromString(date, DateUtil.DATE_FORMATE_STRING_B);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reqDate);
            attendanceClassesTaskDto.setTaskYear(calendar.get(Calendar.YEAR) + "");
            attendanceClassesTaskDto.setTaskMonth((calendar.get(Calendar.MONTH) + 1) + "");
            attendanceClassesTaskDto.setTaskDay(calendar.get(Calendar.DAY_OF_MONTH) + "");
        }
        return getAttendanceClassesTaskBMOImpl.get(attendanceClassesTaskDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attendanceClass/saveAttendanceClassesTaskDetail
     * @path /app/attendanceClass/saveAttendanceClassesTaskDetail
     */
    @RequestMapping(value = "/saveAttendanceClassesTaskDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveAttendanceClassesTaskDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");


        AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskDetailPo.class);
        return saveAttendanceClassesTaskDetailBMOImpl.save(attendanceClassesTaskDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attendanceClass/updateAttendanceClassesTaskDetail
     * @path /app/attendanceClass/updateAttendanceClassesTaskDetail
     */
    @RequestMapping(value = "/updateAttendanceClassesTaskDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateAttendanceClassesTaskDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");

        boolean finishAllTaskDetail = false;

        if (reqJson.containsKey("finishAllTaskDetail") && reqJson.getBoolean("finishAllTaskDetail")) {
            finishAllTaskDetail = true;
        }


        AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskDetailPo.class);
        return updateAttendanceClassesTaskDetailBMOImpl.update(attendanceClassesTaskDetailPo, finishAllTaskDetail);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attendanceClass/deleteAttendanceClassesTaskDetail
     * @path /app/attendanceClass/deleteAttendanceClassesTaskDetail
     */
    @RequestMapping(value = "/deleteAttendanceClassesTaskDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAttendanceClassesTaskDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskDetailPo.class);
        return deleteAttendanceClassesTaskDetailBMOImpl.delete(attendanceClassesTaskDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /attendanceClass/queryAttendanceClassesTaskDetail
     * @path /app/attendanceClass/queryAttendanceClassesTaskDetail
     */
    @RequestMapping(value = "/queryAttendanceClassesTaskDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryAttendanceClassesTaskDetail(@RequestHeader(value = "store-id") String storeId,
                                                                   @RequestParam(value = "page") int page,
                                                                   @RequestParam(value = "row") int row) {
        AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto = new AttendanceClassesTaskDetailDto();
        attendanceClassesTaskDetailDto.setPage(page);
        attendanceClassesTaskDetailDto.setRow(row);
        attendanceClassesTaskDetailDto.setStoreId(storeId);
        return getAttendanceClassesTaskDetailBMOImpl.get(attendanceClassesTaskDetailDto);
    }

    /**
     * 添加设备接口类
     * @serviceCode /attendanceClass/getMonthAttendance
     * @path /app/attendanceClass/getMonthAttendance
     * @return 成功或者失败
     * @throws Exception
     */
    @RequestMapping(path = "/getMonthAttendance", method = RequestMethod.GET)
    public ResponseEntity<String> getMonthAttendance(
            @RequestHeader(value = "store-id") String storeId,
            @RequestParam int page,
            @RequestParam int row,
            @RequestParam(name = "classesId", required = false) String classesId,
            @RequestParam(name = "staffName", required = false) String staffName,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "departmentId", required = false) String departmentId
    ) throws Exception {

        AttendanceClassesTaskDto attendanceClassesTaskDto = new AttendanceClassesTaskDto();
        attendanceClassesTaskDto.setPage(page);
        attendanceClassesTaskDto.setRow(row);
        attendanceClassesTaskDto.setClassId(classesId);
        attendanceClassesTaskDto.setStaffName(staffName);
        attendanceClassesTaskDto.setStoreId(storeId);

        if (!StringUtil.isEmpty(date)) {
            Date reqDate = DateUtil.getDateFromString(date, DateUtil.DATE_FORMATE_STRING_B);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reqDate);
            attendanceClassesTaskDto.setTaskYear(calendar.get(Calendar.YEAR) + "");
            attendanceClassesTaskDto.setTaskMonth((calendar.get(Calendar.MONTH) + 1) + "");
        }
        return  getAttendanceClassesTaskDetailBMOImpl.getMonthAttendance(attendanceClassesTaskDto);
    }

}
