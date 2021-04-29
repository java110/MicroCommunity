package com.java110.api.listener.communityLocation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.communityLocationAttr.CommunityLocationAttrDto;
import com.java110.intf.community.ICommunityLocationAttrInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeCommunityLocationAttrConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listCommunityLocationAttrsListener")
public class ListCommunityLocationAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private ICommunityLocationAttrInnerServiceSMO communityLocationAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeCommunityLocationAttrConstant.LIST_COMMUNITYLOCATIONATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ICommunityLocationAttrInnerServiceSMO getCommunityLocationAttrInnerServiceSMOImpl() {
        return communityLocationAttrInnerServiceSMOImpl;
    }

    public void setCommunityLocationAttrInnerServiceSMOImpl(ICommunityLocationAttrInnerServiceSMO communityLocationAttrInnerServiceSMOImpl) {
        this.communityLocationAttrInnerServiceSMOImpl = communityLocationAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        CommunityLocationAttrDto communityLocationAttrDto = BeanConvertUtil.covertBean(reqJson, CommunityLocationAttrDto.class);

        int count = communityLocationAttrInnerServiceSMOImpl.queryCommunityLocationAttrsCount(communityLocationAttrDto);

        List<CommunityLocationAttrDto> communityLocationAttrDtos = null;

        if (count > 0) {
            communityLocationAttrDtos = communityLocationAttrInnerServiceSMOImpl.queryCommunityLocationAttrs(communityLocationAttrDto);
        } else {
            communityLocationAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, communityLocationAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
