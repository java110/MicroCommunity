package com.java110.api.listener.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.unit.IUnitBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.dto.FloorDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName SaveUnitListener
 * @Description TODO 保存小区单元信息
 * @Author wuxw
 * @Date 2019/5/3 11:54
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Java110Listener("saveUnitListener")
public class SaveUnitListener extends AbstractServiceApiPlusListener {
    private static Logger logger = LoggerFactory.getLogger(SaveUnitListener.class);
    @Autowired
    private IUnitBMO unitBMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_UNIT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含floorId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitNum", "请求报文中未包含unitNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layerCount", "请求报文中未包含layerCount节点");
        Assert.jsonObjectHaveKey(reqJson, "lift", "请求报文中未包含lift节点");

        Assert.isInteger(reqJson.getString("layerCount"), "单元总层数据不是有效数字");

        if (!"1010".equals(reqJson.getString("lift")) && !"2020".equals(reqJson.getString("lift"))) {
            throw new IllegalArgumentException("是否有电梯 传入数据错误");
        }

        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        floorDto.setFloorId(reqJson.getString("floorId"));
        //校验小区楼ID和小区是否有对应关系
        int total = floorInnerServiceSMOImpl.queryFloorsCount(floorDto);

        if (total < 1) {
            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
        }

        //校验单元编号是否已经存在
        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setUnitNum(reqJson.getString("unitNum"));
        int count = unitInnerServiceSMOImpl.queryUnitsCount(unitDto);

        if(count > 0){
            throw new IllegalArgumentException("单元编号已经存在，请勿重复添加");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        unitBMOImpl.addUnit(reqJson, context);
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IFloorInnerServiceSMO getFloorInnerServiceSMOImpl() {
        return floorInnerServiceSMOImpl;
    }

    public void setFloorInnerServiceSMOImpl(IFloorInnerServiceSMO floorInnerServiceSMOImpl) {
        this.floorInnerServiceSMOImpl = floorInnerServiceSMOImpl;
    }
}
