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
package com.java110.community.cmd.propertyRightRegistration;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.propertyRightRegistration.PropertyRightRegistrationDto;
import com.java110.intf.community.IPropertyRightRegistrationV1InnerServiceSMO;
import com.java110.po.propertyRightRegistration.PropertyRightRegistrationPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.List;

/**
 * 类表述：更新
 * 服务编码：propertyRightRegistration.updatePropertyRightRegistration
 * 请求路劲：/app/propertyRightRegistration.UpdatePropertyRightRegistration
 * add by 吴学文 at 2021-10-09 10:34:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "propertyRightRegistration.updatePropertyRightRegistration")
public class UpdatePropertyRightRegistrationCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdatePropertyRightRegistrationCmd.class);


    @Autowired
    private IPropertyRightRegistrationV1InnerServiceSMO propertyRightRegistrationV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "prrId", "prrId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //获取状态
        String state = reqJson.getString("state");
        if (!StringUtil.isEmpty(state) && state.equals("2")) { //审核拒绝
            if (reqJson.containsKey("remark")) {
                //获取备注
                String remark = reqJson.getString("remark");
                if (StringUtil.isEmpty(remark)) {
                    throw new CmdException("备注不能为空！");
                }
            }
        }
        PropertyRightRegistrationDto propertyRightRegistrationDto = new PropertyRightRegistrationDto();
        propertyRightRegistrationDto.setRoomId(reqJson.getString("roomId"));
        //根据房屋查询产权登记表
        List<PropertyRightRegistrationDto> propertyRightRegistrationDtos = propertyRightRegistrationV1InnerServiceSMOImpl.queryPropertyRightRegistrations(propertyRightRegistrationDto);
        if (propertyRightRegistrationDtos != null && propertyRightRegistrationDtos.size() > 0) {
            for (PropertyRightRegistrationDto propertyRightRegistration : propertyRightRegistrationDtos) {
                //获取产权登记id
                String prrId = propertyRightRegistration.getPrrId();
                if (!prrId.equals(reqJson.getString("prrId"))) {
                    throw new CmdException("该房屋已经申请过房屋产权！");
                }
            }
        }
        PropertyRightRegistrationPo propertyRightRegistrationPo = BeanConvertUtil.covertBean(reqJson, PropertyRightRegistrationPo.class);
        int flag = 0;
        if (reqJson.containsKey("flag") && reqJson.getString("flag").equals("1")) {
            flag = propertyRightRegistrationV1InnerServiceSMOImpl.updatePropertyRightRegistration(propertyRightRegistrationPo);
        } else {
            propertyRightRegistrationPo.setState("0"); //修改后状态变为未审核状态
            flag = propertyRightRegistrationV1InnerServiceSMOImpl.updatePropertyRightRegistration(propertyRightRegistrationPo);
        }
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
