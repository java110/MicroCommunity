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
package com.java110.po.maintainanceTask;

import java.io.Serializable;
import java.util.Date;

/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-11-08 15:51:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class MaintainanceTaskPo implements Serializable {

    private String planUserId;
    private String actInsTime;
    private String planInsTime;
    private String originalPlanUserName;
    private String transferDesc;
    private String statusCd = "0";
    private String actUserName;
    private String authDesc;
    private String originalPlanUserId;
    private String taskType;
    private String planId;
    private String planEndTime;
    private String planUserName;
    private String communityId;
    private String actUserId;
    private String taskId;
    private String mpsId;
    private String state;

    public String getPlanUserId() {
        return planUserId;
    }

    public void setPlanUserId(String planUserId) {
        this.planUserId = planUserId;
    }

    public String getActInsTime() {
        return actInsTime;
    }

    public void setActInsTime(String actInsTime) {
        this.actInsTime = actInsTime;
    }

    public String getPlanInsTime() {
        return planInsTime;
    }

    public void setPlanInsTime(String planInsTime) {
        this.planInsTime = planInsTime;
    }

    public String getOriginalPlanUserName() {
        return originalPlanUserName;
    }

    public void setOriginalPlanUserName(String originalPlanUserName) {
        this.originalPlanUserName = originalPlanUserName;
    }

    public String getTransferDesc() {
        return transferDesc;
    }

    public void setTransferDesc(String transferDesc) {
        this.transferDesc = transferDesc;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getAuthDesc() {
        return authDesc;
    }

    public void setAuthDesc(String authDesc) {
        this.authDesc = authDesc;
    }

    public String getOriginalPlanUserId() {
        return originalPlanUserId;
    }

    public void setOriginalPlanUserId(String originalPlanUserId) {
        this.originalPlanUserId = originalPlanUserId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getPlanUserName() {
        return planUserName;
    }

    public void setPlanUserName(String planUserName) {
        this.planUserName = planUserName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getActUserId() {
        return actUserId;
    }

    public void setActUserId(String actUserId) {
        this.actUserId = actUserId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMpsId() {
        return mpsId;
    }

    public void setMpsId(String mpsId) {
        this.mpsId = mpsId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
