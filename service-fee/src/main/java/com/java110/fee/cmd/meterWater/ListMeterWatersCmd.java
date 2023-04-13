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
package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.meterWater.MeterWaterDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：meterWater.listMeterWater
 * 请求路劲：/app/meterWater.ListMeterWater
 * add by 吴学文 at 2022-07-21 09:17:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meterWater.listMeterWaters")
public class ListMeterWatersCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListMeterWatersCmd.class);

    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        MeterWaterDto meterWaterDto = BeanConvertUtil.covertBean(reqJson, MeterWaterDto.class);
        ResultVo resultVo = null;
        if (!freshFeeDtoParam(meterWaterDto, reqJson)) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(1, 0, new JSONArray());
            context.setResponseEntity(responseEntity);
            return;
        }

        int count = meterWaterInnerServiceSMOImpl.queryMeterWatersCount(meterWaterDto);

        List<MeterWaterDto> meterWaterDtos = null;

        if (count > 0) {
            meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        } else {
            meterWaterDtos = new ArrayList<>();
        }

        resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, meterWaterDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private boolean freshFeeDtoParam(MeterWaterDto meterWaterDto, JSONObject reqJson) {

        if (!reqJson.containsKey("roomNum")) {
            return true;
        }

        String roomNum = reqJson.getString("roomNum");

        if (StringUtil.isEmpty(roomNum)) {
            return true;
        }

        if (!roomNum.contains("-")) {
            return false;
        }

        if (StringUtil.isEmpty(meterWaterDto.getObjType())) {
            meterWaterDto.setObjType(MeterWaterDto.PAYER_OBJ_TYPE_ROOM);
        }

        if (MeterWaterDto.PAYER_OBJ_TYPE_ROOM.equals(meterWaterDto.getObjType())) {
            String[] nums = roomNum.split("-", 3);
            if (nums.length != 3) {
                return false;
            }
            RoomDto roomDto = new RoomDto();
            roomDto.setFloorNum(nums[0]);
            roomDto.setUnitNum(nums[1]);
            roomDto.setRoomNum(nums[2]);
            roomDto.setCommunityId(meterWaterDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

            if (roomDtos == null || roomDtos.size() < 1) {
                return false;
            }
            meterWaterDto.setObjId(roomDtos.get(0).getRoomId());

        } else {
            String[] nums = roomNum.split("-", 2);
            if (nums.length != 2) {
                return false;
            }
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setAreaNum(nums[0]);
            parkingSpaceDto.setNum(nums[1]);
            parkingSpaceDto.setCommunityId(meterWaterDto.getCommunityId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                return false;
            }
            meterWaterDto.setObjId(parkingSpaceDtos.get(0).getPsId());
        }

        return true;
    }
}
