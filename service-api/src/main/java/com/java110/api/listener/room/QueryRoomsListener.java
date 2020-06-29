package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IFloorInnerServiceSMO;
import com.java110.core.smo.community.IRoomInnerServiceSMO;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName QueryRoomsListener
 * @Description TODO 查询房屋信息
 * @Author wuxw
 * @Date 2019/5/8 0:15
 * @Version 1.0
 * add by wuxw 2019/5/8
 **/
@Java110Listener("queryRoomsListener")
public class QueryRoomsListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_ROOMS;
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

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        ApiRoomVo apiRoomVo = new ApiRoomVo();
        //查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsCount(BeanConvertUtil.covertBean(reqJson, RoomDto.class));
        apiRoomVo.setTotal(total);
        List<RoomDto> roomDtoList = null;
        if (total > 0) {
            roomDtoList = roomInnerServiceSMOImpl.queryRooms(roomDto);
        }else{
            roomDtoList = new ArrayList<>();
        }
        apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        int row = reqJson.getInteger("row");
        apiRoomVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 校验小区房屋查询入参信息
     *
     * @param reqJson 请求入参信息
     */
    private void validateRoomData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        //Assert.jsonObjectHaveKey(reqJson, "floorId", "请求中未包含floorId信息");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求报文中未包含row节点");

        Assert.isInteger(reqJson.getString("page"), "page不是数字");
        Assert.isInteger(reqJson.getString("row"), "row不是数字");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        int row = Integer.parseInt(reqJson.getString("row"));


        if (row > MAX_ROW) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "row 数量不能大于50");
        }
        //校验小区楼ID和小区是否有对应关系
        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));

        if (total < 1) {
            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
        }

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
