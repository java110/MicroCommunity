package com.java110.job.adapt.applyRoomDiscount;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 优惠申请通知适配器(空置房申请通知有验房权限的员工)
 *
 * @author fqz
 * @date 2021-01-07  17:59
 */
@Component(value = "machineApplyRoomDiscountAdapt")
public class MachineApplyRoomDiscountAdapt extends DatabusAdaptImpl {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

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

    private static Logger logger = LoggerFactory.getLogger(MachineApplyRoomDiscountAdapt.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(data.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
        //房屋id
        String roomId = data.getString("roomId");
        //申请开始时间
        String startTime = data.getString("startTime");
        //申请结束时间
        String endTime = data.getString("endTime");
        applyRoomDiscountDto.setRoomId(roomId);
        applyRoomDiscountDto.setStartTime(startTime);
        applyRoomDiscountDto.setEndTime(endTime);
        //状态为申请验房
        applyRoomDiscountDto.setState("1");
        //有效数据
        applyRoomDiscountDto.setStatusCd("0");
        //查询折扣申请表
        List<ApplyRoomDiscountDto> applyRoomDiscountDtos = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
        //获取申请id
        String ardId = applyRoomDiscountDtos.get(0).getArdId();
        //状态名称
        String stateName = applyRoomDiscountDtos.get(0).getStateName();
        //申请人
        String createUserName = applyRoomDiscountDtos.get(0).getCreateUserName();
        //状态
        String state = applyRoomDiscountDtos.get(0).getState();
        JSONObject paramIn = new JSONObject();
        paramIn.put("ardId", ardId);
        paramIn.put("stateName", stateName);
        paramIn.put("createUserName", createUserName);
        paramIn.put("state", state);
        publishMsg(paramIn, communityDtos.get(0));
    }

    /**
     * 给验房员工推送信息
     *
     * @param paramIn
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
        basePrivilegeDto.setResource("/wechatHouseInspection");
        List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
        String url = sendMsgUrl + accessToken;
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
                data.setFirst(new Content("空置房申请，验房信息如下："));
                data.setKeyword1(new Content(paramIn.getString("ardId")));
                data.setKeyword2(new Content("空置房申请-验房"));
                data.setKeyword3(new Content(paramIn.getString("createUserName")));
                data.setKeyword4(new Content(paramIn.getString("createUserName") + "提交空置房申请，需进行现场验房"));
                data.setKeyword5(new Content(paramIn.getString("stateName")));
                data.setRemark(new Content("请及时处理！"));
                templateMessage.setData(data);
                //获取员工公众号地址
                String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
                templateMessage.setUrl(wechatUrl);
                logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                logger.info("微信模板返回内容:{}", responseEntity);
            }
        }
    }
}
