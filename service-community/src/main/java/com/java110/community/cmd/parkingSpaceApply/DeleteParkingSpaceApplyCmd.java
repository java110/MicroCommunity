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
package com.java110.community.cmd.parkingSpaceApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceApplyV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.po.parkingSpaceApply.ParkingSpaceApplyPo;
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
 * 类表述：删除
 * 服务编码：parkingSpaceApply.deleteParkingSpaceApply
 * 请求路劲：/app/parkingSpaceApply.DeleteParkingSpaceApply
 * add by 吴学文 at 2021-10-18 13:00:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "parkingSpaceApply.deleteParkingSpaceApply")
public class DeleteParkingSpaceApplyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(DeleteParkingSpaceApplyCmd.class);

    @Autowired
    private IParkingSpaceApplyV1InnerServiceSMO parkingSpaceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ParkingSpaceApplyPo parkingSpaceApplyPo = BeanConvertUtil.covertBean(reqJson, ParkingSpaceApplyPo.class);
        int flag = parkingSpaceApplyV1InnerServiceSMOImpl.deleteParkingSpaceApply(parkingSpaceApplyPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }
        if (!StringUtil.isEmpty(reqJson.getString("state")) && (reqJson.getString("state").equals("2002")
                || reqJson.getString("state").equals("3003"))) { //审核通过待缴费状态或完成状态
            if (!StringUtil.isEmpty(reqJson.getString("psId"))) {
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setPsId(reqJson.getString("psId"));
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                Assert.listOnlyOne(parkingSpaceDtos, "查询车位失败");
                ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
                parkingSpacePo.setPsId(parkingSpaceDtos.get(0).getPsId());
                parkingSpacePo.setState("F"); //出售 S，出租 H ，空闲 F
                int i = parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
                if (i < 1) {
                    throw new CmdException("更新数据失败");
                }
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
