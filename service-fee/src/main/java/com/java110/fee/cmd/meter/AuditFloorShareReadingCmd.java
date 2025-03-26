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
import com.java110.dto.data.DatabusDataDto;
import com.java110.dto.floorShareReading.FloorShareReadingDto;
import com.java110.intf.fee.IFloorShareMeterV1InnerServiceSMO;
import com.java110.intf.fee.IFloorShareReadingV1InnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.po.floorShareMeter.FloorShareMeterPo;
import com.java110.po.floorShareReading.FloorShareReadingPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：floorShareReading.updateFloorShareReading
 * 请求路劲：/app/floorShareReading.UpdateFloorShareReading
 * add by 吴学文 at 2025-03-26 09:29:54 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meter.auditFloorShareReading")
public class AuditFloorShareReadingCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(AuditFloorShareReadingCmd.class);


    @Autowired
    private IFloorShareReadingV1InnerServiceSMO floorShareReadingV1InnerServiceSMOImpl;

    @Autowired
    private IFloorShareMeterV1InnerServiceSMO floorShareMeterV1InnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "readingId", "readingId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "审核状态不能为空");
        super.validateProperty(cmdDataFlowContext);
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        FloorShareReadingDto floorShareReadingDto = new FloorShareReadingDto();
        floorShareReadingDto.setReadingId(reqJson.getString("readingId"));
        floorShareReadingDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorShareReadingDto> floorShareReadingDtos
                = floorShareReadingV1InnerServiceSMOImpl.queryFloorShareReadings(floorShareReadingDto);
        if (ListUtil.isNull(floorShareReadingDtos)) {
            throw new CmdException("记录不存在");
        }
        String state = reqJson.getString("state");

        FloorShareReadingPo floorShareReadingPo = new FloorShareReadingPo();
        floorShareReadingPo.setReadingId(reqJson.getString("readingId"));
        floorShareReadingPo.setState(state);
        floorShareReadingPo.setStatsMsg("审核意见：" + reqJson.getString("auditRemark"));
        int flag = floorShareReadingV1InnerServiceSMOImpl.updateFloorShareReading(floorShareReadingPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());

        //todo
        if (FloorShareReadingDto.STATE_F.equals(state)) {
            FloorShareMeterPo floorShareMeterPo = new FloorShareMeterPo();
            floorShareMeterPo.setFsmId(floorShareReadingDtos.get(0).getFsmId());
            floorShareMeterPo.setCurReadingTime(floorShareReadingDtos.get(0).getPreReadingTime());
            floorShareMeterPo.setCurDegree(floorShareReadingDtos.get(0).getPreDegrees());
            floorShareMeterPo.setCommunityId(floorShareReadingDtos.get(0).getCommunityId());
            floorShareMeterV1InnerServiceSMOImpl.updateFloorShareMeter(floorShareMeterPo);
            return ;
        }

        reqJson.put("staffId",userId);
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_SHARE_READING, reqJson));


    }
}
