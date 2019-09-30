package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 小区成员加入
 */
@Java110Listener("communityMemberJoinedListener")
public class CommunityMemberJoinedListener extends AbstractServiceApiDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(CommunityMemberJoinedListener.class);


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
        businesses.add(addCommunityMember(paramObj));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 添加小区成员
     *
     * @param paramInJson 接口请求数据封装
     * @return 封装好的 data数据
     */
    private JSONObject addCommunityMember(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("memberId"));
        businessCommunityMember.put("memberTypeCd", paramInJson.getString("memberTypeCd"));
        String auditStatusCd = MappingCache.getValue(MappingConstant.DOMAIN_COMMUNITY_MEMBER_AUDIT, paramInJson.getString("memberTypeCd"));
        auditStatusCd = StringUtils.isEmpty(auditStatusCd) ? StateConstant.AGREE_AUDIT : auditStatusCd;
        businessCommunityMember.put("auditStatusCd", auditStatusCd);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

        return business;
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
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
