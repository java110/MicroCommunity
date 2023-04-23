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
package com.java110.community.cmd.communityLocation;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.UnitDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingBoxDto;
import com.java110.intf.community.*;
import com.java110.po.community.CommunityLocationPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：更新
 * 服务编码：communityLocation.updateCommunityLocation
 * 请求路劲：/app/communityLocation.UpdateCommunityLocation
 * add by 吴学文 at 2022-07-17 00:10:38 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "communityLocation.updateCommunityLocation")
public class UpdateCommunityLocationCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateCommunityLocationCmd.class);

    @Autowired
    private ICommunityLocationV1InnerServiceSMO communityLocationV1InnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Autowired
    private IParkingBoxV1InnerServiceSMO parkingBoxV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "locationId", "locationId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CommunityLocationPo communityLocationPo = BeanConvertUtil.covertBean(reqJson, CommunityLocationPo.class);
        if (!StringUtil.isEmpty(communityLocationPo.getLocationType()) && communityLocationPo.getLocationType().equals("2000")) { //1000 小区；2000 单元；3000 房屋；4000 岗亭；5000 部门；6000 楼栋；7000 停车场
            UnitDto unitDto = new UnitDto();
            unitDto.setUnitId(reqJson.getString("unitId"));
            List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
            Assert.listOnlyOne(unitDtos, "查询单元错误！");
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorId(reqJson.getString("floorId"));
            List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
            Assert.listOnlyOne(floorDtos, "查询房屋错误！");
            communityLocationPo.setLocationObjId(unitDtos.get(0).getUnitId());
            communityLocationPo.setLocationName(floorDtos.get(0).getFloorNum() + "号楼" + unitDtos.get(0).getUnitNum() + "单元");
            communityLocationPo.setLocationObjName(communityLocationPo.getLocationName());
        }
        if (!StringUtil.isEmpty(communityLocationPo.getLocationType()) && communityLocationPo.getLocationType().equals("6000")) { //6000 楼栋
            communityLocationPo.setLocationObjId(reqJson.getString("floorId"));
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorId(reqJson.getString("floorId"));
            List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
            Assert.listOnlyOne(floorDtos, "查询楼栋错误！");
            communityLocationPo.setLocationName(floorDtos.get(0).getFloorNum() + "号楼");
            communityLocationPo.setLocationObjName(communityLocationPo.getLocationName());
        }
        if (!StringUtil.isEmpty(communityLocationPo.getLocationType()) && communityLocationPo.getLocationType().equals("7000")) { //7000 停车场
            ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
            parkingAreaDto.setPaId(reqJson.getString("paId"));
            List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
            Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");
            communityLocationPo.setLocationObjId(reqJson.getString("paId"));
            communityLocationPo.setLocationName(parkingAreaDtos.get(0).getNum() + "停车场");
            communityLocationPo.setLocationObjName(communityLocationPo.getLocationName());
        }
        if (!StringUtil.isEmpty(communityLocationPo.getLocationType()) && communityLocationPo.getLocationType().equals("4000")) { //4000 岗亭
            ParkingBoxDto parkingBoxDto = new ParkingBoxDto();
            parkingBoxDto.setBoxId(reqJson.getString("boxId"));
            List<ParkingBoxDto> parkingBoxDtos = parkingBoxV1InnerServiceSMOImpl.queryParkingBoxs(parkingBoxDto);
            communityLocationPo.setLocationObjId(reqJson.getString("boxId"));
            communityLocationPo.setLocationName(parkingBoxDtos.get(0).getBoxName());
            communityLocationPo.setLocationObjName(communityLocationPo.getLocationName());
        }
        int flag = communityLocationV1InnerServiceSMOImpl.updateCommunityLocation(communityLocationPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
