package com.java110.core.smo.task;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.task.TaskDto;
import com.java110.dto.task.TaskTemplateDto;
import com.java110.dto.task.TaskTemplateSpecDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITaskInnerServiceSMO
 * @Description 定时任务接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/taskApi")
public interface ITaskInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param taskDto 数据对象分享
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/queryTasks", method = RequestMethod.POST)
    List<TaskDto> queryTasks(@RequestBody TaskDto taskDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param taskDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryTasksCount", method = RequestMethod.POST)
    int queryTasksCount(@RequestBody TaskDto taskDto);


    /**
     * <p>查询任务模板楼信息</p>
     *
     *
     * @param taskTemplateDto  模板对象
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/queryTaskTemplate", method = RequestMethod.POST)
    List<TaskTemplateDto> queryTaskTemplate(@RequestBody TaskTemplateDto taskTemplateDto);

    /**
     * 查询<p>任务模板</p>总记录数
     *
     * @param taskTemplateDto 模板对象
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryTaskTemplateCount", method = RequestMethod.POST)
    int queryTaskTemplateCount(@RequestBody TaskTemplateDto taskTemplateDto);



     int queryTaskTemplateSpecCount(@RequestBody TaskTemplateSpecDto taskTemplateSpecDto);



     List<TaskTemplateSpecDto> queryTaskTemplateSpec(@RequestBody TaskTemplateSpecDto taskTemplateSpecDto);
}
