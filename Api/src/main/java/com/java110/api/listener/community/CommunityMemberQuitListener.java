package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 小区成员退出
 */
@Java110Listener("communityMemberQuitListener")
public class CommunityMemberQuitListener extends AbstractServiceApiDataFlowListener {
    private final static Logger logger = LoggerFactory.getLogger(CommunityMemberQuitListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_MEMBER_QUIT_COMMUNITY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        logger.debug("ServiceDataFlowEvent : {}",event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        //根据 memberId communityId memberTypeCd  query.myCommunity.byMember

        ResponseEntity<String> responseEntity = super.callService(dataFlowContext,"query.myCommunity.byMember",paramObj);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        JSONArray communityMemberInfos = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("communitys");
        Assert.listNotNull(communityMemberInfos,"当前没有任何小区信息"+paramIn);
        String communityMemberId = "";
        for(int _communityMemberIndex = 0 ;_communityMemberIndex < communityMemberInfos.size();_communityMemberIndex++){

            if(communityMemberInfos.getJSONObject(_communityMemberIndex).getString("communityId").equals(paramObj.getString("communityId"))){
                communityMemberId = communityMemberInfos.getJSONObject(_communityMemberIndex).getString("communityMemberId");
                break;
            }
        }

        Assert.hasLength(communityMemberId,"不存在当前数据"+paramIn);

        paramObj.put("communityMemberId",communityMemberId);
        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID,"-1");
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD,"D");
        JSONArray businesses = new JSONArray();
        //添加商户
        businesses.add(deleteCommunityMember(paramObj));

        JSONObject paramInObj = super.restToCenterProtocol(businesses,dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header,dataFlowContext.getRequestCurrentHeaders());

         responseEntity = this.callService(dataFlowContext,service.getServiceCode(),paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 数据校验
     * @param paramIn
     *
     * "communityId": "7020181217000001",
     *         "memberId": "3456789",
     *         "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn){
        Assert.jsonObjectHaveKey(paramIn,"communityId","请求报文中未包含communityId");
        Assert.jsonObjectHaveKey(paramIn,"memberId","请求报文中未包含memberId");
        Assert.jsonObjectHaveKey(paramIn,"memberTypeCd","请求报文中未包含memberTypeCd");
    }

    /**
     * 添加小区成员
     * @param paramInJson
     * @return
     */
    private JSONObject deleteCommunityMember(JSONObject paramInJson){

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ,2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId",paramInJson.getString("communityMemberId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember",businessCommunityMember);

        return business;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
