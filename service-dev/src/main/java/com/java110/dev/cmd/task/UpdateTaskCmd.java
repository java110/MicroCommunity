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
package com.java110.dev.cmd.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.dev.ITaskAttrV1InnerServiceSMO;
import com.java110.intf.dev.ITaskV1InnerServiceSMO;
import com.java110.po.task.TaskPo;
import com.java110.po.taskAttr.TaskAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：更新
 * 服务编码：task.updateTask
 * 请求路劲：/app/task.UpdateTask
 * add by 吴学文 at 2022-08-05 10:23:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "task.updateTask")
public class UpdateTaskCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(UpdateTaskCmd.class);


    @Autowired
    private ITaskV1InnerServiceSMO taskV1InnerServiceSMOImpl;

    @Autowired
    private ITaskAttrV1InnerServiceSMO taskAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskId", "taskId不能为空");
        Assert.hasKeyAndValue(reqJson, "taskName", "请求报文中未包含taskName");
        Assert.hasKeyAndValue(reqJson, "templateId", "请求报文中未包含templateId");
        Assert.hasKeyAndValue(reqJson, "taskCron", "请求报文中未包含taskCron");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        updateTask(reqJson);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    public void updateTask(JSONObject paramInJson) {

//        TaskDto taskDto = new TaskDto();
//        taskDto.setTaskId(paramInJson.getString("taskId"));
//        //taskDto.setJobId(paramInJson.getString("jobId"));
//        List<TaskDto> taskDtos = taskInnerServiceSMOImpl.queryTasks(taskDto);
//
//        Assert.listOnlyOne(taskDtos, "未找到需要修改的活动 或多条数据");
//        paramInJson.putAll(BeanConvertUtil.beanCovertMap(taskDtos.get(0)));
        TaskPo taskPo = BeanConvertUtil.covertBean(paramInJson, TaskPo.class);
        int flag = taskV1InnerServiceSMOImpl.updateTask(taskPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        if (!paramInJson.containsKey("templateSpecs")) {
            return;
        }

        JSONArray templateSpecs = paramInJson.getJSONArray("templateSpecs");
        JSONObject specObj = null;
        TaskAttrPo taskAttrPo = null;
        for (int specIndex = 0; specIndex < templateSpecs.size(); specIndex++) {
            specObj = templateSpecs.getJSONObject(specIndex);
            if (specObj.containsKey("attrId") && !"-1".equals(specObj.getString("attrId"))) {
                taskAttrPo = new TaskAttrPo();
                taskAttrPo.setAttrId(specObj.getString("attrId"));
                taskAttrPo.setTaskId(taskPo.getTaskId());
                taskAttrPo.setValue(specObj.getString("value"));
                taskAttrPo.setSpecCd(specObj.getString("specCd"));
                flag = taskAttrV1InnerServiceSMOImpl.updateTaskAttr(taskAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
                continue;
            }
            taskAttrPo = new TaskAttrPo();
            taskAttrPo.setAttrId("-" + (specIndex + 1));
            taskAttrPo.setTaskId(taskPo.getTaskId());
            taskAttrPo.setValue(specObj.getString("value"));
            taskAttrPo.setSpecCd(specObj.getString("specCd"));
            flag = taskAttrV1InnerServiceSMOImpl.saveTaskAttr(taskAttrPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }
    }
}
