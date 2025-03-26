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
package com.java110.fee.cmd.meter;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.floorShareMeter.FloorShareMeterDto;
import com.java110.dto.floorShareReading.FloorShareReadingDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IFloorShareMeterV1InnerServiceSMO;
import com.java110.intf.fee.IFloorShareReadingV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.floorShareMeter.FloorShareMeterPo;
import com.java110.po.floorShareReading.FloorShareReadingPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：floorShareReading.saveFloorShareReading
 * 请求路劲：/app/floorShareReading.SaveFloorShareReading
 * add by 吴学文 at 2025-03-26 09:29:54 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meter.saveFloorShareReading")
public class SaveFloorShareReadingCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveFloorShareReadingCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IFloorShareReadingV1InnerServiceSMO floorShareReadingV1InnerServiceSMOImpl;

    @Autowired
    private IFloorShareMeterV1InnerServiceSMO floorShareMeterV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "fsmId", "请求报文中未包含fsmId");
        Assert.hasKeyAndValue(reqJson, "preDegrees", "请求报文中未包含preDegrees");
        Assert.hasKeyAndValue(reqJson, "curDegrees", "请求报文中未包含curDegrees");
        Assert.hasKeyAndValue(reqJson, "preReadingTime", "请求报文中未包含preReadingTime");
        Assert.hasKeyAndValue(reqJson, "curReadingTime", "请求报文中未包含curReadingTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        super.validateProperty(cmdDataFlowContext);



    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");

        FloorShareMeterDto floorShareMeterDto = new FloorShareMeterDto();
        floorShareMeterDto.setFsmId(reqJson.getString("fsmId"));
        floorShareMeterDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorShareMeterDto> floorShareMeterDtos = floorShareMeterV1InnerServiceSMOImpl.queryFloorShareMeters(floorShareMeterDto);
        if (ListUtil.isNull(floorShareMeterDtos)) {
            throw new CmdException("未包含公摊表");
        }

        FloorShareReadingPo floorShareReadingPo = BeanConvertUtil.covertBean(reqJson, FloorShareReadingPo.class);
        floorShareReadingPo.setReadingId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        floorShareReadingPo.setCreateStaffName(userDtos.get(0).getName());
        floorShareReadingPo.setState(FloorShareReadingDto.STATE_W);
        floorShareReadingPo.setTitle(userDtos.get(0).getName() + "提交" + floorShareMeterDtos.get(0).getFloorNum() + "栋"+floorShareMeterDtos.get(0).getMeterTypeName()+"抄表");
        int flag = floorShareReadingV1InnerServiceSMOImpl.saveFloorShareReading(floorShareReadingPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        // todo 修改表度数和时间
        FloorShareMeterPo floorShareMeterPo = new FloorShareMeterPo();
        floorShareMeterPo.setFsmId(floorShareMeterDtos.get(0).getFsmId());
        floorShareMeterPo.setCurDegree(reqJson.getString("curDegrees"));
        floorShareMeterPo.setCurReadingTime(reqJson.getString("curReadingTime"));
        floorShareMeterV1InnerServiceSMOImpl.updateFloorShareMeter(floorShareMeterPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
