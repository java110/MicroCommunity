package com.java110.intf.job;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.task.TaskAttrDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITaskAttrInnerServiceSMO
 * @Description 定时任务属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/taskAttrApi")
public interface ITaskAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param taskAttrDto 数据对象分享
     * @return TaskAttrDto 对象数据
     */
    @RequestMapping(value = "/queryTaskAttrs", method = RequestMethod.POST)
    List<TaskAttrDto> queryTaskAttrs(@RequestBody TaskAttrDto taskAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param taskAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryTaskAttrsCount", method = RequestMethod.POST)
    int queryTaskAttrsCount(@RequestBody TaskAttrDto taskAttrDto);
}
