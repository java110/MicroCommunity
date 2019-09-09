package com.java110.api.listener.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.floor.IFloorInnerServiceSMO;
import com.java110.core.smo.unit.IUnitInnerServiceSMO;
import com.java110.dto.FloorDto;
import com.java110.dto.UnitDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiUnitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName QueryUnitsListener
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/2 19:57
 * @Version 1.0
 * add by wuxw 2019/5/2
 **/
@Java110Listener("queryUnitsListener")
public class QueryUnitsListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_UNITS;
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
        validateUnitData(reqJson);

        UnitDto unitDto = BeanConvertUtil.covertBean(reqJson, UnitDto.class);

        List<UnitDto> unitDtoList = unitInnerServiceSMOImpl.queryUnits(unitDto);

        List<ApiUnitVo> apiUnitVos = BeanConvertUtil.covertBeanList(unitDtoList, ApiUnitVo.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiUnitVos), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 校验小区单元查询入参信息
     *
     * @param reqJson 请求入参信息
     */
    private void validateUnitData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        //Assert.jsonObjectHaveKey(reqJson, "floorId", "请求中未包含floorId信息");
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

    public IUnitInnerServiceSMO getUnitInnerServiceSMOImpl() {
        return unitInnerServiceSMOImpl;
    }

    public void setUnitInnerServiceSMOImpl(IUnitInnerServiceSMO unitInnerServiceSMOImpl) {
        this.unitInnerServiceSMOImpl = unitInnerServiceSMOImpl;
    }

    public IFloorInnerServiceSMO getFloorInnerServiceSMOImpl() {
        return floorInnerServiceSMOImpl;
    }

    public void setFloorInnerServiceSMOImpl(IFloorInnerServiceSMO floorInnerServiceSMOImpl) {
        this.floorInnerServiceSMOImpl = floorInnerServiceSMOImpl;
    }
}
