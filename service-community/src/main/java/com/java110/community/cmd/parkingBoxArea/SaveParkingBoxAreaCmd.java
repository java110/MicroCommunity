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
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
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
 * 类表述：保存
 * 服务编码：parkingBoxArea.saveParkingBoxArea
 * 请求路劲：/app/parkingBoxArea.SaveParkingBoxArea
 * add by 吴学文 at 2021-10-18 00:15:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "parkingBoxArea.saveParkingBoxArea")
public class SaveParkingBoxAreaCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveParkingBoxAreaCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingBoxV1InnerServiceSMO parkingBoxV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "boxId", "请求报文中未包含boxId");
        Assert.hasKeyAndValue(reqJson, "paId", "请求报文中未包含paId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "defaultArea", "请求报文中未包含defaultArea");
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(reqJson.getString("paId"));
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        Assert.listOnlyOne(parkingAreaDtos, "停车场不存在！");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
        parkingBoxAreaDto.setPaId(reqJson.getString("paId"));
        parkingBoxAreaDto.setBoxId(reqJson.getString("boxId"));
        List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);
        Assert.listIsNull(parkingBoxAreaDtos, "停车场重复，请重新添加！");
        String defaultArea = reqJson.getString("defaultArea");
        if (ParkingBoxAreaDto.DEFAULT_AREA_TRUE.equals(defaultArea)) {
            ParkingBoxAreaDto parkingBoxArea = new ParkingBoxAreaDto();
            parkingBoxArea.setBoxId(reqJson.getString("boxId"));
            parkingBoxArea.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_FALSE);
            List<ParkingBoxAreaDto> parkingBoxAreas = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxArea);
            if (parkingBoxAreas != null && parkingBoxAreas.size() > 0) {
                for (ParkingBoxAreaDto parkingBox : parkingBoxAreas) {
                    ParkingBoxAreaPo tmpParkingBoxAreaPo = new ParkingBoxAreaPo();
                    tmpParkingBoxAreaPo.setBaId(parkingBox.getBaId());
                    tmpParkingBoxAreaPo.setBoxId(reqJson.getString("boxId"));
                    tmpParkingBoxAreaPo.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_FALSE);
                    parkingBoxAreaV1InnerServiceSMOImpl.updateParkingBoxArea(tmpParkingBoxAreaPo);
                }
            }
        }
        ParkingBoxAreaPo parkingBoxAreaPo = BeanConvertUtil.covertBean(reqJson, ParkingBoxAreaPo.class);
        parkingBoxAreaPo.setBaId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = parkingBoxAreaV1InnerServiceSMOImpl.saveParkingBoxArea(parkingBoxAreaPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        ParkingBoxPo parkingBoxPo = new ParkingBoxPo();
        parkingBoxPo.setBoxId(reqJson.getString("boxId"));
        parkingBoxV1InnerServiceSMOImpl.updateParkingBox(parkingBoxPo);
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
