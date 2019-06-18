package com.java110.api.listener.fee;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.FeeTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.common.util.DateUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.floor.IFloorInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerRoomRelInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.core.smo.unit.IUnitInnerServiceSMO;
import com.java110.dto.FeeDto;
import com.java110.dto.OwnerDto;
import com.java110.dto.OwnerRoomRelDto;
import com.java110.dto.RoomDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiArrearsFeeDataVo;
import com.java110.vo.api.ApiArrearsFeeVo;
import com.java110.vo.api.ApiFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 小区楼数据层侦听类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryArrearsFee")
public class QueryArrearsFeeListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_ARREARS_FEE;
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
        validateFeeData(reqJson);
        FeeDto feeDtoParamIn = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        feeDtoParamIn.setArrearsEndTime(DateUtil.getCurrentDate());

        //车位时处理为 查询多个
        if(FeeTypeConstant.FEE_TYPE_HIRE_PARKING_SPACE.equals(feeDtoParamIn.getFeeTypeCd())){
            feeDtoParamIn.setFeeTypeCd("");
            feeDtoParamIn.setFeeTypeCds(new String[]{FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE,
                                                     FeeTypeConstant.FEE_TYPE_HIRE_UP_PARKING_SPACE});
        }

        int feeCount = feeInnerServiceSMOImpl.queryFeesCount(feeDtoParamIn);
        ResponseEntity<String> responseEntity = null;
        if (feeCount == 0) {
            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(new ApiArrearsFeeVo()), HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
            return;
        }

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDtoParamIn);

        List<ApiArrearsFeeDataVo> apiFeeVo = BeanConvertUtil.covertBeanList(feeDtos, ApiArrearsFeeDataVo.class);

        String[] objIds = this.getObjIds(feeDtos);

        if (FeeTypeConstant.FEE_TYPE_PROPERTY.equals(feeDtoParamIn.getFeeTypeCd())) {

            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setRoomIds(objIds);
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
            freshRoomAndOwnerData(apiFeeVo, ownerDtos);
        } else {

        }


        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeVo), HttpStatus.OK);


        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 房间号
     *
     * @param apiFeeVos 费用出参对象
     * @param ownerDtos  房屋信息
     */
    private void freshRoomAndOwnerData(List<ApiArrearsFeeDataVo> apiFeeVos, List<OwnerDto> ownerDtos) {

        for (ApiArrearsFeeDataVo apiFeeVo : apiFeeVos) {
            for (OwnerDto ownerDto : ownerDtos) {
                if(apiFeeVo.getPayerObjId().equals(ownerDto.getRoomId())){
                    apiFeeVo.setNum(ownerDto.getRoomNum());
                    apiFeeVo.setOwnerName(ownerDto.getName());
                    apiFeeVo.setTel(ownerDto.getLink());
                }
            }
        }
    }

    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateFeeData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "feeTypeCd", "请求中未包含feeTypeCd信息");

    }

    private String[] getObjIds(List<FeeDto> feeDtos) {
        List<String> objIds = new ArrayList<String>();
        for (FeeDto feeDto : feeDtos) {
            objIds.add(feeDto.getPayerObjId());
        }

        return objIds.toArray(new String[objIds.size()]);
    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }

    public IFeeInnerServiceSMO getFeeInnerServiceSMOImpl() {
        return feeInnerServiceSMOImpl;
    }

    public void setFeeInnerServiceSMOImpl(IFeeInnerServiceSMO feeInnerServiceSMOImpl) {
        this.feeInnerServiceSMOImpl = feeInnerServiceSMOImpl;
    }


    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }


    public IFloorInnerServiceSMO getFloorInnerServiceSMOImpl() {
        return floorInnerServiceSMOImpl;
    }

    public void setFloorInnerServiceSMOImpl(IFloorInnerServiceSMO floorInnerServiceSMOImpl) {
        this.floorInnerServiceSMOImpl = floorInnerServiceSMOImpl;
    }

    public IUnitInnerServiceSMO getUnitInnerServiceSMOImpl() {
        return unitInnerServiceSMOImpl;
    }

    public void setUnitInnerServiceSMOImpl(IUnitInnerServiceSMO unitInnerServiceSMOImpl) {
        this.unitInnerServiceSMOImpl = unitInnerServiceSMOImpl;
    }

    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }

    public IOwnerRoomRelInnerServiceSMO getOwnerRoomRelInnerServiceSMOImpl() {
        return ownerRoomRelInnerServiceSMOImpl;
    }

    public void setOwnerRoomRelInnerServiceSMOImpl(IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl) {
        this.ownerRoomRelInnerServiceSMOImpl = ownerRoomRelInnerServiceSMOImpl;
    }


}
