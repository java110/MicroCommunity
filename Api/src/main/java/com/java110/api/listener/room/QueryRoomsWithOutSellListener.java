package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.floor.IFloorInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName QueryRoomsListener
 * @Description TODO 查询未销售房屋信息
 * @Author wuxw
 * @Date 2019/5/8 0:15
 * @Version 1.0
 * add by wuxw 2019/5/8
 **/
@Java110Listener("queryRoomsWithOutSellListener")
public class QueryRoomsWithOutSellListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_ROOMS_WITHOUT_SELL;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        validateRoomData(reqJson);

        //将小区楼ID刷入到 请求参数中
        freshFloorIdToParam(reqJson);

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        ApiRoomVo apiRoomVo = new ApiRoomVo();
        //查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsWithOutSellCount(BeanConvertUtil.covertBean(reqJson, RoomDto.class));
        apiRoomVo.setTotal(total);
        if (total > 0) {
            List<RoomDto> roomDtoList = roomInnerServiceSMOImpl.queryRoomsWithOutSell(roomDto);
            apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        }
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 将floorNum 转化为 floorId 刷入到入参对象中
     *
     * @param reqJson 入参对象
     */
    private void freshFloorIdToParam(JSONObject reqJson) {

        FloorDto floorDto = BeanConvertUtil.covertBean(reqJson, FloorDto.class);
        String floorId = "001";
        //检查 请求报文中是否有floorNum 小区楼编号，如果没有就随机选一个
        try {
            //if (!reqJson.containsKey("floorNum") || StringUtils.isEmpty(reqJson.getString("floorNum"))) {

                List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);

                if (floorDtos.size() == 0) {
                    return;
                }

                floorId = floorDtos.get(0).getFloorId();
            //}
        } finally {
            reqJson.put("floorId", floorId);
        }
    }

    /**
     * 校验小区房屋查询入参信息
     *
     * @param reqJson 请求入参信息
     */
    private void validateRoomData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求报文中未包含row节点");

        Assert.isInteger(reqJson.getString("page"), "page不是数字");
        Assert.isInteger(reqJson.getString("row"), "row不是数字");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        int row = Integer.parseInt(reqJson.getString("row"));


        if (row > MAX_ROW) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "row 数量不能大于50");
        }
       /* //校验小区楼ID和小区是否有对应关系
        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));

        if (total < 1) {
            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
        }*/

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

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }
}
