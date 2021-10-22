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
package com.java110.po.businessTableHis;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2021-09-12 13:58:05 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class BusinessTableHisPo implements Serializable {

    private String actionObj;
private String businessTypeCd;
private String hisId;
private String action;
private String remark;
private String statusCd = "0";
private String actionObjHis;
public String getActionObj() {
        return actionObj;
    }
public void setActionObj(String actionObj) {
        this.actionObj = actionObj;
    }
public String getBusinessTypeCd() {
        return businessTypeCd;
    }
public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }
public String getHisId() {
        return hisId;
    }
public void setHisId(String hisId) {
        this.hisId = hisId;
    }
public String getAction() {
        return action;
    }
public void setAction(String action) {
        this.action = action;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getActionObjHis() {
        return actionObjHis;
    }
public void setActionObjHis(String actionObjHis) {
        this.actionObjHis = actionObjHis;
    }



}
