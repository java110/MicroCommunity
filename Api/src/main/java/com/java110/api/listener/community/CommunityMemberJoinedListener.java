package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.community.ICommunityBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 小区成员加入
 */
@Java110Listener("communityMemberJoinedListener")
public class CommunityMemberJoinedListener extends AbstractServiceApiDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(CommunityMemberJoinedListener.class);
    @Autowired
    private ICommunityBMO communityBMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_MEMBER_JOINED_COMMUNITY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID, "-1");
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        //添加商户
        businesses.add(communityBMOImpl.addCommunityMember(paramObj));


        ResponseEntity<String> responseEntity = communityBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);

        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 数据校验
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId");
        Assert.jsonObjectHaveKey(paramIn, "memberId", "请求报文中未包含memberId");
        Assert.jsonObjectHaveKey(paramIn, "memberTypeCd", "请求报文中未包含memberTypeCd");
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        if(!CommunityMemberTypeConstant.PROPERTY.equals(paramObj.getString("memberTypeCd"))){
            return ;
        }

        //当 memberTypeCd 为物业时 小区只要有入驻 则 不能再次入驻优化
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.PROPERTY);
        String[] auditStatusCds = new String[]{"1000", "1100"};
        communityMemberDto.setAuditStatusCds(auditStatusCds);
        communityMemberDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        communityMemberDto.setCommunityId(paramObj.getString("communityId"));
        int count = communityInnerServiceSMOImpl.getCommunityMemberCount(communityMemberDto);
        if (count > 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "小区已经被物业入驻");
        }


    }

    @Override
    public int getOrder() {
        return 0;
    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }
}
