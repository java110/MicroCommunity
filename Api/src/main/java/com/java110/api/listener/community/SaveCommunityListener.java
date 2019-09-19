package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.*;
import com.java110.common.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


import com.java110.core.annotation.Java110Listener;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveCommunityListener")
public class SaveCommunityListener extends AbstractServiceApiListener {
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写小区名称");
        Assert.hasKeyAndValue(reqJson, "address", "必填，请填写小区地址");
        Assert.hasKeyAndValue(reqJson, "nearbyLandmarks", "必填，请填写小区附近地标");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(addCommunity(reqJson, context));
        businesses.addAll(addCommunityMember(reqJson));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        context.setResponseEntity(responseEntity);
    }


    /**
     * 添加小区成员 开发者 代理商 运维 商户
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    private JSONArray addCommunityMember(JSONObject paramInJson) {

        JSONArray businesses = new JSONArray();

        //添加代理商户
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_ORDER + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("storeId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.AGENT);
        businessCommunityMember.put("auditStatusCd", StatusConstant.STATUS_CD_AUDIT_COMPLETE);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);
        businesses.add(business);

        //添加运维商户
        //添加开发商户
        business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_ORDER + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", "400000000000000001");
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.OPT);
        businessCommunityMember.put("auditStatusCd", StatusConstant.STATUS_CD_AUDIT_COMPLETE);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);
        businesses.add(business);

        business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_ORDER + 3);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", "400000000000000002");
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.DEV);
        businessCommunityMember.put("auditStatusCd", StatusConstant.STATUS_CD_AUDIT_COMPLETE);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);
        businesses.add(business);

        return businesses;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_COMMUNITY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("communityId", GenerateCodeFactory.getCommunityId());
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMUNITY_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunity = new JSONObject();
        businessCommunity.putAll(paramInJson);
        businessCommunity.put("state","1000");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunity", businessCommunity);
        return business;
    }

}
