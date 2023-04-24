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
package com.java110.community.cmd.parkingBoxArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.java110.intf.community.IParkingBoxV1InnerServiceSMO;
import com.java110.po.parkingBox.ParkingBoxPo;
import com.java110.po.parkingBoxArea.ParkingBoxAreaPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：parkingBoxArea.deleteParkingBoxArea
 * 请求路劲：/app/parkingBoxArea.DeleteParkingBoxArea
 * add by 吴学文 at 2021-10-18 00:15:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "parkingBoxArea.deleteParkingBoxArea")
public class DeleteParkingBoxAreaCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(DeleteParkingBoxAreaCmd.class);

    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingBoxV1InnerServiceSMO parkingBoxV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "baId", "baId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
        parkingBoxAreaDto.setBaId(reqJson.getString("baId"));
        parkingBoxAreaDto.setCommunityId(reqJson.getString("communityId"));
        List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);
        Assert.listOnlyOne(parkingBoxAreaDtos, "数据不存在");
        if (ParkingBoxAreaDto.DEFAULT_AREA_TRUE.equals(parkingBoxAreaDtos.get(0).getDefaultArea())) {
            throw new CmdException("默认停车场不能删除");
        }
        ParkingBoxAreaPo parkingBoxAreaPo = BeanConvertUtil.covertBean(reqJson, ParkingBoxAreaPo.class);
        int flag = parkingBoxAreaV1InnerServiceSMOImpl.deleteParkingBoxArea(parkingBoxAreaPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }
        ParkingBoxPo parkingBoxPo = new ParkingBoxPo();
        parkingBoxPo.setBoxId(reqJson.getString("boxId"));
        parkingBoxV1InnerServiceSMOImpl.updateParkingBox(parkingBoxPo);
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
