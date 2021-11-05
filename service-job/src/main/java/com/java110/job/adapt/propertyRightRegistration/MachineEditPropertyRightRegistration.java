package com.java110.job.adapt.propertyRightRegistration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.propertyRightRegistration.PropertyRightRegistrationDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IPropertyRightRegistrationV1InnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 产权登记修改适配器
 *
 * @author fqz
 * @date 2021-10-23 10:23
 */
@Component(value = "machineEditPropertyRightRegistration")
public class MachineEditPropertyRightRegistration extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMO;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IPropertyRightRegistrationV1InnerServiceSMO propertyRightRegistrationV1InnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(MachineEditPropertyRightRegistration.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        //获取产权登记id
        String[] split = data.getString("prrId").split("'");
        String prrId = split[1];
        PropertyRightRegistrationDto propertyRightRegistrationDto = new PropertyRightRegistrationDto();
        propertyRightRegistrationDto.setPrrId(prrId);
        List<PropertyRightRegistrationDto> propertyRightRegistrationDtos = propertyRightRegistrationV1InnerServiceSMOImpl.queryPropertyRightRegistrations(propertyRightRegistrationDto);
        Assert.listOnlyOne(propertyRightRegistrationDtos, "查询产权登记表错误！");
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(propertyRightRegistrationDtos.get(0).getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        //获取创建时间
        String[] createTimes = data.getString("createTime").split("'");
        String createTime = createTimes[1];
        JSONObject paramIn = new JSONObject();
        paramIn.put("prrId", prrId);
        paramIn.put("name", propertyRightRegistrationDtos.get(0).getName());
        paramIn.put("createTime", createTime);
        //获取状态标识
        String state = propertyRightRegistrationDtos.get(0).getState();
        if (!StringUtil.isEmpty(state) && state.equals("0")) {
            //给员工推送消息
            publishMsg(paramIn, communityDtos.get(0));
        }
    }

    /**
     * 产权修改后给员工推送消息
     *
     * @param paramIn
     * @param communityDto
     */
    private void publishMsg(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_PROCESS_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        // 根据特定权限查询 有该权限的 员工
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/wechatPropertyRightRegistrationNotice");
        List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
        String url = sendMsgUrl + accessToken;
        if (userDtos != null && userDtos.size() > 0) {
            for (UserDto userDto : userDtos) {
                //根据 userId 查询到openId
                StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
                staffAppAuthDto.setStaffId(userDto.getUserId());
                staffAppAuthDto.setAppType("WECHAT");
                List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
                if (staffAppAuthDtos != null && staffAppAuthDtos.size() > 0) {
                    String openId = staffAppAuthDtos.get(0).getOpenId();
                    Data data = new Data();
                    PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                    templateMessage.setTemplate_id(templateId);
                    templateMessage.setTouser(openId);
                    data.setFirst(new Content("尊敬的员工，您好！有新的房屋产权修改申请需要您处理，申请信息如下："));
                    data.setKeyword1(new Content(paramIn.getString("prrId")));
                    data.setKeyword2(new Content("房屋产权修改申请"));
                    data.setKeyword3(new Content(paramIn.getString("name")));
                    data.setKeyword4(new Content(paramIn.getString("name") + "提交的房屋产权修改申请" + "\r\n" + "申请时间：" + paramIn.getString("createTime")));
                    data.setKeyword5(new Content("待审核"));
                    data.setRemark(new Content("请及时处理！"));
                    templateMessage.setData(data);
                    //获取员工公众号地址
                    String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
                    templateMessage.setUrl(wechatUrl);
                    logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                    ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                    logger.info("微信模板返回内容:{}", responseEntity);
                } else {
                    continue;
                }
            }
        }
    }

}
