package com.java110.api.listener.floor;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.floor.IFloorInnerServiceSMO;
import com.java110.dto.FloorDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiFloorDataVo;
import com.java110.vo.api.ApiFloorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 小区楼数据层侦听类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("QueryFloorsListener")
public class QueryFloorsListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_FLOOR;
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
        validateFloorData(reqJson);

        int page = reqJson.getInteger("page");
        int row = reqJson.getInteger("row");
        String communityId = reqJson.getString("communityId");

        ApiFloorVo apiFloorVo = new ApiFloorVo();
        //查询总记录数
        int total = floorInnerServiceSMOImpl.queryFloorsCount(communityId);
        apiFloorVo.setTotal(total);
        if (total > 0) {
            List<FloorDto> floorDtoList = floorInnerServiceSMOImpl.queryFloors(page, row, communityId);
            apiFloorVo.setApiFloorDataVoList(BeanConvertUtil.covertBeanList(floorDtoList, ApiFloorDataVo.class));
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFloorVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateFloorData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");

    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }


    public IFloorInnerServiceSMO getFloorInnerServiceSMOImpl() {
        return floorInnerServiceSMOImpl;
    }

    public void setFloorInnerServiceSMOImpl(IFloorInnerServiceSMO floorInnerServiceSMOImpl) {
        this.floorInnerServiceSMOImpl = floorInnerServiceSMOImpl;
    }
}
