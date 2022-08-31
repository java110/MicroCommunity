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
package com.java110.community.cmd.activities;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IActivitiesV1InnerServiceSMO;
import com.java110.po.activities.ActivitiesPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：更新
 * 服务编码：activities.updateActivities
 * 请求路劲：/app/activities.UpdateActivities
 * add by 吴学文 at 2022-06-19 10:49:17 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "activities.updateActivities")
public class UpdateActivitiesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateActivitiesCmd.class);

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IActivitiesV1InnerServiceSMO activitiesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "activitiesId", "活动ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "title", "必填，请填写业活动标题");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择活动类型");
        Assert.hasKeyAndValue(reqJson, "headerImg", "必填，请选择头部照片");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写活动内容");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("headerImg") && !StringUtils.isEmpty(reqJson.getString("headerImg"))) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setRelTypeCd("70000");
            fileRelDto.setObjId(reqJson.getString("activitiesId"));
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos == null || fileRelDtos.size() == 0) {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
                fileRelPo.setFileRealName(reqJson.getString("headerImg"));
                fileRelPo.setFileSaveName(reqJson.getString("headerImg"));
                fileRelPo.setObjId(reqJson.getString("activitiesId"));
                fileRelPo.setSaveWay("table");
                fileRelPo.setRelTypeCd("70000");
                int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存广告失败");
                }
            } else {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId(fileRelDtos.get(0).getFileRelId());
                fileRelPo.setFileRealName(reqJson.getString("headerImg"));
                fileRelPo.setFileSaveName(reqJson.getString("headerImg"));
                fileRelPo.setObjId(reqJson.getString("activitiesId"));
                int flag = fileRelInnerServiceSMOImpl.updateFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存广告失败");
                }
            }
        }
        ActivitiesPo activitiesPo = BeanConvertUtil.covertBean(reqJson, ActivitiesPo.class);
        int flag = activitiesV1InnerServiceSMOImpl.updateActivities(activitiesPo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
