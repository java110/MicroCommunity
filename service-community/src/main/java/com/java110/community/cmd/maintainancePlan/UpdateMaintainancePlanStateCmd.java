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
package com.java110.community.cmd.maintainancePlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.maintainance.MaintainancePlanDto;
import com.java110.intf.community.IMaintainancePlanStaffV1InnerServiceSMO;
import com.java110.intf.community.IMaintainancePlanV1InnerServiceSMO;
import com.java110.po.maintainancePlan.MaintainancePlanPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：maintainancePlan.updateMaintainancePlan
 * 请求路劲：/app/maintainancePlan.UpdateMaintainancePlan
 * add by 吴学文 at 2022-11-07 02:07:55 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "maintainancePlan.updateMaintainancePlanState")
public class UpdateMaintainancePlanStateCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateMaintainancePlanStateCmd.class);


    @Autowired
    private IMaintainancePlanV1InnerServiceSMO maintainancePlanV1InnerServiceSMOImpl;


    @Autowired
    private IMaintainancePlanStaffV1InnerServiceSMO maintainancePlanStaffV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "planId", "planId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写状态");


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MaintainancePlanDto maintainancePlanDto = new MaintainancePlanDto();
        maintainancePlanDto.setCommunityId(reqJson.getString("communityId"));
        maintainancePlanDto.setPlanId(reqJson.getString("planId"));
        List<MaintainancePlanDto> maintainancePlanDtos = maintainancePlanV1InnerServiceSMOImpl.queryMaintainancePlans(maintainancePlanDto);

        Assert.listOnlyOne(maintainancePlanDtos, "根据计划ID查询到多条记录，请检查数据");
        JSONObject businessMaintainancePlan = new JSONObject();
        businessMaintainancePlan.putAll(BeanConvertUtil.beanCovertMap(maintainancePlanDtos.get(0)));
        businessMaintainancePlan.put("state", reqJson.getString("state"));
        MaintainancePlanPo maintainancePlanPo = BeanConvertUtil.covertBean(businessMaintainancePlan, MaintainancePlanPo.class);
        int flag = maintainancePlanV1InnerServiceSMOImpl.updateMaintainancePlan(maintainancePlanPo);
        if (flag < 1) {
            throw new CmdException("修改巡检计划失败");
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
