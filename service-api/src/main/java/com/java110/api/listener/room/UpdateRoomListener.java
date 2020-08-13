package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.room.IRoomBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
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
 * @ClassName SaveUnitListener
 * @Description TODO 修改房屋信息
 * @Author wuxw
 * @Date 2019/5/3 11:54
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Java110Listener("updateRoomListener")
public class UpdateRoomListener extends AbstractServiceApiPlusListener {
    private static Logger logger = LoggerFactory.getLogger(UpdateRoomListener.class);

    @Autowired
    private IRoomBMO roomBMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_ROOMS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitId", "请求报文中未包含unitId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomNum", "请求报文中未包含roomNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layer", "请求报文中未包含layer节点");
        /*Assert.jsonObjectHaveKey(paramIn, "section", "请求报文中未包含section节点");*/
        Assert.jsonObjectHaveKey(reqJson, "apartment", "请求报文中未包含apartment节点");
        Assert.jsonObjectHaveKey(reqJson, "builtUpArea", "请求报文中未包含builtUpArea节点");
        Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
        /* Assert.jsonObjectHaveKey(paramIn, "unitPrice", "请求报文中未包含unitPrice节点");*/

        Assert.isInteger(reqJson.getString("section"), "房间数不是有效数字");
        Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        Assert.isMoney(reqJson.getString("unitPrice"), "房屋单价数据格式错误");

        /*if (!"1010".equals(reqJson.getString("apartment")) && !"2020".equals(reqJson.getString("apartment"))) {
            throw new IllegalArgumentException("不是有效房屋户型 传入数据错误");
        }*/

        if (!"2001".equals(reqJson.getString("state"))
                && !"2002".equals(reqJson.getString("state"))
                && !"2003".equals(reqJson.getString("state"))
                && !"2004".equals(reqJson.getString("state"))) {
            throw new IllegalArgumentException("不是有效房屋状态 传入数据错误");
        }

        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setUnitId(reqJson.getString("unitId"));
        //校验小区楼ID和小区是否有对应关系
        List<UnitDto> units = unitInnerServiceSMOImpl.queryUnitsByCommunityId(unitDto);

        if (units == null || units.size() < 1) {
            throw new IllegalArgumentException("传入单元ID不是该小区的单元");
        }

        Assert.judgeAttrValue(reqJson);

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        roomBMOImpl.updateShellRoom(reqJson, context);

        if (!reqJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("roomId", reqJson.getString("roomId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-")) {
                roomBMOImpl.addRoomAttr(attr, context);
                continue;
            }
            roomBMOImpl.updateRoomAttr(attr, context);
        }
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
