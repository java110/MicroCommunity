package com.java110.api.listener.communityLocation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.ICommunityLocationInnerServiceSMO;
import com.java110.dto.communityLocation.CommunityLocationDto;
import com.java110.result.ResultVo;
import com.java110.utils.constant.ServiceCodeCommunityLocationConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listCommunityLocationsListener")
public class ListCommunityLocationsListener extends AbstractServiceApiListener {

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeCommunityLocationConstant.LIST_COMMUNITYLOCATIONS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ICommunityLocationInnerServiceSMO getCommunityLocationInnerServiceSMOImpl() {
        return communityLocationInnerServiceSMOImpl;
    }

    public void setCommunityLocationInnerServiceSMOImpl(ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl) {
        this.communityLocationInnerServiceSMOImpl = communityLocationInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        CommunityLocationDto communityLocationDto = BeanConvertUtil.covertBean(reqJson, CommunityLocationDto.class);

        int count = communityLocationInnerServiceSMOImpl.queryCommunityLocationsCount(communityLocationDto);

        List<CommunityLocationDto> communityLocationDtos = null;

        if (count > 0) {
            communityLocationDtos = communityLocationInnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);
        } else {
            communityLocationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, communityLocationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
