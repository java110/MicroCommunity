package com.java110.api.listener.index;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.floor.IFloorInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerRoomRelInnerServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.core.smo.unit.IUnitInnerServiceSMO;
import com.java110.dto.FeeDto;
import com.java110.dto.OwnerDto;
import com.java110.dto.OwnerRoomRelDto;
import com.java110.dto.ParkingSpaceDto;
import com.java110.dto.RoomDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiFeeVo;
import com.java110.vo.api.ApiIndexStatisticVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 查询首页统计信息
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryIndexStatistic")
public class QueryIndexStatisticListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;
    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_INDEX_STATISTIC;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 业务层数据处理
     *
     * @param event 时间对象
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        validateIndexStatistic(reqJson);
        // 查询业主 总数量
        ApiIndexStatisticVo apiIndexStatisticVo = new ApiIndexStatisticVo();
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        int ownerCount = ownerInnerServiceSMOImpl.queryOwnersCount(ownerDto);
        int noEnterRoomOwnerCount = ownerInnerServiceSMOImpl.queryNoEnterRoomOwnerCount(ownerDto);
        // 查询房屋 总数量
        int roomCount = roomInnerServiceSMOImpl.queryRoomsCount(BeanConvertUtil.covertBean(reqJson, RoomDto.class));
        int freeRoomCount = roomInnerServiceSMOImpl.queryRoomsWithOutSellCount(BeanConvertUtil.covertBean(reqJson, RoomDto.class));
        // 查询停车位 总数量
        int parkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class));
        ParkingSpaceDto parkingSpaceDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class);
        parkingSpaceDto.setState("F");
        int freeParkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        // 查询商铺 总数量
        int shopCount = 0;
        int freeShopCount = 0;


        apiIndexStatisticVo.setOwnerCount(ownerCount + "");
        apiIndexStatisticVo.setNoEnterRoomCount(noEnterRoomOwnerCount + "");
        apiIndexStatisticVo.setRoomCount(roomCount + "");
        apiIndexStatisticVo.setFreeRoomCount(freeRoomCount + "");
        apiIndexStatisticVo.setParkingSpaceCount(parkingSpaceCount + "");
        apiIndexStatisticVo.setFreeParkingSpaceCount(freeParkingSpaceCount + "");
        apiIndexStatisticVo.setShopCount(shopCount + "");
        apiIndexStatisticVo.setFreeShopCount(freeShopCount + "");
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiIndexStatisticVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateIndexStatistic(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");

    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }


    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }


    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }

    public IParkingSpaceInnerServiceSMO getParkingSpaceInnerServiceSMOImpl() {
        return parkingSpaceInnerServiceSMOImpl;
    }

    public void setParkingSpaceInnerServiceSMOImpl(IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl) {
        this.parkingSpaceInnerServiceSMOImpl = parkingSpaceInnerServiceSMOImpl;
    }
}
