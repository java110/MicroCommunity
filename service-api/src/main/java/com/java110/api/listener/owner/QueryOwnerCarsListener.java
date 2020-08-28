package com.java110.api.listener.owner;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OwnerDto
 * @Description 查询业主车辆
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryOwnerCarsListener")
public class QueryOwnerCarsListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_OWNER_CAR;
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
        validateOwnerCarData(reqJson);

        int row = reqJson.getInteger("row");

        //查询总记录数
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class));
        List<OwnerCarDto> ownerCarDtoList = null;

        if (total > 0) {
            ownerCarDtoList = ownerCarInnerServiceSMOImpl.queryOwnerCars(BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class));

            freshPs(ownerCarDtoList);
        } else {
            ownerCarDtoList = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerCarDtoList);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    private void freshPs(List<OwnerCarDto> ownerCarDtoList) {

        if (ownerCarDtoList == null || ownerCarDtoList.size() < 1) {
            return;
        }

        List<String> psIds = new ArrayList<>();
        for (OwnerCarDto ownerCarDto : ownerCarDtoList) {
            if(StringUtil.isEmpty(ownerCarDto.getPsId())){
                continue;
            }
            psIds.add(ownerCarDto.getPsId());
        }

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(ownerCarDtoList.get(0).getCommunityId());
        parkingSpaceDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        for(ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos){
            for (OwnerCarDto ownerCarDto : ownerCarDtoList){
                if(tmpParkingSpaceDto.getPsId().equals(ownerCarDto.getPsId())){
                    ownerCarDto.setAreaNum(tmpParkingSpaceDto.getAreaNum());
                    ownerCarDto.setNum(tmpParkingSpaceDto.getNum());
                }
            }
        }
    }


    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateOwnerCarData(JSONObject reqJson) {
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
}
