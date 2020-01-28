package com.java110.api.listener.parkingSpace;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.owner.IOwnerCarInnerServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;

import com.java110.dto.owner.OwnerCarDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.owner.ApiOwnerCarDataVo;
import com.java110.vo.api.owner.ApiOwnerCarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ParkingSpaceDto
 * @Description 小区楼数据层侦听类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryParkingSpaceCarsListener")
public class QueryParkingSpaceCarsListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_PARKING_SPACE_CAR;
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
        validateParkingSpaceData(reqJson);

        refreshReqJson(reqJson);

        //根据车牌号去查询 车位信息
        queryParkingSpaceCar(reqJson, dataFlowContext);
        return;
    }

    /**
     * 根据车牌号 查询 停车位
     *
     * @param reqJson         请求报文
     * @param dataFlowContext 上线文对象
     */
    private void queryParkingSpaceCar(JSONObject reqJson, DataFlowContext dataFlowContext) {


        ApiOwnerCarVo apiOwnerCarVo = new ApiOwnerCarVo();

        int row = reqJson.getInteger("row");
        //查询总记录数
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
        apiOwnerCarVo.setTotal(total);
        if (total > 0) {
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            apiOwnerCarVo.setOwnerCars(BeanConvertUtil.covertBeanList(ownerCarDtos, ApiOwnerCarDataVo.class));
        }

        apiOwnerCarVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOwnerCarVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 获取 停车位Ids
     *
     * @param ownerCarDtos 业主车位
     * @return 停车位Ids
     */
    private String[] getPsIds(List<OwnerCarDto> ownerCarDtos) {
        List<String> psIds = new ArrayList<String>();
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            psIds.add(ownerCarDto.getPsId());
        }

        return psIds.toArray(new String[psIds.size()]);
    }

    /**
     * 请求数据处理
     *
     * @param reqJson 请求数据对象
     */
    private void refreshReqJson(JSONObject reqJson) {

        if (!reqJson.containsKey("state")) {
            return;
        }

        if ("SH".equals(reqJson.getString("state"))) {
            reqJson.put("states", new String[]{"S", "H"});
            reqJson.remove("state");
        }
    }


    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateParkingSpaceData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");

    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }


    public IParkingSpaceInnerServiceSMO getParkingSpaceInnerServiceSMOImpl() {
        return parkingSpaceInnerServiceSMOImpl;
    }

    public void setParkingSpaceInnerServiceSMOImpl(IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl) {
        this.parkingSpaceInnerServiceSMOImpl = parkingSpaceInnerServiceSMOImpl;
    }


    public IOwnerCarInnerServiceSMO getOwnerCarInnerServiceSMOImpl() {
        return ownerCarInnerServiceSMOImpl;
    }

    public void setOwnerCarInnerServiceSMOImpl(IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl) {
        this.ownerCarInnerServiceSMOImpl = ownerCarInnerServiceSMOImpl;
    }
}
