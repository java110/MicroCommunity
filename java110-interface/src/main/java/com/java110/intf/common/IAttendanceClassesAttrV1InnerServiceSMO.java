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
package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendanceClasses.AttendanceClassesAttrDto;
import com.java110.po.attendanceClassesAttr.AttendanceClassesAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-07-16 18:00:56 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceClassesAttrV1Api")
public interface IAttendanceClassesAttrV1InnerServiceSMO {


    @RequestMapping(value = "/saveAttendanceClassesAttr", method = RequestMethod.POST)
    public int saveAttendanceClassesAttr(@RequestBody  AttendanceClassesAttrPo attendanceClassesAttrPo);

    @RequestMapping(value = "/updateAttendanceClassesAttr", method = RequestMethod.POST)
    public int updateAttendanceClassesAttr(@RequestBody  AttendanceClassesAttrPo attendanceClassesAttrPo);

    @RequestMapping(value = "/deleteAttendanceClassesAttr", method = RequestMethod.POST)
    public int deleteAttendanceClassesAttr(@RequestBody  AttendanceClassesAttrPo attendanceClassesAttrPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param attendanceClassesAttrDto 数据对象分享
     * @return AttendanceClassesAttrDto 对象数据
     */
    @RequestMapping(value = "/queryAttendanceClassesAttrs", method = RequestMethod.POST)
    List<AttendanceClassesAttrDto> queryAttendanceClassesAttrs(@RequestBody AttendanceClassesAttrDto attendanceClassesAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attendanceClassesAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttendanceClassesAttrsCount", method = RequestMethod.POST)
    int queryAttendanceClassesAttrsCount(@RequestBody AttendanceClassesAttrDto attendanceClassesAttrDto);
}
