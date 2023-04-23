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
 * 类表述：更新
 * 服务编码：parkingBoxArea.updateParkingBoxArea
 * 请求路劲：/app/parkingBoxArea.UpdateParkingBoxArea
 * add by 吴学文 at 2021-10-18 00:15:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "parkingBoxArea.updateParkingBoxArea")
public class UpdateParkingBoxAreaCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateParkingBoxAreaCmd.class);

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
        String defaultArea = reqJson.getString("defaultArea");
        if (ParkingBoxAreaDto.DEFAULT_AREA_TRUE.equals(defaultArea)) {
            ParkingBoxAreaPo tmpParkingBoxAreaPo = new ParkingBoxAreaPo();
            //tmpParkingBoxAreaPo.setBaId(reqJson.getString("baId"));
            tmpParkingBoxAreaPo.setBoxId(reqJson.getString("boxId"));
            tmpParkingBoxAreaPo.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_FALSE);
            parkingBoxAreaV1InnerServiceSMOImpl.updateParkingBoxArea(tmpParkingBoxAreaPo);
        }
        ParkingBoxAreaPo parkingBoxAreaPo = BeanConvertUtil.covertBean(reqJson, ParkingBoxAreaPo.class);
        int flag = parkingBoxAreaV1InnerServiceSMOImpl.updateParkingBoxArea(parkingBoxAreaPo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
        parkingBoxAreaDto.setBoxId(reqJson.getString("boxId"));
        List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);
        if (parkingBoxAreaDtos != null && parkingBoxAreaDtos.size() > 0) {
            for (ParkingBoxAreaDto parkingBoxArea : parkingBoxAreaDtos) {
                if (!parkingBoxArea.getBaId().equals(reqJson.getString("baId"))) {
                    ParkingBoxAreaPo parkingBoxAre = new ParkingBoxAreaPo();
                    parkingBoxAre.setBaId(parkingBoxArea.getBaId());
                    parkingBoxAre.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_FALSE);
                    parkingBoxAreaV1InnerServiceSMOImpl.updateParkingBoxArea(parkingBoxAre);
                }
            }
        }
        ParkingBoxPo parkingBoxPo = new ParkingBoxPo();
        parkingBoxPo.setBoxId(reqJson.getString("boxId"));
        parkingBoxV1InnerServiceSMOImpl.updateParkingBox(parkingBoxPo);
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
