package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.*;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName SaveOwnerListener
 * @Description 保存小区楼信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("saveOwnerListener")
public class SaveOwnerListener extends AbstractServiceApiDataFlowListener {


    private static final int DEFAULT_SEQ_COMMUNITY_MEMBER = 2;


    private static Logger logger = LoggerFactory.getLogger(SaveOwnerListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_OWNER;
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
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        //生成ownerId
        generateOwnerId(paramObj);

        //添加小区楼
        businesses.add(addOwner(paramObj));

        //小区楼添加到小区中
        businesses.add(addCommunityMember(paramObj));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 生成小区楼ID
     *
     * @param paramObj 请求入参数据
     */
    private void generateOwnerId(JSONObject paramObj) {
        String ownerId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        paramObj.put("ownerId", ownerId);
        if (!paramObj.containsKey("memberId")) {
            paramObj.put("memberId", ownerId);
        }
    }


    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private JSONObject addOwner(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(paramInJson);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOwner", businessOwner);

        return business;
    }


    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    private JSONObject addCommunityMember(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ_COMMUNITY_MEMBER);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("ownerId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.OWNER);
        businessCommunityMember.put("auditStatusCd", StatusConstant.STATUS_CD_AUDIT_COMPLETE);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

        return business;
    }

    /**
     * 数据校验
     * <p>
     * name:'',
     * age:'',
     * link:'',
     * sex:'',
     * remark:''
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(paramIn, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(paramIn, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(paramIn, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(paramIn, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId");
    }


    @Override
    public int getOrder() {
        return 0;
    }

}
