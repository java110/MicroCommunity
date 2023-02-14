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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.UnitDto;
import com.java110.dto.community.CommunityLocationDto;
import com.java110.intf.community.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：communityLocation.listCommunityLocation
 * 请求路劲：/app/communityLocation.ListCommunityLocation
 * add by 吴学文 at 2022-07-17 00:10:38 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "communityLocation.listCommunityLocations")
public class ListCommunityLocationCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListCommunityLocationCmd.class);

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        CommunityLocationDto communityLocationDto = BeanConvertUtil.covertBean(reqJson, CommunityLocationDto.class);

        int count = communityLocationInnerServiceSMOImpl.queryCommunityLocationsCount(communityLocationDto);

        List<CommunityLocationDto> communityLocationDtos = new ArrayList<>();

        if (count > 0) {
            List<CommunityLocationDto> communityLocations = communityLocationInnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);
            for (CommunityLocationDto communityLocation : communityLocations) {
                if (!StringUtil.isEmpty(communityLocation.getLocationType()) && communityLocation.getLocationType().equals("2000")
                        && !StringUtil.isEmpty(communityLocation.getLocationObjId()) && !communityLocation.getLocationObjId().equals("-1")) { //单元
                    UnitDto unitDto = new UnitDto();
                    unitDto.setUnitId(communityLocation.getLocationObjId());
                    List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
                    Assert.listOnlyOne(unitDtos, "查询单元错误");
                    communityLocation.setFloorId(unitDtos.get(0).getFloorId());
                    communityLocation.setUnitId(unitDtos.get(0).getUnitId());
                    FloorDto floorDto = new FloorDto();
                    floorDto.setFloorId(unitDtos.get(0).getFloorId());
                    List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
                    Assert.listOnlyOne(floorDtos, "查询楼栋错误");
                } else if (!StringUtil.isEmpty(communityLocation.getLocationType()) && communityLocation.getLocationType().equals("6000")
                        && !StringUtil.isEmpty(communityLocation.getLocationObjId()) && !communityLocation.getLocationObjId().equals("-1")) { //楼栋
                    communityLocation.setFloorId(communityLocation.getLocationObjId());
                } else if (!StringUtil.isEmpty(communityLocation.getLocationType()) && communityLocation.getLocationType().equals("7000")
                        && !StringUtil.isEmpty(communityLocation.getLocationObjId()) && !communityLocation.getLocationObjId().equals("-1")) { //停车场
                    communityLocation.setPaId(communityLocation.getLocationObjId());
                } else if (!StringUtil.isEmpty(communityLocation.getLocationType()) && communityLocation.getLocationType().equals("4000")
                        && !StringUtil.isEmpty(communityLocation.getLocationObjId()) && !communityLocation.getLocationObjId().equals("-1")) { //岗亭
                    communityLocation.setBoxId(communityLocation.getLocationObjId());
                }
                communityLocationDtos.add(communityLocation);
            }
        } else {
            communityLocationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, communityLocationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
