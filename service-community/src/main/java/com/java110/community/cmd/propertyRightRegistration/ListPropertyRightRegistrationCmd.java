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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IPropertyRightRegistrationV1InnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.propertyRightRegistration.PropertyRightRegistrationDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：propertyRightRegistration.listPropertyRightRegistration
 * 请求路劲：/app/propertyRightRegistration.ListPropertyRightRegistration
 * add by 吴学文 at 2021-10-09 10:34:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "propertyRightRegistration.listPropertyRightRegistration")
public class ListPropertyRightRegistrationCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListPropertyRightRegistrationCmd.class);

    @Autowired
    private IPropertyRightRegistrationV1InnerServiceSMO propertyRightRegistrationV1InnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("allNum") && !StringUtil.isEmpty(reqJson.getString("allNum"))) {
            //获取房屋编号(楼栋-单元-房屋)
            String allNum = reqJson.getString("allNum");
            String[] split = allNum.split("-");
            if (split.length != 3) {
                ResultVo resultVo = new ResultVo(ResultVo.CODE_ERROR, "房屋编号格式错误！");
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
                cmdDataFlowContext.setResponseEntity(responseEntity);
            }
            String floorNum = split[0]; //楼栋号
            String unitNum = split[1]; //单元号
            String roomNum = split[2]; //房间号
            //根据楼栋号和小区id查询楼栋
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorNum(floorNum);
            floorDto.setCommunityId(reqJson.getString("communityId"));
            List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
            Assert.listOnlyOne(floorDtos, "查询楼栋错误！");
            //根据楼栋id和单元号查询单元
            UnitDto unitDto = new UnitDto();
            unitDto.setFloorId(floorDtos.get(0).getFloorId());
            unitDto.setUnitNum(unitNum);
            List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
            Assert.listOnlyOne(unitDtos, "查询小区错误！");
            //根据单元id和房间号查询房间
            RoomDto roomDto = new RoomDto();
            roomDto.setUnitId(unitDtos.get(0).getUnitId());
            roomDto.setRoomNum(roomNum);
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            Assert.listOnlyOne(roomDtos, "查询房屋错误！");
            //获取房屋id
            String roomId = roomDtos.get(0).getRoomId();
            reqJson.put("roomId", roomId);
        }

        PropertyRightRegistrationDto propertyRightRegistrationDto = BeanConvertUtil.covertBean(reqJson, PropertyRightRegistrationDto.class);

        int count = propertyRightRegistrationV1InnerServiceSMOImpl.queryPropertyRightRegistrationsCount(propertyRightRegistrationDto);

        List<PropertyRightRegistrationDto> propertyRightRegistrationDtos = null;

        if (count > 0) {
            propertyRightRegistrationDtos = propertyRightRegistrationV1InnerServiceSMOImpl.queryPropertyRightRegistrations(propertyRightRegistrationDto);
        } else {
            propertyRightRegistrationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, propertyRightRegistrationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
