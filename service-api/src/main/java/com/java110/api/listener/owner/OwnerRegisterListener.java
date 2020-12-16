package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.msg.SmsDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName AppUserBindingOwnerListener
 * @Description 业主注册小程序
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("ownerRegisterListener")
public class OwnerRegisterListener extends AbstractServiceApiPlusListener {

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String ID_CARD_SWITCH = "ID_CARD_SWITCH";

    private static final int DEFAULT_SEQ_COMMUNITY_MEMBER = 2;

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Autowired
    private IUserBMO userBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(OwnerRegisterListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_OWNER_REGISTER;
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
        //Assert.hasKeyAndValue(reqJson, "idCard", "未包含身份证号");
        Assert.hasKeyAndValue(reqJson, "link", "未包含联系电话");
        Assert.hasKeyAndValue(reqJson, "msgCode", "未包含联系电话验证码");
        Assert.hasKeyAndValue(reqJson, "password", "未包含密码");

        SmsDto smsDto = new SmsDto();
        smsDto.setTel(reqJson.getString("link"));
        smsDto.setCode(reqJson.getString("msgCode"));
        smsDto = smsInnerServiceSMOImpl.validateCode(smsDto);

        if (!smsDto.isSuccess() && "ON".equals(MappingCache.getValue(SendSmsFactory.SMS_SEND_SWITCH))) {
            throw new IllegalArgumentException(smsDto.getMsg());
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        logger.debug("ServiceDataFlowEvent : {}", event);

        try {
            OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);
            ownerAppUserDto.setStates(new String[]{"10000", "12000"});

            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

            //Assert.listOnlyOne(ownerAppUserDtos, "已经申请过入驻小区");
            if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
                throw new IllegalArgumentException("已经申请过绑定业主");
            }

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
            //ownerDto.setIdCard(reqJson.getString("idCard"));
            ownerDto.setName(reqJson.getString("appUserName"));
            ownerDto.setLink(reqJson.getString("link"));

            //取出开关映射的值
            String val = MappingCache.getValue(DOMAIN_COMMON, ID_CARD_SWITCH);
            //取出身份证
            String idCard = reqJson.getString("idCard");
            if (val.equals("1") && idCard != null) {
                ownerDto.setIdCard(idCard);
            }
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

            Assert.listOnlyOne(ownerDtos, "填写业主信息错误");

            OwnerDto tmpOwnerDto = ownerDtos.get(0);

            DataFlowContext dataFlowContext = event.getDataFlowContext();

            String paramIn = dataFlowContext.getReqData();
            JSONObject paramObj = JSONObject.parseObject(paramIn);
            String appId = context.getAppId();
            if ("992020061452450002".equals(appId)) { //公众号
                paramObj.put("appType", OwnerAppUserDto.APP_TYPE_WECHAT);
            } else if ("992019111758490006".equals(appId)) { //小程序
                paramObj.put("appType", OwnerAppUserDto.APP_TYPE_WECHAT_MINA);
            } else {//app
                paramObj.put("appType", OwnerAppUserDto.APP_TYPE_APP);
            }
            paramObj.put("userId", GenerateCodeFactory.getUserId());
            if (reqJson.containsKey("openId")) {
                paramObj.put("openId", reqJson.getString("openId"));
            } else {
                paramObj.put("openId", "-1");
            }
            //添加小区楼
            ownerBMOImpl.addOwnerAppUser(paramObj, tmpCommunityDto, tmpOwnerDto, dataFlowContext);
            paramObj.put("name", paramObj.getString("appUserName"));
            paramObj.put("tel", paramObj.getString("link"));
            userBMOImpl.registerUser(paramObj, dataFlowContext);

        } catch (Exception e) {
            context.setServiceBusiness(null);
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_UNAUTHORIZED, e.getMessage()));
        }
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

    public IOwnerAppUserInnerServiceSMO getOwnerAppUserInnerServiceSMOImpl() {
        return ownerAppUserInnerServiceSMOImpl;
    }

    public void setOwnerAppUserInnerServiceSMOImpl(IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl) {
        this.ownerAppUserInnerServiceSMOImpl = ownerAppUserInnerServiceSMOImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
