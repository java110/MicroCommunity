package com.java110.job.adapt.roomRenovation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 房屋装修申请适配器
 *
 * @author fqz
 * @date 2021-02-22
 */
@Component(value = "machineSaveRoomRenovation")
public class MachineSaveRoomRenovation extends DatabusAdaptImpl {

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
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMO;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    @Autowired
    private IRoomRenovationsInnerServiceSMO roomRenovationsInnerServiceSMO;

    private static Logger logger = LoggerFactory.getLogger(MachineSaveRoomRenovation.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();
        //获取开始时间
        String beginTime = data.getString("startTime") + " 00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timeOne = format.parse(beginTime);
        Calendar now = Calendar.getInstance();
        String newTime = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
        Date timeTwo = format.parse(newTime);
        if (timeOne.getTime() < timeTwo.getTime()) {
            logger.info("装修时间不能小于当前时间！");
            return;
        }
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(data.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        //查询房屋业主信息
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(data.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
        Assert.listOnlyOne(ownerRoomRelDtos, "查询房屋业主错误！");
        //申请人姓名
        String personName = data.getString("personName");
        //申请联系电话
        String personTel = data.getString("personTel");
        //房屋号
        String roomName = data.getString("roomName");
        //房屋id
        String roomId = data.getString("roomId");
        //装修开始时间
        String startTime = data.getString("startTime");
        //装修结束时间
        String endTime = data.getString("endTime");
        //备注
        String remark = data.getString("remark");
        JSONObject paramIn = new JSONObject();
        paramIn.put("personName", personName);
        paramIn.put("personTel", personTel);
        paramIn.put("roomName", roomName);
        paramIn.put("roomId", roomId);
        paramIn.put("startTime", startTime);
        paramIn.put("endTime", endTime);
        paramIn.put("remark", remark);
        paramIn.put("userId", ownerRoomRelDtos.get(0).getOwnerId());
        //给员工推送消息
        publishMsg(paramIn, communityDtos.get(0));
        //给业主推送信息
        sendMessage(paramIn, communityDtos.get(0));
    }

    /**
     * 装修申请给员工推送消息
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
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_TEMPLATE);
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
        basePrivilegeDto.setResource("/wechatDecorationNotice");
        List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
        String url = sendMsgUrl + accessToken;
        String[] roomName = paramIn.getString("roomName").split("-",3);
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
                    data.setFirst(new Content("尊敬的管理员，您好！有新的装修申请需要您处理，申请信息如下："));
                    data.setKeyword1(new Content(paramIn.getString("personName") + "-" + paramIn.getString("personTel")));
                    data.setKeyword2(new Content(communityDto.getName() + roomName[0] + "栋" + roomName[1] + "单元" + roomName[2] + "室"));
                    data.setKeyword3(new Content(paramIn.getString("startTime") + "至" + paramIn.getString("endTime")));
                    data.setKeyword4(new Content(paramIn.getString("remark")));
                    data.setKeyword5(new Content("待审核"));
                    data.setRemark(new Content("感谢您的使用。"));
                    templateMessage.setData(data);
                    //获取员工公众号地址
                    String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
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

    /**
     * 给业主自己推送消息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMessage(JSONObject paramIn, CommunityDto communityDto) {
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
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_APPLY_TEMPLATE);
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
        //获取房屋id
        String roomId = paramIn.getString("roomId");
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomId);
        //查询业主房屋关系表
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
        //取得业主id
        String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerId);
        //1001 业主本人 1002 家庭成员
        ownerDto.setOwnerTypeCd("1001");
        //查询业主
        List<OwnerDto> ownerDtos = ownerInnerServiceSMO.queryOwners(ownerDto);
        //获得成员id
        String memberId = ownerDtos.get(0).getMemberId();
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(memberId);
        ownerAppUserDto.setAppType("WECHAT");
        //查询绑定业主
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
        //根据房屋id获取房屋装修信息
        RoomRenovationDto roomRenovationDto = new RoomRenovationDto();
        roomRenovationDto.setRoomId(roomId);
        List<RoomRenovationDto> roomRenovationDtos = roomRenovationsInnerServiceSMO.queryRoomRenovations(roomRenovationDto);
        //查询当前绑定业主所属小区
        String communityId = ownerAppUserDtos.get(0).getCommunityId();
        //通过小区id查询小区信息
        CommunityDto community = new CommunityDto();
        community.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(community);
        //获取物业联系方式
        String tel = null;
        if (communityDtos != null && communityDtos.size() > 0) {
            tel = communityDtos.get(0).getTel();
        }
        String[] roomName = paramIn.getString("roomName").split("-",3);
        if (ownerAppUserDtos.size() > 0) {
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("尊敬的" + paramIn.getString("roomName") + "业主，您的房屋装修登记成功："));
            data.setKeyword1(new Content(roomRenovationDtos.get(0).getrId()));
            data.setKeyword2(new Content(communityDto.getName() + roomName[0] + "栋" + roomName[1] + "单元" + roomName[2] + "室"));
            data.setKeyword3(new Content(paramIn.getString("personTel")));
            data.setKeyword4(new Content(paramIn.getString("startTime") + "至" + paramIn.getString("endTime")));
            data.setRemark(new Content("物业联系电话：" + tel + "，请到物业管理处或通过手机缴纳装修押金！"));
            templateMessage.setData(data);
            //获取业主公众号地址
            String wechatUrl = UrlCache.getOwnerUrl();
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }
}
