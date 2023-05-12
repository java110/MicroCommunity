package com.java110.dto.task;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 定时任务数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class TaskDto extends PageDto implements Serializable {

    private String taskCron;
    private String taskName;
    private String state;
    private String stateName;
    private String templateId;
    private String templateName;
    private String taskId;

    private List<TaskAttrDto> taskAttr;

    private TaskTemplateDto taskTemplateDto;


    private Date createTime;

    private String statusCd = "0";

    private String classBean;


    public String getTaskCron() {
        return taskCron;
    }

    public void setTaskCron(String taskCron) {
        this.taskCron = taskCron;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<TaskAttrDto> getTaskAttr() {
        return taskAttr;
    }

    public void setTaskAttr(List<TaskAttrDto> taskAttr) {
        this.taskAttr = taskAttr;
    }

    public TaskTemplateDto getTaskTemplateDto() {
        return taskTemplateDto;
    }

    public void setTaskTemplateDto(TaskTemplateDto taskTemplateDto) {
        this.taskTemplateDto = taskTemplateDto;
    }

    public String getClassBean() {
        return classBean;
    }

    public void setClassBean(String classBean) {
        this.classBean = classBean;
    }
}
