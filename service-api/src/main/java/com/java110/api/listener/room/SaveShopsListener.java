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
package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.room.IRoomBMO;
import com.java110.api.bmo.unit.IUnitBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName SaveShopsListener
 * @Description TODO 保存商铺信息
 * @Author wuxw
 * @Date 2019/5/3 11:54
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Java110Listener("saveShopsListener")
public class SaveShopsListener extends AbstractServiceApiPlusListener {
    private static Logger logger = LoggerFactory.getLogger(SaveShopsListener.class);


    @Autowired
    private IRoomBMO roomBMOImpl;

    @Autowired
    private IUnitBMO unitBMOImpl;
    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_SHOPS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含楼栋信息");
        Assert.jsonObjectHaveKey(reqJson, "roomNum", "请求报文中未包含roomNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layer", "请求报文中未包含layer节点");
        Assert.jsonObjectHaveKey(reqJson, "builtUpArea", "请求报文中未包含section节点");
        Assert.jsonObjectHaveKey(reqJson, "feeCoefficient", "请求报文中未包含apartment节点");

        Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        Assert.isMoney(reqJson.getString("feeCoefficient"), "房屋单价数据格式错误");

        if (!reqJson.containsKey("roomSubType")) {
            reqJson.put("roomSubType", RoomDto.ROOM_SUB_TYPE_WORK);
        }

        if (!reqJson.containsKey("roomRent")) {
            reqJson.put("roomRent", "0");
        }

        if (!reqJson.containsKey("roomArea")) {
            reqJson.put("roomRent", reqJson.getString("builtUpArea"));
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //判断 楼栋下是否存在 0单元 如果存在 不插入 不存在插入
        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setUnitNum("0");
        //校验小区楼ID和小区是否有对应关系
        List<UnitDto> units = unitInnerServiceSMOImpl.queryUnitsByCommunityId(unitDto);
        String unitId = "";
        if (units == null || units.size() < 1) {
            unitId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId);
            JSONObject unit = new JSONObject();
            unit.put("floorId", reqJson.getString("floorId"));
            unit.put("layerCount", "31");
            unit.put("unitId", unitId);
            unit.put("unitNum", "0");
            unit.put("lift", "0");
            unit.put("remark", "系统创建");
            unit.put("unitArea", "1.00");
            unitBMOImpl.addUnit(unit, context);
        } else {
            unitId = units.get(0).getUnitId();
        }
        reqJson.put("unitId", unitId);
        reqJson.put("section", "1");
        reqJson.put("apartment", "10101");
        reqJson.put("state", RoomDto.STATE_SHOP_FREE);
        reqJson.put("roomType", RoomDto.ROOM_TYPE_SHOPS);
        roomBMOImpl.addRoom(reqJson, context);
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IUnitInnerServiceSMO getUnitInnerServiceSMOImpl() {
        return unitInnerServiceSMOImpl;
    }

    public void setUnitInnerServiceSMOImpl(IUnitInnerServiceSMO unitInnerServiceSMOImpl) {
        this.unitInnerServiceSMOImpl = unitInnerServiceSMOImpl;
    }
}
