package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerAppUserInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName AppUserBindingOwnerListener
 * @Description app用户绑定业主接口
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("appUserBindingOwnerListener")
public class AppUserBindingOwnerListener extends AbstractServiceApiListener {


    private static final int DEFAULT_SEQ_COMMUNITY_MEMBER = 2;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(AppUserBindingOwnerListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_APP_USER_BINDING_OWNER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityName", "未包含小区名称");
        Assert.hasKeyAndValue(reqJson, "areaCode", "未包含小区地区");
        Assert.hasKeyAndValue(reqJson, "appUserName", "未包含用户名称");
        Assert.hasKeyAndValue(reqJson, "idCard", "未包含身份证号");
        Assert.hasKeyAndValue(reqJson, "link", "未包含联系电话");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        logger.debug("ServiceDataFlowEvent : {}", event);




        //查询小区是否存在
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCityCode(reqJson.getString("areaCode"));
        communityDto.setName(reqJson.getString("communityName"));
        communityDto.setState("1100");
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "填写小区信息错误");

        CommunityDto tmpCommunityDto = communityDtos.get(0);

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(tmpCommunityDto.getCommunityId());
        ownerDto.setIdCard(reqJson.getString("idCard"));
        ownerDto.setName(reqJson.getString("appUserName"));
        ownerDto.setLink(reqJson.getString("link"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "填写业主信息错误");

        OwnerDto tmpOwnerDto = ownerDtos.get(0);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        //添加小区楼
        businesses.add(addOwnerAppUser(paramObj, tmpCommunityDto, tmpOwnerDto));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());


        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);

    }

    /**
     * 添加业主应用用户关系
     *
     * @param paramInJson
     * @return
     */
    private JSONObject addOwnerAppUser(JSONObject paramInJson, CommunityDto communityDto, OwnerDto ownerDto) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_APP_USER);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwnerAppUser = new JSONObject();
        businessOwnerAppUser.putAll(paramInJson);
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        businessOwnerAppUser.put("state", "10000");
        businessOwnerAppUser.put("appTypeCd", "10010");
        businessOwnerAppUser.put("appUserId", "-1");
        businessOwnerAppUser.put("memberId", ownerDto.getMemberId());
        businessOwnerAppUser.put("communityName", communityDto.getName());
        businessOwnerAppUser.put("communityId", communityDto.getCommunityId());
        businessOwnerAppUser.put("appUserName", ownerDto.getName());
        businessOwnerAppUser.put("idCard", ownerDto.getIdCard());
        businessOwnerAppUser.put("link",ownerDto.getLink());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOwnerAppUser", businessOwnerAppUser);
        return business;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }


    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }


    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }
}
